package com.example.aroundapplcation.http;

import android.content.SharedPreferences;

import com.example.aroundapplcation.model.LoginResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.example.aroundapplcation.constants.SharedPreferencesConstants.ACCESS_TOKEN;
import static com.example.aroundapplcation.constants.SharedPreferencesConstants.REFRESH_TOKEN;
import static com.example.aroundapplcation.constants.SharedPreferencesConstants.USER_ID;
import static java.util.Arrays.asList;

public class TokenInterceptor implements Interceptor {
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final String REFRESH_TOKEN_URL = "/auth/tokens/refresh";

    private static final String AUTH_HEADER = "Authorization";

    private static final List freeUrls = asList(
            "/auth/entry",
            "/auth/phone/check",
            "/auth/registration",
            "/auth/login",
            "/auth/tokens/refresh"
    );

    private final OkHttpClient client;
    private final String baseUrl;
    private final SharedPreferences sharedPreferences;

    public TokenInterceptor(final SharedPreferences sharedPreferences, final String baseUrl) {
        this.sharedPreferences = sharedPreferences;
        this.baseUrl = baseUrl;
        client = new OkHttpClient.Builder().build();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request initialRequest = chain.request();
        if (isHeaderUpdateNeeded(initialRequest)) {
            initialRequest = initialRequest.newBuilder().addHeader(AUTH_HEADER, getAccessToken()).build();
        }

        final Response response = chain.proceed(initialRequest);

        if (isTokenRefreshmentNeeded(response)) {
            final Request tokenRefreshRequest = getRefreshTokenRequest();
            final Response tokenRefreshResponse = client.newCall(tokenRefreshRequest).execute();

            if (tokenRefreshResponse.isSuccessful() && tokenRefreshResponse.body() != null) {
                final LoginResponse refreshResponse = parseRefreshResponse(tokenRefreshResponse.body());
                final Request initialRequestRetry = initialRequest.newBuilder()
                        .header(AUTH_HEADER, refreshResponse.getAccessToken())
                        .build();
                final Response initialRetryResponse = chain.proceed(initialRequestRetry);

                return finalResponse(initialRetryResponse, refreshResponse);
            } else {
                clearSharedPreferences();
                return null;
            }
        } else {
            return response;
        }
    }

    private Response finalResponse(final Response initialRetryResponse, final LoginResponse refreshResponse) {
        if (initialRetryResponse.isSuccessful()) {
            updateSharedPreferences(refreshResponse);
            return initialRetryResponse;
        } else {
            clearSharedPreferences();
            return null;
        }
    }

    private boolean isHeaderUpdateNeeded(final Request request) {
        return !freeUrls.contains(request.url().encodedPath());
    }

    private void clearSharedPreferences() {
        sharedPreferences.edit().clear().apply();
    }

    private void updateSharedPreferences(final LoginResponse refreshResponse) {
        sharedPreferences.edit()
                .putString(ACCESS_TOKEN, refreshResponse.getAccessToken())
                .putString(REFRESH_TOKEN, refreshResponse.getRefreshToken())
                .putLong(USER_ID, refreshResponse.getUserId())
                .apply();
    }

    private boolean isTokenRefreshmentNeeded(final Response response) {
        final boolean isCodeValid = response.code() == HttpURLConnection.HTTP_FORBIDDEN;
        final String url = response.request().url().encodedPath();
        return isCodeValid && !freeUrls.contains(url);
    }

    private LoginResponse parseRefreshResponse(final ResponseBody body) throws IOException {
        try {
            final JSONObject jsonObject = new JSONObject(body.string());
            final String accessToken = jsonObject.getString(ACCESS_TOKEN);
            final String refreshToken = jsonObject.getString(REFRESH_TOKEN);
            final long userId = jsonObject.getLong(USER_ID);
            return new LoginResponse(accessToken, refreshToken, userId);
        } catch (JSONException e) {
            throw new IllegalArgumentException("Refresh Token JSON parsing failed in TokenInterceptor");
        }
    }

    private Request getRefreshTokenRequest() {
        final RequestBody requestBody = RequestBody.create(JSON, getJsonRefreshToken());
        return new Request.Builder()
                .url(baseUrl + REFRESH_TOKEN_URL)
                .post(requestBody)
                .build();
    }

    private String getJsonRefreshToken() {
        final JSONObject jsonRefreshToken = new JSONObject();
        try {
            jsonRefreshToken.put(REFRESH_TOKEN, getRefreshToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonRefreshToken.toString();
    }

    private String getAccessToken() {
        return sharedPreferences.getString(ACCESS_TOKEN, "");
    }

    private String getRefreshToken() {
        return sharedPreferences.getString(REFRESH_TOKEN, "");
    }
}
