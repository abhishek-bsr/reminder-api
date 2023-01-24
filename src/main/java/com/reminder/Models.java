package com.reminder;

import org.json.JSONObject;

import java.time.Instant;

public class Models {
    private String name;
    private Instant remindTime;
    private Boolean isCompleted;
    private Boolean isImportant;
    private String note;

    public Models(JSONObject jsonObject) {
        this.name = jsonObject.has("name") ? jsonObject.getString("name") :
                null;
        this.remindTime = jsonObject.has("reminder_utc") ?
                Instant.parse(jsonObject.getString("reminder_utc")) : null;
        this.isCompleted = jsonObject.has("is_completed") ?
                jsonObject.getBoolean("is_completed") : null;
        this.isImportant = jsonObject.has("is_important") ?
                jsonObject.getBoolean("is_important") : null;
        this.note = jsonObject.has("note") ? jsonObject.getString("note") :
                null;
    }

    // getters
    protected String getName() {
        return this.name;
    }

    protected Instant getRemindTime() {
        return this.remindTime;
    }

    protected Boolean getIsCompleted() {
        return this.isCompleted;
    }

    protected Boolean getIsImportant() {
        return this.isImportant;
    }

    protected String getNote() {
        return this.note;
    }
}
