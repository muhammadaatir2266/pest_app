package com.pestdetect.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pest implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("scientificName")
    private String scientificName;

    @SerializedName("description")
    private String description;

    @SerializedName("isHarmful")
    private boolean harmful;

    @SerializedName("imageUrl")
    private String imageUrl;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isHarmful() { return harmful; }
    public void setHarmful(boolean harmful) { this.harmful = harmful; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}

