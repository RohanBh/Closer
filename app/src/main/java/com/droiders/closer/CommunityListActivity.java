package com.droiders.closer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CommunityListActivity extends AppCompatActivity {

    //0989800988-jkjlkkh*(%$#^&%*(
    ArrayList<String> communityNames;//INITIALIZE STRING LIST
    //9090977857667()()**^%$$%^#((

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView communityList= (RecyclerView) findViewById(R.id.communityListView);
        RViewAdapter adapter=new RViewAdapter(communityNames,this);
        communityList.setAdapter(adapter);
        communityList.setLayoutManager(new LinearLayoutManager(this));
    }

    //*******************************
    public class RViewAdapter extends RecyclerView.Adapter<CommunityListActivity.RViewAdapter.MyViewholder>{

        List<String> cNames;
        private Context ctx;

        public Context getCtx() {
            return ctx;
        }

        public RViewAdapter(List<String> cNames, Context ctx) {
            this.cNames = cNames;
            this.ctx = ctx;
        }

        public class MyViewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public TextView tName;

            public MyViewholder(View itemView) {
                super(itemView);
                tName= (TextView) itemView.findViewById(R.id.name);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Intent i=new Intent(getCtx(),CommunityInfoDisplayActivity.class);
                startActivity(i);
            }
        }

        @Override
        public CommunityListActivity.RViewAdapter.MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getCtx());

            View communtyList= inflater.inflate(R.layout.row_community_list,parent,false);

            CommunityListActivity.RViewAdapter.MyViewholder mHolder= new CommunityListActivity.RViewAdapter.MyViewholder(communtyList);
            return mHolder;
        }

        @Override
        public void onBindViewHolder(CommunityListActivity.RViewAdapter.MyViewholder holder, int position) {
            String name=cNames.get(position);
            holder.tName.setText(name);
        }

        @Override
        public int getItemCount() {
            if(cNames!=null)
                return cNames.size();
            return 0;
        }

    }
    //***********************************

}
