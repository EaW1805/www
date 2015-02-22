package com.eaw1805.www.controllers.site;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

/**
 * Test cases for the site controllers.
 */
public class TestSiteControllers {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(TestSiteControllers.class);

    public void retrieveAbout() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage page = webClient.getPage("http://static.eaw1805.com/site/index.php?option=com_content&view=article&id=20");
        final HtmlDivision div = page.getHtmlElementById("system");

        LOGGER.debug(div.asXml());

        webClient.closeAllWindows();
    }

    /**
     * Simple execution.
     *
     * @param args no arguments needed here
     */
    public static void main(final String[] args) throws Exception {
        final TestSiteControllers testSite = new TestSiteControllers();
        testSite.retrieveAbout();
    }

}
