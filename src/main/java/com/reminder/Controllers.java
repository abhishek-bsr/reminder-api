package com.reminder;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


@WebServlet(name = "Controllers", urlPatterns = "/v1/reminders/*")
public class Controllers extends HttpServlet {
    private final String CHARACTER_ENCODING = "UTF-8";
    private final String CONTENT_TYPE = "application/json";
    private final int UNPROCESSABLE_ENTITY = 422;
    private static Services services = new Services();
    private static Utils utility = new Utils();

    /*
     * method /api/v1/reminders
     * method /api/v1/reminders/${id}
     *
     * param <limit>
     * param <offset>
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, JSONException {
        PrintWriter output = response.getWriter();
        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setContentType(CONTENT_TYPE);

        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.length() == 1) {
                // get limit & offset params
                Integer limit = utility.getParamValue(request, "limit");
                Integer offset = utility.getParamValue(request, "offset");

                JSONArray resultArray = new JSONArray();
                Dao database = new Dao();
                resultArray = database.selectAllData(limit, offset);

                services.successResponse(output, resultArray);
            } else {
                int id = Integer.parseInt(pathInfo.replace("/", ""));
                JSONObject resultObject = new JSONObject();
                Dao database = new Dao();
                resultObject = database.selectData(id);

                /// check if data exists
                if (resultObject.isEmpty()) {
                    String errorMessage = "Reminder <id: " + id + "> not " +
                            "found";
                    services.setResponseHeader(response,
                            HttpServletResponse.SC_NOT_FOUND);
                    services.errorResponse(output,
                            HttpServletResponse.SC_NOT_FOUND, errorMessage);
                } else {
                    services.successResponse(output, resultObject);
                }
            }
        } catch (Exception errorMessage) {
            services.setResponseHeader(response, HttpServletResponse.SC_BAD_REQUEST);
            services.errorResponse(output, HttpServletResponse.SC_BAD_REQUEST,
                    errorMessage.toString());
        }
    }

    /*
     * method /api/v1/reminders
     * method /api/v1/reminders/${id}
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException, JSONException {
        PrintWriter output = response.getWriter();
        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setContentType(CONTENT_TYPE);

        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.length() == 1) {
                Dao database = new Dao();
                database.deleteAllData();

                String successObject = "Deleted all data";
                services.successResponse(output, successObject);
            } else {
                int id = Integer.parseInt(pathInfo.replace("/", ""));
                Dao database = new Dao();
                int value = database.deleteData(id);

                // check if delete query executed
                if (value == 0) {
                    String errorMessage = "Reminder <id: " + id + "> not " +
                            "found";
                    services.setResponseHeader(response,
                            HttpServletResponse.SC_NOT_FOUND);
                    services.errorResponse(output,
                            HttpServletResponse.SC_NOT_FOUND, errorMessage);
                } else {
                    JSONObject successObject =
                            new JSONObject().put("message",
                                    "Deleted Reminder <id:" + id + ">");
                    services.successResponse(output, successObject);
                }
            }
        } catch (Exception errorMessage) {
            services.setResponseHeader(response, HttpServletResponse.SC_BAD_REQUEST);
            services.errorResponse(output, HttpServletResponse.SC_BAD_REQUEST,
                    errorMessage.toString());
        }
    }

    /*
     * method /api/v1/reminders
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, JSONException {
        PrintWriter output = response.getWriter();
        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setContentType(CONTENT_TYPE);

        try {
            JSONObject requestJsonObject = utility.requestIntoJSON(request);
            if (requestJsonObject.has("name") && requestJsonObject.has(
                    "reminder_utc")) {
                // check <reminder_utc> parsing
                String reminderUtcString = requestJsonObject.getString(
                        "reminder_utc");
                Instant reminderUtc = utility.checkReminderUtcFormat(reminderUtcString);
                if (reminderUtc == null) {
                    String errorMessage = "Unable to parse <reminder_utc>";
                    services.setResponseHeader(response, UNPROCESSABLE_ENTITY);
                    services.errorResponse(output, UNPROCESSABLE_ENTITY, errorMessage);
                    return;
                }

                Models models = new Models(requestJsonObject);
                Dao database = new Dao();
                int value = database.insertData(models);

                // check if insert query executed
                if (value == 0) {
                    String errorMessage = "Cannot insert data";
                    services.setResponseHeader(response,
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    services.errorResponse(output,
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            errorMessage);
                } else {
                    JSONObject successObject = new JSONObject();
                    successObject.put("message", "Data inserted");
                    services.successResponse(output, successObject);
                }
            } else {
                String errorMessage = "Missing field <name> (or) <reminder_utc> " +
                        "in request body";
                services.setResponseHeader(response, UNPROCESSABLE_ENTITY);
                services.errorResponse(output, UNPROCESSABLE_ENTITY, errorMessage);
            }
        } catch (Exception errorMessage) {
            services.setResponseHeader(response, HttpServletResponse.SC_BAD_REQUEST);
            services.errorResponse(output, HttpServletResponse.SC_BAD_REQUEST,
                    errorMessage.toString());
        }
    }

    /*
     * method /api/v1/reminders/${id}
     */
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException, JSONException {
        PrintWriter output = response.getWriter();
        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setContentType(CONTENT_TYPE);

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() == 1) {
            String errorMessage = "Missing parameter <id>";
            services.setResponseHeader(response, HttpServletResponse.SC_BAD_REQUEST);
            services.errorResponse(output, HttpServletResponse.SC_BAD_REQUEST,
                    errorMessage.toString());
            return;
        }

        try {
            JSONObject requestJsonObject = utility.requestIntoJSON(request);

            // check <reminder_utc> parsing
            if (requestJsonObject.has("reminder_utc")) {
                String reminderUtcString = requestJsonObject.getString(
                        "reminder_utc");
                Instant reminderUtc = utility.checkReminderUtcFormat(reminderUtcString);
                if (reminderUtc == null) {
                    String errorMessage = "Unable to parse <reminder_utc>";
                    services.setResponseHeader(response, UNPROCESSABLE_ENTITY);
                    services.errorResponse(output, UNPROCESSABLE_ENTITY, errorMessage);
                    return;
                }
            }

            Models model = new Models(requestJsonObject);
            Dao database = new Dao();
            int id = Integer.parseInt(pathInfo.replace("/", ""));
            int value = database.updateData(id, model);

            // check if update query executed
            if (value == 0) {
                String errorMessage = "Cannot update data";
                services.setResponseHeader(response,
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                services.errorResponse(output,
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        errorMessage);
            } else {
                JSONObject successObject = new JSONObject();
                successObject.put("message", "Data updated");
                services.successResponse(output, successObject);
            }
        } catch (Exception errorMessage) {
            services.setResponseHeader(response, HttpServletResponse.SC_BAD_REQUEST);
            services.errorResponse(output, HttpServletResponse.SC_BAD_REQUEST,
                    errorMessage.toString());
        }
    }
}
