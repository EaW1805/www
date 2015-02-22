package com.eaw1805.www.hibernate;

import com.eaw1805.data.HibernateUtil;
import org.springframework.util.Assert;

public class ScenarioContextHolder {

    public static int FIRST_SCENARIO = HibernateUtil.DB_S1;
    public static int LAST_SCENARIO = HibernateUtil.DB_S3;

    private static final ThreadLocal<Integer> contextHolder =
            new ThreadLocal<Integer>();

    public static void setScenario(final Integer scenario) {
        Assert.notNull(scenario, "scenario cannot be null");
        contextHolder.set(scenario);
    }

    public static void setScenario(final String scenario) {
        if (scenario == null) {
            defaultScenario();

        } else {
            if ("1804".equals(scenario)) {
                setScenario(HibernateUtil.DB_FREE);

            } else if ("1808".equals(scenario)) {
                setScenario(HibernateUtil.DB_S3);

            } else if("1805".equals(scenario)) {
                setScenario(HibernateUtil.DB_S2);

            } else if ("1802".equals(scenario)) {//else if 1802
                setScenario(HibernateUtil.DB_S1);

            } else if ("1800".equals(scenario)) {
                setScenario(HibernateUtil.DB_FIELDBATTLE);

            } else {
                defaultScenario();
            }
        }
    }

    public static Integer getScenario() {
        return contextHolder.get();
    }

    public static void defaultScenario() {
        contextHolder.remove();
    }
}
