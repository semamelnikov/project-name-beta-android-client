package com.example.aroundapplcation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhoneCheckResponse {

    @SerializedName("registrationSessionId")
    @Expose
    private String registrationSessionId;

    @SerializedName("codeChecked")
    @Expose
    private boolean codeChecked;

    @SerializedName("codeInvalid")
    @Expose
    private boolean codeInvalid;

    @SerializedName("expirationTimeOver")
    @Expose
    private boolean expirationTimeOver;

    @SerializedName("attemptLimitOver")
    @Expose
    private boolean attemptLimitOver;

    public PhoneCheckResponse(String registrationSessionId, boolean codeChecked, boolean codeInvalid, boolean expirationTimeOver, boolean attemptLimitOver) {
        this.registrationSessionId = registrationSessionId;
        this.codeChecked = codeChecked;
        this.codeInvalid = codeInvalid;
        this.expirationTimeOver = expirationTimeOver;
        this.attemptLimitOver = attemptLimitOver;
    }

    public String getRegistrationSessionId() {
        return registrationSessionId;
    }

    public void setRegistrationSessionId(String registrationSessionId) {
        this.registrationSessionId = registrationSessionId;
    }

    public boolean isCodeChecked() {
        return codeChecked;
    }

    public void setCodeChecked(boolean codeChecked) {
        this.codeChecked = codeChecked;
    }

    public boolean isCodeInvalid() {
        return codeInvalid;
    }

    public void setCodeInvalid(boolean codeInvalid) {
        this.codeInvalid = codeInvalid;
    }

    public boolean isExpirationTimeOver() {
        return expirationTimeOver;
    }

    public void setExpirationTimeOver(boolean expirationTimeOver) {
        this.expirationTimeOver = expirationTimeOver;
    }

    public boolean isAttemptLimitOver() {
        return attemptLimitOver;
    }

    public void setAttemptLimitOver(boolean attemptLimitOver) {
        this.attemptLimitOver = attemptLimitOver;
    }
}
