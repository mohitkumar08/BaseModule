package bit.basemodule.network;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.yumchek.thali55delivery.R;
import com.yumchek.thali55delivery.request.RequestConfirmOrder;
import com.yumchek.thali55delivery.util.AppConstant;
import com.yumchek.thali55delivery.util.AppSharedPref;
import com.yumchek.thali55delivery.util.CheckConnection;

import java.util.concurrent.Executors;

/**
 * Created by bit on 17/2/18.
 */

public class CommonNetworkRequestUtil {

    public static void thaliConfirmOrderDelivery(FragmentActivity fragmentActivity, Callback callback, String orderId, String paymentMode) {
        if (CheckConnection.isNetworkAvailable()) {
            RequestConfirmOrder requestConfirmOrder = new RequestConfirmOrder();
            requestConfirmOrder.setOrderId(orderId);
            requestConfirmOrder.setPaymentMode(paymentMode);
            new CommonAsyncTask(fragmentActivity, callback, AppSharedPref.getStringValue(AppConstant.AUTH_TOKEN), AppConstant.ORDER_CONFIRMED, AppConstant.POST, requestConfirmOrder, true).executeOnExecutor(Executors.newCachedThreadPool());
        } else {
            Toast.makeText(fragmentActivity, fragmentActivity.getString(R.string.internet_error), Toast.LENGTH_LONG).show();
        }
    }
    /*Callback callback = new Callback() {
        @Override
        public void onSuccess(String response) {
            if (response != null) {
                BaseResponse baseResponse = GsonParser.parseStringToObject(response, BaseResponse.class);
                if (baseResponse.getStatusCode() == AppConstant.SUCCESS) {

                }
            }
        }

        @Override
        public void onFailure(int statusCode) {
        }
    };*/
}
