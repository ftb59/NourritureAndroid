package cn.bjtu.nourriture.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.bjtu.nourriture.R;

/**
 * Created by ftb on 15-1-10.
 */
public class TagManager {

    private ArrayList<String> tagStrings = new ArrayList<>();
    private final ViewGroup ll;
    LayoutInflater inflater;

    public TagManager(ViewGroup LL, LayoutInflater inf)
    {
        this.ll = LL;
        this.inflater = inf;
    }

   public void addTagArray(ArrayList<String> tags)
   {
       this.tagStrings = tags;
       addView();
   }

    public void addView()
    {
        View[] tags = new View[tagStrings.size()];
        ll.removeAllViews();
        for (int i = 0; i < tagStrings.size(); i++) {
            String tg = tagStrings.get(i);
            tags[i] = newView(tg);
            TextView removeButton = (TextView) tags[i].findViewById(R.id.deleteTag);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String objTag = (String) v.getTag();
                    for (int i = 0; i < tagStrings.size(); i++) {
                        String tg = tagStrings.get(i);
                        if (tg.equals(objTag)) {
                            tagStrings.remove(i);
                            break;
                        }
                    }
                    addView();
                }
            });
            if (!tg.equals(""))
                ll.addView(tags[i]);
        }
    }

    private View newView(String tagName)
    {
        View v = inflater.inflate(R.layout.tag, null);
        TextView name = (TextView) v.findViewById(R.id.nameTag);
        TextView x = (TextView) v.findViewById(R.id.deleteTag);
        x.setTag(tagName);
        if (tagName.length() > 10)
            tagName = tagName.substring(0, 10) + "...";
        name.setText(tagName);
        return (v);
    }

    public void addTag(String Tag)
    {
        tagStrings.add(Tag);
        addView();
    }

    public ArrayList<String> getTags()
    {
        return tagStrings;
    }
}
