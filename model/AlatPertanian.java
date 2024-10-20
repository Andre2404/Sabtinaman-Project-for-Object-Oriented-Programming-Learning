/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rizar
 */public class AlatPertanian {
    private int idAlat;
    private String namaAlat;
    private String kategori;
    private String spesifikasi;
    private int hargaSewa;
    private String statusKetersediaan;
    private String gambar;

    public AlatPertanian(int idAlat, String namaAlat, String kategori, String spesifikasi, int hargaSewa, String statusKetersediaan, String gambar) {
        this.idAlat = idAlat;
        this.namaAlat = namaAlat;
        this.kategori = kategori;
        this.spesifikasi = spesifikasi;
        this.hargaSewa = hargaSewa;
        this.statusKetersediaan = statusKetersediaan;
        this.gambar = gambar;
    }

    public int getIdAlat() { return idAlat; }
    public void setIdAlat(int idAlat) { this.idAlat = idAlat; }

    public String getNamaAlat() { return namaAlat; }
    public void setNamaAlat(String namaAlat) { this.namaAlat = namaAlat; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public String getSpesifikasi() { return spesifikasi; }
    public void setSpesifikasi(String spesifikasi) { this.spesifikasi = spesifikasi; }

    public int getHargaSewa() { return hargaSewa; }
    public void setHargaSewa(int hargaSewa) { this.hargaSewa = hargaSewa; }

    public String getStatusKetersediaan() { return statusKetersediaan; }
    public void setStatusKetersediaan(String statusKetersediaan) { this.statusKetersediaan = statusKetersediaan; }

    public String getGambar() { return gambar; }
    public void setGambar(String gambar) { this.gambar = gambar; }
}

