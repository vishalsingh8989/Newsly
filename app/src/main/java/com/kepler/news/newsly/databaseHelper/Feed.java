package com.kepler.news.newsly.databaseHelper;


import android.arch.persistence.room.*;


import java.util.Date;
/**
 * Created by vishaljasrotia on 27/07/17.
 */

@Entity
public class Feed {
    @PrimaryKey
    public  String newsSource;
    public boolean subscribed;
    public int priority;

    public Feed(String newsSource, boolean subscribed, int priority)
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
        public Feed build() {
            return new Feed(newsSource, subscribed, priority);
        }
    }



    @Override
    public String toString() {
        return "Feed{" +
                "newsSource=" + newsSource +
                ", priority='" + priority + '\'' +
                ", subscribed=" + subscribed +
                '}';
    }

}
