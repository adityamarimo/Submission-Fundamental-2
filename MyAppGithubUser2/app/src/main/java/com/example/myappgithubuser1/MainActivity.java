package com.example.myappgithubuser1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView rvGithubUser;
    private ProgressBar progressBar;
    private SearchView searchView;
    private TextView textView;
    private ImageView imageView;

    private String[] dataName;
    private String[] dataCompany;
    private String[] dataUsername;
    private String[] dataRepository;
    private String[] dataFollower;
    private String[] dataFollowing;
    private String[] dataLocation;
    private String[] dataPhoto;
    private ArrayList<GithubUser> listGithubUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Github User App");

        progressBar = findViewById(R.id.progressBar);
        searchView = findViewById(R.id.search);
        imageView = findViewById(R.id.imgNotFoundMain);
        textView = findViewById(R.id.txtNotFoundMain);
        rvGithubUser = findViewById(R.id.rv_list);
        rvGithubUser.setHasFixedSize(true);

        imageView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);

        Search();
        getListUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void Search() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    listGithubUsers.clear();
                    getListUser();
                } else {
                    listGithubUsers.clear();
                    getsearchUser(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getListUser() {
        progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.github.com/users";
        client.addHeader("User-Agent", "request");
        client.addHeader("Authorization", "token 59d9d071fd9afbefd510033da6866d667efe6ff4");

        RequestHandle requestHandle = client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);

                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Log.d(TAG,"PANJANG JSON : "+jsonArray.length());

                    if (jsonArray.length() == 0){
                        imageView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                    }else{
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String username = jsonObject.get("login").toString();
                            getUserDetail(username);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                // Parsing JSON
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.INVISIBLE);
                String errorMessage;
                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbiden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage = "Gagal Koneksi Dengan API Github";
                        break;
                }
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getsearchUser(String username) {
        progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.github.com/search/users?q=" + username;
        client.addHeader("User-Agent", "request");
        client.addHeader("Authorization", "token 59d9d071fd9afbefd510033da6866d667efe6ff4");

        RequestHandle requestHandle = client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);

                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    if (jsonArray.length() == 0){
                        imageView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                    }else{
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            String username = item.get("login").toString();
                            getUserDetail(username);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                // Parsing JSON
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.INVISIBLE);
                String errorMessage;
                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbiden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage = "Gagal Koneksi Dengan API Github";
                        break;
                }
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserDetail(String username) {
        progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.github.com/users/" + username;
        client.addHeader("User-Agent", "request");
        client.addHeader("Authorization", "token 59d9d071fd9afbefd510033da6866d667efe6ff4");

        RequestHandle requestHandle = client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);

                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    GithubUser githubUser = new GithubUser();
                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.getString("company") == "null") githubUser.setCompany("-");
                    else githubUser.setCompany(jsonObject.getString("company"));

                    if (jsonObject.getString("name") == "null") githubUser.setName("-");
                    else githubUser.setName(jsonObject.getString("name"));

                    if (jsonObject.getString("location") == "null") githubUser.setLocation("-");
                    else githubUser.setLocation(jsonObject.getString("location"));

                    githubUser.setLogin(jsonObject.getString("login"));
                    githubUser.setAvatar(jsonObject.getString("avatar_url"));
                    githubUser.setRepository(jsonObject.getString("public_repos"));
                    githubUser.setFollowers(jsonObject.getString("followers"));
                    githubUser.setFollowing(jsonObject.getString("following"));

                    listGithubUsers.add(githubUser);

                    showRecyclerList();

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                // Parsing JSON
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.INVISIBLE);
                String errorMessage;
                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbiden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage = "Gagal Koneksi Dengan API Github";
                        break;
                }
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRecyclerList() {
        rvGithubUser.setLayoutManager(new LinearLayoutManager(this));
        GithubUserAdapter listHeroAdapter = new GithubUserAdapter(listGithubUsers);
        rvGithubUser.setAdapter(listHeroAdapter);

        listHeroAdapter.setOnItemClickCallback(new GithubUserAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(GithubUser data) {
                showselectedList(data);
            }
        });
    }


    private void showselectedList(GithubUser githubUser) {
        Intent moveToDetailActivity = new Intent(MainActivity.this, DetailActivity.class);
        moveToDetailActivity.putExtra(DetailActivity.EXTRA_GITHUB, githubUser);
        startActivity(moveToDetailActivity);
    }
}
