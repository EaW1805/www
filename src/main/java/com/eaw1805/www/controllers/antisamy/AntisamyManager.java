package com.eaw1805.www.controllers.antisamy;

import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;

import java.net.URL;

/**
 * Antisamy Security Manager. Prevents XSS attacks.
 */
public class AntisamyManager {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(AntisamyManager.class);

    /**
     * Our instance of the AntisamyManager.
     */
    private static AntisamyManager ourInstance = null;

    /**
     * Antisamy policy file, allows image tags.
     */
    private static final String POLICY_FILE_WITH_IMG = "antisamy-ebay-1.4.4.xml";

    /**
     * Antisamy policy file, without image tags.
     */
    private static final String POLICY_FILE_WITHOUT_IMG = "antisamy-slashdot-1.4.4.xml";

    /**
     * The antisamy Object.
     */
    private final AntiSamy antiSamy;

    /**
     * The Policy which allows imgTags.
     */
    private Policy policyImg;

    /**
     * The Policy without image tags.
     */
    private Policy policyNoImg;

    /**
     * Returns the AntisamyManager instance.
     *
     * @return the AntisamyManager
     */
    public static AntisamyManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new AntisamyManager();
        }
        return ourInstance;
    }

    /**
     * Default Constructor.
     */
    public AntisamyManager() {
        final URL urlWithImg = getClass().getResource("/empire/webapp/resources/antisamy/" + POLICY_FILE_WITH_IMG);
        final URL urlWithoutImg = getClass().getResource("/empire/webapp/resources/antisamy/" + POLICY_FILE_WITHOUT_IMG);

        try {
            policyImg = Policy.getInstance(urlWithImg);
            policyNoImg = Policy.getInstance(urlWithoutImg);
        } catch (final PolicyException e) {
            LOGGER.fatal(e);
        }

        antiSamy = new AntiSamy();
    }

    /**
     * Scan the dirty html input.
     *
     * @param dirtyInput a String with the HTML input.
     * @return a String with clean HTML code.
     */
    public String scanWithImgTag(final String dirtyInput) {

        try {
            final CleanResults cleanResult = antiSamy.scan(dirtyInput, policyImg, AntiSamy.SAX);
            return cleanResult.getCleanHTML();
        } catch (Exception e) {
            return dirtyInput;
        }
    }

    /**
     * Scan the dirty html input.
     *
     * @param dirtyInput a String with the HTML input.
     * @return a String with clean HTML code.
     */
    public String scanWithOutImgTag(final String dirtyInput) {
        try {
            final CleanResults cleanResult = antiSamy.scan(dirtyInput, policyNoImg, AntiSamy.SAX);
            return cleanResult.getCleanHTML();
        } catch (Exception e) {
            return dirtyInput;
        }
    }
}
