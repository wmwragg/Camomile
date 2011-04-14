package app.camomile.exceptions;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import org.apache.wink.providers.json.*;
import org.json.*;

public class CamomileForbiddenException extends WebApplicationException {
  // Create a HTTP 403 (Forbidden) exception.
  public CamomileForbiddenException() {
    super(Response.status(Response.Status.FORBIDDEN).build());
  }

  // Create a HTTP 403 (Forbidden) exception.
  // @param message the String that is the entity of the 403 response.
  public CamomileForbiddenException(String error) {
    super(constructResponse(error));
  }

  private final static Response constructResponse(String error) {
    JSONObject jobj = new JSONObject();
    try {
      jobj.put("ERROR", error);
    } catch(JSONException e) {
      // Do nothing with the exception at the moment
    }

    return Response.status(Response.Status.FORBIDDEN).entity(jobj.toString()).type("application/json").build();
  }
}
