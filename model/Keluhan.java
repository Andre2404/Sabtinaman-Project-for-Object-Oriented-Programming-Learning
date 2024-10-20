/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rizar
 */
public class Keluhan {
    private int idKeluhan;
    private int idPengguna;
    private int idAlat;
    private String deskripsiMasalah;
    private String tanggalLaporan;
    private String status;

    public Keluhan(int idKeluhan, int idPengguna, int idAlat, String deskripsiMasalah, String tanggalLaporan, String status) {
        this.idKeluhan = idKeluhan;
        this.idPengguna = idPengguna;
        this.idAlat = idAlat;
        this.deskripsiMasalah = deskripsiMasalah;
        this.tanggalLaporan = tanggalLaporan;
        this.status = status;
    }

    public int getIdKeluhan() { return idKeluhan; }
    public void setIdKeluhan(int idKeluhan) { this.idKeluhan = idKeluhan; }

    public int getIdPengguna() { return idPengguna; }
    public void setIdPengguna(int idPengguna) { this.idPengguna = idPengguna; }

    public int getIdAlat() { return idAlat; }
    public void setIdAlat(int idAlat) { this.idAlat = idAlat; }

    public String getDeskripsiMasalah() { return deskripsiMasalah; }
    public void setDeskripsiMasalah(String deskripsiMasalah) { this.deskripsiMasalah = deskripsiMasalah; }

    public String getTanggalLaporan() { return tanggalLaporan; }
    public void setTanggalLaporan(String tanggalLaporan) { this.tanggalLaporan = tanggalLaporan; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

