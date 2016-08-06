package org.macharya.beans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import java.util.Calendar;

@HippoEssentialsGenerated(internalName = "visualr:geojson")
@Node(jcrType = "visualr:geojson")
public class Geojson extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "visualr:title")
    public String getTitle() {
        return getProperty("visualr:title");
    }

    @HippoEssentialsGenerated(internalName = "visualr:dateCreated")
    public Calendar getDateCreated() {
        return getProperty("visualr:dateCreated");
    }

    @HippoEssentialsGenerated(internalName = "visualr:geoJsonData")
    public String getGeoJsonData() {
        return getProperty("visualr:geoJsonData");
    }

    @HippoEssentialsGenerated(internalName = "visualr:licenseTerms")
    public String getLicenseTerms() {
        return getProperty("visualr:licenseTerms");
    }

    @HippoEssentialsGenerated(internalName = "visualr:mapLayerName")
    public String getMapLayerName() {
        return getProperty("visualr:mapLayerName");
    }
}
