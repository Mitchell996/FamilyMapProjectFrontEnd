package com.example.mitchelljohnson.familymapproject.MainActivityFragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.v4.app.*;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import com.example.mitchelljohnson.familymapproject.settingsActivity.SettingsActivity;
import com.example.mitchelljohnson.familymapproject.FilterActivity;
import com.example.mitchelljohnson.familymapproject.SearchActivity.SearchActivity;
import com.example.mitchelljohnson.familymapproject.dataObjects.model;
import android.content.Context;


import com.example.mitchelljohnson.familymapproject.R;


public class MainActivity extends AppCompatActivity {
private boolean signedIn = false;
    private static final String TAG = "MainActivity";
    private static final String KEY_AUTH = "AUTH";
    private static final String KEY_SIGN_IN = "SIGN_IN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginFragment loginFragment;
        FragmentManager fm = this.getSupportFragmentManager();
        loginFragment = (LoginFragment) fm.findFragmentById(R.id.mainFrameLayout);
        model m = model.getInstance();
        if(!m.isLoggedIn()) {
            loginFragment = LoginFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.mainFrameLayout, loginFragment)
                    .commit();
            signedIn = true;
        }
        else {
            MapsActivity mapsActivity = new MapsActivity();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.mainFrameLayout, mapsActivity);
            fragmentTransaction.commit();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.map_activity_menu, menu);
        return true;
    }
   public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.filter:
                startActivity(new Intent(this, FilterActivity.class));
                return true;
            default:
                return false;
        }

    }
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putBoolean(KEY_SIGN_IN,signedIn);
    }
    public static void startTopActivity(Context context, boolean newInstance) {
        Intent intent = new Intent(context, MainActivity.class);
        if (newInstance) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

}
