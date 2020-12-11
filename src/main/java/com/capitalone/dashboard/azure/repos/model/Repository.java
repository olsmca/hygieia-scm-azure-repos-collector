package com.capitalone.dashboard.azure.repos.model;

public class Repository {
    private String id;
    private String name;
    private String url;
    private Project project;

    public String getID() { return id; }
    public void setID(String value) { this.id = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getURL() { return url; }
    public void setURL(String value) { this.url = value; }

    public Project getProject() { return project; }
    public void setProject(Project value) { this.project = value; }
}
