package com.capitalone.dashboard.azure.repos.model;

public class CreatedBy {
    private String displayName;
    private String url;
    private Links links;
    private String id;
    private String uniqueName;
    private String imageURL;
    private String descriptor;

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String value) { this.displayName = value; }

    public String getURL() { return url; }
    public void setURL(String value) { this.url = value; }

    public Links getLinks() { return links; }
    public void setLinks(Links value) { this.links = value; }

    public String getID() { return id; }
    public void setID(String value) { this.id = value; }

    public String getUniqueName() { return uniqueName; }
    public void setUniqueName(String value) { this.uniqueName = value; }

    public String getImageURL() { return imageURL; }
    public void setImageURL(String value) { this.imageURL = value; }

    public String getDescriptor() { return descriptor; }
    public void setDescriptor(String value) { this.descriptor = value; }
}
