package diabetes.aclass.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.Objects;

@Entity(tableName = "measurements")
public class MeasurementEntity {
    private int idCounter = 0;

    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NonNull
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("value_min")
    @Expose
    private int value_min;

    @SerializedName("value_max")
    @Expose
    private int value_max;

    @SerializedName("last_value")
    @Expose
    private int last_value;
    private Timestamp created_at;
    private Timestamp updated_at;

    public MeasurementEntity(int id, String name, int value_min, int value_max, int last_value, Timestamp created_at, Timestamp updated_at) {
        this.id = idCounter++;
        this.name = name;
        this.value_min = value_min;
        this.value_max = value_max;
        this.last_value = last_value;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getIdCounter() {
        return idCounter;
    }

    public void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue_min() {
        return value_min;
    }

    public void setValue_min(int value_min) {
        this.value_min = value_min;
    }

    public int getValue_max() {
        return value_max;
    }

    public void setValue_max(int value_max) {
        this.value_max = value_max;
    }

    public int getLast_value() {
        return last_value;
    }

    public void setLast_value(int last_value) {
        this.last_value = last_value;
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
        if (!(o instanceof MeasurementEntity)) return false;
        MeasurementEntity that = (MeasurementEntity) o;
        return getIdCounter() == that.getIdCounter() &&
                getId() == that.getId() &&
                getValue_min() == that.getValue_min() &&
                getValue_max() == that.getValue_max() &&
                getLast_value() == that.getLast_value() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getCreated_at(), that.getCreated_at()) &&
                Objects.equals(getUpdated_at(), that.getUpdated_at());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(getIdCounter(), getId(), getName(), getValue_min(), getValue_max(), getLast_value(), getCreated_at(), getUpdated_at());
    }

    @Override
    public String toString() {
        return "MeasurementEntity{" +
                "idCounter=" + idCounter +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", value_min=" + value_min +
                ", value_max=" + value_max +
                ", last_value=" + last_value +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
