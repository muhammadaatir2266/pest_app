package com.pestdetect.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ScanResponse implements Serializable {
    @SerializedName("isPestDetected")
    private boolean pestDetected;

    @SerializedName("message")
    private String message;

    @SerializedName("scanId")
    private String scanId;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("pest")
    private Pest pest;

    @SerializedName("confidenceScore")
    private double confidenceScore;

    @SerializedName("isHarmful")
    private boolean harmful;

    @SerializedName("affectedCrops")
    private List<Crop> affectedCrops;

    @SerializedName("recommendedPesticides")
    private List<Pesticide> recommendedPesticides;

    @SerializedName("createdAt")
    private String createdAt;

    public boolean isPestDetected() { return pestDetected; }
    public void setPestDetected(boolean pestDetected) { this.pestDetected = pestDetected; }

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

    public boolean isHarmful() { return harmful; }
    public void setHarmful(boolean harmful) { this.harmful = harmful; }

    public List<Crop> getAffectedCrops() { return affectedCrops; }
    public void setAffectedCrops(List<Crop> affectedCrops) { this.affectedCrops = affectedCrops; }

    public List<Pesticide> getRecommendedPesticides() { return recommendedPesticides; }
    public void setRecommendedPesticides(List<Pesticide> recommendedPesticides) { this.recommendedPesticides = recommendedPesticides; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
