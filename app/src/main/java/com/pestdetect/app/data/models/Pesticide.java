package com.pestdetect.app.data.models;

import java.io.Serializable;

public class Pesticide implements Serializable {
    private String id;
    private String name;
    private String activeIngredient;
    private String type; // organic / chemical
    private String dosage;
    private String applicationMethod;
    private String safetyNotes;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getActiveIngredient() { return activeIngredient; }
    public String getType() { return type; }
    public String getDosage() { return dosage; }
    public String getApplicationMethod() { return applicationMethod; }
    public String getSafetyNotes() { return safetyNotes; }
}
