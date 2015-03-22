package com.eaw1805.www.controllers.site;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.cache.Cachable;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Returns the HTML code of the static html pages.
 */
public class ArticleManager {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ArticleManager.class);

    /**
     * Our instance of the ArticleManager.
     */
    private static ArticleManager ourInstance = null;

    /**
     * The URL of the Static site.
     */
    private static final String DIRECT_ARTICLE_URL =
            "http://direct.eaw1805.com/site/index.php?option=com_content&view=article&id=";

    /**
     * The URL of the Static Items.
     */
    private static final String DIRECT_ITEM_URL =
            "http://direct.eaw1805.com/site/index.php?option=com_content&view=category&layout=blog&id=";

    /**
     * The URL of the Static Items.
     */
    private static final String NEWS_URL =
            "http://direct.eaw1805.com/site/index.php/news";

    /**
     * The URL of the RECAPTCHA server.
     */
    private static final String RECAPTCHA =
            "http://www.google.com/recaptcha/api/verify";

    /**
     * The URL of the phpbb server.
     */
    private static final String PHPBB =
            "http://forum.eaw1805.com/ucp.php?mode=register";

    /**
     * The URL of the server via the static alias.
     */
    private static final String URL_STATIC =
            "http://static.eaw1805.com/";

    /**
     * The URL of the server via the direct alias.
     */
    private static final String URL_DIRECT =
            "http://direct.eaw1805.com/";

    /**
     * The URL of the JENKINS server.
     */
    private static final String JENKINS =
            "http://gamekeeper.oplongames.com:8098/jenkins";

    /**
     * The HTML unit Web Client.
     */
    private final WebClient webClient;

    /**
     * Returns the ArticleManager instance.
     *
     * @return the ArticleManager
     */
    public static ArticleManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new ArticleManager();
        }
        return ourInstance;
    }

    /**
     * Default Constructor.
     */
    public ArticleManager() {
        webClient = new WebClient(BrowserVersion.FIREFOX_3);
//        webClient.setJavaScriptEnabled(true);
    }

    /**
     * Returns the html code of the specific article.
     *
     * @param remoteIP  The IP address of the user who solved the CAPTCHA.
     * @param challenge The value of "recaptcha_challenge_field" sent via the form.
     * @param response  The value of "recaptcha_response_field" sent via the form.
     * @return the String with the HTML code.
     */
    public String getCAPTCHAResult(final String remoteIP, final String challenge, final String response) {
        final StringBuilder url = new StringBuilder();
        url.append(RECAPTCHA);
        url.append("?privatekey=6LfgG9gSAAAAANscM_5hqgRN5svoDSXhZ6ZWVFTD");
        url.append("&remoteip=");
        url.append(remoteIP);
        url.append("&challenge=");
        url.append(challenge);
        url.append("&response=");
        url.append(response);

        LOGGER.info("Posting request to RECAPTCHA [" + url.toString() + "]");
        try {
            final WebRequestSettings webreq = new WebRequestSettings(new URL(url.toString()), HttpMethod.POST);
            final TextPage page = webClient.getPage(webreq);

            webClient.closeAllWindows();

            return page.getContent();

        } catch (final IOException e) {
            LOGGER.error("Error while verifying RECAPTCHA challenge", e);
        }
        return "";
    }

    /**
     * Returns the html code of the specific article.
     *
     * @param articleID the article ID.
     * @return the String with the HTML code.
     */
    @Cachable(cacheName = "constantCache")
    public String getArticleAsHtml(final int articleID) {
        LOGGER.info("Downloading Static Content for article id: " + articleID);
        try {
            final HtmlPage page = webClient.getPage(new StringBuilder().append(DIRECT_ARTICLE_URL).append(articleID).toString());

            final HtmlElement element = page.getBody().getElementById("system");
            webClient.closeAllWindows();

            return element.asXml().replaceAll(URL_DIRECT + "site/", URL_STATIC + "site/").replaceAll("src=\"/site/", "src=\"" + URL_STATIC + "site/").replaceAll("href=\"/site/", "href=\"" + URL_STATIC + "site/").replaceAll("url\\(/site/", "url(" + URL_STATIC + "site/");

        } catch (final IOException e) {
            LOGGER.error("Error while getting Article", e);
        }
        return "";
    }

    /**
     * Returns the html code of the specific article.
     *
     * @param blogID the blog ID.
     * @param itemID the item ID.
     * @return the String with the HTML code.
     */
    @Cachable(cacheName = "constantCache")
    public String getItemAsHtml(final int blogID, final int itemID) {
        LOGGER.info("Downloading Static Content for blog id: " + blogID + " and itemID: " + itemID);
        try {
            final HtmlPage page = webClient.getPage(new StringBuilder().append(DIRECT_ITEM_URL)
                    .append(blogID).append("&Itemid=").append(itemID).toString());

            final HtmlElement element = page.getBody().getElementById("system");
            webClient.closeAllWindows();

            return element.asXml().replaceAll(URL_DIRECT + "site/", URL_STATIC + "site/").replaceAll("src=\"/site/", "src=\"" + URL_STATIC + "site/").replaceAll("href=\"/site/", "href=\"" + URL_STATIC + "site/").replaceAll("url\\(/site/", "url(" + URL_STATIC + "site/");

        } catch (final IOException e) {
            LOGGER.error("Error while getting Article", e);
        }
        return "";
    }

    /**
     * Returns a List with the retrieved news.
     *
     * @return a List with the HTML of each article.
     */
    @Cachable(cacheName = "newsCache")
    public List<String> getNews() {
        return getNewsNoCache();
    }

    /**
     * Returns a List with the retrieved news.
     *
     * @return a List with the HTML of each article.
     */
    public List<String> getNewsNoCache() {
        try {
            final HtmlPage page = webClient.getPage(NEWS_URL);

            final HtmlElement element = page.getBody().getElementById("system");
            webClient.closeAllWindows();

            final List<String> articles = new ArrayList<String>();
            for (final DomElement htmlElement : element.getChildElements()) {
                for (DomElement htmlElement1 : htmlElement.getAllHtmlChildElements()) {
                    final DomElement article = htmlElement1.getAllHtmlChildElements().iterator().next();

                    articles.add(article.asXml().replaceAll(URL_DIRECT + "site/", URL_STATIC + "site/").replaceAll("src=\"/site/", "src=\"" + URL_STATIC + "site/"));
                }
            }

            return articles;

        } catch (final IOException e) {
            LOGGER.error("Error while getting Article", e);
        }

        return new ArrayList<String>();
    }

    /**
     * Returns the html code of the specific article.
     *
     * @param scenarioId the scenario to build.
     * @return the String with the HTML code.
     */
    public void getBuild(final int scenarioId, final int action) {
        // Construct Build URL
        final StringBuilder url = new StringBuilder();
        url.append(JENKINS);
        url.append("/job/");

        switch (scenarioId) {
            case HibernateUtil.DB_FREE:
                url.append("Engine%20(Scenario%201804)");
                break;

            case HibernateUtil.DB_S3:
                if (action == -1000) {
                    url.append("Custom%20Setup%20(Scenario%201808)");
                } else {
                    url.append("Engine%20(Scenario%201808)");
                }
                break;

            case HibernateUtil.DB_S2:
                if (action == -1000) {
                    url.append("Custom%20Setup%20(Scenario%201805)");
                } else {
                    url.append("Engine%20(Scenario%201805)");
                }
                break;

            case -100:
                url.append("Fieldbattle%20(Scenario%201802)");
                break;

            case -103:
                url.append("Fieldbattle%20(Scenario%201800)");
                break;

            case HibernateUtil.DB_S1:
                if (action == -1000) {
                    url.append("Custom%20Setup%20(Scenario%201802)");
                } else {
                    url.append("Engine%20(Scenario%201802)");
                }
                break;

            default:
                //then nothing to do here
                return;
        }

        url.append("/build?token=eawBUILDtoken999888");

        try {
            // Create your httpclient
            DefaultHttpClient client = new DefaultHttpClient();

            // Then provide the right credentials
            client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                    new UsernamePasswordCredentials("engine", "eng123eaw"));

            // Generate BASIC scheme object and stick it to the execution context
            BasicScheme basicAuth = new BasicScheme();
            BasicHttpContext context = new BasicHttpContext();
            context.setAttribute("preemptive-auth", basicAuth);

            // Add as the first (because of the zero) request interceptor
            // It will first intercept the request and preemptively initialize the authentication scheme if there is not
            client.addRequestInterceptor(new PreemptiveAuth(), 0);

            LOGGER.info("Posting request to JENKINS [" + url.toString() + "]");
            HttpGet get = new HttpGet(url.toString());

            // Execute your request with the given context
            HttpResponse response = client.execute(get, context);

//            LOGGER.info("Response from jenkins : " );
            HttpEntity entity = response.getEntity();

//            InputStream in =             response.getEntity().getContent();
//            String encoding = "UTF-8";
//            String body = IOUtils.toString(in, encoding);
//            System.out.println(body);
            //EntityUtils.consume(entity);

        } catch (final IOException e) {
            LOGGER.error("Error while contacting JENKINS", e);
        }
    }

    /**
     * Preemptive authentication interceptor
     */
    static class PreemptiveAuth implements HttpRequestInterceptor {

        /*
           * (non-Javadoc)
           *
           * @see org.apache.http.HttpRequestInterceptor#process(org.apache.http.HttpRequest,
           * org.apache.http.protocol.HttpContext)
           */
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            // Get the AuthState
            AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);

            // If no auth scheme available yet, try to initialize it preemptively
            if (authState.getAuthScheme() == null) {
                AuthScheme authScheme = (AuthScheme) context.getAttribute("preemptive-auth");
                CredentialsProvider credsProvider = (CredentialsProvider) context
                        .getAttribute(ClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                if (authScheme != null) {
                    Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(), targetHost
                            .getPort()));
                    if (creds == null) {
                        throw new HttpException("No credentials for preemptive authentication");
                    }
                    authState.setAuthScheme(authScheme);
                    authState.setCredentials(creds);
                }
            }
        }

    }

    public static void main(final String[] args) {
//        ArticleManager.getInstance().getBuild(HibernateUtil.DB_FREE, 0);
        /*LOGGER.info(ArticleManager.getInstance().getItemAsHtml(21, 90));
        LOGGER.info(ArticleManager.getInstance().getItemAsHtml(21, 90));*/
        ArticleManager.getInstance().getBuild(-100, 0);
    }
}
