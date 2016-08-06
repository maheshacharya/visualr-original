package org.macharya.services;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.hippoecm.frontend.session.UserSession;
import org.macharya.utils.GeoJsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Binary;
import javax.jcr.*;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.ws.rs.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by maheshacharya on 7/26/16.
 */
public class GeoJsonResource {
    private static Logger logger = LoggerFactory.getLogger(GeoJsonResource.class);
    private static final String GEOJSON_CONTENT_FOLDER = "/content/documents/geo-targeting/geojson";
    private static final String GEOJSON_DOC_QUERY = "//element(*,visualr:geojson)[hippostd:state='published' and hippo:availability='live']";
    private static final String GEOJSON_MAP_LAYER = "//element(*,visualr:geojson)[@visualr:mapLayerName='%s' and hippostd:state='published' and hippo:availability='live']";
    private static final String MAP_LAYER_NAME = "world-countries";
    private static final String GEOJSON_CONTENT_PATH = "/content/documents/geo-targeting/geojson/";
    private static final String GEOJSON_DATA_PROEPRTY = "visualr:geoJsonData";
    private static final String VALUE_LIST_QUERY = "//element(*, selection:valuelist)[fn:name()='%s']/element(*,selection:listitem)  order by @selection:label";
    private static final String TARGETING_CHARACTERISTICS_NODE_PATH = "/targeting:targeting/targeting:characteristics/";
    private String queryString;

    private Session getSession() {
        return UserSession.get().getJcrSession();
    }

    /**
     * @param query
     * @param session
     * @param queryType
     * @return
     * @throws RepositoryException
     */
    private Query getQuery(String query, Session session, String queryType) throws RepositoryException {
        return session.getWorkspace().getQueryManager().createQuery(query, queryType);
    }

    /**
     * @param query
     * @return
     * @throws RepositoryException
     */
    private Query getQuery(String query) throws RepositoryException {
        Session session = getSession();
        return getQuery(query, session, Query.XPATH);
    }

    @Path("/layerProperties")
    @GET
    public String getLayerProperties(@QueryParam("mapLayerName") String mapLayerName) {
        try {

            Node node = getSession().getNode(GEOJSON_CONTENT_PATH + mapLayerName);
            return GeoJsonUtils.getGeoJsonInfo(node);

        } catch (RepositoryException e) {
            logger.warn("Error", e);
        }
        return "";
    }


    @Path("/layer")
    @GET
    public String getMapLayerName(@QueryParam("mapLayerName") String mapLayerName) {


        try {

            String queryString = String.format(GEOJSON_MAP_LAYER, mapLayerName);
            Query query = getQuery(queryString);
            QueryResult result = query.execute();
            if (result != null) {
                if (result.getNodes().hasNext()) {
                    Node mapLayerDoc = result.getNodes().nextNode();
                    String data = mapLayerDoc.getProperty(GEOJSON_DATA_PROEPRTY).getString();
                    if (StringUtils.isEmpty(data)) {
                        Node node = mapLayerDoc.getNode("visualr:geoJsonDataLink");
                        if (node != null) {
                            String uuid = node.getProperty("hippo:docbase").getString();
                            Node jsonasset = getSession().getNodeByIdentifier(uuid);
                            Node asset = jsonasset.getNodes().nextNode().getNode("hippogallery:asset");
                            Binary bin = asset.getProperty("jcr:data").getBinary();
                            InputStream in = bin.getStream();
                            BufferedInputStream bis = new BufferedInputStream(in);
                            ByteArrayOutputStream buf = new ByteArrayOutputStream();
                            int resultx = bis.read();
                            while (resultx != -1) {
                                byte b = (byte) resultx;
                                buf.write(b);
                                resultx = bis.read();
                            }
                            data = new String(buf.toByteArray());

                        }
                    }
                    return data;

                }
            }
        } catch (RepositoryException e) {
            logger.warn("Repository Exception", e);

        } catch (IOException e) {
            logger.warn("IOException", e);
        }
        return "";
    }


    @Path("/valuelist")
    @GET
    public String getValueList(@QueryParam("name") String valueListName,
                               @QueryParam("filterLabels") String labels,
                               @QueryParam("filterKeys") String keys) {

        if (StringUtils.isEmpty(labels) && StringUtils.isEmpty(keys)) {
            try {
                return getValueList(valueListName);
            } catch (RepositoryException e) {
                logger.warn("Error ", e);
            }
        }

        String jsonwrapper = "{\n" +
                "  \"countries\": [\n%s" +

                "  ]\n" +
                "}";
        String response = "{ \"code\":\"%s\", \"name\":\"%s\"}";

        String[] lbls = labels.split(",");
        String filter = "";
        int i = 0;
        for (String label : lbls) {
            if (i > 0) {
                filter += " or ";
            }
            i++;
            filter += "( @selection:label='" + label.trim() + "')";
        }
        filter = "(" + filter + ")";

        String queryString = "/jcr:root/content/documents/geo-targeting/value-lists/" + valueListName + "/" + valueListName + "//element(*,selection:listitem)[" + filter + "]";
        try {
            Query query = getQuery(queryString);
            QueryResult result = query.execute();
            NodeIterator it = result.getNodes();
            StringBuilder builder = new StringBuilder();
            int index = 0;
            while (it.hasNext()) {
                Node node = it.nextNode();
                if (index > 0) {
                    builder.append(",");
                }
                String code = node.getProperty("selection:key").getString();
                String desc = node.getProperty("selection:label").getString();
                builder.append(String.format(response, code, desc));
                index++;
            }
            jsonwrapper = String.format(jsonwrapper, builder.toString());
        } catch (RepositoryException e) {
            logger.warn("RepositoryException", e);
        }

        return jsonwrapper;
    }

    /**
     * @param valueListName
     * @return
     * @throws RepositoryException
     */
    private String getValueList(String valueListName) throws RepositoryException {

        String vlQuery = String.format(VALUE_LIST_QUERY, valueListName);
        Query query = getQuery(vlQuery);
        QueryResult result = query.execute();
        NodeIterator it = result.getNodes();
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (it.hasNext()) {
            Node n = null;
            try {
                n = it.nextNode();
                String key = n.getProperty("selection:key").getString();
                String val = n.getProperty("selection:label").getString();
                if (i > 0) {
                    builder.append(",");
                }
                builder.append("{\"key\":\"" + key + "\",");
                builder.append("\"value\":\"" + val + "\"}");
                i++;
            } catch (Exception e) {
                logger.info("Error occured, current node will be ignored " + n.getPath(), e);

            }
        }
        System.out.println("{\"valuelist\":[" + builder.toString() + "]}");
        return "{\"valuelist\":[" + builder.toString() + "]}";
    }


    @Path("/save")
    @POST
    public String save(
            @FormParam("characteristic") String characteristic,
            @FormParam("targetName") String targetName,
            @FormParam("targetValues") String targetValues) {
        if (characteristic == null) {
            characteristic = "country";
        }

        String charBase = TARGETING_CHARACTERISTICS_NODE_PATH + characteristic;
        Session session = getSession();
        try {
            Node node = session.getNode(charBase);

            Node n = node.addNode(targetName.replaceAll(" ", "").toLowerCase() + "-" + new Date().getTime(), "targeting:targetgroup");
            n.setProperty("targeting:name", targetName);
            String[] vals = targetValues.split(",");
            ValueFactory valueFactory = session.getValueFactory();
            Value[] values = new Value[vals.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = valueFactory.createValue(vals[i]);
            }
            n.setProperty("targeting:propertynames", values);
            session.save();
        } catch (RepositoryException e) {
            logger.warn("Repository Exception", e);
        }

        return "{\"message\":\"success\"}";
    }

}