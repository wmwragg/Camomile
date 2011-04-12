package app.camomile.db;

import javax.ws.rs.*;
import javax.naming.*;
import org.json.*;
import javax.sql.*;
import java.sql.*;
import java.util.*;
import app.camomile.rest.exceptions.*;

// Immutable query class
public final class DbExecuteUpdate {
  private final JSONObject jobj = new JSONObject();
 
  public DbExecuteUpdate(String connection, String update) {
    String error;
    Connection con = null;
    Statement stmt = null;

    // Get connection
    try {
      Context initCtx = new InitialContext();
      DataSource ds = (DataSource) initCtx.lookup("jdbc/" + connection);
      con = ds.getConnection();
    } catch (NamingException cnfe) {
      error = "NamingException: " + cnfe;
      throw new CamomileNotFoundException(error);
    } catch (SQLException cnfe) {
      error = "SQLException: Could not connect to database - " + cnfe;
      throw new CamomileInternalServerErrorException(error);
    } catch (Exception e) {
      error = "Exception: An unkown error occurred while connecting to database - " + e;
      throw new CamomileInternalServerErrorException(error);
    }

    // Process update
    try{
      stmt = con.createStatement();
      int count = stmt.executeUpdate(update);

      // Create the return JSON
      jobj.put("Count", count);

    } catch(SQLException e){
      error = "SQLException: Could not exexcute the query - " + e;
      throw new CamomileInternalServerErrorException(error);
    } catch(Exception e){
      error = "An unknown exception occured while retrieving data - " + e;
      throw new CamomileInternalServerErrorException(error);
    } finally {
      // Close resources
      try {
        if ( stmt != null ) {
          stmt.close();
        }
        if ( con != null ) {
          con.close();
        }
      } catch (SQLException sqle) {
        error = "SQLException: Unable to close the database connection - " + sqle;
        throw new CamomileInternalServerErrorException(error);
      }
    }
  }

  public final JSONObject getResult() {
      return jobj;
  } 
}

