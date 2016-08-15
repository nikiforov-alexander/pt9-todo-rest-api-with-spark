package com.teamtreehouse.techdegrees.testing;

/**
 * Very simple class, that has request body and status. Used in
 * @see com.teamtreehouse.techdegrees.testing.ApiClient
 * to be returned after request made to server in our integration test
 */
public class ApiResponse {
    private final int status;
    private final String body;

    public ApiResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }
}
