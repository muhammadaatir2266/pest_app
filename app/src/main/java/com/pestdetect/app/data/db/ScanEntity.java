package com.pestdetect.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "offline_scans")
public class ScanEntity {

    @PrimaryKey
    @NonNull
    private String id;

    private String imageUrl;
    private String pestName;
    private String scientificName;
    private String description;
    private double confidenceScore;
    private boolean isHarmful;
    private String createdAt;
    private boolean isSynced;

    public ScanEntity(@NonNull String id, String imageUrl, String pestName, String scientificName, String description, double confidenceScore, boolean isHarmful, String createdAt, boolean isSynced) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.pestName = pestName;
        this.scientificName = scientificName;
        this.description = description;
        this.confidenceScore = confidenceScore;
        this.isHarmful = isHarmful;
        this.createdAt = createdAt;
        this.isSynced = isSynced;
    }

    @NonNull
    public String getId() { return id; }
    public String getImageUrl() { return imageUrl; }
    public String getPestName() { return pestName; }
    public String getScientificName() { return scientificName; }
    public String getDescription() { return description; }
    public double getConfidenceScore() { return confidenceScore; }
    public boolean isHarmful() { return isHarmful; }
    public String getCreatedAt() { return createdAt; }
    public boolean isSynced() { return isSynced; }

    public void setSynced(boolean synced) { isSynced = synced; }
}
