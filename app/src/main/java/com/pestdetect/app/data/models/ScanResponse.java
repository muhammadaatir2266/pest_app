package com.pestdetect.app.data.models;

import java.io.Serializable;
import java.util.List;

public class ScanResponse implements Serializable {
    private boolean isPestDetected = true;
    private String message;
    private String scanId;
    private String imageUrl;
    private Pest pest;
    private double confidenceScore;
    private boolean isHarmful;
    private List<Crop> affectedCrops;
    private List<Pesticide> recommendedPesticides;
    private String createdAt;

    public boolean isPestDetected() { return isPestDetected; }
    public void setPestDetected(boolean pestDetected) { isPestDetected = pestDetected; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getScanId() { return scanId; }
    public void setScanId(String scanId) { this.scanId = scanId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Pest getPest() { return pest; }
    public void setPest(Pest pest) { this.pest = pest; }

    public double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; }

    public boolean isHarmful() { return isHarmful; }
    public void setHarmful(boolean harmful) { isHarmful = harmful; }

    public List<Crop> getAffectedCrops() { return affectedCrops; }
    public void setAffectedCrops(List<Crop> affectedCrops) { this.affectedCrops = affectedCrops; }

    public List<Pesticide> getRecommendedPesticides() { return recommendedPesticides; }
    public void setRecommendedPesticides(List<Pesticide> recommendedPesticides) { this.recommendedPesticides = recommendedPesticides; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
