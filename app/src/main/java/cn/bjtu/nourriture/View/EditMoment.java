package cn.bjtu.nourriture.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.bjtu.nourriture.ApiResults.ingredients;
import cn.bjtu.nourriture.ApiResults.moments;
import cn.bjtu.nourriture.ApiResults.recipes;
import cn.bjtu.nourriture.R;
import cn.bjtu.nourriture.service.FoodAPIService;

/**
 * Created by ftb on 15-1-17.
 */
public class EditMoment {
    EditText contentInput;
    EditText recipeInput;
    TextView remove;
    TextView cancel;
    TextView valid;
    private recipes recipeToSend = null;
    ViewGroup addedRecipe;
    ViewGroup apiRecipes;
    List<String> recipesStrings = new ArrayList<>();
    LayoutInflater inflater;
    Activity activity;

    public EditMoment(LayoutInflater inflater, Activity activity)
    {
        this.inflater = inflater;
        this.activity = activity;
    }

    private View newViewRecipeAdded(String title)
    {
        View v = inflater.inflate(R.layout.tag, null);
        TextView name = (TextView) v.findViewById(R.id.nameTag);
        TextView x = (TextView) v.findViewById(R.id.deleteTag);
        x.setTag(title);
        String s = title;
        if (s.length() > 10)
            s = s.substring(0, 10) + "...";
        name.setText(s);
        return v;
    }

    private void addViewRecipesAdded()
    {
        View[] ings = new View[recipesStrings.size()];
        addedRecipe.removeAllViews();
        for (int i = 0; i < recipesStrings.size(); i++) {
            String tg = recipesStrings.get(i);
            ings[i] = newViewRecipeAdded(tg);
            TextView removeButton = (TextView) ings[i].findViewById(R.id.deleteTag);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String objTag = (String) v.getTag();
                    for (int i = 0; i < recipesStrings.size(); i++) {
                        String tg = recipesStrings.get(i);
                        if (tg.equals(objTag)) {
                            recipesStrings.remove(i);
                            recipeInput.setEnabled(true);
                            recipeToSend = null;
                            break;
                        }
                    }
                    addViewRecipesAdded();
                }
            });
            if (!tg.equals(""))
                addedRecipe.addView(ings[i]);
        }
    }

    public void edit(final moments currentMoment) {
        final Dialog dialog = new Dialog(activity);

        LocalBroadcastManager.getInstance(activity).registerReceiver(getRecipes, new IntentFilter("/recipes/search2"));
        dialog.setContentView(R.layout.moment_create);
        getViews(dialog);
        addViewRecipesAdded();
        contentInput.setText(currentMoment.getDescription());
        dialog.setTitle("Edit a moment");
        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moments m = new moments("", contentInput.getText().toString(), null, recipeToSend);
                m.id = currentMoment.id;
                Intent i = new Intent(activity, FoodAPIService.class);
                i.putExtra("json", m.toJson());
                i.putExtra("target", "/moments/" + m.getId());
                i.putExtra("ret", "/moments");
                i.putExtra("post", false);
                i.putExtra("patch", true);
                i.putExtra("connected", true);
                activity.startService(i);
                dialog.dismiss();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, FoodAPIService.class);
                i.putExtra("json", "{}");
                i.putExtra("target", "/moments/" + currentMoment.getId());
                i.putExtra("ret", "/moments");
                i.putExtra("delete", true);
                i.putExtra("post", false);
                i.putExtra("connected", true);
                activity.startService(i);
                dialog.dismiss();
            }
        });

        recipeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (recipeInput.getText().length() >= 3) {
                    recipes recipe = new recipes();
                    recipe.title = recipeInput.getText().toString();
                    Intent i = new Intent(activity, FoodAPIService.class);
                    i.putExtra("json", recipe.toJson());
                    i.putExtra("ret", "/recipes/search2");
                    i.putExtra("target", "/recipes/search");
                    i.putExtra("post", true);
                    i.putExtra("connected", true);
                    activity.startService(i);
                }
                else
                {
                    apiRecipes.removeAllViews();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private boolean checkRecipeAdded(String objTag)
    {
        for (int i = 0; i < recipesStrings.size(); i++) {
            String tg = recipesStrings.get(i);
            Log.e("tg", tg + " " + objTag);
            if (tg.equals(objTag)) {

                return true;
            }
        }
        return false;
    }

    private void newViewRecipe(String title, final recipes RE)
    {
        View v = inflater.inflate(R.layout.ingredient_small, null);
        TextView tt = (TextView) v.findViewById(R.id.title);
        final String save = title;;
        if (title.length() > 15)
            title = title.substring(0, 15) + "...";
        tt.setText(title);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ingrédients ajoutés

                apiRecipes.removeAllViews();
                InputMethodManager imm = (InputMethodManager)activity.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(recipeInput.getWindowToken(), 0);
                recipeInput.setText("");
                if (!checkRecipeAdded(save)) {
                    recipesStrings.add(save);
                    recipeInput.setEnabled(false);
                    recipeToSend = RE;
                    addViewRecipesAdded();
                }
            }
        });
        apiRecipes.addView(v);
    }

    private BroadcastReceiver getRecipes = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("response");
            if (apiRecipes == null)
                return ;
            Gson gson = new Gson();

            Type listType = new TypeToken<List<recipes>>() {
            }.getType();
            JSONObject jObject = null;
            try {
                jObject = new JSONObject(response);
                JSONArray ar = jObject.getJSONArray("recipes");
                List<recipes> recipes = gson.fromJson(ar.toString(), listType);
                apiRecipes.removeAllViews();
                for (int i = 0; i < recipes.size(); i++) {
                    recipes in = recipes.get(i);
                    newViewRecipe(in.getTitle(), in);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void getViews(Dialog v) {
        contentInput = (EditText) v.findViewById(R.id.contentInput);
        recipeInput = (EditText) v.findViewById(R.id.recipeInput);
        remove = (TextView) v.findViewById(R.id.remove);
        cancel = (TextView) v.findViewById(R.id.cancel);
        valid = (TextView) v.findViewById(R.id.valid);
        addedRecipe = (ViewGroup) v.findViewById(R.id.addedRecipes);
        apiRecipes = (ViewGroup) v.findViewById(R.id.apiRecipe);
    }
}
