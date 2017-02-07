package com.droiders.closer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droiders.closer.Users.UserNotice;
import com.droiders.closer.Users.community;
import com.droiders.closer.Users.users;
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

import static com.droiders.closer.R.id.post;

public class ViewPagerActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
private String communityName;


    private MobileServiceClient mClient;
    private MobileServiceTable<community> mCommTable;
    private MobileServiceTable<users> mToDoTable;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Intent i=getIntent();
        mId=i.getStringExtra("id");
        communityName=i.getStringExtra("CommunityName");
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(ViewPagerActivity.this,PostActivity.class);
                i.putExtra("CommunityName",communityName);
                i.putExtra("id",mId);
                startActivity(i);
            }
        });

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

    static ArrayList<UserNotice> userNotices=new ArrayList<UserNotice>();
    static ArrayList<UserNotice> userRequests=new ArrayList<UserNotice>();
    static ArrayList<UserNotice> userShared=new ArrayList<UserNotice>();

    private void pullFromTable() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final List<community> rows = refreshItemsFromMobileServiceTable2();
                    final List<users> result;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                            String username,post;
                            for(community item:rows){
                                username=getUserName(item.getUserid());
                                post=item.getPost();
                                if(item.getRequest()){
                                    //requests
                                    userRequests.add(new UserNotice(username, post));
                                }else if(item.getShared()){
                                    //shared items
                                    userShared.add(new UserNotice(username, post));
                                }else{
                                    //posts
                                    userNotices.add(new UserNotice(username, post));
                                }
                            }}catch (Exception e){
                                createAndShowDialog(e,"Error");
                            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        RecyclerView mRecyclerView;
        RViewAdapter mRViewAdapter;

        int vPagerPos;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            vPagerPos=getArguments().getInt(ARG_SECTION_NUMBER);
            //+_+_+_++_+_+_+_)_()U()U
            userNotices=null;   //Initialise the list
            //)((_((__+)++_+_))(_)(_)(
        }



        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_view_pager, container, false);
            mRecyclerView= (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
            mRViewAdapter=new RViewAdapter(userNotices,userShared,userRequests,getActivity());
            mRecyclerView.setAdapter(mRViewAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            return rootView;
        }


        //*******************************
        public class RViewAdapter extends RecyclerView.Adapter<RViewAdapter.MyViewholder>{

            List<UserNotice> info;
            List<UserNotice> sharedNotices;
            List<UserNotice> requestNotices;
            //requested=1,posted=2 or shared=0
            private Context ctx;

            public Context getCtx() {
                return ctx;
            }

            public RViewAdapter(List<UserNotice> info, List<UserNotice> sharedNotices, List<UserNotice> requestNotices, Context ctx) {
                this.info = info;
                this.sharedNotices = sharedNotices;
                this.requestNotices = requestNotices;
                this.ctx = ctx;
            }

            public class MyViewholder extends RecyclerView.ViewHolder {

                public TextView tName;
                public TextView tRequest;
                public TextView tContent;

                public MyViewholder(View itemView) {
                    super(itemView);
                    tName= (TextView) itemView.findViewById(R.id.name);
                    tRequest= (TextView) itemView.findViewById(R.id.intent);
                    tContent= (TextView) itemView.findViewById(post);

                }
            }

            @Override
            public RViewAdapter.MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater=LayoutInflater.from(getCtx());

                View movieView= inflater.inflate(R.layout.row_item_layout,parent,false);

                MyViewholder mHolder= new MyViewholder(movieView);
                return mHolder;
            }

            @Override
            public void onBindViewHolder(RViewAdapter.MyViewholder holder, int position) {

                if(vPagerPos==0){
                    UserNotice user=sharedNotices.get(position);
                    holder.tName.setText(user.getName());
                    holder.tContent.setText(user.getContent());
                    holder.tRequest.setText("Shared");
                }

                else if(vPagerPos==1){
                    UserNotice user=requestNotices.get(position);
                    holder.tName.setText(user.getName());
                    holder.tContent.setText(user.getContent());
                    holder.tRequest.setText("Requested");
                }

                else if(vPagerPos==2){
                    UserNotice user=info.get(position);
                    holder.tName.setText(user.getName());
                    holder.tContent.setText(user.getContent());
                    holder.tRequest.setText("Posted");
                }

            }

            @Override
            public int getItemCount() {
                switch (vPagerPos){
                    case 0:return sharedNotices.size();
                    case 1:return requestNotices.size();
                    case 2:return info.size();
                    default:return -1;
                }
            }
        }
        //*******************************
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Offered";
                case 1:
                    return "Requests";
                case 2:
                    return "General";
            }
            return null;
        }
    }


    private String getUserName(String id) throws ExecutionException, InterruptedException, MobileServiceException {
        return mToDoTable.where().field("id").eq(mId).execute().get().get(0).getName();
    }
    private List<community> refreshItemsFromMobileServiceTable2() throws ExecutionException, InterruptedException, MobileServiceException {
        return mCommTable.where().field("post").ne("").execute().get();
    }
    private void createTable(){
        // Get the Mobile Service Table instance to use
        mCommTable = mClient.getTable(communityName,community.class);
        mToDoTable=mClient.getTable(users.class);
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
}
