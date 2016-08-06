package org.macharya.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maheshacharya on 7/28/16.
 */
public class Message {

    Map<String, String> properties = new HashMap();

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }



}
