package com.example.aroundapplcation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationResponse {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("hashPassword")
    @Expose
    private String hashPassword;

    @SerializedName("status")
    @Expose
    private String status;

    public RegistrationResponse(long id, String phone, String hashPassword, String status) {
        this.id = id;
        this.phone = phone;
        this.hashPassword = hashPassword;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
