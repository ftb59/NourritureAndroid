package cn.bjtu.nourriture.View;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import cn.bjtu.nourriture.Adapters.ingredients.Card;
import cn.bjtu.nourriture.Adapters.ingredients.CardArrayAdapter;
import cn.bjtu.nourriture.ApiResults.ingredients;
import cn.bjtu.nourriture.R;
import cn.bjtu.nourriture.service.FoodAPIService;

public class FragmentIngredients extends Fragment {
    private CardArrayAdapter cardArrayAdapter;
    private ListView listView;
    private BroadcastReceiver ingredientsReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("response");
            Gson gson = new Gson();

            listView = (ListView) rootView.findViewById(R.id.card_listView);
            LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            String s = "\"offset\":0";
            if (response.contains(s)) {
                cardArrayAdapter = new CardArrayAdapter(getActivity(), R.layout.list_item_card, FragmentIngredients.class, li, Create, f);
                listView.setAdapter(cardArrayAdapter);
            }
            Type listType = new TypeToken<List<ingredients>>() {
            }.getType();
            JSONObject jObject = null;
            try {
                jObject = new JSONObject(response);
                JSONArray ing = jObject.getJSONArray("ingredients");
                List<ingredients> ingredients = (List<ingredients>) gson.fromJson(ing.toString(), listType);
                for (int i = 0; i < ingredients.size(); i++) {
                    ingredients in = ingredients.get(i);
                    List<String> blacklist = in.getBlacklist();
                    String bl = "Blacklist:";
                    for (int x = 0; x < blacklist.size(); x++) {
                        String b = blacklist.get(x);
                        if (x == 0)
                            bl = "Blacklist: " + b;
                        else
                            bl = bl + ", " + b;
                    }
                    List<String> tags = in.getLabels();
                    String tg = "Tags:";
                    for (int x = 0; x < tags.size(); x++) {
                        String b = tags.get(x);
                        if (x == 0)
                            tg = "Tags: " + b;
                        else
                            tg = tg + ", " + b;
                    }
                    Card card = new Card(in.getName(), bl, tg);
                    card.obj = in;
                    cardArrayAdapter.add(card);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };
    private View rootView;
    private BroadcastReceiver refreshList = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getActivity(), FoodAPIService.class);
            i.putExtra("json", "{}");
            i.putExtra("target", "/ingredients/search");
            i.putExtra("post", true);
            i.putExtra("connected", true);
            getActivity().startService(i);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);


        listView = (ListView) rootView.findViewById(R.id.card_listView);

        listView.addHeaderView(new View(this.getActivity()));
        listView.addFooterView(new View(this.getActivity()));

        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cardArrayAdapter = new CardArrayAdapter(this.getActivity(), R.layout.list_item_card, FragmentIngredients.class, li, Create, f);

        Intent i = new Intent(this.getActivity(), FoodAPIService.class);
        i.putExtra("json", "{}");
        i.putExtra("target", "/ingredients/search");
        i.putExtra("post", true);
        i.putExtra("connected", true);
        this.getActivity().startService(i);
        listView.setAdapter(cardArrayAdapter);
        addIngredient(rootView);
        return rootView;
    }

    IngredientCreateEdit Create = new IngredientCreateEdit();
    final Fragment f = this;

    protected void addIngredient(View rootView) {
        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ListView listView = (ListView) rootView.findViewById(R.id.card_listView);
        FloatingActionButton button = (FloatingActionButton) rootView.findViewById(R.id.fab);
        button.attachToListView(listView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create.create(getActivity(), inflater, f);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(ingredientsReceiver, new IntentFilter("/ingredients/search"));
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(refreshList, new IntentFilter("/ingredients"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(ingredientsReceiver);
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(refreshList);
    }

    IngredientCreateEdit i;

    public static final int GET_FROM_GALLERY = 3;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("result", requestCode + "");

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                Create.setImage(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
};
