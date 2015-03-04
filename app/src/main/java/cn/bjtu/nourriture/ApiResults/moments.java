package cn.bjtu.nourriture.ApiResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.bjtu.nourriture.FoodApi.FoodApiObject;
import cn.bjtu.nourriture.FoodApi.Secure;

/**
 * Created by ftb on 15-1-13.
 */
public class moments extends FoodApiObject {

    public String id;
    public String title;
    public String description;
    public users users;
    public recipes recipes;
    public List<ingredients> ingredients;
    public int likes;
    public int comments;
    public String type; // Not implemented in api yet
    public String created_at;
    public String updated_at;

    public moments(String title, String description, List<ingredients> in, cn.bjtu.nourriture.ApiResults.recipes recipes)
    {
        this.title = title;
        this.description = description;
        this.ingredients = in;
        this.recipes = recipes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public users getUsers() {
        return users;
    }

    public void setUsers(users users) {
        this.users = users;
    }

    public recipes getRecipes() {
        return recipes;
    }

    public void setRecipes(recipes recipes) {
        this.recipes = recipes;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
}
