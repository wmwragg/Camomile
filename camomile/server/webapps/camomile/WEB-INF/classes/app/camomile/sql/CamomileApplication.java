package app.camomile.sql;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class CamomileApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(CamomileSqlResource.class);
        return classes;
    }

}
