package com.kepler.news.newsly;

import android.view.View;

/**
 * Created by vishaljasrotia on 28/05/17.
 */

public class NewsStory {



    private String id          = "";
    private String sourceName  = "";
    private String description = "";
    private String title       = "";
    private String urltoimage  = "";
    private String author      = "";
    private String category    = "";
    private String url         = "";
    private String publishedat = "";
    private String sourceUrl   = "";
    private String addtime     = "";
    private String source           = "";
    private String langauge         = "";
    private String country          = "";


    public void setCountry(String country) {
        this.country = country;
    }

    public void setLangauge(String langauge) {
        this.langauge = langauge;
    }


    public void setSource(String source) {
        this.source = source;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public void setUrltoimage(String urltoimage) {
        this.urltoimage = urltoimage;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPublishedat(String publishedat) {
        this.publishedat = publishedat;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }


    public String getCountry() {
        return country;
    }

    public String getLangauge() {
        return langauge;
    }

    public String getSource() {
        return source;
    }

    public String getId() {
        return id;
    }

    public String getAddtime() {
        return addtime;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getUrltoimage() {
        return urltoimage;
    }

    public String getCategory() {
        return category;
    }

    public String getUrl() {
        return url;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getPublishedat() {
        return publishedat;
    }


}
