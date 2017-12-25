package com.kepler.news.newsly.databaseHelper;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

/**
 * Created by vishaljasrotia on 8/19/17.
 */

@Entity
public class News {

    @PrimaryKey
    public static String id  = "" ;
    public static String title;
    public static String summary;
    public static String language;
    public static String country;
    public static String category;
    public static String tags;
    public static String article_url;
    public static String source_url;
    public static String source_name;
    public static String top_image;
    public static String publish_date;
    public static String authors;
    public static String meta_favicon;
    public static String trending;

    public News(){

    }

    public News(String id ,
            String title,
            String summary,
            String language,
             String country,
             String category,
             String tags,
             String article_url,
             String source_url,
             String source_name,
             String top_image,
             String publish_date,
             String authors,
             String meta_favicon,
             String trending)
    {

        this.id = id;
        this.title = title;
        this.summary = summary;
        this.language = language;
        this.country = country;
        this.category = category;
        this.tags = tags;
        this.article_url = article_url;
        this.source_url = source_url;
        this.source_name = source_name;
        this.top_image = top_image;
        this.publish_date = publish_date;
        this.authors = authors;
        this.meta_favicon = meta_favicon;
        this.trending= trending;


    }



    public static NewsBuilder builder() {
        return new NewsBuilder();
    }

    public static class NewsBuilder {

        public static String id ;
        public static String title;
        public static String summary;
        public static String language;
        public static String country;
        public static String category;
        public static String tags;
        public static String article_url;
        public static String source_url;
        public static String source_name;
        public static String top_image;
        public static String publish_date;
        public static String authors;
        public static String meta_favicon;
        public static String trending;


        public News build() {
            return new News( id ,
                     title,
                     summary,
                     language,
                     country,
                     category,
                     tags,
                     article_url,
                     source_url,
                     source_name,
                     top_image,
                     publish_date,
                     authors,
                     meta_favicon,
                     trending);


        }
    }

    @Override
    public String toString() {
        return "News{" +
                "source=" + source_name +
                ", title=" + title +
                '}';
    }




}
