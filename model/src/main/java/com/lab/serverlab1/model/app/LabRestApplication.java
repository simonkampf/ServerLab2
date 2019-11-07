package com.lab.serverlab1.model.app;

import com.lab.serverlab1.model.rest.LabRestService;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
public class LabRestApplication extends Application {
    private Set<Object> singletons = new HashSet<Object>();
    public LabRestApplication() {
        singletons.add(new LabRestService());
    }
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}