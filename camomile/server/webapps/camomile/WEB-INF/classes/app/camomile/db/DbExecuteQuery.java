package app.camomile.db;

import javax.ws.rs.*;
import javax.naming.*;
import org.json.*;
import javax.sql.*;
import java.sql.*;
import java.util.*;
import app.camomile.exceptions.*;

import org.joda.time.*;

// Immutable query class
public final class DbExecuteQuery {
  private final JSONObject jobj = new JSONObject();
 
  public DbExecuteQuery(String connection, String query, int limit) {
    String error;
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;

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

    // Process query
    try{
      stmt = con.createStatement();
      rs = stmt.executeQuery(query);

      ResultSetMetaData rsmd = rs.getMetaData();
      int fieldCount = rsmd.getColumnCount();

      ArrayList<String> listColumns = new ArrayList<String>();
      for (int num = 1; num <= fieldCount; num++) {
        listColumns.add(rsmd.getColumnName(num));
      }

      ArrayList<String> listTypes = new ArrayList<String>();
      for (int num = 1; num <= fieldCount; num++) {
        listTypes.add(rsmd.getColumnTypeName(num));
      }

      ArrayList<Object> listJavaTypes = new ArrayList<Object>();
      for (int num = 1; num <= fieldCount; num++) {
        listJavaTypes.add(rsmd.getColumnType(num));
      }

      ArrayList<ArrayList> list = new ArrayList<ArrayList>();
      boolean isLast = false;
      int count = 0;
      while (rs.next()) {
        ArrayList<Object> listRecord = new ArrayList<Object>();
        for (int num = 1; num <= fieldCount; num++) {
          // Get the right java types for the database types
          // Note: More need to be added
          int type = rsmd.getColumnType(num);
          if (type == Types.DATE || type == Types.TIME || type == Types.TIMESTAMP) {
            java.util.Date dateTime = null;

            if (type == Types.DATE) {
              dateTime = (java.util.Date) rs.getDate(num);
            } else if (type == Types.TIME) {
              dateTime = (java.util.Date) rs.getTime(num);
            } else if (type == Types.TIMESTAMP) {
              dateTime = (java.util.Date) rs.getTimestamp(num);
            }

            if (dateTime != null) {
              long utcTimestamp = dateTime.getTime();
              long offset = (new DateTime(dateTime)).getZone().toTimeZone().getRawOffset();
              ArrayList<Object> listTime = new ArrayList<Object>();
              listTime.add(utcTimestamp);
              listTime.add(offset);
              listRecord.add(listTime);
            } else {
              listRecord.add(dateTime);
            }
          } else if (type == Types.TINYINT || type == Types.SMALLINT || type == Types.INTEGER) {
            listRecord.add(rs.getInt(num));
          } else if (type == Types.BIGINT) {
            listRecord.add(rs.getLong(num));
          } else if (type == Types.BOOLEAN || type == Types.BIT) {
            listRecord.add(rs.getBoolean(num));
          } else {
            listRecord.add(rs.getString(num));
          }
        }
        list.add(listRecord);

        isLast = rs.isLast();

        // If there is a limit > 0 then stop when the limit is reached
        count++;
        if (count == limit) {
          break;
        }
      }

      // Create the return JSON
      jobj.put("Count", count);
      if (limit == 0) {
        jobj.put("Limit", "_all");
      } else {
        jobj.put("Limit", limit);
      }
      jobj.put("More Rows", !isLast);
      jobj.put("Columns", listColumns);
      jobj.put("Types", listTypes);
      jobj.put("java.sql.Types", listJavaTypes);
      jobj.put("Rows", list);

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

