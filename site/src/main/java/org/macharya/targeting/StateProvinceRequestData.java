package org.macharya.targeting;

import com.onehippo.cms7.targeting.geo.Location;
import org.macharya.services.StateLocation;

/**
 * Created by maheshacharya on 8/4/16.
 */
public class StateProvinceRequestData {


    private String state;


    public StateProvinceRequestData() {
    }

    public StateProvinceRequestData(Location location) {
        state = ((StateLocation) location).getState();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }



}
