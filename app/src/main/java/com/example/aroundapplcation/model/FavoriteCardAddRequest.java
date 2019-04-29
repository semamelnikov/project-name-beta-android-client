package com.example.aroundapplcation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavoriteCardAddRequest {

    @SerializedName("cardId")
    @Expose
    private Long cardId;
}
