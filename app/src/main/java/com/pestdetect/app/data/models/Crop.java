package com.pestdetect.app.data.models;

import java.io.Serializable;

public class Crop implements Serializable {
    private String cropName;
    private String category;
    private String damageDescription;
    private String severity;

    public String getCropName() { return cropName; }
    public String getCategory() { return category; }
    public String getDamageDescription() { return damageDescription; }
    public String getSeverity() { return severity; }
}
