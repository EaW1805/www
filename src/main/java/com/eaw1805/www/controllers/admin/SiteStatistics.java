package com.eaw1805.www.controllers.admin;

import com.eaw1805.data.cache.Cachable;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.managers.beans.EngineProcessManagerBean;
import com.eaw1805.data.managers.beans.PaymentHistoryManagerBean;
import com.eaw1805.data.model.User;
import com.eaw1805.www.commands.ChartData;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class SiteStatistics
        extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(SiteStatistics.class);

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

    /**
     * Instance of PaymentHistoryManager class.
     */
    protected transient PaymentHistoryManagerBean pmHistoryManager;

    public void setPmHistoryManager(PaymentHistoryManagerBean pmHistoryManager) {
        this.pmHistoryManager = pmHistoryManager;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/statistics")
    public ModelAndView setupPage(final ModelMap model) throws InvalidPageException {
        try {
            final User thisUser = getUser();

            //be sure user is logged in.
            if (thisUser == null || thisUser.getUserId() <= 0 || thisUser.getUserType() != 3) {
                throw new InvalidPageException("Page not found");
            }

            // Titles of charts
            final String[] chartTitle = {"User Growth Rate",
                    "Active Users Growth Rate",
                    "Engine Turn's Growth Rate",
                    "Credits Charges Growth Rate",
                    "Credits Purchases Growth Rate",
                    //"Issued Orders Growth Rate (Scenario 1802)",
                    //"Issued Orders Growth Rate (Scenario 1805)",
                    "Issued Orders Growth Rate (Scenario 1802, 1805)"
            };

            // Titles of series
            final String[] seriesTitle = {"Weekly User Registrations",
                    "Weekly Active Users",
                    "Weekly Engine Invocations",
                    "Weekly Credit Charges",
                    "Weekly Credit Purchases",
                    //"Weakly Issued Orders",
                    //"Weakly Issued Orders",
                    "Weakly Issued Orders"};

            // Titles of axis
            final String[] axisTitle = {"New Users",
                    "Active Users",
                    "Engine Invocations",
                    "Credit Charges",
                    "Credit Purchases",
                    //"Orders Issued (Scenario 1802)",
                    //"Orders Issued (Scenario 1805)",
                    "Issued Orders",
            };

            // Data
            final List<List<ChartData>> chartData = new ArrayList<List<ChartData>>();
            chartData.add(getGrowth(getUserManager().countByWeek(), 10d));
            chartData.add(getGrowth(pmHistoryManager.playerChargesByWeek("game"), 10d));
            chartData.add(getGrowth(engineManager.countByWeek(), 100d));
            chartData.add(getGrowth(pmHistoryManager.chargesByWeek("game"), 1500d));
            chartData.add(getGrowth(pmHistoryManager.chargesByWeek("paypal"), 2000d));
            getGrowthOrders(chartData);

            model.put("chartTitle", chartTitle);
            model.put("seriesTitle", seriesTitle);
            model.put("axisTitle", axisTitle);
            model.put("chartData", chartData);

            LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Site Statistics");
            return new ModelAndView("admin/statistics", model);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Cachable(cacheName = "adminCache")
    protected final List<ChartData> getGrowth(final Map<Integer, Map<Integer, Long>> userMap, final double initTotal) {
        // setup statistics
        final Map<Long, ChartData> sortedData = new TreeMap<Long, ChartData>();

        // Variables to calc the running total
        final Calendar cal = Calendar.getInstance();
        double totUsers = initTotal;

        for (Map.Entry<Integer, Map<Integer, Long>> yearMap : userMap.entrySet()) {
            for (Map.Entry<Integer, Long> weekMap : yearMap.getValue().entrySet()) {
                cal.set(Calendar.YEAR, yearMap.getKey());
                cal.set(Calendar.WEEK_OF_YEAR, weekMap.getKey() + 1);

                final ChartData thisWeek = new ChartData();
                thisWeek.setYear(cal.get(Calendar.YEAR));
                thisWeek.setMonth(cal.get(Calendar.MONTH));
                thisWeek.setDay(cal.get(Calendar.DATE));
                thisWeek.setValue(weekMap.getValue().intValue());

                // calculate growth
                final double growth = 100d * weekMap.getValue().doubleValue() / totUsers;
                thisWeek.setDoubleValue(growth);

                sortedData.put(cal.getTimeInMillis(), thisWeek);
                totUsers += weekMap.getValue().doubleValue();
            }
        }

        // Sort chart data
        final List<ChartData> growthUsers = new ArrayList<ChartData>();
        for (ChartData chartData : sortedData.values()) {
            growthUsers.add(chartData);
        }

        return growthUsers;
    }

    @Cachable(cacheName = "adminCache")
    protected void getGrowthOrders(final List<List<ChartData>> dataMap) {
        final Map<Integer, Map<Integer, Long>> totalMap = new HashMap<Integer, Map<Integer, Long>>();

        final Calendar cal = Calendar.getInstance();

        for (int scenarioId = HibernateUtil.DB_FIRST; scenarioId <= HibernateUtil.DB_LAST; scenarioId++) {
            // setup statistics
            final Map<Integer, Map<Integer, Long>> orderMap = engineManager.countByWeek(scenarioId);
            final Map<Long, ChartData> sortedData = new TreeMap<Long, ChartData>();

            // Variables to calc the running total
            double totOrders = 10000d;

            for (Map.Entry<Integer, Map<Integer, Long>> yearMap : orderMap.entrySet()) {
                // Keep track of global total
                Map<Integer, Long> totalYear;
                if (!totalMap.containsKey(yearMap.getKey())) {
                    totalYear = new HashMap<Integer, Long>();
                    totalMap.put(yearMap.getKey(), totalYear);

                } else {
                    totalYear = totalMap.get(yearMap.getKey());
                }

                // Iterate through weeks of year
                for (Map.Entry<Integer, Long> weekMap : yearMap.getValue().entrySet()) {
                    // update global total
                    if (!totalYear.containsKey(weekMap.getKey())) {
                        totalYear.put(weekMap.getKey(), weekMap.getValue());

                    } else {
                        totalYear.put(weekMap.getKey(), totalYear.get(weekMap.getKey()) + weekMap.getValue());
                    }

                    cal.set(Calendar.YEAR, yearMap.getKey());
                    cal.set(Calendar.WEEK_OF_YEAR, weekMap.getKey() + 1);

                    final ChartData thisWeek = new ChartData();
                    thisWeek.setYear(cal.get(Calendar.YEAR));
                    thisWeek.setMonth(cal.get(Calendar.MONTH));
                    thisWeek.setDay(cal.get(Calendar.DATE));
                    thisWeek.setValue(weekMap.getValue().intValue());

                    // calculate growth
                    final double growth = 100d * weekMap.getValue().doubleValue() / totOrders;
                    thisWeek.setDoubleValue(growth);

                    sortedData.put(cal.getTimeInMillis(), thisWeek);
                    totOrders += weekMap.getValue().doubleValue();
                }
            }

            // Sort chart data
            final List<ChartData> growthOrders = new ArrayList<ChartData>();
            for (ChartData chartData : sortedData.values()) {
                growthOrders.add(chartData);
            }

            // temporarily disabled
            // dataMap.add(growthOrders);
        }

        // sort global data
        final Map<Long, ChartData> globalData = new TreeMap<Long, ChartData>();
        double globOrders = 10000d;
        for (Map.Entry<Integer, Map<Integer, Long>> yearMap : totalMap.entrySet()) {
            for (Map.Entry<Integer, Long> weekMap : yearMap.getValue().entrySet()) {
                cal.set(Calendar.YEAR, yearMap.getKey());
                cal.set(Calendar.WEEK_OF_YEAR, weekMap.getKey() + 1);

                final ChartData thisWeek = new ChartData();
                thisWeek.setYear(cal.get(Calendar.YEAR));
                thisWeek.setMonth(cal.get(Calendar.MONTH));
                thisWeek.setDay(cal.get(Calendar.DATE));
                thisWeek.setValue(weekMap.getValue().intValue());

                // calculate growth
                final double growth = 100d * weekMap.getValue().doubleValue() / globOrders;
                thisWeek.setDoubleValue(growth);

                globalData.put(cal.getTimeInMillis(), thisWeek);
                globOrders += weekMap.getValue().doubleValue();
            }
        }

        // Sort global data
        final List<ChartData> growthTotalOrders = new ArrayList<ChartData>();
        for (ChartData chartData : globalData.values()) {
            growthTotalOrders.add(chartData);
        }

        dataMap.add(growthTotalOrders);
    }


}
