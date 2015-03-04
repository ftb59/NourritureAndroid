package cn.bjtu.nourriture.Adapters.ingredients;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjtu.nourriture.ApiResults.ingredients;
import cn.bjtu.nourriture.R;
import cn.bjtu.nourriture.View.FragmentIngredients;
import cn.bjtu.nourriture.View.IngredientCreateEdit;
import cn.bjtu.nourriture.View.MainActivity;
import cn.bjtu.nourriture.service.FoodAPIService;

public class CardArrayAdapter  extends ArrayAdapter<Card> {
    private static final String TAG = "CardArrayAdapter";
    private List<Card> cardList = new ArrayList<Card>();

    private Activity activity;
    private LayoutInflater inflater;
    private Class cl = MainActivity.class;
    private Fragment fragment;
    int offset = 0;

    static class CardViewHolder {
        TextView line1;
        TextView line2;
        TextView line3;
    }

    IngredientCreateEdit Create;

    public CardArrayAdapter(Activity a, int textViewResourceId, Class cl, LayoutInflater i, IngredientCreateEdit c, Fragment f) {
        super(a, textViewResourceId);
        this.cl = cl;
        activity = a;
        inflater = i;
        Create = c;
        fragment = f;
    }
    public CardArrayAdapter(Activity a, int textViewResourceId) {
        super(a, textViewResourceId);
    }


    @Override
    public void add(Card object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public Card getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.line1 = (TextView) row.findViewById(R.id.line1);
            viewHolder.line2 = (TextView) row.findViewById(R.id.line2);
            viewHolder.line3 = (TextView) row.findViewById(R.id.line3);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        final Card card = getItem(position);
        viewHolder.line1.setText(card.getLine1());
        viewHolder.line2.setText(card.getLine2());
        viewHolder.line3.setText(card.getLine3());
        if (cl == FragmentIngredients.class) {
            if (offset == position + 1) {
                Intent i = new Intent(activity, FoodAPIService.class);
                i.putExtra("json", "{\"offset\":" + offset + "}");
                i.putExtra("target", "/ingredients/search");
                i.putExtra("post", true);
                i.putExtra("connected", true);
                offset += 5;
                activity.startService(i);
            }
        }
        else
        {
            if (offset == position + 1) {
                Intent i = new Intent(activity, FoodAPIService.class);
                i.putExtra("json", "{\"offset\":" + offset + "}");
                i.putExtra("target", "/users/search");
                i.putExtra("post", true);
                i.putExtra("connected", true);
                offset += 5;
                activity.startService(i);
            }
        }
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCard(card);
            }
        });
        return row;
    }

    private void editCard(Card c)
    {
        if (cl == FragmentIngredients.class)
        {
            Create.edit(activity, inflater, (ingredients) c.obj, fragment);
        }
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}