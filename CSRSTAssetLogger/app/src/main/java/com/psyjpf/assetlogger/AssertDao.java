package com.psyjpf.assetlogger;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface AssertDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Asset asset);

    @Transaction
    @Query("SELECT * FROM Asset")
    List<AssetViewModel> getAllAssets();
}
