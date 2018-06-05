package bit.basemodule.request;

import com.google.gson.annotations.Expose;

public class BaseRequest {

    @Expose
    private String userId;

    public BaseRequest(String userId) {
        this.userId = userId;
    }

    public BaseRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
