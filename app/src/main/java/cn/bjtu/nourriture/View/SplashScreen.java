package cn.bjtu.nourriture.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bjtu.nourriture.FoodApi.Users;
import cn.bjtu.nourriture.Preferences.PrefUtils;
import cn.bjtu.nourriture.R;
import cn.bjtu.nourriture.service.FoodAPIService;

public class SplashScreen extends ActionBarActivity {

    private BroadcastReceiver connectReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("response");
            try {
                JSONObject jObject = new JSONObject(response);
                JSONObject user = jObject.getJSONObject("user");
                String username = user.getString("pseudo");
                String email = user.getString("email");
                String token = jObject.getString("token");
                String id = user.getString("id");
                String firstname= user.getString("firstname");
                String lastname = user.getString("lastname");
                String avatar = user.getString("avatar");

                PrefUtils.saveToPrefs(context, PrefUtils.PREFS_PSEUDO_KEY, username);
                PrefUtils.saveToPrefs(context, PrefUtils.PREFS_TOKEN_KEY, token);
                PrefUtils.saveToPrefs(context, PrefUtils.PREFS_EMAIL_KEY, email);
                PrefUtils.saveToPrefs(context, PrefUtils.PREFS_ID_KEY, email);
                PrefUtils.saveToPrefs(context, PrefUtils.PREFS_FIRSTNAME_KEY, firstname);
                PrefUtils.saveToPrefs(context, PrefUtils.PREFS_LASTNAME_KEY, lastname);
                PrefUtils.saveToPrefs(context, PrefUtils.PREFS_AVATAR_KEY, avatar);
                PrefUtils.saveToPrefs(context, PrefUtils.PREFS_ID_KEY, id);
                Intent myIntent = new Intent(getBaseContext(), NavDrawer.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(myIntent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private BroadcastReceiver errorReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("response");
            Toast.makeText(context, "cannot connect", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final Animation zoomAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom);
        final View circle = this.findViewById(R.id.view2);
        if (!PrefUtils.getFromPrefs(SplashScreen.this, PrefUtils.PREFS_PASSWORD_KEY, "").equals("")) {
            // GO MAIN mais sleep avant
            Intent i = new Intent(this, FoodAPIService.class);
            Users u = new Users(PrefUtils.getFromPrefs(SplashScreen.this, PrefUtils.PREFS_EMAIL_KEY, ""), "", "", "", "", PrefUtils.getFromPrefs(SplashScreen.this, PrefUtils.PREFS_PASSWORD_KEY, ""));
            i.putExtra("json", u.toJson());
            i.putExtra("target", "sessions");
            startService(i);

        }
        else
        {
            Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(myIntent);
            finish();
        }
        circle.startAnimation(zoomAnimation);
        zoomAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                circle.startAnimation(zoomAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(connectReceiver, new IntentFilter("sessions"));
        LocalBroadcastManager.getInstance(this).registerReceiver(errorReceiver, new IntentFilter("error"));
    }

    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(connectReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(errorReceiver);
    }

}



