package com.droiders.closer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

public class CreateCommunityActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_community_page);
        CollapsingToolbarLayout mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbar.setTitle("New Community");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.createCommunityFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateCommunityActivity.this,CommunityInfoDisplayActivity.class);
                startActivity(intent);
            }
        });
    }
}
