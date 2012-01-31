package org.camomile.exceptions;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import org.json.*;

public class CamomileBadRequestException extends WebApplicationException {
  // Create a HTTP 400 (Bad Request) exception.
  public CamomileBadRequestException() {
    super(Response.status(Response.Status.BAD_REQUEST).build());
  }

  // Create a HTTP 400 (Bad Request) exception.
  // @param message the String that is the entity of the 400 response.
  public CamomileBadRequestException(String error) {
    super(constructResponse(error));
  }

  private final static Response constructResponse(String error) {
    JSONObject jobj = new JSONObject();
    try {
      jobj.put("ERROR", error);
    } catch(JSONException e) {
      // Do nothing with the exception at the moment
    }

    return Response.status(Response.Status.BAD_REQUEST).entity(jobj.toString()).type("application/json").build();
  }
}
