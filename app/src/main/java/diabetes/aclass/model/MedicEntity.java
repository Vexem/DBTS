package diabetes.aclass.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.sql.Timestamp;

public class MedicEntity {

    @SerializedName("medic_id")
    @Expose
    public int id;

    @SerializedName("medic_name")
    @Expose
    public String medic_name;

    @SerializedName("medic_mail")
    @Expose
    public String medic_mail;

    @SerializedName("medic_hostiptal")
    @Expose
    public String medic_hospital;

    @SerializedName("created_at")
    @Expose
    public Timestamp created_at;

    @SerializedName("updated_at")
    @Expose
    public Timestamp updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedic_name() {
        return medic_name;
    }

    public void setMedic_name(String medic_name) {
        this.medic_name = medic_name;
    }

    public String getMedic_mail() {
        return medic_mail;
    }

    public void setMedic_mail(String medic_mail) {
        this.medic_mail = medic_mail;
    }

    public String getMedic_hospital() {
        return medic_hospital;
    }

    public void setMedic_hospital(String medic_hospital) {
        this.medic_hospital = medic_hospital;
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
