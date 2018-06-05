package bit.basemodule.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketException;
import java.net.UnknownHostException;

import bit.basemodule.BaseApplication;
import bit.basemodule.BuildConfig;
import bit.basemodule.response.BaseResponse;
import bit.basemodule.utility.AppConstant;
import bit.basemodule.utility.GsonParser;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
 A general  AsyncTask class used for hitting web service.
 */
public final class CommonAsyncTask<T> extends AsyncTask {
    private final String requestUrl;
    private final String mMethod;
    private final WeakReference<Activity> contextWeakReference;
    private final Callback callback;
    private final String authToken;
    private final String requestPayload;
    private ProgressDialog mProgressDialog;
    private String responseData;
    private int statusCode;

    public CommonAsyncTask(Activity activity, Callback callbackForRestExecAsyncTask, String authToken, String path, String method, T data, boolean showProgressDialog) {
        super();
        this.contextWeakReference = new WeakReference<>(activity);
        this.callback = callbackForRestExecAsyncTask;
        this.authToken = authToken;
        this.requestUrl = BuildConfig.HOST_URL + path;
        this.mMethod = method;
        this.requestPayload = new Gson().toJson(data);
        if (showProgressDialog) {
            this.mProgressDialog = new ProgressDialog(contextWeakReference.get());
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.setMessage("Please Wait");
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            if (mProgressDialog != null) {
                mProgressDialog.show();
                mProgressDialog.setCancelable(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {

        try {
            if (null != mMethod) {

                /*
                 * Add header which is passed
                 * */

                Headers.Builder headers = new Headers.Builder();
                /*                headers.add(AppConstant.AUTH_TOKEN, authToken);*/

                /*
                 * check request type [GET REQUEST]
                 * */
                Request request = null;
                if (AppConstant.GET.equals(mMethod)) {
                    HttpUrl.Builder builder = new HttpUrl.Builder();
                    if (requestUrl.contains("http") || requestUrl.contains("https")) {
                        request = new Request.Builder().url(requestUrl).headers(headers.build()).get().build();
                    } else {
                        HttpUrl url = builder.scheme("http").host(requestUrl).build();
                        request = new Request.Builder().url(url).headers(headers.build()).get().build();
                    }

                } else if (AppConstant.POST.equals(mMethod)) {
                    HttpUrl.Builder builder = new HttpUrl.Builder();
                    RequestBody body = null;
                    if (requestPayload != null) {
                        body = RequestBody.create(AppConstant.JSON_MEDIA_TYPE, requestPayload);
                    }
                    if (requestUrl.contains("http") || requestUrl.contains("https")) {
                        request = new Request.Builder().url(requestUrl).headers(headers.build()).post(body).build();
                    } else {
                        HttpUrl httpUrl = builder.scheme("http").host(requestUrl).build();
                        request = new Request.Builder().url(httpUrl).headers(headers.build()).post(body).build();
                    }

                }
                Response response = BaseApplication.getOkHttpClientInstance().newCall(request).execute();
                if (response.isSuccessful()) {
                    statusCode = response.code();
                    responseData = response.body().string();
                    response.body().close();
                    return responseData;
                } else {
                    statusCode = response.code();
                    response.body().close();
                    return responseData;
                }

            }
        } catch (UnknownHostException e) {
            statusCode = AppConstant.UnknownHostException;

        } catch (SocketException e) {
            statusCode = AppConstant.SocketException;
            e.printStackTrace();


        } catch (IOException e) {
            statusCode = AppConstant.IOException;
            e.printStackTrace();

        } catch (Exception e) {
            statusCode = AppConstant.EXCEPTION;
            e.printStackTrace();

        }
        return responseData;
    }

    @Override
    protected void onPostExecute(Object resp) {
        super.onPostExecute(resp);
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (responseData!=null) {
            BaseResponse baseResponse;
            try {
                baseResponse = GsonParser.parseStringToObject(responseData, BaseResponse.class);
                if (baseResponse.getStatusCode() == AppConstant.SUCCESS) {
                    callback.onSuccess(responseData);
                } else {
                    statusCode = baseResponse.getStatusCode();
                    callback.onFailure(statusCode, responseData, null);
                }
            } catch (Exception e) {
                callback.onFailure(statusCode, responseData, e);
                e.printStackTrace();
            }
        }else {
            callback.onFailure(statusCode, responseData, null);

        }
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}