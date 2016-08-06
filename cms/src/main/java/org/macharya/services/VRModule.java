package org.macharya.services;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.onehippo.repository.jaxrs.RepositoryJaxrsEndpoint;
import org.onehippo.repository.jaxrs.RepositoryJaxrsService;
import org.onehippo.repository.modules.DaemonModule;

public class VRModule implements DaemonModule {

    @Override
    public void initialize(final Session session) throws RepositoryException {
        RepositoryJaxrsService.addEndpoint(
                new RepositoryJaxrsEndpoint("/geojson").singleton(new GeoJsonResource()));
    }

    @Override
    public void shutdown() {
        RepositoryJaxrsService.removeEndpoint("/geojson");
    }


}