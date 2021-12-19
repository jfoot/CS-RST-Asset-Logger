package com.psyjpf.assetlogger;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Asset.class}, version = 1, exportSchema = false)
public abstract class ROOMDatabase extends RoomDatabase {


    public abstract AssertDao timeTableDao();

    private static volatile ROOMDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 10;
    static ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ROOMDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ROOMDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ROOMDatabase.class, "assert_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(createCallback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    private static final RoomDatabase.Callback createCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
