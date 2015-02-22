package com.eaw1805.www.hibernate;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class ScenarioRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return ScenarioContextHolder.getScenario();
    }
}
