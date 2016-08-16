package com.teamtreehouse.techdegrees.testing;

/**
 * This class is used to model ApiError or Any Exception thrown in
 * requests. If any method throws exception in App, then
 * exception is handled and returns JSON:
 * {
 *     errorMessage: "some Message",
 *     status: 123
 * }
 * With this class gson.fromJson(request.body, ApiError.class) command
 * could be made to be able to compare both status and error message fields
 * errors easily.
 * May be there is a better way to do that. But this one was fastest I
 * come up with
 */
public class ApiErrorModel {
    private String errorMessage;
    private int status;

    public ApiErrorModel(String message, int status) {
        this.errorMessage = message;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiErrorModel that = (ApiErrorModel) o;

        if (status != that.status) return false;
        return errorMessage != null ? errorMessage.equals(that.errorMessage) : that.errorMessage == null;

    }

    @Override
    public int hashCode() {
        int result = errorMessage != null ? errorMessage.hashCode() : 0;
        result = 31 * result + status;
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "ApiErrorModel{" +
                "errorMessage='" + errorMessage + '\'' +
                ", status=" + status +
                '}';
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
