package com.eaw1805.www.controllers.social;

import com.eaw1805.data.managers.UserManager;
import com.eaw1805.data.managers.beans.UserManagerBean;
import com.eaw1805.data.model.User;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class FacebookImporter {


    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(SocialController.class);

    /**
     * This method is used to save a facebook user to our database as a normal user.
     */
    public User saveUpdateFacebookUser(String accessToken, final UserManagerBean userManager, final User thisUser) throws ParseException, JSONException, NoSuchAlgorithmException {
        if (accessToken == null) {
            return null;
        }
        final Facebook facebook = new FacebookTemplate(accessToken);
        //debug messages to see what you received.
        LOGGER.debug("email? " + facebook.userOperations().getUserProfile().getEmail());
        LOGGER.debug("firstname? " + facebook.userOperations().getUserProfile().getFirstName());
        LOGGER.debug("middlename? " + facebook.userOperations().getUserProfile().getMiddleName());
        LOGGER.debug("lastname? " + facebook.userOperations().getUserProfile().getLastName());
        if (facebook.userOperations().getUserProfile().getHometown() != null) {
            LOGGER.debug("townname? " + facebook.userOperations().getUserProfile().getHometown().getName());
            LOGGER.debug("townname? " + facebook.userOperations().getUserProfile().getHometown().getId());
        }
        if (facebook.userOperations().getUserProfile().getLocation() != null) {
            LOGGER.debug("locationname? " + facebook.userOperations().getUserProfile().getLocation().getName());
        }
        LOGGER.debug("userid? " + facebook.userOperations().getUserProfile().getId());
        LOGGER.debug("username? " + facebook.userOperations().getUserProfile().getName());
        LOGGER.debug("timezone? " + facebook.userOperations().getUserProfile().getTimezone());


        //try to find the country for this user, if no country given back, Facebook will be used.
        final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        final String query = "SELECT current_location FROM user WHERE uid=" + facebook.userOperations().getUserProfile().getId();
        parameters.set("q", query);
        Map<String, Object> resultSet = (Map<String, Object>) facebook.fetchObject("fql", Map.class, parameters);
        String country = "Facebook";
        //parse the result screen to locate the country attribute.
        if (resultSet.containsKey("data")) {
            final String data = resultSet.get("data").toString();
            if (data.contains("country=")) {
                String countryData =  data.split("country=")[1];
                if (!countryData.startsWith(",")) {
                    country = countryData.split(",")[0];
                }
            }
        }

        //check if facebook user already exists
        final List<User> users = userManager.searchByFacebookId(facebook.userOperations().getUserProfile().getId());
        final User user;
        String email = facebook.userOperations().getUserProfile().getEmail();
        if (email == null) {
            email = "facebook_denied_email";
        }
        if (users.isEmpty()) {
            //if user doesn't exist, create a new one.
            user = new User();
            user.setDateJoin(new Date().getTime());
            user.setUsername(generateUniqueName(facebook.userOperations().getUserProfile().getName(), userManager));
            user.setFacebookId(facebook.userOperations().getUserProfile().getId());
            user.setFacebookAccessToken(accessToken);
            user.setEmail(email);
            user.setEnableNotifications(true);
            user.setPublicMail(false);
            user.setFullname(facebook.userOperations().getUserProfile().getLastName() + " " + facebook.userOperations().getUserProfile().getMiddleName() + " " + facebook.userOperations().getUserProfile().getFirstName());
            user.setLocation(country);
            user.setTimezone(facebook.userOperations().getUserProfile().getTimezone());

            user.setPassword("facebook");
            user.setRemoteAddress(thisUser.getRemoteAddress());

            user.setPassword(convertToMD5(user.getPassword()));
            user.setEmailEncoded(convertToMD5(email));
            user.setCreditFree(50);

            LOGGER.info("Adding a new user");
            userManager.addUser(user);

        } else {
            user = users.get(0);
            user.setFacebookAccessToken(accessToken);
            LOGGER.info("ACCESS TOKEN UPDATE " + user.getUsername() + " - " + user.getFacebookAccessToken());
            userManager.update(user);
        }

        return userManager.searchByFacebookId(user.getFacebookId()).get(0);

    }

    public String generateUniqueName(String username, final UserManagerBean userManager) {
        int counter = 1;
        String temp = username;
        while (userManager.getByUserName(username) != null) {
            username = temp + counter;
            counter++;
        }
        return username;
    }

    /**
     * Encrypts the password string of the new user.
     *
     * @param password the password inserted to the form
     * @return the encrypted password.
     * @throws java.security.NoSuchAlgorithmException
     *          No encryption algorithm exception.
     */
    protected String convertToMD5(final String password) throws NoSuchAlgorithmException {

        final MessageDigest messageDialect = MessageDigest.getInstance("MD5");
        messageDialect.update(password.getBytes());

        final byte byteData[] = messageDialect.digest();

        //convert the byte to hex format
        final StringBuilder hexString = new StringBuilder();

        for (final byte singleByte : byteData) {
            final String hex = Integer.toHexString(0xff & singleByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}


