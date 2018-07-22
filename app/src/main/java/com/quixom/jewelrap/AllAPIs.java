package com.quixom.jewelrap;

import com.quixom.jewelrap.WalletPointPOJO.WalletBean;
import com.quixom.jewelrap.categoriesPOJO.categoryBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by TBX on 12/8/2017.
 */

public interface AllAPIs {

    @Multipart
    @POST("user/login/")
    Call<loginBean> login(
            @Part("email") String email,
            @Part("password") String password,
            @Part("android_device_id") String deviceId

    );


    @GET("user/get_paid_status/")
    Call<String> getStatus(
            //@Header("Authorization") String auth,
            //@Header("X-Android") String version
    );


    @Headers({
            "Accept: application/vnd.bambuser.v1+json",
            "Authorization: Bearer 374rnkqn332isfldjc8a3oki8"
    })


    @GET("api/v1/inventory/")
    Call<categoryBean> getCategories();




    @GET("api/v1/walletpoint/")
    Call<WalletBean> wallet(@Query("user_id") String userId);

}
