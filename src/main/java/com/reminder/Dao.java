package com.reminder;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.time.Instant;
import java.util.Properties;

public class Dao {
    final Properties props = new Properties();
    private static Connection startConnection = null;
    private final static Dotenv env = Dotenv.load();
    final String DB_PORT = env.get("DB_PORT");
    final String DB_URL = "jdbc:postgresql://localhost:" + DB_PORT + "/";

    protected Dao() throws SQLException, ClassNotFoundException {
        final String DB_NAME = env.get("DB_NAME");
        final String username = env.get("PSQL_USER");
        final String password = env.get("PSQL_PASS");
        props.setProperty("user", username);
        props.setProperty("password", password);

        Class.forName("org.postgresql.Driver");
        startConnection = DriverManager.getConnection(DB_URL + DB_NAME, props);
    }

    // add data
    protected int insertData(Models model) throws SQLException {
        final String tableName = env.get("TABLE_NAME");
        PreparedStatement stmt = startConnection.prepareStatement(
                "INSERT INTO " + tableName + " (name, reminder_utc, " +
                        "is_completed, is_important, note) VALUES" +
                        "(?, ?, ?, ?, ?);");
        stmt.setString(1, model.getName());
        stmt.setTimestamp(2, Timestamp.from(model.getRemindTime()));
        stmt.setBoolean(3, model.getIsCompleted() != null ?
                model.getIsCompleted() : false);
        stmt.setBoolean(4, model.getIsImportant() != null ?
                model.getIsImportant() : false);
        stmt.setString(5, model.getNote() != null ? model.getNote() :
                "Add Note...");

        int result = stmt.executeUpdate();
        stmt.close();

        return result;
    }

    // read data
    protected JSONObject selectData(int id) throws SQLException {
        final String tableName = env.get("TABLE_NAME");
        PreparedStatement stmt = startConnection.prepareStatement("SELECT * " +
                "FROM " + tableName + " WHERE id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        JSONObject object = new JSONObject();
        while (rs.next()) {
            object.put("id", rs.getInt(1));
            object.put("name", rs.getString(2));

            Timestamp ts = (Timestamp) rs.getObject(3);
            Instant time = ts.toInstant();
            object.put("reminder_utc", time);

            object.put("is_completed", rs.getBoolean(4));
            object.put("is_important", rs.getBoolean(5));
            object.put("note", rs.getString(6));
        }

        rs.close();
        stmt.close();

        return object;
    }

    protected JSONArray selectAllData(Integer limit, Integer offset) throws SQLException {
        final String tableName = env.get("TABLE_NAME");
        String query = "SELECT * FROM " + tableName;
        if (limit != null)
            query += " LIMIT " + limit;
        if (offset != null)
            query += " OFFSET " + offset;

        PreparedStatement stmt = startConnection.prepareStatement(query + ";");
        ResultSet rs = stmt.executeQuery();

        JSONArray arrayObject = new JSONArray();
        while (rs.next()) {
            JSONObject object = new JSONObject();
            object.put("id", rs.getInt(1));
            object.put("name", rs.getString(2));

            Timestamp ts = (Timestamp) rs.getObject(3);
            Instant time = ts.toInstant();
            object.put("reminder_utc", time);

            object.put("is_completed", rs.getBoolean(4));
            object.put("is_important", rs.getBoolean(5));
            object.put("note", rs.getString(6));

            arrayObject.put(object);
        }

        rs.close();
        stmt.close();

        return arrayObject;
    }

    // update data
    protected int updateData(int id, Models model) throws SQLException {
        String query = "SET";
        if (model.getName() != null)
            query += " name = '" + model.getName() + "',";
        if (model.getRemindTime() != null)
            query += " reminder_utc = date '" + model.getRemindTime() + "',";
        if (model.getIsCompleted() != null)
            query += " is_completed = " + model.getIsCompleted() + ',';
        if (model.getIsImportant() != null)
            query += " is_important = " + model.getIsImportant() + ',';
        if (model.getNote() != null)
            query += " note = '" + model.getNote() + "',";

        query = query.substring(0, query.length() - 1);

        final String tableName = env.get("TABLE_NAME");
        PreparedStatement stmt =
                startConnection.prepareStatement("UPDATE " + tableName + " " +
                        query + " WHERE id = (?);");
        stmt.setInt(1, id);

        int result = stmt.executeUpdate();
        stmt.close();

        return result;
    }

    // delete data
    protected int deleteData(int id) throws SQLException {
        final String tableName = env.get("TABLE_NAME");
        PreparedStatement stmt =
                startConnection.prepareStatement("DELETE FROM " + tableName +
                        " WHERE id = ?;");
        stmt.setInt(1, id);

        int result = stmt.executeUpdate();
        stmt.close();

        return result;
    }

    protected void deleteAllData() throws SQLException {
        String tableName = env.get("TABLE_NAME");
        PreparedStatement stmt =
                startConnection.prepareStatement("DELETE FROM " + tableName + ";");

        stmt.executeUpdate();
        stmt.close();
    }
}
