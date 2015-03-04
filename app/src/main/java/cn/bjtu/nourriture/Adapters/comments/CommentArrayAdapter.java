package cn.bjtu.nourriture.Adapters.comments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjtu.nourriture.ApiResults.TimeFormat;
import cn.bjtu.nourriture.ApiResults.comments;
import cn.bjtu.nourriture.ApiResults.ingredients;
import cn.bjtu.nourriture.R;
import cn.bjtu.nourriture.View.DetailMoment;
import cn.bjtu.nourriture.service.FoodAPIService;


public class CommentArrayAdapter extends ArrayAdapter<comments> {
    private static final String TAG = "CommentArrayAdapter";
    private List<comments> commentsList = new ArrayList<comments>();

    private Activity activity;
    private LayoutInflater inflater;

    static class commentsViewHolder {
        TextView deleteComment;
        ImageView img;
        TextView pseudo;
        TextView content;
        TextView hour;
        TextView nblikes;
        TextView likeComment;
    }

    public CommentArrayAdapter(Activity a, int textViewResourceId) {
        super(a, textViewResourceId);
        activity = a;
    }

    @Override
    public void add(comments object) {
        commentsList.add(object);
        Log.e("obj", object.getComment());
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.commentsList.size();
    }

    @Override
    public comments getItem(int index) {
        return this.commentsList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Log.e("x", position + " " + commentsList.size());
        commentsViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.comment, parent, false);
            viewHolder = new commentsViewHolder();
            viewHolder.pseudo = (TextView) row.findViewById(R.id.pseudo);
            viewHolder.hour = (TextView) row.findViewById(R.id.hour);
            viewHolder.content = (TextView) row.findViewById(R.id.content);
            viewHolder.deleteComment = (TextView) row.findViewById(R.id.deleteComment);
            viewHolder.nblikes = (TextView) row.findViewById(R.id.nblikes);
            viewHolder.likeComment = (TextView) row.findViewById(R.id.likeComment);
            row.setTag(viewHolder);
        } else {
            viewHolder = (commentsViewHolder)row.getTag();
        }
        final comments comments = getItem(position);
//        viewHolder.pseudo.setText(comments.getUsers().getPseudo());
        viewHolder.pseudo.setText("PSEUDO A RECUP");
        viewHolder.hour.setText(TimeFormat.formatSmall(comments.getCreated_at().toString()));
        viewHolder.content.setText(comments.getComment());
        String nblikescommentStr;


        viewHolder.deleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent i = new Intent(activity, FoodAPIService.class);
                                i.putExtra("json", "{}");
                                i.putExtra("target", "/comments/moments/" + comments.mid+ "/" + comments.getId());
                                i.putExtra("ret", "/comments/delete");
                                i.putExtra("delete", true);
                                i.putExtra("post", false);
                                i.putExtra("connected", true);
                                activity.startService(i);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure to delete this comment?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        nblikescommentStr = " - " + comments.getLike_count() + " like";
        if (comments.getLike_count() > 1)
            nblikescommentStr += "s";
        viewHolder.nblikes.setText(nblikescommentStr);
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}