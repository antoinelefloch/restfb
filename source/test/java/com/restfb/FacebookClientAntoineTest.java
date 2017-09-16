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
package com.restfb;

import static org.junit.Assert.*;

import com.restfb.WebRequestor.Response;
import com.restfb.types.User;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://restfb.com">Mark Allen</a>
 */
public class FacebookClientAntoineTest {

  // final String myAccessToken =
  // "EAACEdEose0cBAJ95xu6slhhDea977q3YW22ZCnbYljIyhwcSS2kZAGagY6CzWIky9bFw6fSU2q6DmPSCWPGOmDXhWgMqpdjO2WmorLa2ffL8o7y9MSfiYi738i7V0353T0LrNkZAN9LgZBmHqDZCvZBaGBJRAecbdvXAQN2ool8ZACetzIfQmeSb4L0PMzZCZBZCz3N8ZAzNqAHNAZDZD";
  // final String myAccessToken =
  // "EAACEdEose0cBAGa5PutLhNPq5UNcNKQev4KsI7oMSi4PyxVUhR4SvD877UZAdC7A5Txde68rufLHUSm8Us8GdtVtuQci86XAyivsjL2M2daoKu3SBS60ZBbiApNPtroCOB1HVdyju12LAvJlOn0RkZAUip8vtedNZBCNdedZAO9V9uRrc4azA0ZClzYsZBsNySBTseBZB3epYwZDZD";
  // final String myAccessToken =
  // "EAACEdEose0cBAJlOZACVjq6WZC8TzWF627V8okHTE7eZCom90S04KgHoOUCnNrNwTkWNCh6Gn8gsDAo73LoysXpaeZAkwNNZA5LVjeoFpfO4zSMwYhgcZCLl0rAsVSBNzlEMziTxwQGeWdM8V5NildZBwZA5P6sF99rPcTcuHqLEQTjCX13zBdnBAryIX6rr1oUZD";
  final String myAccessToken =
      "EAACEdEose0cBAE7O5BdNKMg6y3sw03KdcCH5ne7ILsZC09arQ00vDy6YzIM8GYwIKMDNE31iBQ4O21JQEbP5ySDXoyD7ivijOA7icQFzOACsz25Q6T1r8ZAtCHqA59dQiJ7opIdsHR536RwF4upUxQNZAwD6kEJnCdPoQOdGK1u2DVYmcH3Kq0xA9CeMuNOAu7FvJZBZAogZDZD";
  
  @Test
  public void checkFetchUser() {

    FacebookClient fbc = new DefaultFacebookClient(myAccessToken, Version.VERSION_2_10);
    // new DefaultFacebookClient(myAccessToken, requestor, new DefaultJsonMapper(), Version.VERSION_2_10);

    User me = fbc.fetchObject("me", User.class);
    // JsonObject query = fbc.fetchObjects(idList, JsonObject.class);

    @SuppressWarnings("unused")
    int toto = 3;
  }

  @Test
  public void checkFetchFriends() {

    FacebookClient fbc = new DefaultFacebookClient(myAccessToken, Version.VERSION_2_10);
    // new DefaultFacebookClient(myAccessToken, requestor, new DefaultJsonMapper(), Version.VERSION_2_10);

    Connection<User> myFriends = fbc.fetchConnection("me/friends", User.class);
    
    @SuppressWarnings("unused")
    int toto = 3;
  }

  //@Test
  public void checkFetchObjects() {
    FakeWebRequestor requestor = new FakeWebRequestor();
    FacebookClient fbc =
        new DefaultFacebookClient("accesstoken", requestor, new DefaultJsonMapper(), Version.VERSION_2_10);

    List<String> idList = new ArrayList<String>();
    idList.add("123456789 ");
    idList.add("abcdefghijkl");
    idList.add("m_mid:35723r72$bfehZFDEBDET");

    fbc.fetchObjects(idList, String.class);

    assertTrue(requestor.getSavedUrl().contains("123456789"));
    assertTrue(requestor.getSavedUrl().contains("abcdefghijkl"));
    assertTrue(requestor.getSavedUrl().contains("m_mid%3A35723r72%24bfehZFDEBDET"));
  }

  // @Test
  public void checkFetchObjects2() {

    FakeWebRequestor requestor = new FakeWebRequestor();
    FacebookClient fbc =
        new DefaultFacebookClient(myAccessToken, requestor, new DefaultJsonMapper(), Version.VERSION_2_10);

    List<String> idList = new ArrayList<String>();
    idList.add("123456789  ");
    idList.add("abcdefghijkl");
    idList.add("m_mid:35723r72$bfehZFDEBDET");

    String queryUrl = fbc.fetchObjects(idList, String.class);
    // JsonObject query = fbc.fetchObjects(idList, JsonObject.class);

    assertTrue(requestor.getSavedUrl().contains("123456789"));
    assertTrue(requestor.getSavedUrl().contains("abcdefghijkl"));
    assertTrue(requestor.getSavedUrl().contains("m_mid%3A35723r72%24bfehZFDEBDET"));
  }

  /**
   * Simple way to create a {@code FacebookClient} whose web requests always return the provided synthetic
   * {@code response}.
   * 
   * @param response
   *          The synthetic response to return.
   * @return A {@code FacebookClient} for testing.
   */
  protected FacebookClient facebookClientWithResponse(final Response response) {
    return new DefaultFacebookClient(null, new DefaultWebRequestor() {
      @Override
      public Response executeGet(String url) throws IOException {
        return response;
      }

      @Override
      public Response executePost(String url, String parameters) throws IOException {
        return response;
      }

      @Override
      public Response executePost(String url, String parameters, BinaryAttachment... binaryAttachments)
          throws IOException {
        return response;
      }

      @Override
      public Response executeDelete(String url) throws IOException {
        return response;
      }

    }, new DefaultJsonMapper(), Version.LATEST);
  }
}