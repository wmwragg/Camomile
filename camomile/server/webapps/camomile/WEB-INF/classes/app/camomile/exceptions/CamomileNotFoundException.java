package app.camomile.exceptions;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import org.apache.wink.providers.json.*;
import org.json.*;

public class CamomileNotFoundException extends WebApplicationException {
  // Create a HTTP 404 (Not Found) exception.
  public CamomileNotFoundException() {
    super(Response.status(Response.Status.NOT_FOUND).build());
  }

  // Create a HTTP 404 (Not Found) exception.
  // @param message the String that is the entity of the 404 response.
  public CamomileNotFoundException(String error) {
    super(constructResponse(error));
  }

  private final static Response constructResponse(String error) {
    JSONObject jobj = new JSONObject();
    try {
      jobj.put("ERROR", error);
    } catch(JSONException e) {
      // Do nothing with the exception at the moment
    }

    return Response.status(Response.Status.NOT_FOUND).entity(jobj.toString()).type("application/json").build();
  }
}
