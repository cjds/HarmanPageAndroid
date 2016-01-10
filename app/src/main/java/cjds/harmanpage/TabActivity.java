package cjds.harmanpage;

import android.media.MediaRecorder;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

import java.io.IOException;

public class TabActivity extends AppCompatActivity {
    private static final String KEY = "2FA8-2FD6-C27D-47E8-A256-D011-3751-2BD6";

    /*
     * this is the adapter used for controlling the PagerAdapter
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    //the Audio recorder
    MediaRecorder myAudioRecorder = new MediaRecorder();

    //This is true if the Mic is recording
    boolean recording=false;

    /*
    This is the class that holds the view
     */
    private ViewPager mViewPager;

    String outputFile=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";;
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

        // This is the recording button. Pressing it will start recocrding
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.record_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recording){
                    recording=false;
                    myAudioRecorder.stop();
                    myAudioRecorder.release();
                    Snackbar.make(view, "Stopped Recording", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else{
                    recording=true;
                    try {
                        myAudioRecorder.prepare();
                        myAudioRecorder.start();
                    }
                    catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Snackbar.make(view, "Started Recording", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
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
            if(position==1){
                return  RoomFragment.newInstance(1);
            }
            else
                return DeviceFragment.newInstance(1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
            }
            return null;
        }
    }
}
