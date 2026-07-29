package com.pestdetect.app.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScanDao {

    @Query("SELECT * FROM offline_scans ORDER BY createdAt DESC")
    List<ScanEntity> getAllScans();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertScan(ScanEntity scan);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ScanEntity> scans);

    @Query("DELETE FROM offline_scans WHERE id = :id")
    void deleteScanById(String id);

    @Query("DELETE FROM offline_scans")
    void clearAll();
}
