package org.macharya;

import com.google.gson.Gson;
import org.apache.jackrabbit.commons.JcrUtils;
import org.junit.Test;
import org.macharya.model.GeoJsonInfo;
import org.macharya.utils.GeoJsonUtils;

import javax.jcr.*;

/**
 * Created by maheshacharya on 8/2/16.
 */
public class TestGeoJson {

    @Test
    public void test() {
        try {
            Repository repo = JcrUtils.getRepository("rmi://127.0.0.1:1099/hipporepository");
            Session session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
            Node node = session.getNode("/content/documents/geo-targeting/geojson/world-countries");
            System.out.println(GeoJsonUtils.getGeoJsonInfo(node));
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }


}
