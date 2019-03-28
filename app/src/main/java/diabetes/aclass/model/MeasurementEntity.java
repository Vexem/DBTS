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
    private String created_atDate;
    private String created_atTime;
    private String updated_at;

    public MeasurementEntity(int patient_id, int value, String created_at){
        this.id = patient_id;
        this.value = value;
        this.created_atDate = created_at.substring(0,10);
        this.created_atTime = "at: " + created_at.substring(11,19);

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

    public int getValue(){ return value;}

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_atDate() {
        return created_atDate;
    }

    public String getCreated_atTime() {
        return created_atTime;
    }

    public int getYear(){
        return Integer.parseInt(created_atDate.substring(0,4));
    }
    public int getMonth(){
        return Integer.parseInt(created_atDate.substring(5,7))-1;
    }
    public int getDay(){
        return Integer.parseInt(created_atDate.substring(8,10));
    }
    public String getDate(){
        return getDay()+"/"+getMonth()+"/"+getYear();
    }



    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

}
