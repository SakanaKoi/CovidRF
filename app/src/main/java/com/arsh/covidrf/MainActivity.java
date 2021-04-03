package com.arsh.covidrf;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import static com.arsh.covidrf.Consts.URL_GET_STATUS_RF;

//TODO 01 добавить gradle-зависимость :implementation 'com.android.volley:volley:1.1.1'                             DONE
//TODO 02 создать объект класса RequestQueue ("очередь запросов")
//TODO 03 создать объект класса Request ("запрос")
//TODO 05 реализовать листенеры для обрабтки успешного ответа и ошибки
//TODO 06 вынести создание очереди запросов RequestQueue в синглетон (создать класс-наследник Application)
//TODO 07 добавить вью для выбора страны из доступных и реализовать отправку запросов

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView tvCured;
    private TextView tvDeath;
    private TextView tvTotal;
    //private Loader<String> stringLoader;
    //TODO перенести RequestQueue в положенное место
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //stringLoader = LoaderManager.getInstance(this).initLoader(1, null, new MyLoaderCallBack());
        tvCured = findViewById(R.id.tvCured);
        tvDeath = findViewById(R.id.tvDeaths);
        tvTotal = findViewById(R.id.tvCases);
        progressBar = findViewById(R.id.progressBar);
        progressBar.animate();
        doRequestToApi("RU");
    }

    private void doRequestToApi(String countryCode) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_GET_STATUS_RF, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                CountryStatus obj = gson.fromJson(String.valueOf(response), CountryStatus.class);
                fillTextView(obj.getRecovered(), obj.getDeaths(), obj.getCases());
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
            }
        });
        App.getApp().addToRequestQueue(request);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //stringLoader.forceLoad();
    }

    public void fillTextView(int recovered, int deaths, int cases){
        tvCured.setText("" + recovered);
        tvDeath.setText("" + deaths);
        tvTotal.setText("" + cases);
    }

//    class MyLoaderCallBack implements LoaderManager.LoaderCallbacks<String> {
//        @NonNull
//        @Override
//        public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
//            return new MyAsyncTaskLoader(MainActivity.this);
//        }
//
//        @Override
//        public void onLoadFinished(@NonNull Loader<String> loader, String data) {
//            Gson gson = new Gson();
//            try {
//                CountryStatus obj = gson.fromJson(data, CountryStatus.class);
//                fillTextView(obj.getRecovered(),obj.getDeaths(),obj.getCases());
//            } catch (RuntimeException ex) {
//                Toast.makeText(MainActivity.this,data,Toast.LENGTH_LONG).show();
//            }
//            progressBar.setVisibility(View.INVISIBLE);
//        }
//
//        @Override
//        public void onLoaderReset(@NonNull Loader<String> loader) {
//            loader = null;
//        }
//    }
}