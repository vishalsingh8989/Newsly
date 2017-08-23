package com.kepler.news.newsly.databaseHelper;


import android.arch.persistence.room.*;

/**
 * Created by vishaljasrotia on 27/07/17.
 */

@Entity
public class NewsSource {
    @PrimaryKey
    public  String newsSource;
    public boolean subscribed;
    public int priority;

    public NewsSource(String newsSource, boolean subscribed, int priority)
    {
        this.newsSource = newsSource;
        this.subscribed = subscribed;
        this.priority   = priority;

    }


    public static FeedBuilder builder(){
        return new FeedBuilder();
    }

    public static class FeedBuilder {
        public String newsSource;
        public boolean subscribed;
        public int priority;

        public FeedBuilder setNewsSource(String newsSource) {
            this.newsSource = newsSource;
            return this;
        }

        public FeedBuilder setPriority(int priority) {
            this.priority = priority;
            return this;
        }

        public FeedBuilder setSubscribed(boolean subscribed) {
            this.subscribed = subscribed;
            return this;
        }
        public NewsSource build() {
            return new NewsSource(newsSource, subscribed, priority);
        }
    }



    @Override
    public String toString() {
        return "NewsSource{" +
                "newsSource=" + newsSource +
                ", priority='" + priority + '\'' +
                ", subscribed=" + subscribed +
                '}';
    }

}
