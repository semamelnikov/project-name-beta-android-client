package com.example.aroundapplcation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhoneCheckRequest {
    @SerializedName("registrationSessionId")
    @Expose
    private String registrationSessionId;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("code")
    @Expose
    private String code;

    public PhoneCheckRequest(String registrationSessionId, String phone, String code) {
        this.registrationSessionId = registrationSessionId;
        this.phone = phone;
        this.code = code;
    }

    public String getRegistrationSessionId() {
        return registrationSessionId;
    }

    public void setRegistrationSessionId(String registrationSessionId) {
        this.registrationSessionId = registrationSessionId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
