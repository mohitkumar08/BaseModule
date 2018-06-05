package bit.basemodule.network;

import android.os.Process;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import bit.basemodule.BaseApplication;
import bit.basemodule.BuildConfig;
import bit.basemodule.response.BaseResponse;
import bit.basemodule.utility.AppConstant;
import bit.basemodule.utility.GsonParser;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class BackgroundRequest<T> implements Runnable {
    private final String requestUrl;
    private final String mMethod;
    private final Callback callback;
    private final String authToken;
    private final String requestPayload;
    private String responseData;
    private int statusCode;

    public BackgroundRequest(Callback callbackForRestExecAsyncTask, String authToken, String path, String method, T data) {
        this.callback = callbackForRestExecAsyncTask;
        this.authToken = authToken;
        this.requestUrl = BuildConfig.HOST_URL + path;
        this.mMethod = method;
        this.requestPayload = new Gson().toJson(data);

    }

    @Override
    public void run() {
        try {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            if (null != mMethod) {
                Headers.Builder headers = new Headers.Builder();

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
                BaseApplication.getOkHttpClientInstance().newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (e instanceof UnknownHostException) {
                            statusCode = AppConstant.UnknownHostException;

                        } else if (e instanceof SocketException) {
                            statusCode = AppConstant.SocketException;
                            e.printStackTrace();


                        } else if (e instanceof IOException) {
                            statusCode = AppConstant.IOException;
                            e.printStackTrace();

                        } else if (e instanceof Exception) {
                            statusCode = AppConstant.EXCEPTION;
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        statusCode = response.code();
                        responseData = response.body().string();
                        BaseResponse baseResponse;
                        try {
                           baseResponse = GsonParser.parseStringToObject(responseData, BaseResponse.class);
                            if (baseResponse.getStatusCode() == AppConstant.SUCCESS) {
                                callback.onSuccess(responseData);
                            } else {
                                statusCode = baseResponse.getStatusCode();
                                callback.onFailure(statusCode,responseData,null);
                            }
                        } catch (Exception e) {
                            callback.onFailure(statusCode,responseData,e);
                            e.printStackTrace();
                        }
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
