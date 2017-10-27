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
package com.restfb.voyans;

import com.restfb.BinaryAttachment;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultJsonMapper;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.WebRequestor.Response;
import com.restfb.exception.FacebookNetworkException;
import com.restfb.types.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://restfb.com">Mark Allen</a>
 */
public class FacebookClientVoyansTest {

    final String facebookFieldList = "first_name,middle_name,name_format,payment_pricepoints,last_name,short_name,relationship_status,age_range,public_key,picture,hometown,significant_other,is_verified,interested_in,meeting_for,favorite_teams,favorite_athletes,inspirational_people";

    final String facebookExtFieldList = "id,name,picture,about,band_interests,bio,birthday,genre,hometown,culinary_team,verification_status,website";

    // -- short lived access token
    // final String myAccessToken =
    // "X";

    // -- long lived access token expires on December 24th, 2017:
//    final String myAccessToken = "X";
    final String myAccessToken = "X";

    // @Test
    public void checkFetchUser() {
        
        FacebookClient fbc = new DefaultFacebookClient(myAccessToken, Version.VERSION_2_10);

        UserVoyans me = fbc.fetchObject("me", UserVoyans.class, Parameter.with("fields", facebookFieldList));

        System.out.println(" me = " + me);

        @SuppressWarnings("unused")
        int toto = 3;
    }

    // @Test
    public void checkNumberFriends() {

        FacebookClient fbc = new DefaultFacebookClient(myAccessToken, Version.VERSION_2_10);

        Connection<User> myFriends = null;
        try {
            myFriends = fbc.fetchConnection("me/friends", User.class);
        } catch (FacebookNetworkException fbne) {
            System.out.println("fbne = " + fbne);
        }

        System.out.println(" myFriends.getTotalCount() = " + myFriends.getTotalCount());
    }

    // @Test
    public void checkTaggableFriends() {

        FacebookClient fbc = new DefaultFacebookClient(myAccessToken, Version.VERSION_2_10);

        List<UserVoyans> friends = new ArrayList<UserVoyans>(50);
        Connection<UserVoyans> connections = fbc.fetchConnection("/me/taggable_friends", UserVoyans.class);
        int i = 1;
        for (UserVoyans connection : connections.getData()) {
            friends.add(new UserVoyans(null, connection.getName(), connection.getPicture()));
            System.out.println(i + " connection.getName() = " + connection.getName());
            i++;
        }
        System.out.println(" friends.getTotalCount() = " + friends.size());

        @SuppressWarnings("unused")
        int toto = 3;
    }

    // @Test
    public void checkFetchLikes() {

        // Pages liked
        FacebookClient fbc = new DefaultFacebookClient(myAccessToken, Version.VERSION_2_10);

        List<UserVoyans> friends = new ArrayList<UserVoyans>(50);

        Connection<UserVoyans> likes = fbc.fetchConnection("me/likes", UserVoyans.class);

        int i = 1;
        for (UserVoyans like : likes.getData()) {
            friends.add(new UserVoyans(like.getId(), like.getName(), null));
            System.out.println(i + "  " + like.getName() + "    ---- id=" + like.getId());

            System.out.println(" =================================================== like.getId() = " + like.getId());
            UserVoyans user = fbc.fetchObject(like.getId(), UserVoyans.class, Parameter.with("fields", facebookExtFieldList));
            // UserVoyans user = fbc.fetchObject("817871031724357" /*like.getId()*/, UserVoyans.class, Parameter.with("fields",
            // facebookFieldList));
            System.out.println(i + " " + user.toString2());

            if (i == 3)
                break;
            i++;
        }
        System.out.println(" friends.getTotalCount() = " + friends.size());

        @SuppressWarnings("unused")
        int toto = 3;
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

    
    //
    public static void main(String args[]) {
        FacebookClientVoyansTest fc = new FacebookClientVoyansTest();
        fc.checkFetchUser();
        fc.checkNumberFriends();
        fc.checkTaggableFriends();
        fc.checkFetchLikes();
    }
}
