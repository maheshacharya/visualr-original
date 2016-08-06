package org.macharya.services;


import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.GeoIp2Provider;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.onehippo.cms7.targeting.geo.GeoIPService;
import com.onehippo.cms7.targeting.geo.GeoIPServiceImpl;
import com.onehippo.cms7.targeting.geo.Location;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.net.InetAddress;
import java.net.URL;


/**
 * Created by maheshacharya on 8/5/16.
 */
public class StateServiceImpl implements GeoIPService {
    private static final Logger log = LoggerFactory.getLogger(GeoIPServiceImpl.class);
    private static final String CITY_LITE_DATABASE_RESOURCE = "/GeoLite2-City.mmdb";
    private static final String CITY_DATABASE_RESOURCE = "/Geo2-City.mmdb";
    private volatile boolean initialized = false;
    private GeoIp2Provider lookupService;
    private String databaseResource;
    private String databaseFile;

    public StateServiceImpl() {

    }

    public StateLocation getLocation(String ip) {
        if (!this.initialized) {
            this.initialize();
            this.initialized = true;
        }
        StateLocation loc;
        if (this.lookupService != null) {

            try {
                InetAddress e = InetAddress.getByName(ip);
                CityResponse response = this.lookupService.city(e);
                com.maxmind.geoip2.record.Location location = response.getLocation();
                String continentCode = null;
                if (response.getContinent() != null) {
                    continentCode = response.getContinent().getCode();
                }

                String isoCode = null;
                if (response.getCountry() != null) {
                    isoCode = response.getCountry().getIsoCode();
                }

                String cityName = null;
                if (response.getCity() != null) {
                    cityName = response.getCity().getName();
                }

                if (location == null) {
                    loc = new StateLocation(continentCode, isoCode, cityName, 0.0D, 0.0D);
                    loc.setState(response.getMostSpecificSubdivision().getIsoCode());
                    return loc;
                }

                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();
                if (latitude != null && longitude != null) {
                    loc = new StateLocation(continentCode, isoCode, cityName, location.getLatitude().doubleValue(), location.getLongitude().doubleValue());
                    loc.setState(response.getMostSpecificSubdivision().getIsoCode());
                    return loc;
                }

                loc = new StateLocation(continentCode, isoCode, cityName, 0.0D, 0.0D);
                loc.setState(response.getMostSpecificSubdivision().getIsoCode());
                return loc;
            } catch (GeoIp2Exception | IOException var10) {
                log.debug("Failed to lookup address {} in Geo IP database: ", new Object[]{ip, var10.toString()});
            }
        }

        return null;
    }

    public void setDatabaseResource(String databaseResource) {
        this.databaseResource = databaseResource;
    }

    public void setDatabaseFile(String databaseFile) {
        this.databaseFile = databaseFile;
    }

    public synchronized boolean available() {
        if (!this.initialized) {
            this.initialize();
            this.initialized = true;
        }

        return this.lookupService != null;
    }

    private synchronized void initialize() {
        if (!this.initialized) {
            if (this.databaseFile != null) {
                File db = new File(this.databaseFile);
                if (db.exists()) {
                    try {
                        this.lookupService = (new DatabaseReader.Builder(db)).build();
                        return;
                    } catch (IOException var18) {
                        log.error("Failed to create Geo IP lookup service from file. Trying to fall back on resource instead.", var18);
                    }
                } else {
                    log.warn("Specified Geo IP database file " + this.databaseFile + " does not exist. Trying to fall back on resource instead.");
                }
            }

            URL db1 = null;
            if (this.databaseResource != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Locating database resource " + this.databaseResource);
                }

                db1 = this.getClass().getResource(this.databaseResource);
                if (db1 != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Found database resource");
                    }
                } else {
                    log.warn("Couldn\'t locate database resource " + this.databaseResource + ". Trying to fall back on default city database resource.");
                }
            }

            if (db1 == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Locating database resource /Geo2-City.mmdb");
                }

                db1 = this.getClass().getResource("/Geo2-City.mmdb");
                if (db1 != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Found database resource");
                    }
                } else if (log.isDebugEnabled()) {
                    log.debug("Couldn\'t locate database resource. Trying to fall back on default city lite resource.");
                }
            }

            if (db1 == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Locating database resource /GeoLite2-City.mmdb");
                }

                db1 = this.getClass().getResource("/GeoLite2-City.mmdb");
                if (db1 != null && log.isDebugEnabled()) {
                    log.debug("Found database resource");
                }
            }

            if (db1 != null) {
                String filename = this.getFileName(db1);
                String tmpdir = System.getProperty("java.io.tmpdir");
                File file = new File(tmpdir, filename);
                if (!file.exists()) {
                    InputStream e = null;
                    FileOutputStream out = null;

                    try {
                        if (log.isDebugEnabled()) {
                            log.debug("Creating temporary Geo IP database file at " + file.getCanonicalPath());
                        }

                        file.createNewFile();
                        e = db1.openStream();
                        out = new FileOutputStream(file);
                        IOUtils.copy(e, out);
                    } catch (FileNotFoundException var15) {
                        log.error("Unable to create temporary Geo IP database file from resource.", var15);
                    } catch (IOException var16) {
                        log.error("Unable to create temporary Geo IP database file from resource.", var16);
                    } finally {
                        IOUtils.closeQuietly(e);
                        IOUtils.closeQuietly(out);
                    }
                }

                try {
                    this.lookupService = (new DatabaseReader.Builder(file)).build();
                } catch (IOException var14) {
                    log.error("Failed to create Geo IP lookup service", var14);
                }
            } else {
                log.error("Couldn\'t locate database resource. Geo IP lookup service will not be available.");
            }

        }
    }

    private String getFileName(URL db) {
        String file = db.getFile();
        int offset = file.lastIndexOf(47);
        if (offset != -1) {
            String fileName = file.substring(offset + 1);
            offset = fileName.lastIndexOf(46);
            if (offset != -1) {
                fileName = fileName.substring(0, offset) + ".dat";
            }

            return fileName;
        } else {
            return "geoip.dat";
        }
    }
}
