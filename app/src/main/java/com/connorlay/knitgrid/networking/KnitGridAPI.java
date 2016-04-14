package com.connorlay.knitgrid.networking;

import com.connorlay.knitgrid.models.Pattern;
import com.connorlay.knitgrid.models.Stitch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by connorlay on 4/12/16.
 */
public interface KnitGridAPI {
    @GET("v1/stitches")
    Call<List<Stitch>> indexStitches();

    @GET("v1/patterns")
    Call<List<Pattern>> indexPatterns();

    @POST("v1/patterns")
    Call<Void> createPattern(@Body Pattern pattern);
}
