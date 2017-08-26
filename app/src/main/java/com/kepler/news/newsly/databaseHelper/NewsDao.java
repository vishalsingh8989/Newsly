package com.kepler.news.newsly.databaseHelper;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.kepler.news.newsly.NewsStory;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.arch.persistence.room.RoomWarnings.*;

/**
 * Created by vishaljasrotia on 8/19/17.
 */


@Dao
public interface NewsDao {


    @Insert(onConflict = OnConflictStrategy.ABORT)
    void addNews(News news);

    @Query("select count(*) from news")
    int getNewsCount();

    @SuppressWarnings(CURSOR_MISMATCH)
    @Query("select * from news where sourceName =:sourceName order by addtime DESC")
    List<NewsStory> getSourceNews(String sourceName);

    //@SuppressWarnings(CURSOR_MISMATCH)
    @Query("select * from news ORDER BY addtime  DESC limit 0, 200 ")
    List<NewsStory> getAllNews();



    @Query("select MAX(addtime) from news")
    int getLastFetchTime();


    @Query("delete from news where publishedat =:publishedat ")
    void delete(String publishedat);


    /*UPDATE table_name
    SET column1 = value1, column2 = value2, ...
    WHERE condition;
    */
    @Update
    void updateLike(News news);

    @Query("select * from news where id=:id ")
    News getLikeStatus(String id);

    @Query("select * from news where id=:id ")
    News getBookMarkStatus(String id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateBookmark(News likeStatus);


    @Query("select * from news where bookmark =:status")
    List<NewsStory> getBookMarkedNews(boolean status);
}


