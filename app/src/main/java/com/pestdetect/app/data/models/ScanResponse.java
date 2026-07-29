package com.pestdetect.app.data.models;

import java.io.Serializable;
import java.util.List;

public class ScanResponse implements Serializable {
    private String scanId;
    private String imageUrl;
    private Pest pest;
    private double confidenceScore;
    private boolean isHarmful;
    private List<Crop> affectedCrops;
    private List<Pesticide> recommendedPesticides;
    private String createdAt;

    public String getScanId() { return scanId; }
    public String getImageUrl() { return imageUrl; }
    public Pest getPest() { return pest; }
    public double getConfidenceScore() { return confidenceScore; }
    public boolean isHarmful() { return isHarmful; }
    public List<Crop> getAffectedCrops() { return affectedCrops; }
    public List<Pesticide> getRecommendedPesticides() { return recommendedPesticides; }
    public String getCreatedAt() { return createdAt; }
}
