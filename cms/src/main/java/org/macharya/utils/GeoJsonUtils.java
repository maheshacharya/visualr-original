package org.macharya.utils;

import com.google.gson.Gson;
import org.macharya.model.GeoJsonInfo;

import javax.jcr.*;

/**
 * Created by maheshacharya on 8/2/16.
 */
public class GeoJsonUtils {

    public static String getGeoJsonInfo(Node node) throws RepositoryException {
        NodeIterator nodeIterator = node.getNodes();
        while (nodeIterator.hasNext()) {
            Node n = nodeIterator.nextNode();
            if (n.hasProperty("hippostd:stateSummary") && n.hasProperty("hippostd:state")) {
                if (n.getProperty("hippostd:state").getString().equals("published") &&
                        n.getProperty("hippostd:stateSummary").getString().equals("live")) {
                    GeoJsonInfo info = processGeoJsonDoc(n);
                    Gson gson = new Gson();
                    return gson.toJson(info);

                }
            }

        }
        return null;
    }

    private static String[] properties = {
            "visualr:defaultcharacteristic",
            "visualr:mapLayerName",
            "visualr:title",
            "visualr:zoomLevel",
            "visualr:characteristics",
            "visualr:latitude",
            "visualr:longitude"

    };

    private static GeoJsonInfo processGeoJsonDoc(Node n) throws RepositoryException {
        Node mapCenter = n.getNode("visualr:mapCenter");

        GeoJsonInfo info = new GeoJsonInfo();
        for (String prop : properties) {
            try {
                if (n.hasProperty(prop)) {
                    processProperty(n, prop, info);
                } else {
                    if (mapCenter.hasProperty(prop)) {
                        processProperty(mapCenter, prop, info);
                    }
                }

            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }
        return info;
    }

    private static void processProperty(Node n, String prop, GeoJsonInfo info) throws RepositoryException {

        Property property = n.getProperty(prop);
        if (property.isMultiple()) {
            Value[] values = property.getValues();
            for (Value val : values) {
                info.getCharacteristics().add(val.getString());

            }


        } else {
            if (prop.equals("visualr:defaultcharacteristic")) {
                info.setDefaultCharacteristic(property.getString());
            } else if (prop.equals("visualr:zoomLevel")) {
                info.setZoomLevel(property.getString());
            } else if (prop.equals("visualr:latitude")) {
                info.setLatitude(property.getString());
            } else if (prop.equals("visualr:longitude")) {
                info.setLongitude(property.getString());
            } else if (prop.equals("visualr:characteristics")) {
                info.getCharacteristics().add(property.getString());
            }
        }

    }
}
