/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rizar
 */
public class Pengguna {

    public static Pengguna login(String email, String password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    private int idPengguna;
    private String nama;
    private String alamat;
    private String email;
    private int nomorKontak;
    private int saldoPengguna;

    public Pengguna(int idPengguna, String nama, String alamat, String email, int nomorKontak, int saldoPengguna) {
        this.idPengguna = idPengguna;
        this.nama = nama;
        this.alamat = alamat;
        this.email = email;
        this.nomorKontak = nomorKontak;
        this.saldoPengguna = saldoPengguna;
    }

    public int getIdPengguna() { return idPengguna; }
    public void setIdPengguna(int idPengguna) { this.idPengguna = idPengguna; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getNomorKontak() { return nomorKontak; }
    public void setNomorKontak(int nomorKontak) { this.nomorKontak = nomorKontak; }

    public int getSaldoPengguna() { return saldoPengguna; }
    public void setSaldoPengguna(int saldoPengguna) { this.saldoPengguna = saldoPengguna; }

    public void saveToDatabase() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

