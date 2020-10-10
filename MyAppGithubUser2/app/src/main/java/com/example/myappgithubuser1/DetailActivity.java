package com.example.myappgithubuser1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_GITHUB = "extra_github";

    TextView tvName, tvUsername, tvRepository, tvFollower, tvFollowing, tvCompany, tvLocation;
    ImageView imgPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setTitle("Detail User");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        getSupportActionBar().setElevation(0);

        setComponent();
        setData();
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

    private void setData() {
        GithubUser githubUser = getIntent().getParcelableExtra(EXTRA_GITHUB);
        tvName.setText(githubUser.getName());
        tvUsername.setText(githubUser.getLogin());
        tvRepository.setText(githubUser.getRepository());
        tvFollower.setText(githubUser.getFollowers());
        tvFollowing.setText(githubUser.getFollowing());
        tvCompany.setText("Company : " + githubUser.getCompany());
        tvLocation.setText("Location : " + githubUser.getLocation());
        Glide.with(this).load(githubUser.getAvatar()).into(imgPhoto);
    }

    private void setComponent() {
        tvName = findViewById(R.id.txt_nameavatar);
        tvUsername = findViewById(R.id.txt_username);
        tvRepository = findViewById(R.id.txt_repo);
        tvFollower = findViewById(R.id.txt_follower);
        tvFollowing = findViewById(R.id.txt_following);
        tvCompany = findViewById(R.id.txt_company);
        tvLocation = findViewById(R.id.txt_location);
        imgPhoto = findViewById(R.id.img_avatar);
    }
}
