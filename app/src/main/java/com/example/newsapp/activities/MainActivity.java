package com.example.newsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.adapters.RecyclerAdapter;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    RecyclerView recyclerView;
    List<Article> articleList = new ArrayList<>();
    RecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;
    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Set the status bar color to white
            window.setStatusBarColor(getResources().getColor(R.color.white, getTheme()));

            // Set the status bar text color to dark (black)
            WindowInsetsController insetsController = window.getInsetsController();
            if (insetsController != null) {
                insetsController.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
            }
        } else {
            // For devices running Android 6.0 (API level 23) and above
            // Set the status bar color to white
            window.setStatusBarColor(getResources().getColor(R.color.white));

            // Set the status bar text color to dark (black)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);
        btn3=findViewById(R.id.btn3);
        btn4=findViewById(R.id.btn4);
        btn5=findViewById(R.id.btn5);
        btn6=findViewById(R.id.btn6);
        btn7=findViewById(R.id.btn7);
        searchView=findViewById(R.id.searchView);
        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.black));
        @SuppressLint("CutPasteId") EditText editText2 = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setHintTextColor(getResources().getColor(R.color.black));
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        recyclerView = findViewById(R.id.rv);
        progressIndicator = findViewById(R.id.progressBar);

        getNews("GENERAL",null);
        setRecyclerView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNews("GENERAL",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(articleList);
        recyclerView.setAdapter(adapter);

    }

    private void changeProgressBar(boolean show) {
        if(show){
            progressIndicator.setVisibility(View.VISIBLE);
        }
        else {
            progressIndicator.setVisibility(View.INVISIBLE);
        }
    }

    private void getNews(String category,String query) {

        //Retrieving API Key...
        Properties properties = new Properties();
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("apikeys.properties");
            properties.load(inputStream);
            String apiKey = properties.getProperty("API_KEY");
            Log.e("APIFetching","key "+apiKey);

            NewsApiClient newsApiClient = new NewsApiClient(apiKey);
            newsApiClient.getTopHeadlines(
                    new TopHeadlinesRequest.Builder()
                            .language("en")
                            .category(category)
                            .q(query)
                            .build(),
                    new NewsApiClient.ArticlesResponseCallback() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(ArticleResponse response) {
                            runOnUiThread(() ->{
                                changeProgressBar(false);
                                articleList = response.getArticles();
                                adapter.updateData(articleList);
                                adapter.notifyDataSetChanged();
                            });


                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.e("NewsApp", "Error: " + throwable.getMessage());
                        }
                    }
            );

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("APIFetching","Error is "+ e.getMessage());
            Toast.makeText(this, "Something went wrong. Please try again after some time", Toast.LENGTH_SHORT).show();
            // Handle file not found or other exceptions
        }

        changeProgressBar(true);

    }

    @Override
    public void onClick(View view) {
        Vibrator vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(30);
        Button btn = (Button) view;
        String category = btn.getText().toString();
        getNews(category,null);
    }
}