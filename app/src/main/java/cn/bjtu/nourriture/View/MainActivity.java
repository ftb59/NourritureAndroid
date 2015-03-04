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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bjtu.nourriture.FoodApi.Users;
import cn.bjtu.nourriture.Preferences.PrefUtils;
import cn.bjtu.nourriture.R;
import cn.bjtu.nourriture.service.FoodAPIService;

public class MainActivity extends ActionBarActivity {
    public static final String PREFS_PSEUDO_KEY = "__PSEUDO__" ;
    public static final String PREFS_PASSWORD_KEY = "__PASSWORD__" ;
    public static final String PREFS_TOKEN_KEY = "__TOKEN__" ;
    public static final String PREFS_EMAIL_KEY = "__EMAIL__";
    public static final String PREFS_ID_KEY = "__ID__" ;
    public static final String PREFS_FIRSTNAME_KEY = "__FIRSTNAME__" ;
    public static final String PREFS_LASTNAME_KEY = "__LASTNAME__" ;
    public static final String PREFS_AVATAR_KEY = "__AVATAR__" ;
    private BroadcastReceiver subscribeReceiver = new BroadcastReceiver() {

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

                PrefUtils.saveToPrefs(context, PREFS_PSEUDO_KEY, username);
                PrefUtils.saveToPrefs(context, PREFS_TOKEN_KEY, token);
                PrefUtils.saveToPrefs(context, PREFS_EMAIL_KEY, email);
                PrefUtils.saveToPrefs(context, PREFS_ID_KEY, email);
                PrefUtils.saveToPrefs(context, PREFS_FIRSTNAME_KEY, firstname);
                PrefUtils.saveToPrefs(context, PREFS_LASTNAME_KEY, lastname);
                PrefUtils.saveToPrefs(context, PREFS_AVATAR_KEY, avatar);
                PrefUtils.saveToPrefs(context, PREFS_ID_KEY, id);
                Intent myIntent = new Intent(getBaseContext(), NavDrawer.class);
                startActivity(myIntent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
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

                PrefUtils.saveToPrefs(context, PREFS_PSEUDO_KEY, username);
                PrefUtils.saveToPrefs(context, PREFS_TOKEN_KEY, token);
                PrefUtils.saveToPrefs(context, PREFS_EMAIL_KEY, email);
                PrefUtils.saveToPrefs(context, PREFS_ID_KEY, email);
                PrefUtils.saveToPrefs(context, PREFS_FIRSTNAME_KEY, firstname);
                PrefUtils.saveToPrefs(context, PREFS_LASTNAME_KEY, lastname);
                PrefUtils.saveToPrefs(context, PREFS_AVATAR_KEY, avatar);
                PrefUtils.saveToPrefs(context, PREFS_ID_KEY, id);
                Intent myIntent = new Intent(getBaseContext(), NavDrawer.class);
                startActivity(myIntent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Toolbar toolbar;
    private ActionBar actionBar;
    private BroadcastReceiver errorReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("response");
            TextView err = (TextView) findViewById(R.id.error);
            err.setText(response);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Animation zoomAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom);
        final View circle = this.findViewById(R.id.view2);
        final Button buttonSign = (Button) this.findViewById(R.id.buttonSign);
        final Button buttonRegister = (Button) this.findViewById(R.id.buttonRegister);
        final RelativeLayout registerLayout = (RelativeLayout) this.findViewById(R.id.registerLayout);
        final RelativeLayout signinLayout = (RelativeLayout) this.findViewById(R.id.signinLayout);
        if (!PrefUtils.getFromPrefs(MainActivity.this, PrefUtils.PREFS_PASSWORD_KEY, "").equals(""))
        {
            Intent i = new Intent(this, FoodAPIService.class);
            Users u = new Users(PrefUtils.getFromPrefs(MainActivity.this, PrefUtils.PREFS_EMAIL_KEY, ""), "", "", "", "", PrefUtils.getFromPrefs(MainActivity.this, PrefUtils.PREFS_PASSWORD_KEY, ""));
            i.putExtra("json", u.toJson());
            i.putExtra("target", "sessions");
            startService(i);
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

        Button buttonReg = (Button) this.findViewById(R.id.sendRegister);
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribe();
            }
        });
        Button buttonSignin = (Button) this.findViewById(R.id.sendSignin);
        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });

        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSign.setBackgroundColor(getResources().getColor(R.color.secondColorLight));
                buttonRegister.setBackgroundColor(getResources().getColor(R.color.mainColorLight));
                registerLayout.setVisibility(View.GONE);
                signinLayout.setVisibility(View.VISIBLE);
                TextView err = (TextView) findViewById(R.id.error);
                err.setText("");
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSign.setBackgroundColor(getResources().getColor(R.color.mainColorLight));
                buttonRegister.setBackgroundColor(getResources().getColor(R.color.secondColorLight));
                signinLayout.setVisibility(View.GONE);
                registerLayout.setVisibility(View.VISIBLE);
                TextView err = (TextView) findViewById(R.id.error);
                err.setText("");
            }
        });
    }

    void subscribe() {
        EditText pseudo = (EditText) this.findViewById(R.id.pseudo);
        EditText email = (EditText) this.findViewById(R.id.email);
        EditText password = (EditText) this.findViewById(R.id.password);
        EditText confirmation_pwd = (EditText) this.findViewById(R.id.confirmation);

        if (!password.getText().toString().equals(confirmation_pwd.getText().toString())) {
            TextView err = (TextView) findViewById(R.id.error);
            err.setText("the password and the confirmation must be the same");
            return;
        }
        Intent i = new Intent(this, FoodAPIService.class);
        PrefUtils.saveToPrefs(this, PREFS_PASSWORD_KEY, password.getText().toString());
        Users u = new Users(email.getText().toString().toLowerCase().replace(" ", ""), "", "", pseudo.getText().toString().toLowerCase().replace(" ", ""), "", password.getText().toString());
        i.putExtra("json", u.toJson());
        i.putExtra("target", "users");
        startService(i);
    }

    void connect() {
        EditText pseudo = (EditText) this.findViewById(R.id.pseudoSign);
        EditText password = (EditText) this.findViewById(R.id.passwordSign);

        PrefUtils.saveToPrefs(this, PREFS_PASSWORD_KEY, password.getText().toString());
        Intent i = new Intent(this, FoodAPIService.class);
        Users u = new Users(pseudo.getText().toString().toLowerCase().replace(" ", ""), "", "", "", "", password.getText().toString());
        i.putExtra("json", u.toJson());
        i.putExtra("target", "sessions");
        startService(i);
    }

    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(subscribeReceiver, new IntentFilter("users"));
        LocalBroadcastManager.getInstance(this).registerReceiver(connectReceiver, new IntentFilter("sessions"));
        LocalBroadcastManager.getInstance(this).registerReceiver(errorReceiver, new IntentFilter("error"));
    }

    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(subscribeReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(connectReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(errorReceiver);
    }
}
