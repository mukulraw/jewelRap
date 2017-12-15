package com.quixomtbx.jewelrap;

import com.quixomtbx.jewelrap.WalletPointPOJO.WalletBean;
import com.quixomtbx.jewelrap.categoriesPOJO.categoryBean;

import retrofit2.Call;
import retrofit2.http.GET;
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
            @Part("password") String password
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
