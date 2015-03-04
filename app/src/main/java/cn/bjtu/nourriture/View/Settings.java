package cn.bjtu.nourriture.View;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import cn.bjtu.nourriture.R;


public class Settings extends PreferenceActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.addPreferencesFromResource(R.xml.settings);
    }
}
