package com.kepler.news.newsly.databaseHelper;


import android.arch.persistence.room.*;

import java.util.List;


/**
 * Created by vishaljasrotia on 27/07/17.
 */

@Dao
public interface NewsSourceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTask(NewsSource newsSource);

    @Query("select * from NewsSource")
    public List<NewsSource> getAllFeeds();

    @Query("select * from NewsSource where newsSource = :newsSource limit 0,50")
    public List<NewsSource> getFeed(String newsSource);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(NewsSource newsSource);

    @Query("select * from NewsSource order by priority ASC LIMIT 0,1")
    public int  getMaxPriority();

    @Query("select * from NewsSource where newsSource = :newsSource ")
    public NewsSource getSingleFeed(String newsSource);



    @Query("delete from NewsSource")
    void removeAllTasks();

    @Query("select * from NewsSource order by priority ASC")
    List<NewsSource> getPriorityFeeds();

}
