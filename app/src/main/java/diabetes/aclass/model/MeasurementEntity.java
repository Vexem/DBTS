package diabetes.aclass.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "measurements")
public class MeasurementEntity {
    private int idCounter = 0;

    @SerializedName("patient_id")
    @Expose
    @PrimaryKey
    @NonNull
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("value")
    @Expose
    private int value;

    @SerializedName("created_at")
    @Expose
    private Timestamp created_at;
    private Timestamp updated_at;

    public MeasurementEntity(int patient_id, int value, String created_at){
    this.id = patient_id;
    this.value = value;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(created_at);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            this.created_at = timestamp;
        } catch(Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
        }
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

}
