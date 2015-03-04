package cn.bjtu.nourriture.View;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import cn.bjtu.nourriture.Preferences.PrefUtils;
import cn.bjtu.nourriture.R;
import it.neokree.materialnavigationdrawer.MaterialAccount;
import it.neokree.materialnavigationdrawer.MaterialAccountListener;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.MaterialSection;
import it.neokree.materialnavigationdrawer.MaterialSectionListener;

/**
 * Created by ftb on 15-1-18.
 */
public class NavDrawer extends MaterialNavigationDrawer implements MaterialAccountListener {

    MaterialAccount account;
    MaterialSection section1, section2, section3, section4, section5;

    @Override
    public void init(Bundle savedInstanceState) {


        // add first account
        account = new MaterialAccount( PrefUtils.getFromPrefs(this, PrefUtils.PREFS_PSEUDO_KEY,""), PrefUtils.getFromPrefs(this, PrefUtils.PREFS_EMAIL_KEY,""),new ColorDrawable(Color.parseColor("#9e9e9e")),this.getResources().getDrawable(R.drawable.bamboo));
        this.addAccount(account);

        // set listener
        this.setAccountListener(this);

        //this.replaceDrawerHeader(this.getResources().getDrawable(R.drawable.mat2));

        // create sections
        section1 = this.newSection("Ingredients",new FragmentIngredients());
        section2 = this.newSection("Recipes",new FragmentRecipes());
        section3 = this.newSection("Moments",new FragmentMoments());
        section4 = this.newSection("Users",new FragmentUsers());
      final Activity activity = this;
        section5 = this.newSection("Disconnect",new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {

                PrefUtils.saveToPrefs(activity,PrefUtils.PREFS_PASSWORD_KEY, "");
                Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
;


        // add your sections to the drawer
        this.addSection(section1);
        this.addSection(section2);
        this.addSection(section3);
        this.addSection(section4);
        this.addBottomSection(section5);

        this.addMultiPaneSupport();

        this.setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);

        // start thread
        t.start();

    }


    @Override
    public void onAccountOpening(MaterialAccount account) {
    }

    @Override
    public void onChangeAccount(MaterialAccount newAccount) {
        // when another account is selected
    }

    // after 5 second (async task loading photo from website) change user photo
    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
                account.setPhoto(getResources().getDrawable(R.drawable.photo));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyAccountDataChanged();

                    }
                });
                //Log.w("PHOTO","user account photo setted");

        }
    });
}