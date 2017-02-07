package com.droiders.closer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.droiders.closer.Users.UserNotice;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

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

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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


    //================================================================================
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        RecyclerView mRecyclerView;
        RViewAdapter mRViewAdapter;
        ArrayList<UserNotice> userNotices;
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
            mRViewAdapter=new RViewAdapter(userNotices,getActivity());
            mRecyclerView.setAdapter(mRViewAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            return rootView;
        }


        //*******************************
        public class RViewAdapter extends RecyclerView.Adapter<RViewAdapter.MyViewholder>{

            List<UserNotice> info;
            //requested=1,posted=2 or shared=0
            private Context ctx;

            public Context getCtx() {
                return ctx;
            }

            public RViewAdapter(List<UserNotice> info, Context ctx) {
                this.info = info;
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
                    tContent= (TextView) itemView.findViewById(R.id.post);

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
                UserNotice user=info.get(position);
                holder.tName.setText(user.getName());
                if(vPagerPos==0)
                    holder.tRequest.setText("Shared");
                else if(vPagerPos==1)
                    holder.tRequest.setText("Requested");
                else if(vPagerPos==2)
                    holder.tRequest.setText("Posted");
                holder.tContent.setText(user.getContent());

            }

            @Override
            public int getItemCount() {
                if(info!=null)
                    return info.size();
                return 0;
            }

        }
        //*******************************
    }

    //================================================================================

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
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
}
