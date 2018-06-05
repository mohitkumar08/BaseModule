package bit.basemodule.response;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class BaseResponse implements Serializable {
    @Expose
    protected int statusCode;
    @Expose
    protected String message;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
