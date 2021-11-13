package com.example.randog.models;

import java.io.Serializable;

public class Perro implements Serializable {
    private String fileSizeBytes,
                    url,
                    uuid;

    public Perro(String fileSizeBytes, String url, String uuid){
        this.fileSizeBytes = fileSizeBytes;
        this.url = url;
        this.uuid = uuid;
    }

    public Perro(){

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
