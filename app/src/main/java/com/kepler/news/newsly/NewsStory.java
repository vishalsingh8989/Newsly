package com.kepler.news.newsly;

/**
 * Created by vishaljasrotia on 28/05/17.
 */

public class NewsStory {


    public  String id ;
    public  String title;
    public  String summary;
    public  String language;
    public String country;
    public  String category;
    public  String tags;
    public  String article_url;
    public  String source_url;
    public  String source_name;
    public  String top_image;
    public  String publish_date;
    public  String authors;
    public  String meta_favicon;
    public  String trending;

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setArticle_url(String article_url) {
        this.article_url = article_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public void setTop_image(String top_image) {
        this.top_image = top_image;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setTrending(String trending) {
        this.trending = trending;
    }


    public void setMeta_favicon(String meta_favicon) {
        this.meta_favicon = meta_favicon;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getCountry() {
        return country;
    }

    public String getLanguage() {
        return language;
    }

    public String getArticle_url() {
        return article_url;
    }

    public String getSource_name() {
        return source_name;
    }

    public String getSource_url() {
        return source_url;
    }

    public String getAuthors() {
        return authors;
    }

    public String getMeta_favicon() {
        return meta_favicon;
    }

    public String getTags() {
        return tags;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public String getTop_image() {
        return top_image;
    }

    public String getCategory() {
        return category;
    }

    public String getTrending() {
        return trending;
    }
}


