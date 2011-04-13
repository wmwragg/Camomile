package app.camomile.rest;

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
import app.camomile.rest.exceptions.*;

@Path("/{connection}/_sql")
public class CamomileSqlResource {

  @GET
  @Consumes("application/json")
  @Produces("application/json")
  public JSONObject getAllMessage(JSONObject jsonSql, @PathParam("connection") String connection, @Context ServletContext context) {
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

    return jobj;
  }

  @GET
  @Path("/{limit}")
  @Consumes("application/json")
  @Produces("application/json")
  public JSONObject getLimitMessage(JSONObject jsonSql, @PathParam("connection") String connection, @PathParam("limit") int limit, @Context ServletContext context) {
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

    return jobj;
  }

  @POST
  @Consumes("application/json")
  @Produces("application/json")
  public JSONObject postMessage(JSONObject jsonSql, @PathParam("connection") String connection, @Context ServletContext context) {
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

    return jobj;
  }
}

