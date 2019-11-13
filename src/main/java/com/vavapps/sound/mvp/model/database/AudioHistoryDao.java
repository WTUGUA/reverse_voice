package com.vavapps.sound.mvp.model.database;

import android.arch.persistence.room.*;
import com.vavapps.sound.mvp.model.entity.AudioEntity;
import io.reactivex.Single;

import java.util.List;

@Dao
public interface AudioHistoryDao {
    @Query("SELECT * FROM audio_history")
    Single<List<AudioEntity>> getAllAudios();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(AudioEntity audioEntity);

    @Delete
    void remove(AudioEntity audioEntity);

}
