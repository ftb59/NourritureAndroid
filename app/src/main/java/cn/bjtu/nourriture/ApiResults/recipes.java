package cn.bjtu.nourriture.ApiResults;

import java.util.List;

import cn.bjtu.nourriture.FoodApi.FoodApiObject;

/**
 * Created by ftb on 15-1-4.
 */
public class recipes extends FoodApiObject {

    public String id;
    public List<String> blacklist;
    public String description;
    public String image;
    public List<String> ingredients;
    public time time_total;
    public time time_prep;
    public String title;
    public int likes;
    public int comments;
    public String type; // Not implemented in api yet
    public String created_at;
    public String updated_at;
    public users users;
    public int ingredients_length;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(List<String> blacklist) {
        this.blacklist = blacklist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public time getTime_total() {
        return time_total;
    }

    public void setTime_total(time time_total) {
        this.time_total = time_total;
    }

    public time getTime_prep() {
        return time_prep;
    }

    public void setTime_prep(time time_prep) {
        this.time_prep = time_prep;
    }
}
