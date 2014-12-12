package com.jakkash.hdwallpaper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.jakkash.hdwallpaper.R.string;
import com.google.android.gms.ads.AdRequest;
import com.me.doapps.v2.Master;

public class MainActivity extends Master implements ActionBar.TabListener {

    private String[] tabs = {"LATEST", "ALL PHOTOS", "MY FAVORITES"};
    private TabsPagerAdapter mAdapter;
    private ViewPager viewPager;
    ActionBar.Tab tab;
    private InterstitialAd interstitial = null;
    AdRequest adRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.pager);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);

        //tab added in action bar
        for (String tab_name : tabs) {
            tab = getSupportActionBar().newTab();
            tab.setText(tab_name);
            tab.setTabListener(this);
            getSupportActionBar().addTab(tab);
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                getSupportActionBar().setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        // Look up the AdView as a resource and load a request.
        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequestb= new AdRequest.Builder().build();
        adView.loadAd(adRequestb);


        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-8995045147204986/6753137354");


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                adRequest = new AdRequest.Builder().build();
                interstitial.loadAd(adRequest);
            }
        }, 60000);

    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction transaction) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction transaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_about:

                Intent about = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(about);

                return true;

            case R.id.menu_overflow:
                return true;

            case R.id.menu_moreapp:

                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/search?q=jakkash+application")));

                return true;

            case R.id.menu_rateapp:

                final String appName = "com.jakkash.apksaver";//your application package name i.e play store application url
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id="
                                    + appName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                    + appName)));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //you may open Interstitial Ads here
            interstitial.loadAd(adRequest);

            // Toast.makeText(appContext, "BAck", Toast.LENGTH_LONG).show();
            AlertDialog.Builder alert = new AlertDialog.Builder(
                    MainActivity.this);
            alert.setTitle(string.app_name);
            alert.setIcon(R.drawable.icon);
            alert.setMessage("Estas seguro de salir?");

            alert.setPositiveButton("Salir y calificar (Gracias)",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            //you may open Interstitial Ads here
                            //interstitial.loadAd(adRequest);
                            final String appName = "com.jakkash.apksaver";//your application package name i.e play store application url

                            Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://play.google.com/store/apps/details?id=" + appName));
                            startActivity(intent);
                            finish();
                        }


                    });

            alert.setNegativeButton("Salir y no calificar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {


                            finish();

                        }
                    });
            alert.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);

    }

    public InterstitialAd getInterstitial() {
        return interstitial;
    }


}
