/**
 * Copyright (c) 2010-2017 Mark Allen, Norbert Bartels.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.restfb.util;

import static com.restfb.util.StringUtils.isBlank;
import static java.lang.String.format;
import static java.util.Collections.singleton;

import com.restfb.FacebookClient;
import com.restfb.exception.FacebookJsonMappingException;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonException;
import com.restfb.json.JsonObject;

import java.util.*;

/**
 * Utility methods to ease querying
 * <a target="_blank" href="http://developers.facebook.com/docs/reference/fql/insights/">Insight Metrics</a> over a set
 * of dates.
 * 
 * @author Andrew Liles
 * @since 1.6.10
 */
public class InsightUtils {
  /**
   * Represents a time period for Facebook Insights queries.
   */
  public enum Period {
    DAY(60 * 60 * 24), WEEK(60 * 60 * 24 * 7), DAYS_28(60 * 60 * 24 * 28), MONTH(2592000), LIFETIME(0);

    private final int periodLength;

    private Period(int periodLength) {
      this.periodLength = periodLength;
    }

    public int getPeriodLength() {
      return periodLength;
    }
  }

  private static final TimeZone PST_TIMEZONE = TimeZone.getTimeZone("PST");

  static Map<String, String> buildQueries(String baseQuery, List<Date> datesByQueryIndex) {
    Map<String, String> fqlByQueryIndex = new LinkedHashMap<String, String>();
    for (int queryIndex = 0; queryIndex < datesByQueryIndex.size(); queryIndex++) {
      Date d = datesByQueryIndex.get(queryIndex);
      String query = baseQuery + convertToUnixTimeOneDayLater(d);
      fqlByQueryIndex.put(String.valueOf(queryIndex), query);
    }
    return fqlByQueryIndex;
  }

  static String createBaseQuery(Period period, String pageObjectId, Set<String> metrics) {
    StringBuilder q = new StringBuilder();
    q.append("SELECT metric, value ");
    q.append("FROM insights ");
    q.append("WHERE object_id='");
    q.append(pageObjectId);
    q.append('\'');

    String metricInList = buildMetricInList(metrics);
    if (!isBlank(metricInList)) {
      q.append(" AND metric IN (");
      q.append(metricInList);
      q.append(")");
    }

    q.append(" AND period=");
    q.append(period.getPeriodLength());
    q.append(" AND end_time=");
    return q.toString();
  }

  static String buildMetricInList(Set<String> metrics) {
    StringBuilder in = new StringBuilder();
    if (!isEmpty(metrics)) {
      int metricCount = 0;
      for (String metric : metrics) {
        if (!isBlank(metric)) {
          if (metricCount > 0) {
            in.append(',');
          }

          in.append('\'');
          in.append(metric.trim());
          in.append('\'');
          metricCount++;
        }
      }
    }
    return in.toString();
  }

  /**
   * Slides this date back to midnight in the PST timezone fit for the Facebook Query Language.
   * <p>
   * More details are available at
   * <a target="_blank" href="http://developers.facebook.com/docs/reference/fql/insights" >http://developers.facebook.
   * com/docs/reference/fql/insights</a>.
   * 
   * @param date
   *          The date to slide back.
   * @return A midnight-PST version of the provided {@code date}.
   * @see #convertToMidnightInPacificTimeZone(Set)
   */
  public static Date convertToMidnightInPacificTimeZone(Date date) {
    if (date == null) {
      throw new IllegalArgumentException("The 'date' parameter is required.");
    }

    Set<Date> convertedDates = convertToMidnightInPacificTimeZone(singleton(date));

    if (convertedDates.size() != 1) {
      throw new IllegalStateException("Internal error, expected 1 date.");
    }

    return convertedDates.iterator().next();
  }

  /**
   * Slides these dates back to midnight in the PST timezone fit for the Facebook Query Language.
   * <p>
   * More details are available at
   * <a target="_blank" href="http://developers.facebook.com/docs/reference/fql/insights" >http://developers.facebook.
   * com/docs/reference/fql/insights</a>.
   * 
   * @param dates
   *          The dates to slide back.
   * @return Midnight-PST versions of the provided {@code dates}.
   * @see #convertToMidnightInPacificTimeZone(Date)
   */
  public static SortedSet<Date> convertToMidnightInPacificTimeZone(Set<Date> dates) {
    Calendar calendar = Calendar.getInstance(PST_TIMEZONE);
    SortedSet<Date> convertedDates = new TreeSet<Date>();
    for (Date d : dates) {
      calendar.setTime(d);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      convertedDates.add(calendar.getTime());
    }
    return convertedDates;
  }

  /**
   * Converts into a "unix time", which means convert into the number of seconds (NOT milliseconds) from the Epoch fit
   * for the Facebook Query Language. Notice that if you want data for September 15th then you need to present to
   * Facebook the NEXT DAY, ie. the upper exclusive limit of your date range. So beyond all the sliding to midnight code
   * you see in {@link #convertToMidnightInPacificTimeZone(Date)}, we need to go further and slide this input date
   * forward one day.
   * 
   * In retrospect, this should have been implemented via the Facebook end_time_date() function.
   * 
   * @param date
   *          The date to convert.
   * @return Unix time representation of the given {@code date}.
   */
  static long convertToUnixTimeOneDayLater(Date date) {
    long time = date.getTime() / 1000L;
    // note we cannot use a Daylight sensitive Calendar here since that would
    // adjust the time incorrectly over the DST junction
    time += Period.DAY.getPeriodLength();
    return time;
  }

  /**
   * Slides this time back to midnight in the PST timezone and converts into a "unix time", which means convert into the
   * number of seconds (NOT milliseconds) from the Epoch fit for the Facebook Query Language. Notice that if you want
   * data for September 15th then you need to present to Facebook the NEXT DAY, ie. the upper exclusive limit of your
   * date range. So beyond all the sliding to midnight issue, we need to go further and slide this input date forward
   * one day.
   * 
   * @param date
   *          The date to convert.
   * @return Midnight-PST Unix time representation of the given {@code date}.
   * @see #convertToMidnightInPacificTimeZone(Date)
   */
  static long convertToUnixTimeAtPacificTimeZoneMidnightOneDayLater(Date date) {
    return convertToUnixTimeOneDayLater(convertToMidnightInPacificTimeZone(date));
  }

  /**
   * Is the provided {@code collection} {@code null} or empty?
   * 
   * @param collection
   *          The collection to check.
   * @return {@code true} if {@code collection} is {@code null} or empty, {@code false} otherwise.
   */
  static <T> boolean isEmpty(Collection<T> collection) {
    return collection == null || collection.isEmpty();
  }
}