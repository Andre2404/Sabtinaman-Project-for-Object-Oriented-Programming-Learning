package model;

import java.sql.Timestamp;

public class Keluhan {
    private int idKeluhan;
    private Perusahaan company;
    private Pengguna user;
    private Alat alat;
    private TransaksiSewa sewa;
    private String deskripsi;
    private Timestamp tanggal;
    private String status;
    private String tanggapan;
    
   public Keluhan(int idKeluhan, Perusahaan company, Pengguna user, Alat alat, TransaksiSewa sewa, String deskripsi, Timestamp tanggal, String status, String tanggapan) {
        this.idKeluhan = idKeluhan;
        this.company = company;
        this.user = user;
        this.alat = alat;
        this.sewa = sewa;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
        this.status = status;
        this.tanggapan = tanggapan;
    }

    public String getTanggapan() {
        return tanggapan;
    }

    public void setTanggapan(String tanggapan) {
        this.tanggapan = tanggapan;
    }
    
    public int getIdKeluhan() {
        return idKeluhan;
    }

    public void setIdKeluhan(int idKeluhan) {
        this.idKeluhan = idKeluhan;
    }

    public Pengguna getUser() {
        return user;
    }

    public void setUser(Pengguna user) {
        this.user = user;
    }

    public Alat getAlat() {
        return alat;
    }

    public void setAlat(Alat alat) {
        this.alat = alat;
    }

    public TransaksiSewa getSewa() {
        return sewa;
    }

    public void setSewa(TransaksiSewa sewa) {
        this.sewa = sewa;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public Timestamp getTanggal() {
        return tanggal;
    }

    public void setTanggal(Timestamp tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Perusahaan getCompany() {
        return company;
    }

    public void setCompany(Perusahaan company) {
        this.company = company;
    }

 

   
}
