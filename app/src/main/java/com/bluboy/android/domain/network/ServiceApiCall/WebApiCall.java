package com.bluboy.android.domain.network.ServiceApiCall;


import com.bluboy.android.data.models.ForceUpdateRs;
import com.bluboy.android.data.models.IpResponse;
import com.bluboy.android.data.models.RoomRs;
import com.bluboy.android.data.models.UserEndGameRs;
import com.bluboy.android.presentation.utility.AppConstant;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by sparken02 on 27/6/17.
 */

public interface WebApiCall {

    @FormUrlEncoded
    @POST("Game_api/user_leave_game")
    Call<UserEndGameRs>userLeaveGameApi(@Field(AppConstant.authKey)String authKey,
                                        @Field(AppConstant.tournament_id) String tournamentId,
                                        @Field(AppConstant.roomCode) String roomCode);
//                                        @Field(AppConstant.PARAM_APP_VERSION) String appVersion);

    @FormUrlEncoded
    @POST("afterauth/get_user_room_id")
    Call<RoomRs>getUserRoom(@Field(AppConstant.authKey)String authKey,
                            @Field(AppConstant.tournament_id) String tournamentId,
                            @Field(AppConstant.PARAM_APP_VERSION) String appVersion );

    @FormUrlEncoded
    @POST("Beforeauth/check_version_update")
    Call<ForceUpdateRs>forceUpdateApp(@Field(AppConstant.version) String version);

    @GET(".")
    Call<IpResponse> getIPAddress(@Query("format") String param);

}