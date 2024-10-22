package model;

import java.sql.Date;

public class TransaksiPupuk {
    private int idTransaksi;
    private int idPengguna;
    private int idPupuk;
    private int jumlahKg;
    private double totalHarga;
    private Date tanggalBeli;

    // Constructor
    public TransaksiPupuk(int idTransaksi, int idPengguna, int idPupuk, int jumlahKg, double totalHarga, Date tanggalBeli) {
        this.idTransaksi = idTransaksi;
        this.idPengguna = idPengguna;
        this.idPupuk = idPupuk;
        this.jumlahKg = jumlahKg;
        this.totalHarga = totalHarga;
        this.tanggalBeli = tanggalBeli;
    }

    public TransaksiPupuk(int i, int currentUserId, int idPupuk, int jumlahKg, double totalHarga) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Getter dan Setter
    public int getIdTransaksi() {
        return idTransaksi;
    }

    public int getIdPengguna() {
        return idPengguna;
    }

    public int getIdPupuk() {
        return idPupuk;
    }

    public int getJumlahKg() {
        return jumlahKg;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public Date getTanggalBeli() {
        return tanggalBeli;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public void setIdPengguna(int idPengguna) {
        this.idPengguna = idPengguna;
    }

    public void setIdPupuk(int idPupuk) {
        this.idPupuk = idPupuk;
    }

    public void setJumlahKg(int jumlahKg) {
        this.jumlahKg = jumlahKg;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public void setTanggalBeli(Date tanggalBeli) {
        this.tanggalBeli = tanggalBeli;
    }
}
