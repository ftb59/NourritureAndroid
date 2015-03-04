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

import cn.bjtu.nourriture.Adapters.recipes.RecipeArrayAdapter;
import cn.bjtu.nourriture.ApiResults.recipes;
import cn.bjtu.nourriture.R;
import cn.bjtu.nourriture.service.FoodAPIService;

public class FragmentRecipes extends Fragment {
    LayoutInflater inflater;
    private RecipeArrayAdapter recipeArrayAdapter;
    private ListView listView;
    private View rootView;
    private recipes recipeToSend = null;
    //private List<ingredients> ingredientsToSend = new ArrayList<>();

    private BroadcastReceiver recipesReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("response");
            Gson gson = new Gson();

            listView = (ListView) rootView.findViewById(R.id.card_listView);

            LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            String s = "\"offset\":0";
            if (response.contains(s)) {
                recipeArrayAdapter = new RecipeArrayAdapter(getActivity(), R.layout.list_item_card);
                listView.setAdapter(recipeArrayAdapter);
            }

            Type listType = new TypeToken<List<recipes>>() {
            }.getType();
            JSONObject jObject = null;
            try {
                jObject = new JSONObject(response);
                JSONArray ar = jObject.getJSONArray("recipes");
                List<recipes> recipes = (List<recipes>) gson.fromJson(ar.toString(), listType);
                for (int i = 0; i < recipes.size(); i++) {
                    recipes mm = recipes.get(i);
                    recipeArrayAdapter.add(mm);
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
            i.putExtra("target", "/recipes/search");
            i.putExtra("post", true);
            i.putExtra("connected", true);
            getActivity().startService(i);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        this.inflater = inflater;

        listView = (ListView) rootView.findViewById(R.id.card_listView);

        listView.addHeaderView(new View(this.getActivity()));
        listView.addFooterView(new View(this.getActivity()));

        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        recipeArrayAdapter = new RecipeArrayAdapter(this.getActivity(), R.layout.list_item_card_recipe);

        Intent i = new Intent(this.getActivity(), FoodAPIService.class);
        i.putExtra("json", "{}");
        i.putExtra("target", "/recipes/search");
        i.putExtra("post", true);
        i.putExtra("connected", true);
        FloatingActionButton button = (FloatingActionButton) rootView.findViewById(R.id.fab);
        button.setVisibility(View.INVISIBLE);
        this.getActivity().startService(i);
        listView.setAdapter(recipeArrayAdapter);
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(recipesReceiver, new IntentFilter("/recipes/search"));
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(refreshList, new IntentFilter("/recipes"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(recipesReceiver);
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(refreshList);
    }
}