package cn.bjtu.nourriture.View;

import android.support.v4.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import cn.bjtu.nourriture.Adapters.ingredients.Card;
import cn.bjtu.nourriture.Adapters.ingredients.CardArrayAdapter;
import cn.bjtu.nourriture.ApiResults.users;
import cn.bjtu.nourriture.Preferences.PrefUtils;
import cn.bjtu.nourriture.R;
import cn.bjtu.nourriture.service.FoodAPIService;

/**
 * Created by ftb on 15-1-11.
 */
public class FragmentFollowers extends Fragment {
    private CardArrayAdapter cardArrayAdapter;
    private ListView listView;
    private BroadcastReceiver usersReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("response");
            Gson gson = new Gson();
            Type listType = new TypeToken<List<users>>() {
            }.getType();
            JSONObject jObject = null;
            try {
                jObject = new JSONObject(response);
                JSONArray json = jObject.getJSONArray("users");
                List<users> usrs = (List<users>) gson.fromJson(json.toString(), listType);
                for (int i = 0; i < usrs.size(); i++) {
                    users usr = usrs.get(i);
                    List<String> access = usr.getAccess();
                    String acss = "";
                    for (int x = 0;x < access.size(); x++) {
                        String a = access.get(x);
                        if (x == 0)
                            acss = "Access:" + a;
                        else
                            acss = acss + ", " + a;
                    }
                    Card card = new Card(usr.getPseudo(), acss);
                    cardArrayAdapter.add(card);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        listView = (ListView) rootView.findViewById(R.id.card_listView);

        listView.addHeaderView(new View(this.getActivity()));
        listView.addFooterView(new View(this.getActivity()));

        cardArrayAdapter = new CardArrayAdapter(this.getActivity(), R.layout.list_item_card);

        Intent i = new Intent(this.getActivity(), FoodAPIService.class);
        i.putExtra("json", "{}");
        i.putExtra("target", "followers/" + PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_ID_KEY, "Unknown"));
        i.putExtra("post", false);
        i.putExtra("connected", true);
        this.getActivity().startService(i);
        listView.setAdapter(cardArrayAdapter);
        return rootView;
    }



    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(usersReceiver, new IntentFilter("users/search"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(usersReceiver);
    }
}
