/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rizar
 */
public class Pupuk {
    private int idPupuk;
    private String namaPupuk;
    private String kategori;
    private int harga;
    private int stok;

    public Pupuk(int idPupuk, String namaPupuk, String kategori, int harga, int stok) {
        this.idPupuk = idPupuk;
        this.namaPupuk = namaPupuk;
        this.kategori = kategori;
        this.harga = harga;
        this.stok = stok;
    }

    public int getIdPupuk() { return idPupuk; }
    public void setIdPupuk(int idPupuk) { this.idPupuk = idPupuk; }

    public String getNamaPupuk() { return namaPupuk; }
    public void setNamaPupuk(String namaPupuk) { this.namaPupuk = namaPupuk; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public int getHarga() { return harga; }
    public void setHarga(int harga) { this.harga = harga; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
}

