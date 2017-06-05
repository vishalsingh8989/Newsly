package com.kepler.news.newsly;

import android.view.View;

/**
 * Created by vishaljasrotia on 28/05/17.
 */

public class NewsStory {

    private View.OnClickListener requestBtnClickListener;
    private String sourceName = "";
    private String description = "";
    private String title        = "";
    private String urltoimage = "";
    private String author    = "";
    private String category    = "";
    private String url      = "";


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


    public void setUrl(String url) {
        this.url = url;
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

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }
}
