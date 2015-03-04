package cn.bjtu.nourriture.FoodApi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ftb on 15-1-3.
 */
public class Users extends FoodApiObject{
    String email;
    String lastname;
    String firstname;
    String pseudo;
    String avatar; // Path
    String password;

    public Users(String email, String lastname, String firstname, String pseudo, String avatar, String password)
    {
        this.email = email;
        this.lastname = lastname;
        this.firstname = firstname;
        this.pseudo = pseudo;
        this.avatar = avatar;
        this.password = password;
    }

}
