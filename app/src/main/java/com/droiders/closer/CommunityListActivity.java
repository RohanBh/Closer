package com.droiders.closer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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

import com.droiders.closer.Users.communities;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CommunityListActivity extends AppCompatActivity {

    //0989800988-jkjlkkh*(%$#^&%*(
    ArrayList<String> communityNames=new ArrayList<String>();//INITIALIZE STRING LIST
    //9090977857667()()**^%$$%^#((

    private MobileServiceClient mClient;
    private MobileServiceTable<communities> mToDoTable;

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



        try {
            mClient = new MobileServiceClient(
                    "https://droidersapp.azurewebsites.net",
                    this);
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });
            createTable();
        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e) {
            createAndShowDialog(e, "Error");
        }
        pullFromTable();
    }
    private void pullFromTable() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final List<communities> results = refreshItemsFromMobileServiceTable();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (communities item : results) {
                                communityNames.add(item.getName());
                            }
                            //
                            RecyclerView communityList= (RecyclerView) findViewById(R.id.communityListView);
                            RViewAdapter adapter=new RViewAdapter(communityNames,CommunityListActivity.this);
                            communityList.setAdapter(adapter);
                            communityList.setLayoutManager(new LinearLayoutManager(CommunityListActivity.this));

                        }
                    });
                } catch (final Exception e){
                    createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };
        runAsyncTask(task);
    }
    private List<communities> refreshItemsFromMobileServiceTable() throws ExecutionException, InterruptedException, MobileServiceException {
        return mToDoTable.select("name").execute().get();
    }
    private void createTable(){
        // Get the Mobile Service Table instance to use
        mToDoTable = mClient.getTable(communities.class);
    }
    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }
    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }
    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }
    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
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
                tName= (TextView) itemView.findViewById(R.id.communityName);
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
