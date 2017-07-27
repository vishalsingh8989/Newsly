package com.kepler.news.newsly.databaseHelper;


import android.arch.persistence.room.*;

import java.util.List;


/**
 * Created by vishaljasrotia on 27/07/17.
 */

@Dao
public interface FeedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTask(Feed feed);

    @Query("select * from feed")
    public List<Feed> getAllFeeds();

    @Query("select * from feed where newsSource = :newsSource")
    public List<Feed> getFeed(String newsSource);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Feed feed);

    @Query("delete from feed")
    void removeAllTasks();
}
