package com.pestdetect.app.utils;

public class Constants {
    // Option 1: Live Railway Production Backend
    public static final String BASE_URL = "https://pestbackend-production.up.railway.app/api/";
    
    // Option 2: Local Backend on physical mobile device via PC Wi-Fi IP (Current PC IP: 10.145.139.101)
    // public static final String BASE_URL = "http://10.145.139.101:5000/api/";
    
    // Intent Extras
    public static final String EXTRA_IMAGE_PATH = "extra_image_path";
    public static final String EXTRA_SCAN_ID = "extra_scan_id";
    public static final String EXTRA_SCAN_RESULT = "extra_scan_result";
}
