package cn.bjtu.nourriture.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.bjtu.nourriture.Adapters.moment.MomentArrayAdapter;
import cn.bjtu.nourriture.ApiResults.ingredients;
import cn.bjtu.nourriture.ApiResults.moments;
import cn.bjtu.nourriture.ApiResults.recipes;
import cn.bjtu.nourriture.R;
import cn.bjtu.nourriture.service.FoodAPIService;

public class FragmentMoments extends Fragment {
   // EditText titleInput;
    EditText contentInput;
    EditText recipeInput;
  //  EditText ingredientInput;
    TextView remove;
    TextView cancel;
    TextView valid;

  /*  ViewGroup apiIngredient;
    ViewGroup addedIngredient;*/
    ViewGroup addedRecipe;
    ViewGroup apiRecipes;
    LayoutInflater inflater;
    private MomentArrayAdapter momentArrayAdapter;
    private ListView listView;
    private View rootView;
    private recipes recipeToSend = null;
    //private List<ingredients> ingredientsToSend = new ArrayList<>();

    private BroadcastReceiver momentsReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("response");
            Gson gson = new Gson();

            listView = (ListView) rootView.findViewById(R.id.card_listView);

            LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            String s = "\"offset\":0";
            if (response.contains(s)) {
                momentArrayAdapter = new MomentArrayAdapter(getActivity(), R.layout.list_item_card);
                listView.setAdapter(momentArrayAdapter);
            }

            Type listType = new TypeToken<List<moments>>() {
            }.getType();
            JSONObject jObject = null;
            try {
                jObject = new JSONObject(response);
                JSONArray ar = jObject.getJSONArray("moments");
                List<moments> moments = (List<moments>) gson.fromJson(ar.toString(), listType);
                for (int i = 0; i < moments.size(); i++) {
                    moments mm = moments.get(i);
                    momentArrayAdapter.add(mm);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };
    private BroadcastReceiver refreshList = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getActivity(), FoodAPIService.class);
            i.putExtra("json", "{}");
            i.putExtra("target", "/moments/search");
            i.putExtra("post", true);
            i.putExtra("connected", true);
            getActivity().startService(i);
        }
    };/*
    private BroadcastReceiver getIngredients = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("response");
            if (apiIngredient==null)
                return ;
            Gson gson = new Gson();

            Type listType = new TypeToken<List<ingredients>>() {
            }.getType();
            JSONObject jObject = null;
            try {
                jObject = new JSONObject(response);
                JSONArray ar = jObject.getJSONArray("ingredients");
                List<ingredients> ingredients = gson.fromJson(ar.toString(), listType);

                apiIngredient.removeAllViews();
                for (int i = 0; i < ingredients.size(); i++) {
                    ingredients in = ingredients.get(i);
                    newViewIngredient(in.getName(), in);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    List<String> ingredientsStrings = new ArrayList<>();

    // Ingredient recherchés
    private void newViewIngredient(String title, final ingredients I)
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

                apiIngredient.removeAllViews();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ingredientInput.getWindowToken(), 0);
                ingredientInput.setText("");
                if (!checkIngredientAdded(save)) {
                    ingredientsStrings.add(save);
                    ingredientsToSend.add(I);
                    addViewIngredientsAdded();
                }
            }
        });
        apiIngredient.addView(v);
    }

    // Ingredients ajoutés

    private void addViewIngredientsAdded()
    {
        View[] ings = new View[ingredientsStrings.size()];
        addedIngredient.removeAllViews();
        for (int i = 0; i < ingredientsStrings.size(); i++) {
            String tg = ingredientsStrings.get(i);
            ings[i] = newViewIngredientAdded(tg);
            TextView removeButton = (TextView) ings[i].findViewById(R.id.deleteTag);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String objTag = (String) v.getTag();
                    for (int i = 0; i < ingredientsStrings.size(); i++) {
                        String tg = ingredientsStrings.get(i);
                        if (tg.equals(objTag)) {
                            ingredientsStrings.remove(i);
                            ingredientsToSend.remove(i);
                            break;
                        }
                    }
                    addViewIngredientsAdded();
                }
            });
            if (!tg.equals(""))
                addedIngredient.addView(ings[i]);
        }
    }

    private boolean checkIngredientAdded(String objTag)
    {
        for (int i = 0; i < ingredientsStrings.size(); i++) {
            String tg = ingredientsStrings.get(i);
            Log.e("tg", tg + " " + objTag);
            if (tg.equals(objTag)) {

                return true;
            }
        }
        return false;
    }

    private View newViewIngredientAdded(String title)
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
    }*/

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

    List<String> recipesStrings = new ArrayList<>();

    // Ingredient recherchés
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
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
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

    // Recettes ajoutées

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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        this.inflater = inflater;

        listView = (ListView) rootView.findViewById(R.id.card_listView);

        listView.addHeaderView(new View(this.getActivity()));
        listView.addFooterView(new View(this.getActivity()));

        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        momentArrayAdapter = new MomentArrayAdapter(this.getActivity(), R.layout.list_item_card_moment);

        Intent i = new Intent(this.getActivity(), FoodAPIService.class);
        i.putExtra("json", "{}");
        i.putExtra("target", "/moments/search");
        i.putExtra("post", true);
        i.putExtra("connected", true);
        this.getActivity().startService(i);
        listView.setAdapter(momentArrayAdapter);
        addMoment(rootView);
        return rootView;
    }

    protected void addMoment(View rootView) {
        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ListView listView = (ListView) rootView.findViewById(R.id.card_listView);
        FloatingActionButton button = (FloatingActionButton) rootView.findViewById(R.id.fab);
        button.attachToListView(listView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create(inflater);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(momentsReceiver, new IntentFilter("/moments/search"));
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(refreshList, new IntentFilter("/moments"));
      //  LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(getIngredients, new IntentFilter("/ingredients/search"));
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(getRecipes, new IntentFilter("/recipes/search"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(momentsReceiver);
      //  LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(getIngredients);
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(getRecipes);
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(refreshList);
    }

    private void getViews(Dialog v) {
        //titleInput = (EditText) v.findViewById(R.id.titleInput);
        contentInput = (EditText) v.findViewById(R.id.contentInput);
        recipeInput = (EditText) v.findViewById(R.id.recipeInput);
       // ingredientInput = (EditText) v.findViewById(R.id.ingredientInput);
        remove = (TextView) v.findViewById(R.id.remove);
        cancel = (TextView) v.findViewById(R.id.cancel);
        valid = (TextView) v.findViewById(R.id.valid);
      /*  apiIngredient = (ViewGroup) v.findViewById(R.id.apiIngredient);
        addedIngredient = (ViewGroup) v.findViewById(R.id.addedIngredient);*/
        addedRecipe = (ViewGroup) v.findViewById(R.id.addedRecipes);
        apiRecipes = (ViewGroup) v.findViewById(R.id.apiRecipe);
    }

    public void create(LayoutInflater inflater) {
        final Dialog dialog = new Dialog(this.getActivity());
        dialog.setContentView(R.layout.moment_create);
        getViews(dialog);
        addViewRecipesAdded();
       // addViewIngredientsAdded();
        dialog.setTitle("Write a moment");
        remove.setVisibility(View.INVISIBLE);
        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 moments m = new moments("", contentInput.getText().toString(), null, recipeToSend);
                Intent i = new Intent(getActivity(), FoodAPIService.class);
                i.putExtra("json", m.toJson());
                i.putExtra("target", "/moments");
                i.putExtra("post", true);
                i.putExtra("connected", true);
                getActivity().startService(i);
                dialog.dismiss();
            }
        });


/*
        ingredientInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ingredientInput.getText().length() >= 3) {
                    ingredients ingredient = new ingredients("", ingredientInput.getText().toString(), null, "", "", null, null);
                    Intent i = new Intent(getActivity(), FoodAPIService.class);
                    i.putExtra("json", ingredient.toJson());
                    i.putExtra("ret", "/ingredients/search");
                    i.putExtra("target", "/ingredients/search");
                    i.putExtra("post", true);
                    i.putExtra("connected", true);
                    getActivity().startService(i);
                }
                else
                {
                    apiIngredient.removeAllViews();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        recipeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (recipeInput.getText().length() >= 3) {
                    recipes recipe = new recipes();
                    recipe.title = recipeInput.getText().toString();
                    Intent i = new Intent(getActivity(), FoodAPIService.class);
                    i.putExtra("json", recipe.toJson());
                    i.putExtra("ret", "/recipes/search");
                    i.putExtra("target", "/recipes/search");
                    i.putExtra("post", true);
                    i.putExtra("connected", true);
                    getActivity().startService(i);
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


}


