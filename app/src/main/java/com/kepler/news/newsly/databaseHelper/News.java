package com.kepler.news.newsly.databaseHelper;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by vishaljasrotia on 8/19/17.
 */

@Entity
public class News {
    @PrimaryKey
    public  String id;
    public  String title;
    public  String description;
    public  String publishedat;
    public  String source;
    public  String sourceName;
    public  String url;
    public  String urltoimage;
    public  String author;
    public  String language;
    public  String country;
    public  String category;
    public  int  addtime;


    public News(String id,
                String title,
                String description,
                String publishedat,
                String source,
                String sourceName,
                String url,
                String urltoimage,
                String author,
                String language,
                String country,
                String category,

                int addtime) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.publishedat = publishedat;
        this.source = source;
        this.sourceName = sourceName;
        this.url = url;
        this.urltoimage = urltoimage;
        this.author =author;
        this.language= language;
        this.country = country;
        this.category = category;
        this.addtime = addtime;

    }



    public static NewsBuilder builder(){
        return new NewsBuilder();
    }

    public static class NewsBuilder {
        public  String id;
        public  String title;
        public  String description;
        public  String publishedat;
        public  String source;
        public  String sourceName;
        public  String url;
        public  String urltoimage;
        public  String author;
        public  String language;
        public  String country;
        public  String name;
        public  String category;
        public int addtime;


        public News build() {
            return new News(id,title, description, publishedat,source,sourceName,url,urltoimage,author,language,country,category, addtime);
        }
    }

    @Override
    public String toString() {
        return "News{" +
                "source=" + source +
                ", title=" + title +
                '}';
    }




}
