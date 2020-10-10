package com.example.myappgithubuser1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FollowingFragment extends Fragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<GithubUser> listFollowing = new ArrayList<>();

    ProgressBar progressBar;
    RecyclerView rvFollowing;
    TextView txtNotFound;
    ImageView imgNotFound;

    public FollowingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBarFollowing);
        imgNotFound = view.findViewById(R.id.imgNotFoundFollowing);
        txtNotFound = view.findViewById(R.id.txtNotFoundFollowing);

        imgNotFound.setVisibility(View.INVISIBLE);
        txtNotFound.setVisibility(View.INVISIBLE);
        rvFollowing = view.findViewById(R.id.rv_listFollowing);
        rvFollowing.setHasFixedSize(true);

        listFollowing.clear();
        GithubUser githubUser = getActivity().getIntent().getParcelableExtra(DetailActivity.EXTRA_GITHUB);
        String username = githubUser.getLogin();

        getListFollower(username);
    }

    private void getListFollower(String username) {
        progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.github.com/users/" + username + "/following";
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

                    if (jsonArray.length() == 0){
                        imgNotFound.setVisibility(View.VISIBLE);
                        txtNotFound.setVisibility(View.VISIBLE);
                    }
                    else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String username = jsonObject.get("login").toString();
                            getDetailUser(username);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
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
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDetailUser(String username) {
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

                    listFollowing.add(githubUser);

                    showRecyclerList();

                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void showRecyclerList() {
        rvFollowing.setLayoutManager(new LinearLayoutManager(getActivity()));

        GithubUserAdapter listGithubAdapter = new GithubUserAdapter(listFollowing);
        rvFollowing.setAdapter(listGithubAdapter);

        listGithubAdapter.setOnItemClickCallback(new GithubUserAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(GithubUser data) {
                //NO ACTION
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false);
    }
}