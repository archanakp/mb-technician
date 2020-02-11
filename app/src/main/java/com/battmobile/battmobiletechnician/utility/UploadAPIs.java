package com.battmobile.battmobiletechnician.utility;

import com.battmobile.battmobiletechnician.chat.model.ChatListPojo;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UploadAPIs {

    @Multipart
    @POST("stock/api/upload-battery-image")
    Call<ResponseBody> uploadScrapBatteryImage(@Part MultipartBody.Part file, @Part("battery_image") RequestBody requestBody);

    @Multipart
    @POST("expenses/api/upload-receipt-image")
    Call<ResponseBody> uploadExpenseImage(@Part MultipartBody.Part file, @Part("receipt_image") RequestBody requestBody);

    @Multipart
    @POST("company-vehicles/api/upload-insurance-receipt")
    Call<ResponseBody> uploadInsuranceImage(@Part MultipartBody.Part file, @Part("receipt_image") RequestBody requestBody);

    @Multipart
    @POST("company-vehicles/api/upload-service-receipt")
    Call<ResponseBody> uploadServiceImage(@Part MultipartBody.Part file, @Part("receipt_image") RequestBody requestBody);

    @Multipart
    @POST("jobs/api/upload-job-image")
    Call<ResponseBody> uploadJobImage(@Part MultipartBody.Part file,
                                      @Part("image") RequestBody requestBody,
                                      @Part("job_id") RequestBody job_id,
                                      @Part("image_type") RequestBody image_type);

    @Multipart
    @POST("users/api/upload-profile-image")
    Call<ResponseBody> uploadProfileImage(@Part MultipartBody.Part file, @Part("profile_image") RequestBody requestBody);

    @GET("users-list")
    Observable<ChatListPojo> chatlistpojo(@Query("type") String type);
}
