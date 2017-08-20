package com.kepler.news.newsly.databaseHelper;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by vishaljasrotia on 27/07/17.
 */

@Database(entities = {Feed.class}, version = 1, exportSchema = false)
public abstract class NewsSourceDatabase extends RoomDatabase {

    private static NewsSourceDatabase INSTANCE;

    public abstract FeedDao feedModel();

    public static NewsSourceDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context, NewsSourceDatabase.class, "feeds")
//                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(), NewsSourceDatabase.class)
                            // To simplify the exercise, allow queries on the main thread.
                            // Don't do this on a real app!
                            .allowMainThreadQueries()
                            .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
