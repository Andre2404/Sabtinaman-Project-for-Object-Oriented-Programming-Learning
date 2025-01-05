package model;
import java.sql.Timestamp;
import java.util.List;

public class TransaksiSewa {
    private int idTransaksi;
    private Pengguna user;
    private Perusahaan company;
    private Alat alat;
    private int totalHarga;
    private String tipeSaldo;

    // Constructor
    public TransaksiSewa(int idTransaksi, Pengguna user, Perusahaan company, Alat alat, String tipeSaldo, int totalHarga) {
        this.idTransaksi = idTransaksi;
        this.user = user;
        this.company = company;
        this.alat = alat;
        this.tipeSaldo = tipeSaldo;
        this.totalHarga = totalHarga;
        
    }

    public Perusahaan getCompany() {
        return company;
    }

    public void setCompany(Perusahaan company) {
        this.company = company;
    }
    
    public int getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(int totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getTipeSaldo() {
        return tipeSaldo;
    }

    // Getters and Setters
    public void setTipeSaldo(String tipeSaldo) {
        this.tipeSaldo = tipeSaldo;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
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

    
}
