package org.macharya.targeting;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.onehippo.cms7.targeting.data.AbstractTargetingData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by maheshacharya on 8/4/16.
 */
public class StateProvinceTargetingData extends AbstractTargetingData {
    private static final Logger log = LoggerFactory.getLogger(StateProvinceTargetingData.class);
    private String state;

    @JsonCreator
    protected StateProvinceTargetingData(@JsonProperty("collectorId") String collectorId) {
        super(collectorId);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
