package com.pestdetect.app.data.models;

import java.io.Serializable;

public class Pest implements Serializable {
    private String id;
    private String name;
    private String scientificName;
    private String description;
    private boolean isHarmful;
    private String imageUrl;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isHarmful() { return isHarmful; }
    public void setHarmful(boolean harmful) { isHarmful = harmful; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
