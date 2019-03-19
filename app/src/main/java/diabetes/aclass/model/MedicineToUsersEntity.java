package diabetes.aclass.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity(tableName = "medicinetouser")
public class MedicineToUsersEntity {

    @SerializedName("user_id")
    @Expose
    @PrimaryKey
    @NonNull
    private int user_id;

    @SerializedName("medicine_id")
    @Expose
    @PrimaryKey
    @NonNull
    private int medicine_id;

    public MedicineToUsersEntity(int user_id, int medicine_id) {
        this.user_id = user_id;
        this.medicine_id = medicine_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(int medicine_id) {
        this.medicine_id = medicine_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicineToUsersEntity)) return false;
        MedicineToUsersEntity that = (MedicineToUsersEntity) o;
        return getUser_id() == that.getUser_id() &&
                getMedicine_id() == that.getMedicine_id();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(getUser_id(), getMedicine_id());
    }

    @Override
    public String toString() {
        return "MedicineToUsersEntity{" +
                "user_id=" + user_id +
                ", medicine_id=" + medicine_id +
                '}';
    }
}
