package com.droiders.closer;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import static com.droiders.closer.R.id.fab;

public class CommunityInfoDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_info_page);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        String communityName = i.getStringExtra("CommunityName");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbar.setTitle(communityName);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.goToNoticesFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityInfoDisplayActivity.this,ViewPagerActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
