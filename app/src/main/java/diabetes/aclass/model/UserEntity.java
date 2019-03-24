package diabetes.aclass.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;

import java.sql.Timestamp;
import java.util.Objects;

@Entity(tableName = "users")
public class UserEntity {

    @SerializedName("user_id")
    @Expose
    @PrimaryKey
    @NonNull
    private String id;

    @SerializedName("provider")
    @Expose
    private String provider;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("email")
    @Expose
    private String email;


    @SerializedName("oauth_token")
    @Expose
    private String oauth_token;

    @SerializedName("oauth_expires_at")
    @Expose
    private Timestamp oauth_expires_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOauth_token() {
        return oauth_token;
    }

    public void setOauth_token(String oauth_token) {
        this.oauth_token = oauth_token;
    }

    public Timestamp getOauth_expires_at() {
        return oauth_expires_at;
    }

    public void setOauth_expires_at(Timestamp oauth_expires_at) {
        this.oauth_expires_at = oauth_expires_at;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, UserEntity.class);
    }
}


