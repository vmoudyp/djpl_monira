package id.exorty.snpk.monira.service;

import id.exorty.snpk.monira.ui.model.ReturnDataReq;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiEndpointInterface {
    @GET("get_activity_detail_data/{id_type_of_activity}")
    Call<ReturnDataReq> getActivityDetailData(@Header("Content-Type") String content_type,
                                              @Header("Authorization") String auth,
                                              @Header("app_name") String app_name,
                                              @Header("device_id") String device_id,
                                              @Header("token") String token,
                                              @Path("id_type_of_activity") int id_type_of_activity, @Query("TA") int year);
}
