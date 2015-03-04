package cn.bjtu.nourriture.ApiResults;

import java.util.List;

import cn.bjtu.nourriture.FoodApi.FoodApiObject;

/**
 * Created by ftb on 15-1-4.
 */
public class users extends FoodApiObject{
    public String id;
    public String pseudo;
    public String avatar;
    public List<String> access;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getAccess() {
        return access;
    }

    public void setAccess(List<String> access) {
        this.access = access;
    }
}
