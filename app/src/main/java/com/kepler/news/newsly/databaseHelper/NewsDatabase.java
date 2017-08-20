package com.kepler.news.newsly.databaseHelper;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by vishaljasrotia on 8/19/17.
 */

@Database(entities = {News.class}, version = 1, exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {

    private static NewsDatabase NEWSDBINSTANCE;

    public abstract NewsDao feedModel();

    public static NewsDatabase getDatabase(Context context) {
        if (NEWSDBINSTANCE == null) {
            NEWSDBINSTANCE =
                    Room.databaseBuilder(context, NewsDatabase.class, "news")
//                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(), NewsSourceDatabase.class)
                            // To simplify the exercise, allow queries on the main thread.
                            // Don't do this on a real app!
                            .allowMainThreadQueries()
                            .build();
        }

        return NEWSDBINSTANCE;
    }

    public static void destroyInstance() {
        NEWSDBINSTANCE = null;
    }

}
