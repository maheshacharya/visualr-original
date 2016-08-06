package org.macharya.services;

import com.onehippo.cms7.targeting.geo.Location;

/**
 * Created by maheshacharya on 8/5/16.
 */
public class StateLocation extends Location {



    public StateLocation(String continentCode, String country, String city, double latitude, double longitude) {
        super(continentCode, country, city, latitude, longitude);
    }

    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
