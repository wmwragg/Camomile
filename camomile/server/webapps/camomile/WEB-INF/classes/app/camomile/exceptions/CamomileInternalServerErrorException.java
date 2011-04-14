package app.camomile.exceptions;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import org.apache.wink.providers.json.*;
import org.json.*;

public class CamomileInternalServerErrorException extends WebApplicationException {
  // Create a HTTP 500 (Internal Server Error) exception.
  public CamomileInternalServerErrorException() {
    super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
  }

  // Create a HTTP 500 (Internal Server Error) exception.
  // @param message the String that is the entity of the 500 response.
  public CamomileInternalServerErrorException(String error) {
    super(constructResponse(error));
  }

  private final static Response constructResponse(String error) {
    JSONObject jobj = new JSONObject();
    try {
      jobj.put("ERROR", error);
    } catch(JSONException e) {
      // Do nothing with the exception at the moment
    }

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jobj.toString()).type("application/json").build();
  }
}
