/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rizar
 */
public class KategoriPupuk {
    private int idKategoriPupuk;
    private int idPupuk;
    private String namaKategori;
    private String deskripsi;

    public KategoriPupuk(int idKategoriPupuk, int idPupuk, String namaKategori, String deskripsi) {
        this.idKategoriPupuk = idKategoriPupuk;
        this.idPupuk = idPupuk;
        this.namaKategori = namaKategori;
        this.deskripsi = deskripsi;
    }

    public int getIdKategoriPupuk() { return idKategoriPupuk; }
    public void setIdKategoriPupuk(int idKategoriPupuk) { this.idKategoriPupuk = idKategoriPupuk; }

    public int getIdPupuk() { return idPupuk; }
    public void setIdPupuk(int idPupuk) { this.idPupuk = idPupuk; }

    public String getNamaKategori() { return namaKategori; }
    public void setNamaKategori(String namaKategori) { this.namaKategori = namaKategori; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
}

