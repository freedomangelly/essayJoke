package com.android.baselibrary.http;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/14.
 *         Date:2019/2/14
 * @description
 */

public interface RetrofitInterface {
    @GET
    Call getRetrofitGet(Map<String,Object> params);

    @POST
    Call getRetrofitPost(Map<String,Object> params);
}
