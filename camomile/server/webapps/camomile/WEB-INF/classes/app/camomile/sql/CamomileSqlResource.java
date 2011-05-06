package app.camomile.sql;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;
import org.apache.wink.providers.json.*;
import org.json.*;
import javax.naming.*;
import javax.sql.*;
import java.sql.*;
import app.camomile.db.*;
import app.camomile.exceptions.*;

@Path("/{connection}")
@Consumes("application/json")
@Produces("application/json")
public class CamomileSqlResource {
  @Context ServletContext context;
  @PathParam("connection") String connection;
  @DefaultValue("compact") @QueryParam("json") String jsonStyle;

  @GET
  public String getAllMessage(JSONObject jsonSql) {
    String error;
    JSONObject jobj = null;
    String allowSQL = context.getInitParameter(connection + ":allow sql");

    if (allowSQL != null && allowSQL.equals("true")) {
      try {
        DbExecuteQuery dbQ = new DbExecuteQuery(connection, jsonSql.getString("SQL"), 0);
        jobj = dbQ.getResult();
      } catch(CamomileInternalServerErrorException e) {
        throw e;
      } catch(CamomileNotFoundException e) {
        throw e;
      } catch(JSONException e) {
        error = "JSONException: Badly formed JSON request string - " + e;      
        throw new CamomileBadRequestException(error);
      } catch(Exception e) {
        error = "Exception: An unkown error occurred while creating response - " + e;
        throw new CamomileInternalServerErrorException(error);
      }
    } else {
        error = "Exception: Raw SQL not allowed for this connection";
        throw new CamomileForbiddenException(error);
    }

    String returnJson = "";
    try {
      if (jsonStyle.equals("compact")) {
        returnJson = jobj.toString();
      } else {
        returnJson = jobj.toString(4);
      }
    } catch(JSONException e) {
      error = "JSONException: Badly formed JSON result string - " + e;      
      throw new CamomileBadRequestException(error);
    }
    return returnJson;
  }

  @GET
  @Path("/{limit}")
  public String getLimitMessage(JSONObject jsonSql, @PathParam("limit") int limit) {
    String error;
    JSONObject jobj = null;
    String allowSQL = context.getInitParameter(connection + ":allow sql");

    if (allowSQL != null && allowSQL.equals("true")) {
      try {
        DbExecuteQuery dbQ = new DbExecuteQuery(connection, jsonSql.getString("SQL"), limit);
        jobj = dbQ.getResult();
      } catch(CamomileInternalServerErrorException e) {
        throw e;
      } catch(CamomileNotFoundException e) {
        throw e;
      } catch(JSONException e) {
        error = "JSONException: Badly formed JSON request string - " + e;      
        throw new CamomileBadRequestException(error);
      } catch(Exception e) {
        error = "Exception: An unkown error occurred while creating response - " + e;
        throw new CamomileInternalServerErrorException(error);
      }
    } else {
        error = "Exception: Raw SQL not allowed for this connection";
        throw new CamomileForbiddenException(error);
    }

    String returnJson = "";
    try {
      if (jsonStyle.equals("compact")) {
        returnJson = jobj.toString();
      } else {
        returnJson = jobj.toString(4);
      }
    } catch(JSONException e) {
      error = "JSONException: Badly formed JSON result string - " + e;      
      throw new CamomileBadRequestException(error);
    }
    return returnJson;
  }

  @POST
  public String postMessage(JSONObject jsonSql) {
    String error;
    JSONObject jobj = null;
    String allowSQL = context.getInitParameter(connection + ":allow sql");

    if (allowSQL != null && allowSQL.equals("true")) {
      try {
        DbExecuteUpdate dbU = new DbExecuteUpdate(connection, jsonSql.getString("SQL"));
        jobj = dbU.getResult();
      } catch(CamomileInternalServerErrorException e) {
        throw e;
      } catch(CamomileNotFoundException e) {
        throw e;
      } catch(JSONException e) {
        error = "JSONException: Badly formed JSON request string - " + e;      
        throw new CamomileBadRequestException(error);
      } catch(Exception e) {
        error = "Exception: An unkown error occurred while creating response - " + e;
        throw new CamomileInternalServerErrorException(error);
      }
    } else {
        error = "Exception: Raw SQL not allowed for this connection";
        throw new CamomileForbiddenException(error);
    }

    String returnJson = "";
    try {
      if (jsonStyle.equals("compact")) {
        returnJson = jobj.toString();
      } else {
        returnJson = jobj.toString(4);
      }
    } catch(JSONException e) {
      error = "JSONException: Badly formed JSON result string - " + e;      
      throw new CamomileBadRequestException(error);
    }
    return returnJson;
  }
}

