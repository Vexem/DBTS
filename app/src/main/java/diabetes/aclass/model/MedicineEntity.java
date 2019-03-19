package diabetes.aclass.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity(tableName = "medicines")
public class MedicineEntity {

    private int idCount;

    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NonNull
    private int id;

    @SerializedName("medicine_name")
    @Expose
    @PrimaryKey
    @NonNull
    private String medicine_name;

    public MedicineEntity(int id, String medicine_name) {
        this.id = idCount++;
        this.medicine_name = medicine_name;
    }

    public int getIdCount() {
        return idCount;
    }

    public void setIdCount(int idCount) {
        this.idCount = idCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicineEntity)) return false;
        MedicineEntity that = (MedicineEntity) o;
        return getIdCount() == that.getIdCount() &&
                getId() == that.getId() &&
                Objects.equals(getMedicine_name(), that.getMedicine_name());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(getIdCount(), getId(), getMedicine_name());
    }

    @Override
    public String toString() {
        return "MedicineEntity{" +
                "idCount=" + idCount +
                ", id=" + id +
                ", medicine_name='" + medicine_name + '\'' +
                '}';
    }
}
