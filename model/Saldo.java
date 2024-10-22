package model;

import java.sql.Timestamp;

public class Saldo {
    private int idSaldo;
    private int idPengguna;
    private int idPerusahaan;
    private double jumlah;
    private String tipeSaldo;
    private Timestamp tanggalTransaksi;

    public Saldo(int idSaldo, int idPengguna, int idPerusahaan, double jumlah, String tipeSaldo, Timestamp tanggalTransaksi) {
        this.idSaldo = idSaldo;
        this.idPengguna = idPengguna;
        this.idPerusahaan = idPerusahaan;
        this.jumlah = jumlah;
        this.tipeSaldo = tipeSaldo;
        this.tanggalTransaksi = tanggalTransaksi;
    }

    public int getIdSaldo() { return idSaldo; }
    public int getIdPengguna() { return idPengguna; }
    public int getIdPerusahaan() { return idPerusahaan; }
    public double getJumlah() { return jumlah; }
    public String getTipeSaldo() { return tipeSaldo; }
    public Timestamp getTanggalTransaksi() { return tanggalTransaksi; }
}
