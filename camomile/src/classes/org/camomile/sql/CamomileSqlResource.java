package org.camomile.sql;

import javax.ws.rs.*;
import org.json.*;
import org.restlet.Context;
import org.restlet.util.Series;
import org.camomile.db.*;
import org.camomile.exceptions.*;

@Path("{connection}")
@Consumes("application/json")
@Produces("application/json")
public class CamomileSqlResource {
  @PathParam("connection") String connection;
  @DefaultValue("compact") @QueryParam("json") String jsonStyle;
 
  // Get the context for this resource 
  Series appContext = Context.getCurrent().getParameters();

  @GET
  @Path("/sql")
  public String getAllMessage(JSONObject jsonSql) {
    return doAction(jsonSql, 0, true);
  }

  @GET
  @Path("/sql/{limit}")
  public String getLimitMessage(JSONObject jsonSql, @PathParam("limit") int limit) {
    return doAction(jsonSql, limit, true);
  }

  @POST
  @Path("/sql")
  public String postMessage(JSONObject jsonSql) {
    return doAction(jsonSql, 0, false);
  }

  private String doAction(JSONObject jsonSql, int limit, boolean select) {
    String error;
    JSONObject jobj = null;
    String allowSQL = appContext.getFirstValue(connection + ":allow sql");

    if (allowSQL != null && allowSQL.equals("true")) {
      try {
        if (select) {
          DbExecuteQuery dbQ = new DbExecuteQuery(connection, jsonSql.getString("SQL"), limit);
          jobj = dbQ.getResult();
          //jobj = new JSONObject("{\"" + connection + "\" : \"" + jsonSql.getString("SQL")  + "\"}");
        } else {
          DbExecuteUpdate dbU = new DbExecuteUpdate(connection, jsonSql.getString("SQL"));
          jobj = dbU.getResult();
          //jobj = new JSONObject("{\"" + connection + "\" : \"UPDATE: " + jsonSql.getString("SQL")  + "\"}");
        }
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

