package com.kepler.news.newsly.databaseHelper;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by vishaljasrotia on 12/25/17.
 */

@Database(entities = {News.class}, version  = 1, exportSchema = false)

public abstract class NewsDatabase extends RoomDatabase {

    private static NewsDatabase INSTANCE;

    public abstract NewsDao feedModel();

    public static NewsDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), NewsDatabase.class, "newsly-database")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
