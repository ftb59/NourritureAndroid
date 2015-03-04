package cn.bjtu.nourriture.View;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import cn.bjtu.nourriture.Adapters.TagManager;
import cn.bjtu.nourriture.ApiResults.ingredients;
import cn.bjtu.nourriture.R;
import cn.bjtu.nourriture.service.FoodAPIService;

/**
 * Created by ftb on 15-1-10.
 */
public class IngredientCreateEdit {

    private  ImageView img;
    public void create(final Activity activity, LayoutInflater inflater, final Fragment f)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.ingredient_create);
        ViewGroup ll = (ViewGroup) dialog.findViewById(R.id.tagList);
        final TagManager tagAdapter = new TagManager(ll, inflater);
        ViewGroup LL = (ViewGroup) dialog.findViewById(R.id.blList);
        final TagManager blAdapter = new TagManager(LL, inflater);
        dialog.setTitle("Create an ingredient");


        img = (ImageView) dialog.findViewById(R.id.image);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f.startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 3);
            }
        });
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("You need to choose a valid name for an ingredient, the name must "+
                "contain at least 2 characters. The name must be unique. You must choose a picture.");

        TextView remove = (TextView) dialog.findViewById(R.id.remove);
        remove.setVisibility(View.INVISIBLE);
        TextView dialogButton = (TextView) dialog.findViewById(R.id.valid);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name = (TextView) dialog.findViewById(R.id.ingredientInput);
                ingredients in = new ingredients("", name.getText().toString(),null,"","", tagAdapter.getTags(),blAdapter.getTags());
                Intent i = new Intent(activity, FoodAPIService.class);
                i.putExtra("json", in.toJson());
                i.putExtra("target", "/ingredients");
                if (imgs != null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    imgs.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                    byte[] b = bos.toByteArray();
                    i.putExtra("img", b);
                }
                i.putExtra("post", true);
                i.putExtra("connected", true);
                activity.startService(i);
                dialog.dismiss();
            }
        });

        TextView cancelButton = (TextView) dialog.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView addButton = (TextView) dialog.findViewById(R.id.add);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) dialog.findViewById(R.id.tagInput);
                if (et.equals(""))
                    return;
                tagAdapter.addTag(et.getText().toString());
                et.setText("");
            }
        });

        TextView blAddButton = (TextView) dialog.findViewById(R.id.bladd);

        blAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) dialog.findViewById(R.id.blInput);
                if (et.equals(""))
                    return;
                blAdapter.addTag(et.getText().toString());
                et.setText("");
            }
        });

        dialog.show();
    }

    public void edit(final Activity activity, LayoutInflater inflater, final ingredients ingredient, final Fragment f)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.ingredient_create);
        ViewGroup ll = (ViewGroup) dialog.findViewById(R.id.tagList);
        final TagManager tagAdapter = new TagManager(ll, inflater);
        ViewGroup LL = (ViewGroup) dialog.findViewById(R.id.blList);
        final TagManager blAdapter = new TagManager(LL, inflater);
        dialog.setTitle("Edit an ingredient");

        img = (ImageView) dialog.findViewById(R.id.image);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f.startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 3);
            }
        });
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("You need to choose a valid name for an ingredient, the name must "+
                "contain at least 2 characters. The name must be unique. You must choose a picture.");

        TextView name = (TextView) dialog.findViewById(R.id.ingredientInput);
        name.setText(ingredient.getName());
        tagAdapter.addTagArray((ArrayList<String>) ingredient.getLabels());
        blAdapter.addTagArray((ArrayList<String>) ingredient.getBlacklist());
        TextView dialogButton = (TextView) dialog.findViewById(R.id.valid);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name = (TextView) dialog.findViewById(R.id.ingredientInput);
                ingredients in = new ingredients("", name.getText().toString(),null,"","", tagAdapter.getTags(),blAdapter.getTags());
                Intent i = new Intent(activity, FoodAPIService.class);
                i.putExtra("json", in.toJson());
                i.putExtra("target", "/ingredients/" + ingredient.getId());
                i.putExtra("ret", "/ingredients");
                if (imgs != null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    imgs.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                    byte[] b = bos.toByteArray();
                    i.putExtra("img", b);
                }
                i.putExtra("patch", true);
                i.putExtra("post", false);
                i.putExtra("connected", true);
                activity.startService(i);
                dialog.dismiss();
            }
        });

        TextView removeButton = (TextView) dialog.findViewById(R.id.remove);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name = (TextView) dialog.findViewById(R.id.ingredientInput);
                ingredients in = new ingredients("", name.getText().toString(),null,"","", tagAdapter.getTags(),blAdapter.getTags());
                Intent i = new Intent(activity, FoodAPIService.class);
                i.putExtra("json", in.toJson());
                i.putExtra("target", "/ingredients/" + ingredient.getId());
                i.putExtra("ret", "/ingredients");
                i.putExtra("delete", true);
                i.putExtra("post", false);
                i.putExtra("connected", true);
                activity.startService(i);
                dialog.dismiss();
            }
        });

        TextView cancelButton = (TextView) dialog.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView addButton = (TextView) dialog.findViewById(R.id.add);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) dialog.findViewById(R.id.tagInput);
                if (et.equals(""))
                    return;
                tagAdapter.addTag(et.getText().toString());
                et.setText("");
            }
        });

        TextView blAddButton = (TextView) dialog.findViewById(R.id.bladd);

        blAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) dialog.findViewById(R.id.blInput);
                if (et.equals(""))
                    return;
                blAdapter.addTag(et.getText().toString());
                et.setText("");
            }
        });

        dialog.show();
    }

    private Bitmap imgs;

    public void setImage(Bitmap image)
    {
        img.setImageBitmap(image);
        imgs = image;
    }
}
