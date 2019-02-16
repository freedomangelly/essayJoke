package com.android.baselibrary.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/15.
 *         Date:2019/2/15
 * @description
 */

public class LoggingInterceptor implements Interceptor {
    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();

        return response;
    }
}
