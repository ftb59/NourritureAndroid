package cn.bjtu.nourriture.Adapters.recipes;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjtu.nourriture.ApiResults.TimeFormat;
import cn.bjtu.nourriture.ApiResults.recipes;
import cn.bjtu.nourriture.R;
import cn.bjtu.nourriture.View.fillSmallRecipe;
import cn.bjtu.nourriture.service.FoodAPIService;


public class RecipeArrayAdapter extends ArrayAdapter<recipes> {
    private static final String TAG = "RecipeArrayAdapter";
    private List<recipes> recipesList = new ArrayList<recipes>();
    int offset = 5;

    private Activity activity;
    private LayoutInflater inflater;

    static class recipesViewHolder {
        TextView pseudo;
        TextView hour;
        TextView nblikescomment;
        ViewGroup buttons;
        ListView comment_listView;
        ImageButton like;
        ImageButton comment;
        ImageButton edit;
    }

    public RecipeArrayAdapter(Activity a, int textViewResourceId) {
        super(a, textViewResourceId);
        activity = a;
    }

    @Override
    public void add(recipes object) {
        recipesList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.recipesList.size();
    }

    @Override
    public recipes getItem(int index) {
        return this.recipesList.get(index);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        recipesViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_card_recipe, parent, false);
            viewHolder = new recipesViewHolder();
            viewHolder.pseudo = (TextView) row.findViewById(R.id.pseudo);
            viewHolder.hour = (TextView) row.findViewById(R.id.hour);
            viewHolder.like = (ImageButton) row.findViewById(R.id.like);
            viewHolder.comment = (ImageButton) row.findViewById(R.id.comment);
            viewHolder.edit = (ImageButton) row.findViewById(R.id.edit);
            viewHolder.nblikescomment = (TextView) row.findViewById(R.id.nblikescomment);
            viewHolder.buttons = (ViewGroup) row.findViewById(R.id.buttons);
            viewHolder.comment_listView = (ListView) row.findViewById(R.id.comment_listView);
            row.setTag(viewHolder);
        } else {
            viewHolder = (recipesViewHolder)row.getTag();
        }
        final recipes recipes = getItem(position);
        final LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        viewHolder.pseudo.setText("PSEUDO A RECUP");
        viewHolder.hour.setText(TimeFormat.format(recipes.created_at.toString()));
        String nblikescommentStr;

        viewHolder.edit.setVisibility(View.GONE);

        View.OnClickListener commentRecipe = new View.OnClickListener() {
            @Override
            public void onClick(View v) {/*
                Intent myIntent = new Intent(activity.getBaseContext(), DetailRecipe.class);
                myIntent.putExtra("id_recipe", recipes.getId());
                activity.startActivity(myIntent);*/
            }};

        viewHolder.comment.setOnClickListener(commentRecipe);
        viewHolder.nblikescomment.setOnClickListener(commentRecipe);

        nblikescommentStr = recipes.likes + " like";
        if (recipes.comments > 1)
            nblikescommentStr += "s";
        nblikescommentStr += " - " + recipes.comments + " comment";
        if (recipes.comments > 1)
            nblikescommentStr += "s";
        viewHolder.nblikescomment.setText(nblikescommentStr);
        if (offset == position + 1)
        {
            Intent i = new Intent(activity, FoodAPIService.class);
            i.putExtra("json", "{\"offset\":" + offset+ "}");
            i.putExtra("target", "/recipes/search");
            i.putExtra("post", true);
            i.putExtra("connected", true);
            offset += 5;
            activity.startService(i);
        }
        fillSmallRecipe f = new fillSmallRecipe();
        f.fill(recipes,row);
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}