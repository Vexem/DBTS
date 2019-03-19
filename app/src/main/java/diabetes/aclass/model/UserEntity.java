package diabetes.aclass.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

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
    private int id;

    @SerializedName("surname")
    @Expose
    private String surname;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("oauth_token")
    @Expose
    private String oauth_token;

    @SerializedName("is_doctor")
    @Expose
    private Boolean is_doctor;

    @SerializedName("oauth_expires_at")
    @Expose
    private Timestamp oauth_expires_at;
    private Timestamp created_at;
    private Timestamp updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOauth_token() {
        return oauth_token;
    }

    public void setOauth_token(String oauth_token) {
        this.oauth_token = oauth_token;
    }

    public Boolean getIs_doctor() {
        return is_doctor;
    }

    public void setIs_doctor(Boolean is_doctor) {
        this.is_doctor = is_doctor;
    }

    public Timestamp getOauth_expires_at() {
        return oauth_expires_at;
    }

    public void setOauth_expires_at(Timestamp oauth_expires_at) {
        this.oauth_expires_at = oauth_expires_at;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(oauth_token, that.oauth_token) &&
                Objects.equals(is_doctor, that.is_doctor) &&
                Objects.equals(oauth_expires_at, that.oauth_expires_at) &&
                Objects.equals(created_at, that.created_at) &&
                Objects.equals(updated_at, that.updated_at);
    }

}


