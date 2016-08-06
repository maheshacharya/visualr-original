package org.macharya.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheshacharya on 8/2/16.
 */
public class GeoJsonInfo implements Serializable{

    private String defaultCharacteristic;
    private List<String> characteristics;
    private String zoomLevel;
    private String latitude;
    private String longitude;

    public String getDefaultCharacteristic() {
        return defaultCharacteristic;
    }

    public void setDefaultCharacteristic(String defaultCharacteristic) {
        this.defaultCharacteristic = defaultCharacteristic;
    }

    public List<String> getCharacteristics() {
        if (characteristics == null) {
            characteristics = new ArrayList();
        }
        return characteristics;
    }

    public void setCharacteristics(List<String> characteristics) {
        this.characteristics = characteristics;
    }

    public String getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(String zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
