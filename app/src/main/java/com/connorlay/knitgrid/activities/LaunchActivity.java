package com.connorlay.knitgrid.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.models.Stitch;
import com.connorlay.knitgrid.networking.KnitGridAPI;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        if (isFirstLaunch()) {
            getStitchesFromAPI();
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit().putBoolean
                    ("initial_launch", false).commit();
        } else {
            launchPatternListActivity();
        }
    }

    private void getStitchesFromAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://knitgrid-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KnitGridAPI api = retrofit.create(KnitGridAPI.class);

        api.indexStitches().enqueue(new Callback<List<Stitch>>() {
            @Override
            public void onResponse(Call<List<Stitch>> call, Response<List<Stitch>> response) {
                for (Stitch stitch : response.body()) {
                    switch (stitch.getAbbreviation()) {
                        case "K1":
                            stitch.setIconID(R.drawable.k);
                            break;
                        case "p":
                            stitch.setIconID(R.drawable.p);
                            break;
                        case "k2tog":
                            stitch.setIconID(R.drawable.k2tog);
                            break;
                        case "ssk":
                            stitch.setIconID(R.drawable.ssk);
                            break;
                        case "yo":
                            stitch.setIconID(R.drawable.yo);
                            break;
                        default:
                            stitch.setIconID(R.drawable.blank);
                            break;
                    }
                    stitch.save();
                }
                launchPatternListActivity();
            }

            @Override
            public void onFailure(Call<List<Stitch>> call, Throwable t) {
                Log.e("RETROFIT ERROR", t.getLocalizedMessage());
            }
        });
    }

    private void launchPatternListActivity() {
        Intent intent = new Intent(this, PatternListActivity.class);
        startActivity(intent);
    }

    private boolean isFirstLaunch() {
        return getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).getBoolean
                ("initial_launch", true);
    }
}

