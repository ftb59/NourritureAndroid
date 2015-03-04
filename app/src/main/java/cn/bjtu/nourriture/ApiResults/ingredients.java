package cn.bjtu.nourriture.ApiResults;

import java.io.File;
import java.util.List;

import cn.bjtu.nourriture.FoodApi.FoodApiObject;

/**
 * Created by ftb on 15-1-3.
 */
public class ingredients extends FoodApiObject {
    public String id;
    public String name;
    public String icon;
    public File imageFile;
    public String color;
    public List<String> labels;
    public List<String> blacklist;

    public ingredients(String id, String name, File imageFile, String icon, String color, List<String> labels, List<String> blacklist) {
        this.id = id;
        this.name = name;
        this.imageFile = imageFile;
        this.icon = icon;
        this.color = color;
        this.labels = labels;
        this.blacklist = blacklist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<String> getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(List<String> blacklist) {
        this.blacklist = blacklist;
    }
}
