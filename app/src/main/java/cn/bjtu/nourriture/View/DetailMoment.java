package cn.bjtu.nourriture.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import cn.bjtu.nourriture.Adapters.comments.CommentArrayAdapter;
import cn.bjtu.nourriture.Adapters.ingredients.Card;
import cn.bjtu.nourriture.Adapters.ingredients.CardArrayAdapter;
import cn.bjtu.nourriture.ApiResults.TimeFormat;
import cn.bjtu.nourriture.ApiResults.comments;
import cn.bjtu.nourriture.ApiResults.moments;
import cn.bjtu.nourriture.ApiResults.users;
import cn.bjtu.nourriture.R;
import cn.bjtu.nourriture.service.FoodAPIService;

public class DetailMoment extends ActionBarActivity {

    String id = null;
    ViewHolder viewHolder;
    Activity activity;
    private CommentArrayAdapter commentArrayAdapter;
    private BroadcastReceiver errorReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("response");
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
        }
    };
    private BroadcastReceiver commentsReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("response");
            Gson gson = new Gson();
            Type listType = new TypeToken<List<comments>>() {
            }.getType();
            JSONObject jObject = null;
            try {
                jObject = new JSONObject(response);
                JSONArray json = jObject.getJSONArray("comments");
                commentArrayAdapter = new CommentArrayAdapter(activity, R.layout.comment);
                viewHolder.comment_listView.setAdapter(commentArrayAdapter);
                List<comments> cmt = (List<comments>) gson.fromJson(json.toString(), listType);
                for (int i = 0; i < cmt.size(); i++) {
                    comments c = cmt.get(i);
                    Log.e("c", c.getComment());
                    c.mid = id;
                    commentArrayAdapter.add(c);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private BroadcastReceiver momentReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("response");
            Gson gson = new Gson();
            Type type = new TypeToken<moments>() {
            }.getType();
            Type usr = new TypeToken<users>() {
            }.getType();
            JSONObject jObject = null;
            try {
                jObject = new JSONObject(response);
                JSONObject json = jObject.getJSONObject("moment");
                final moments m = (moments) gson.fromJson(json.toString(),type);
                json = jObject.getJSONObject("created_by");
               users u = (users) gson.fromJson(json.toString(),usr);
                    viewHolder.pseudo.setText(u.getPseudo());
                    viewHolder.hour.setText(TimeFormat.format(m.getCreated_at().toString()));
                    viewHolder.content.setText(m.getDescription());
                    viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditMoment e = new EditMoment(getLayoutInflater(), activity);
                        e.edit(m);
                    }
                });
                    String nblikescommentStr;

                    nblikescommentStr = m.getLikes() + " like";
                    if (m.getLikes() > 1)
                        nblikescommentStr += "s";
                    nblikescommentStr += " - " + m.getComments() + " comment";
                    if (m.getComments() > 1)
                        nblikescommentStr += "s";
                    viewHolder.nblikescomment.setText(nblikescommentStr);
                viewHolder.card.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private BroadcastReceiver commentSender = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            getComments();
            getMoments();
        }
    };

    static class ViewHolder {
        TextView pseudo;
        TextView hour;
        TextView content;
        TextView nblikescomment;
        ListView comment_listView;
        LinearLayout Recipe;
        ImageButton like;
        ImageButton comment;
        ImageButton edit;
        ViewGroup buttons;
        ViewGroup card;
        EditText commentInput;
        Button sendComment;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_moment);

        id = getIntent().getStringExtra("id_moment");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        viewHolder = new ViewHolder();
        viewHolder.comment_listView = (ListView) findViewById(R.id.comment_listView);
        View header = getLayoutInflater().inflate(R.layout.list_item_card_moment, null);
        viewHolder.comment_listView.addHeaderView(header);
        getViews();
        viewHolder.comment.setVisibility(View.GONE);
        viewHolder.card.setVisibility(View.INVISIBLE);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getMoments();
        getComments();
        activity =this;

        viewHolder.sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = viewHolder.commentInput.getText().toString();
                if (msg.replace(" ", "").length() == 0)
                    return ;
                sendComment(msg);
                viewHolder.commentInput.setText("");
                InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        });
    }

    public void sendComment(String s)
    {
        Intent i = new Intent(this, FoodAPIService.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("comment", s);
            i.putExtra("json", jsonObject.toString());
            i.putExtra("target", "/comments/moments/" + id);
            i.putExtra("ret", "/comments/create");
            i.putExtra("post", true);
            i.putExtra("connected", true);
            this.startService(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getMoments()
    {
        Intent i = new Intent(this, FoodAPIService.class);
        JSONObject js = new JSONObject();
        try {
            js.put("limit", 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        i.putExtra("json", js.toString());
        i.putExtra("target", "/moments/" + id);
        i.putExtra("ret", "/moments/search");
        i.putExtra("post", false);
        i.putExtra("connected", true);
        this.startService(i);
    }

    public void getComments()
    {
        Intent i = new Intent(this, FoodAPIService.class);
        JSONObject js = new JSONObject();
        try {
            js.put("limit", 40);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        i.putExtra("json", js.toString());
        i.putExtra("target", "/comments/moments/" + id + "/search");
        i.putExtra("ret", "/comments/search");
        i.putExtra("post", true);
        i.putExtra("connected", true);
        this.startService(i);
    }

    private void getViews() {
        viewHolder.comment_listView = (ListView) findViewById(R.id.comment_listView);
        viewHolder.pseudo = (TextView) viewHolder.comment_listView.findViewById(R.id.pseudo);
        viewHolder.hour = (TextView) viewHolder.comment_listView.findViewById(R.id.hour);
        viewHolder.content = (TextView) viewHolder.comment_listView.findViewById(R.id.content);
        viewHolder.Recipe = (LinearLayout) viewHolder.comment_listView.findViewById(R.id.Recipe);
        viewHolder.like = (ImageButton) viewHolder.comment_listView.findViewById(R.id.like);
        viewHolder.comment = (ImageButton) viewHolder.comment_listView.findViewById(R.id.comment);
        viewHolder.edit = (ImageButton) viewHolder.comment_listView.findViewById(R.id.edit);
        viewHolder.nblikescomment = (TextView) viewHolder.comment_listView.findViewById(R.id.nblikescomment);
        viewHolder.buttons = (ViewGroup) viewHolder.comment_listView.findViewById(R.id.buttons);
        viewHolder.card = (ViewGroup) viewHolder.comment_listView.findViewById(R.id.card);
        viewHolder.commentInput = (EditText) findViewById(R.id.commentInput);
        viewHolder.sendComment = (Button) findViewById(R.id.sendComment);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(commentSender, new IntentFilter("/comments/delete"));
        LocalBroadcastManager.getInstance(this).registerReceiver(commentsReceiver, new IntentFilter("/comments/search"));
        LocalBroadcastManager.getInstance(this).registerReceiver(commentSender, new IntentFilter("/comments/create"));
        LocalBroadcastManager.getInstance(this).registerReceiver(momentReceiver, new IntentFilter("/moments/search"));
        LocalBroadcastManager.getInstance(this).registerReceiver(errorReceiver, new IntentFilter("error"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(commentsReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(commentSender);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(momentReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(errorReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
