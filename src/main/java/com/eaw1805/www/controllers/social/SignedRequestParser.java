package com.eaw1805.www.controllers.social;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.StringTokenizer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;



import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.social.connect.ConnectionData;
import sun.misc.BASE64Decoder;

/**
 * Facebook signed_request parser.
 *
 * @author Jose Luis Martin
 */
public class SignedRequestParser {

    public static final String SIGN_ALGORITHM = "HMACSHA256";
    private static final Log log = LogFactory.getLog(SignedRequestParser.class);

    private String secret;

    public JSONObject execute(String signedReq) throws Exception
    {
        //Once user authorized the application, FB returns the following parameter

        if(signedReq == null)
        {
            System.out.println("ERROR: Unable to retrieve signed_request parameter");
            return null;
        }
        int count = 0;
        String payload = null;
        //The parameter contains encoded signature and payload separated by ‘.’
        StringTokenizer st = new StringTokenizer(signedReq, ".");
        //Retrieve payload (Note: encoded signature is used for internal verification and it is optional)
        while (st.hasMoreTokens())
        {
            if(count == 1)
            {
                payload = st.nextToken();
                break;
            }
            else
                st.nextToken();

            count++;
        }

        //Initialize Base64 decoder


        //Replace special character in payload as indicated by FB
        payload = payload.replace("-", "+").replace("_", "/").trim();
        //Decode payload
        try
        {
            byte[] decodedPayload = com.caucho.util.Base64.decode(padBase64(payload)).getBytes();
            payload = new String(decodedPayload, "UTF8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        //JSON Decode - payload
        try
        {
            System.out.println(payload);
            JSONObject payloadObject = (JSONObject)new JSONParser().parse(payload);


            return payloadObject;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("ERROR: Unable to perform JSON decode");
            return null;
        }
//        System.out.println("9");

    }


    public static String padBase64(String b64) {
        String padding = "";
        // If you are a java developer, *this* is the critical bit.. FB expects
        // the base64 decode to do this padding for you (as the PHP one
        // apparently
        // does...
        switch (b64.length() % 4) {
            case 0:
                break;
            case 1:
                padding = "===";
                break;
            case 2:
                padding = "==";
                break;
            default:
                padding = "=";
        }
        return b64 + padding;

    }


    public ConnectionData parse(String signedRequest, String secret) {
        this.secret = secret;
        System.out.println("**parsing**");
        System.out.println("--" + signedRequest);
        System.out.println("--" + secret);
        ConnectionData data = null;

        if (signedRequest == null)
            return null;
        try {
            String[] requestArray = signedRequest.split("\\.");
            System.out.println(requestArray[0]);
            System.out.println(requestArray[1]);


            String payload = requestArray[1];
            payload = payload.replace("-", "+").replace("_", "/").trim();
            System.out.println("Verified? " + verifySign(requestArray[0], payload));
            if (requestArray.length == 2 && verifySign(requestArray[0], payload)) {
                System.out.println("false");


                String decoded = com.caucho.util.Base64.decode(padBase64(payload));
                JSONObject json = (JSONObject)new JSONParser().parse(decoded);
                String providerUserId = json.get("user_id").toString();
                String accessToken = json.get("oauth_token").toString();
                data = new ConnectionData("facebook", providerUserId, "", "", null, accessToken,
                        secret, null, null);
            } else {
                System.out.println("false");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
//            log.error(e);
        }

        return data;
    }

    /**
     * Verify payload signature
     */
    private boolean verifySign(String sign, String payload) {
        SecretKeySpec sks = new SecretKeySpec(secret.getBytes(), SIGN_ALGORITHM);
        Mac mac;
        try {
            mac = Mac.getInstance(SIGN_ALGORITHM);
            mac.init(sks);
            byte[] my = mac.doFinal(payload.getBytes());
            System.out.println("sign : " + sign);
            byte[] their = com.caucho.util.Base64.decode(padBase64(sign)).getBytes();

            return Arrays.equals(my, their);

        } catch (Exception nsae) {
            nsae.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws ParseException {
        String txt = "{\"algorithm\":\"HMAC-SHA256\",\"expires\":1392159600,\"issued_at\":1392152691,\"oauth_token\":\"CAAU2mOeuZCwkBAKBNZBzd7RHZA475ogCyEot6N3QZCgmni1q1pks1C8LApWtS61ndqpTkPcAVAd4SiHorrHkDtLZALpeaKzmBqwyEgla8I67ihwfDrVQ8pZBEZBQI9QwQAWUT8S6nduFEG02iETidztzNh0fQfm6znJhhfpMkUNR3SqXB5yijLuiPNVnQgNkAIZD\",\"user\":{\"country\":\"gr\",\"locale\":\"en_US\",\"age\":{\"min\":21}},\"user_id\":\"1192039370\"}";
        System.out.println(txt);
        JSONObject json = (JSONObject)new JSONParser().parse(txt);

        System.out.println(json.get("user_id"));
        System.out.println(json.get("oauth_token"));
    }

    /**
     * @return the secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * @param secret the secret to set
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }
}
