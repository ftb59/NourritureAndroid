package cn.bjtu.nourriture.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import cn.bjtu.nourriture.Preferences.PrefUtils;
import cn.bjtu.nourriture.R;
import cn.bjtu.nourriture.service.FoodAPIService;

public class Profile extends ActionBarActivity {
    private Toolbar toolbar;
    private ActionBar actionBar;

    private BroadcastReceiver usersReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("response");


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        Intent i = new Intent(this, FoodAPIService.class);
        i.putExtra("json", "{}");
        i.putExtra("target", "/users/" + PrefUtils.getFromPrefs(Profile.this, PrefUtils.PREFS_ID_KEY, "0"));
        i.putExtra("post", false);
        i.putExtra("connected", true);

        this.startService(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(usersReceiver, new IntentFilter("/users"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(usersReceiver);
    }
}