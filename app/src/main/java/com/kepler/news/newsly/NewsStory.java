package com.kepler.news.newsly;

/**
 * Created by vishaljasrotia on 28/05/17.
 */

public class NewsStory {
    private String sourceName = "";
    private String description = "";
    private String urltoimage = "";


    public void setDescription(String description) {
        this.description = description;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public void setUrltoimage(String urltoimage) {
        this.urltoimage = urltoimage;
    }


    public String getDescription() {
        return description;
    }


    public String getSourceName() {
        return sourceName;
    }

    public String getUrltoimage() {
        return urltoimage;
    }
}
