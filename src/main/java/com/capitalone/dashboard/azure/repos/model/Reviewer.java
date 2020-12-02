package com.capitalone.dashboard.azure.repos.model;

public class Reviewer {
    private String reviewerURL;
    private long vote;
    private boolean hasDeclined;
    private boolean isFlagged;
    private String displayName;
    private String url;
    private Links links;
    private String id;
    private String uniqueName;
    private String imageURL;

    public String getReviewerURL() { return reviewerURL; }
    public void setReviewerURL(String value) { this.reviewerURL = value; }

    public long getVote() { return vote; }
    public void setVote(long value) { this.vote = value; }

    public boolean getHasDeclined() { return hasDeclined; }
    public void setHasDeclined(boolean value) { this.hasDeclined = value; }

    public boolean getIsFlagged() { return isFlagged; }
    public void setIsFlagged(boolean value) { this.isFlagged = value; }

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
}
