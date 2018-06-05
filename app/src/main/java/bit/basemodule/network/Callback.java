package bit.basemodule.network;

/**
 * Created by bit on 14/12/17.
 */

public interface Callback {
    void onSuccess(String response);

    void onFailure(int statusCode,String data,Exception e);
}
