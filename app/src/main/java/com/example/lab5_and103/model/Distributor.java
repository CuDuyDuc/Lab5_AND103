package com.example.lab5_and103.model;

import com.google.gson.annotations.SerializedName;

public class Distributor {
    @SerializedName("_id")
    private String id;
    private String name, creatAt, updateAt;

    public Distributor() {
    }

    public Distributor(String id, String name, String creatAt, String updateAt) {
        this.id = id;
        this.name = name;
        this.creatAt = creatAt;
        this.updateAt = updateAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatAt() {
        return creatAt;
    }

    public void setCreatAt(String creatAt) {
        this.creatAt = creatAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
