package model;

import java.sql.Timestamp;

public class Keluhan {
    private int idKeluhan;
    private int idPengguna;
    private int idAlat;
    private int idTransaksiSewa;
    private String deskripsi;
    private Timestamp tanggal;
    private String status;

    public Keluhan(int idKeluhan, int idPengguna, int idAlat, int idTransaksiSewa, String deskripsi, Timestamp tanggal, String status) {
        this.idKeluhan = idKeluhan;
        this.idPengguna = idPengguna;
        this.idAlat = idAlat;
        this.idTransaksiSewa = idTransaksiSewa;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
        this.status = status;
    }

    public int getIdKeluhan() { return idKeluhan; }
    public int getIdPengguna() { return idPengguna; }
    public int getIdAlat() { return idAlat; }
    public int getIdTransaksiSewa() { return idTransaksiSewa; }
    public String getDeskripsi() { return deskripsi; }
    public Timestamp getTanggal() { return tanggal; }
    public String getStatus() { return status; }
}
