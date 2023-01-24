package com.reminder;

import org.json.JSONObject;

import java.io.IOException;

import java.time.Instant;
import java.time.format.DateTimeParseException;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;


public class Utils {
    protected JSONObject requestIntoJSON(HttpServletRequest request) throws IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining());
        JSONObject requestJsonBody = new JSONObject(requestBody);

        return requestJsonBody.getJSONObject("reminders");
    }

    protected Instant checkReminderUtcFormat(String reminderUtc) {
        try {
            return Instant.parse(reminderUtc);
        } catch (DateTimeParseException _err) {
            return null;
        }
    }

    protected Integer getParamValue(HttpServletRequest request, String paramName) {
        String param = request.getParameter(paramName);
        return param != null ? Integer.parseInt(param) : null;
    }
}
