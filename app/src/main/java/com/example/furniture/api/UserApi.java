package com.example.furniture.api;

import com.example.furniture.models.AvatarResponse;
import com.example.furniture.models.DeleteAvatarResponse;
import com.example.furniture.models.UserProfileResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface UserApi {
    @GET("api/Users/profile")
    Call<UserProfileResponse> getUserProfile();

    @Multipart
    @PUT("api/Users/avatar/upload")
    Call<AvatarResponse> uploadAvatar(@Part MultipartBody.Part file);

    @DELETE("api/Users/avatar")
    Call<DeleteAvatarResponse> deleteAvatar();
}
