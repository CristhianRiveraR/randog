package com.example.randog.models;

import java.io.Serializable;

public class Perro implements Serializable {
    private String fileSizeBytes,
                    url;

    public Perro(String fileSizeBytes, String url){
        this.fileSizeBytes = fileSizeBytes;
        this.url = url;
    }
    public String getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(String fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
