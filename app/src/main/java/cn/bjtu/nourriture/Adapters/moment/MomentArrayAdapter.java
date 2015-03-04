package cn.bjtu.nourriture.Adapters.moment;


import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.bjtu.nourriture.ApiResults.TimeFormat;
import cn.bjtu.nourriture.ApiResults.ingredients;
import cn.bjtu.nourriture.ApiResults.moments;
import cn.bjtu.nourriture.ApiResults.recipes;
import cn.bjtu.nourriture.R;
import cn.bjtu.nourriture.View.DetailMoment;
import cn.bjtu.nourriture.View.EditMoment;
import cn.bjtu.nourriture.View.NavDrawer;
import cn.bjtu.nourriture.service.FoodAPIService;


public class MomentArrayAdapter extends ArrayAdapter<moments> {
    private static final String TAG = "MomentArrayAdapter";
    private List<moments> momentsList = new ArrayList<moments>();
    int offset = 5;

    private Activity activity;
    private LayoutInflater inflater;

    static class momentsViewHolder {
        TextView pseudo;
        TextView hour;
        TextView title;
        TextView content;
        TextView nblikescomment;
        ViewGroup buttons;
        ListView comment_listView;
        LinearLayout Recipe;
        LinearLayout Ingredients;
        ImageButton like;
        ImageButton comment;
        ImageButton edit;
    }

    public MomentArrayAdapter(Activity a, int textViewResourceId) {
        super(a, textViewResourceId);
        activity = a;
    }

    @Override
    public void add(moments object) {
        momentsList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.momentsList.size();
    }

    @Override
    public moments getItem(int index) {
        return this.momentsList.get(index);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        momentsViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_card_moment, parent, false);
            viewHolder = new momentsViewHolder();
            viewHolder.pseudo = (TextView) row.findViewById(R.id.pseudo);
            viewHolder.hour = (TextView) row.findViewById(R.id.hour);
        //    viewHolder.title = (TextView) row.findViewById(R.id.title);
            viewHolder.content = (TextView) row.findViewById(R.id.content);
            viewHolder.Recipe = (LinearLayout) row.findViewById(R.id.Recipe);
        //    viewHolder.Ingredients = (LinearLayout) row.findViewById(R.id.Ingredients);
            viewHolder.like = (ImageButton) row.findViewById(R.id.like);
            viewHolder.comment = (ImageButton) row.findViewById(R.id.comment);
            viewHolder.edit = (ImageButton) row.findViewById(R.id.edit);
            viewHolder.nblikescomment = (TextView) row.findViewById(R.id.nblikescomment);
            viewHolder.buttons = (ViewGroup) row.findViewById(R.id.buttons);
            viewHolder.comment_listView = (ListView) row.findViewById(R.id.comment_listView);
            row.setTag(viewHolder);
        } else {
            viewHolder = (momentsViewHolder)row.getTag();
        }
        final moments moments = getItem(position);
//        viewHolder.pseudo.setText(moments.getUsers().getPseudo());
        final LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        viewHolder.pseudo.setText("PSEUDO A RECUP");
        viewHolder.hour.setText(TimeFormat.format(moments.getCreated_at().toString()));
        //viewHolder.title.setText(moments.getTitle());
        viewHolder.content.setText(moments.getDescription());
//        viewHolder.comment_listView.setVisibility(View.INVISIBLE);
        String nblikescommentStr;

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditMoment e = new EditMoment(inflater, activity);
                e.edit(getItem(position));
            }
        });

        View.OnClickListener commentMoment = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(activity.getBaseContext(), DetailMoment.class);
                myIntent.putExtra("id_moment", moments.getId());

                activity.startActivity(myIntent);
            }};

        viewHolder.comment.setOnClickListener(commentMoment);
        viewHolder.nblikescomment.setOnClickListener(commentMoment);

        nblikescommentStr = moments.getLikes() + " like";
        if (moments.getLikes() > 1)
            nblikescommentStr += "s";
        nblikescommentStr += " - " + moments.getComments() + " comment";
        if (moments.getComments() > 1)
            nblikescommentStr += "s";
        viewHolder.nblikescomment.setText(nblikescommentStr);
        if (offset == position + 1)
        {
            Intent i = new Intent(activity, FoodAPIService.class);
            i.putExtra("json", "{\"offset\":" + offset+ "}");
            i.putExtra("target", "/moments/search");
            i.putExtra("post", true);
            i.putExtra("connected", true);
            offset += 5;
            activity.startService(i);
        }
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}