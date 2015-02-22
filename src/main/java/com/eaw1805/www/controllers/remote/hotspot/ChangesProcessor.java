package com.eaw1805.www.controllers.remote.hotspot;

import com.eaw1805.data.constants.OrderConstants;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ChangesProcessor
        extends OrderConstants {

    void addData(Collection<?> dbData, Collection<?> chData);

    void addData(Map<?, ?> dbData, Map<?, ?> chData);

    List<?> processChanges();

}
