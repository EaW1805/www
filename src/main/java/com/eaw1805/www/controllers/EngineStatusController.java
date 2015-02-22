package com.eaw1805.www.controllers;

import com.eaw1805.data.managers.beans.EngineProcessManagerBean;
import com.eaw1805.data.model.EngineProcess;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Reports engine status in timeline XML format.
 */
public class EngineStatusController
        implements Controller {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(GenericController.class);


    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {

        // retrieve user
        LOGGER.debug("Engine Status (XML)");

        // Set the content type
        response.setContentType("application/xml");

        final PrintWriter pwr = response.getWriter();

        // header
        pwr.write("<data \n" +
                "    wiki-url=\"http://www.eaw1805.com/\"\n" +
                "    wiki-section=\"Empires at War Engine Status\"\n" +
                "    >\n");

        // iterate list of turn processes
        final List<EngineProcess> lstExecutions = engineManager.list();
        for (final EngineProcess process : lstExecutions) {
            // Get real date
            final Calendar realCal = Calendar.getInstance();
            realCal.setTime(process.getDateStart());
            final String realStart = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss Z").format(new Date());

            realCal.add(Calendar.SECOND, process.getDuration());
            final String realEnd = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss Z").format(new Date());

            // Get game date
            final Calendar turnCal = Calendar.getInstance();
            turnCal.set(1805, Calendar.JANUARY, 1);
            turnCal.add(Calendar.MONTH, process.getTurn());
            final String turnTitle = "Game " + process.getGameId() + " / " + turnCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);

            pwr.write("    <event start=\"" + realStart + "\"\n" +
                    "        end=\"" + realEnd + "\"\n" +
                    "        isDuration=\"true\"\n" +
                    "        title=\"'" + turnTitle + "'\">");

            pwr.write("        </event>\n");
        }

        // footer
        pwr.write("</data>");

        pwr.flush();
        pwr.close();
        return null;
    }

    /**
     * Instance EngineProcessManager class to perform queries
     * about engineProcess objects.
     */
    private transient EngineProcessManagerBean engineManager;

    /**
     * Setter method used by spring to inject a EngineProcessManagerBean bean.
     *
     * @param injEngineManager a EngineProcessManagerBean bean.
     */
    public void setEngineProcessManager(final EngineProcessManagerBean injEngineManager) {
        engineManager = injEngineManager;
    }

}
