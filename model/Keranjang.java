/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

public class Keranjang {
    
    private int idKeranjang;
    private int idTransaksi;
    private Alat alat;
    private Pupuk pupuk;
    private int jumlah;  // Kuantitas barang yang disewa
    private long durasi;  // Lama sewa (dalam hari)
    private int totalHarga;  // Total harga sewa
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembali;
    private Pengguna pengguna;
    private Perusahaan perusahaan;
    
    public Keranjang(int idKeranjang, int idTransaksi, Pengguna pengguna, Perusahaan perusahaan, Alat alat, int jumlah, long durasi, int totalHarga, LocalDate tanggalPinjam, LocalDate tanggalKembali){
        this.idKeranjang=idKeranjang;
        this.idTransaksi = idTransaksi;
        this.pengguna = pengguna;
        this.perusahaan = perusahaan;
        this.alat=alat;
        this.jumlah=jumlah;
        this.durasi=durasi;
        this.totalHarga=totalHarga;
        this.tanggalPinjam=tanggalPinjam;
        this.tanggalKembali=tanggalKembali;
    }   
    
    public Keranjang(Pengguna pengguna, Perusahaan perusahaan, Alat alat, int jumlah, long durasi, int totalHarga, LocalDate tanggalPinjam, LocalDate tanggalKembali){
        this.pengguna = pengguna;
        this.perusahaan = perusahaan;
        this.alat=alat;
        this.jumlah=jumlah;
        this.durasi=durasi;
        this.totalHarga=totalHarga;
        this.tanggalPinjam=tanggalPinjam;
        this.tanggalKembali=tanggalKembali;
    }   
    
    public Keranjang(int idKeranjang,  int idTransaksi, Pengguna pengguna, Perusahaan perusahaan, Pupuk pupuk, int jumlah, int totalHarga){
        this.idKeranjang=idKeranjang;
        this.idTransaksi = idTransaksi;
        this.pengguna = pengguna;
        this.perusahaan = perusahaan;
        this.pupuk=pupuk;
        this.jumlah=jumlah;
        this.totalHarga=totalHarga;
    }
    public Keranjang(Pengguna pengguna, Perusahaan perusahaan, Pupuk pupuk, int jumlah, int totalHarga){
        this.pengguna = pengguna;
        this.perusahaan = perusahaan;
        this.pupuk=pupuk;
        this.jumlah=jumlah;
        this.totalHarga=totalHarga;
    }   

    public Alat getAlat() {
        return alat;
    }

    public void setAlat(Alat alat) {
        this.alat = alat;
    }

    public Pupuk getPupuk() {
        return pupuk;
    }

    public void setPupuk(Pupuk pupuk) {
        this.pupuk = pupuk;
    }
    
    public int getIdKeranjang() {
        return idKeranjang;
    }

    public void setIdKeranjang(int idKeranjang) {
        this.idKeranjang = idKeranjang;
    }


    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public long getDurasi() {
        return durasi;
    }

    public void setDurasi(long durasi) {
        this.durasi = durasi;
    }

    public int getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(int totalHarga) {
        this.totalHarga = totalHarga;
    }

    public LocalDate getTanggalPinjam() {
        return tanggalPinjam;
    }

    public void setTanggalPinjam(LocalDate tanggalPinjam) {
        this.tanggalPinjam = tanggalPinjam;
    }

    public LocalDate getTanggalKembali() {
        return tanggalKembali;
    }

    public void setTanggalKembali(LocalDate tanggalKembali) {
        this.tanggalKembali = tanggalKembali;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }
    
    
}