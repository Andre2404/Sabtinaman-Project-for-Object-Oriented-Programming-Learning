/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rizar
 */
public class Perusahaan {

    public static Perusahaan login(String email, String password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    private int idPerusahaan;
    private String nama;
    private String alamat;
    private String email;
    private int nomorKontak;
    private int saldoPerusahaan;

    public Perusahaan(int idPerusahaan, String nama, String alamat, String email, int nomorKontak, int saldoPerusahaan) {
        this.idPerusahaan = idPerusahaan;
        this.nama = nama;
        this.alamat = alamat;
        this.email = email;
        this.nomorKontak = nomorKontak;
        this.saldoPerusahaan = saldoPerusahaan;
    }

    public int getIdPerusahaan() { return idPerusahaan; }
    public void setIdPerusahaan(int idPerusahaan) { this.idPerusahaan = idPerusahaan; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getNomorKontak() { return nomorKontak; }
    public void setNomorKontak(int nomorKontak) { this.nomorKontak = nomorKontak; }

    public int getSaldoPerusahaan() { return saldoPerusahaan; }
    public void setSaldoPerusahaan(int saldoPerusahaan) { this.saldoPerusahaan = saldoPerusahaan; }

    public void saveToDatabase() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

