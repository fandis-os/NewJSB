package com.ngs_tech.newjsb.models;

public class PeralatanTolModel {
    private String nama_alat;
    private String id_alat;

    public PeralatanTolModel(){

    }

    public PeralatanTolModel(String nama_alat, String id_alat){
        this.nama_alat = nama_alat;
        this.id_alat = id_alat;
    }

    public String getNama_alat() {
        return nama_alat;
    }

    public void setNama_alat(String nama_alat) {
        this.nama_alat = nama_alat;
    }

    public String getId_alat() {
        return id_alat;
    }

    public void setId_alat(String id_alat) {
        this.id_alat = id_alat;
    }
}
