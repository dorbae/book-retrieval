package io.dorbae.gb.bookretrieval.util;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.dorbae.gb.bookretrieval.exception.ApiNotReachableException;

import java.io.IOException;
import java.util.Map;

/*
 *****************************************************************
 *
 * HttpUtil
 *
 * @description HttpUtil
 *
 *
 *****************************************************************
 *
 * @version 1.0.0    2019/11/12 16:10     dorbae	최초 생성
 * @since 2019/11/12 16:10
 * @author dorbae(dorbae.io @ gmail.com)
 *
 */
public class HttpUtil {

    /**
     * @param url    URL
     * @param params Request Query Parameter
     * @return
     * @throws ApiNotReachableException
     * @throws IOException
     */
    public static String getHttpGetAsString(String url, Map<String, String> params) throws ApiNotReachableException, IOException {
        return getHttpGetAsString(url, null, params);
    }

    /**
     * @param url     URL
     * @param headers Request Header
     * @param params  Request Query Parameter
     * @return
     * @throws ApiNotReachableException
     * @throws IOException
     */
    public static String getHttpGetAsString(String url, Map<String, String> headers, Map<String, String> params) throws ApiNotReachableException, IOException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

        if (params != null && !params.isEmpty()) {
            params.entrySet().iterator()
                    .forEachRemaining(entry -> urlBuilder.addQueryParameter(entry.getKey(), entry
                            .getValue()));
        }

        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null && !headers.isEmpty()) {
            headers.entrySet().iterator()
                    .forEachRemaining(entry -> requestBuilder.addHeader(entry.getKey(), entry.getValue()));
        }

        Request getRequest = requestBuilder.url(urlBuilder.build()).get().build();
        OkHttpClient client = new OkHttpClient();

        Response response = client.newCall(getRequest).execute();
        if (response.code() != 200) {
            throw new ApiNotReachableException("Failed to get a proper result. [STATUS_CODE=" + response.code() + "]");
        }
        return response.body().string();
    }
}
