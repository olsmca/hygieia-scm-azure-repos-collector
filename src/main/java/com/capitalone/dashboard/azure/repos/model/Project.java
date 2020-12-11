package com.capitalone.dashboard.azure.repos.model;

public class Project {
    private String id;
    private String name;
    private String state;
    private String visibility;
    private String lastUpdateTime;

    public String getID() { return id; }
    public void setID(String value) { this.id = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getState() { return state; }
    public void setState(String value) { this.state = value; }

    public String getVisibility() { return visibility; }
    public void setVisibility(String value) { this.visibility = value; }

    public String getLastUpdateTime() { return lastUpdateTime; }
    public void setLastUpdateTime(String value) { this.lastUpdateTime = value; }
}
