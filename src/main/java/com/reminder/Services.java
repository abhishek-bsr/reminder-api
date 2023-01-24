package com.reminder;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class Services {
    private JSONObject responseBuilder(Object object) {
        return new JSONObject().put("reminders", object);
    }

    protected void setResponseHeader(HttpServletResponse response,
                                  int statusCode) {
        response.setStatus(statusCode);
    }
    protected void errorResponse(PrintWriter output, int statusCode, String errorMessage) {
        JSONObject errorObject = new JSONObject();
        errorObject.put("status_code", statusCode);
        errorObject.put("error", errorMessage);

        JSONObject responseObject = responseBuilder(errorObject);
        output.println(responseObject);
    }

    protected void successResponse(PrintWriter output, Object successObject) {
        JSONObject responseObject = responseBuilder(successObject);
        output.println(responseObject);
    }
}
