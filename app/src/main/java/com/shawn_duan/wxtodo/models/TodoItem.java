package com.shawn_duan.wxtodo.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by sduan on 9/27/16.
 */

public class TodoItem extends RealmObject {
    @PrimaryKey
    private long timestamp;
    @Required
    private String title;
    private String content;
    private boolean isDone;
    private int estimateTimeInMin;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public int getEstimateTimeInMin() {
        return estimateTimeInMin;
    }

    public void setEstimateTimeInMin(int estimateTimeInMin) {
        this.estimateTimeInMin = estimateTimeInMin;
    }

}
