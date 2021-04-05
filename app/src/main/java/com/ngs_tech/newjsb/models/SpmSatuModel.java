package com.ngs_tech.newjsb.models;

public class SpmSatuModel {

    private String id;
    private String id_kat;
    private String penomoran;
    private String nama_kat;
    private String indikator;
    private String sub_indikator;

    public SpmSatuModel(){

    }

    public String getPenomoran() {
        return penomoran;
    }

    public void setPenomoran(String penomoran) {
        this.penomoran = penomoran;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_kat() {
        return id_kat;
    }

    public void setId_kat(String id_kat) {
        this.id_kat = id_kat;
    }

    public String getNama_kat() {
        return nama_kat;
    }

    public void setNama_kat(String nama_kat) {
        this.nama_kat = nama_kat;
    }

    public String getIndikator() {
        return indikator;
    }

    public void setIndikator(String indikator) {
        this.indikator = indikator;
    }

    public String getSub_indikator() {
        return sub_indikator;
    }

    public void setSub_indikator(String sub_indikator) {
        this.sub_indikator = sub_indikator;
    }
}
