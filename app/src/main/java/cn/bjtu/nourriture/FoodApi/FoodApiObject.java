package cn.bjtu.nourriture.FoodApi;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bjtu.nourriture.ApiResults.recipes;

/**
 * Created by ftb on 15-1-3.
 */
public class FoodApiObject {

    private Boolean isDefault(String value)
    {
        if (value.equals("") || value.equals("0"))
            return true;
        return false;
    }

    public String toJson() {
        Field[] aClassFields = this.getClass().getDeclaredFields();
        JSONObject js = new JSONObject();
        for(Field f : aClassFields){
            try {

                if (f.get(this) != null && !isDefault(f.get(this).toString())) {
                    if (f.getName().equals("recipes"))
                    {
                        recipes r = (recipes) f.get(this);
                        js.put("recipes", r.id);
                    }
                    else if (f.getType() == String.class)
                        js.put(f.getName(), Secure.forJSON(f.get(this).toString()));
                    else if (f.getType() == int.class) {
                        js.put(f.getName(), f.get(this));
                    }
                    else {
                        JSONArray mJSONArray = new JSONArray((List<String>) f.get(this));
                        js.put(f.getName(), mJSONArray);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return js.toString();
    }
}
