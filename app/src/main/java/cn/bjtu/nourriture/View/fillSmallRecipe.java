package cn.bjtu.nourriture.View;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.bjtu.nourriture.ApiResults.parts;
import cn.bjtu.nourriture.ApiResults.recipes;
import cn.bjtu.nourriture.ApiResults.time;
import cn.bjtu.nourriture.R;

/**
 * Created by ftb on 15-1-17.
 */
public class fillSmallRecipe {

    public TextView description;
    public ImageView image;
    public TextView ingredients;
    public TextView time_total;
    public TextView time_prep;
    public TextView title;

    public void fill(recipes re, View v)
    {
        description = (TextView) v.findViewById(R.id.description);
        ingredients = (TextView) v.findViewById(R.id.ingredients);
        time_total = (TextView) v.findViewById(R.id.totalTime);
        time_prep = (TextView) v.findViewById(R.id.PrepTime);
        title  = (TextView) v.findViewById(R.id.title);
        title.setText(re.title);
        description.setText(re.description);
        ingredients.setText(Integer.toString(re.ingredients_length));
        String time;
        time t = re.getTime_total();
        if (t.getH() != 0)
        {
            time = t.getH() + "h " + t.getM();
        }
        else
            time = t.getM() + " min";
        time_total.setText(time);
        t = re.getTime_prep();
        if (t.getH() != 0)
        {
            time = t.getH() + "h " + t.getM();
        }
        else
            time = t.getM() + " min";
        time_prep.setText(time);
    }
}
