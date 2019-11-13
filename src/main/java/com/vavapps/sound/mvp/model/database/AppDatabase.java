package com.vavapps.sound.mvp.model.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.vavapps.sound.mvp.model.entity.AudioEntity;

@Database(entities = {AudioEntity.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    private static final Object sLock = new Object();

    public abstract AudioHistoryDao audioHistoryDao();

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "audio.db")
                                .fallbackToDestructiveMigration()
                                .build();
            }
            return INSTANCE;
        }
    }
}
