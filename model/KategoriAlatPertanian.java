/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rizar
 */
public class KategoriAlatPertanian {
    private int idKategoriAlat;
    private int idAlatPertanian;
    private String namaKategori;
    private String deskripsi;

    public KategoriAlatPertanian(int idKategoriAlat, int idAlatPertanian, String namaKategori, String deskripsi) {
        this.idKategoriAlat = idKategoriAlat;
        this.idAlatPertanian = idAlatPertanian;
        this.namaKategori = namaKategori;
        this.deskripsi = deskripsi;
    }

    public int getIdKategoriAlat() { return idKategoriAlat; }
    public void setIdKategoriAlat(int idKategoriAlat) { this.idKategoriAlat = idKategoriAlat; }

    public int getIdAlatPertanian() { return idAlatPertanian; }
    public void setIdAlatPertanian(int idAlatPertanian) { this.idAlatPertanian = idAlatPertanian; }

    public String getNamaKategori() { return namaKategori; }
    public void setNamaKategori(String namaKategori) { this.namaKategori = namaKategori; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
}
