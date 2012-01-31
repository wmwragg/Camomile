import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.ext.jaxrs.JaxRsApplication;

import org.json.*;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.dbcp.*;

import java.util.*;
import java.io.*;
import java.sql.*;

import org.camomile.sql.*;

public class CamomileServer {

    public static void main(String[] args) throws Exception {
        // Read in the json server configuration file
        String serverJson = new Scanner( new File("server.json"), "UTF-8").useDelimiter("\\A").next();
        JSONObject serverConfig = new JSONObject(serverJson);

        // create Component (as ever for Restlet)
        Component comp = new Component();
        Server server = comp.getServers().add(Protocol.HTTP, serverConfig.getInt("http port"));

        // Create a protected child context and then add some values that will 
        // be accessable by the application
        Context protectedContext  = comp.getContext().createChildContext(); 
        
        // Read in the json configuration file
        String connectionsJson = new Scanner( new File("connections.json"), "UTF-8").useDelimiter("\\A").next();
        JSONObject connectionsConfig = new JSONObject(connectionsJson);

        // Put the configuration into the apps context
        Iterator namesItr = connectionsConfig.keys();
        while (namesItr.hasNext()) {
          String namesKey = (String) namesItr.next();
          JSONObject connectionsParams = connectionsConfig.getJSONObject(namesKey);
          Iterator paramsItr = connectionsParams.keys();

          while (paramsItr.hasNext()) { 
            String paramsKey = (String) paramsItr.next();
            protectedContext.getParameters().add(namesKey + ":" + paramsKey, connectionsParams.getString(paramsKey)); 
          }
        }      
 
        // create JAX-RS runtime environment
        JaxRsApplication application = new JaxRsApplication(protectedContext);

        // attach Application
        application.add(new CamomileApplication());

        // Create a database pool and setup the database connections within it, so that they can be accessed via
        // the java.sql.DriverManager
        namesItr = connectionsConfig.keys();
        while (namesItr.hasNext()) {
          String namesKey = (String) namesItr.next();
          JSONObject connectionsParams = connectionsConfig.getJSONObject(namesKey);

          System.out.println("Loading JDBC driver for '" + namesKey + "' ...");
          try {
              Class.forName(connectionsParams.getString("driver"));
          } catch (ClassNotFoundException e) {
              e.printStackTrace();
          }
          System.out.println("Done.");
  
          // Create the pool and register the driver
          GenericObjectPool connectionPool = new GenericObjectPool(null);
          ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectionsParams.getString("url"), connectionsParams.getString("user"), connectionsParams.getString("password"));
          PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);
          PoolingDriver driver = new PoolingDriver();
          driver.registerPool(namesKey, connectionPool);
        }

        // Attach the application to the component and start it
        comp.getDefaultHost().attach(application);
        comp.start();

        System.out.println("Server started on port " + server.getPort());
        System.out.println("Press key to stop server");
        System.in.read();
        System.out.println("Stopping server");
        comp.stop();
        System.out.println("Server stopped");
    }
}
