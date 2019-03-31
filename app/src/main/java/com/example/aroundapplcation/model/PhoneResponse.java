package com.example.aroundapplcation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhoneResponse {

    @SerializedName("process")
    @Expose
    private String process;

    @SerializedName("registrationSessionId")
    @Expose
    private String registrationSessionId;

    @SerializedName("processAvailable")
    @Expose
    private boolean processAvailable;

    @SerializedName("phoneChecked")
    @Expose
    private boolean phoneChecked;

    @SerializedName("codeSent")
    @Expose
    private boolean codeSent;

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getRegistrationSessionId() {
        return registrationSessionId;
    }

    public void setRegistrationSessionId(String registrationSessionId) {
        this.registrationSessionId = registrationSessionId;
    }

    public boolean isProcessAvailable() {
        return processAvailable;
    }

    public void setProcessAvailable(boolean processAvailable) {
        this.processAvailable = processAvailable;
    }

    public boolean isPhoneChecked() {
        return phoneChecked;
    }

    public void setPhoneChecked(boolean phoneChecked) {
        this.phoneChecked = phoneChecked;
    }

    public boolean isCodeSent() {
        return codeSent;
    }

    public void setCodeSent(boolean codeSent) {
        this.codeSent = codeSent;
    }
}
