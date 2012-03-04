package org.camomile.db;

import javax.ws.rs.*;
import javax.naming.*;
import org.json.*;
import javax.sql.*;
import java.sql.*;
import java.util.*;
import org.camomile.exceptions.*;

// Immutable query class
public final class DbExecuteInsert {
  private final JSONObject jobj = new JSONObject();
 
  public DbExecuteInsert(String connection, String update) {
    String error;
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;

    // Get connection
    try {
      con = DriverManager.getConnection("jdbc:apache:commons:dbcp:" + connection);
    } catch (SQLException cnfe) {
      error = "SQLException: Could not connect to database - " + cnfe;
      throw new CamomileInternalServerErrorException(error);
    } catch (Exception e) {
      error = "Exception: An unkown error occurred while connecting to database - " + e;
      throw new CamomileInternalServerErrorException(error);
    }

    // Process insert
    try {
      stmt = con.createStatement();
      int count = stmt.executeUpdate(update, Statement.RETURN_GENERATED_KEYS);
      rs = stmt.getGeneratedKeys();

      if (rs != null) {
        // Create the return JSON
        if (rs.next()) {
          ResultSetMetaData rsmd = rs.getMetaData();
          int fieldCount = rsmd.getColumnCount();
          ArrayList<Object> listRecord = new ArrayList<Object>();
          for (int num = 1; num <= fieldCount; num++) {
            // Get the right java types for the database types
            // Note: More need to be added
            int type = rsmd.getColumnType(num);
            if (type == Types.TINYINT || type == Types.SMALLINT || type == Types.INTEGER) {
              listRecord.add(rs.getInt(num));
            } else if (type == Types.BIGINT) {
              listRecord.add(rs.getLong(num));
            } else if (type == Types.BOOLEAN || type == Types.BIT) {
              listRecord.add(rs.getBoolean(num));
            } else {
              listRecord.add(rs.getString(num));
            }
          }
          jobj.put("keys", listRecord);
        }
      }
      jobj.put("count", count);

    } catch(SQLException e){
      error = "SQLException: Could not exexcute the query - " + e;
      throw new CamomileInternalServerErrorException(error);
    } catch(Exception e){
      error = "An unknown exception occured while retrieving data - " + e;
      throw new CamomileInternalServerErrorException(error);
    } finally {
      // Close resources
      try {
        if ( rs != null ) {
          rs.close();
        }
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

