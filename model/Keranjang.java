/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

public class Keranjang {
    private int idKeranjang;
    private int idTransaksi;
    private String namaAlat;
    private String namaPupuk;
    private String jenisPupuk;
    private Alat alat;
    private Pupuk pupuk;
    private int jumlah;  // Kuantitas barang yang disewa
    private long durasi;  // Lama sewa (dalam hari)
    private int totalHarga;  // Total harga sewa
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembali;
    private Pengguna pengguna;
    private Perusahaan perusahaan;
    private String tipeSaldo;
    
    public Keranjang(int idKeranjang, String namaAlat, Alat alat, int jumlah, long durasi, int totalHarga, LocalDate tanggalPinjam, LocalDate tanggalKembali){
        this.idKeranjang=idKeranjang;
        this.idTransaksi = idTransaksi;
        this.pengguna = pengguna;
        this.perusahaan = perusahaan;
        this.namaAlat=namaAlat;
        this.alat=alat;
        this.jumlah=jumlah;
        this.durasi=durasi;
        this.totalHarga=totalHarga;
        this.tanggalPinjam=tanggalPinjam;
        this.tanggalKembali=tanggalKembali;
        this.tipeSaldo = tipeSaldo;
    }   
    
    public Keranjang(int idKeranjang, String namaPupuk,String jenisPupuk, Pupuk pupuk, int jumlah, int totalHarga){
        this.idKeranjang=idKeranjang;
        this.idTransaksi = idTransaksi;
        this.pengguna = pengguna;
        this.perusahaan = perusahaan;
        this.namaPupuk=namaPupuk;
        this.jenisPupuk=jenisPupuk;
        this.pupuk=pupuk;
        this.jumlah=jumlah;
        this.totalHarga=totalHarga;
        this.tipeSaldo = tipeSaldo;
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

    public String getNamaAlat() {
        return namaAlat;
    }

    public void setNamaAlat(String namaAlat) {
        this.namaAlat = namaAlat;
    }

    public String getNamaPupuk() {
        return namaPupuk;
    }

    public void setNamaPupuk(String namaPupuk) {
        this.namaPupuk = namaPupuk;
    }
    
    public String getJenisPupuk() {
        return jenisPupuk;
    }

    public void setJenisPupuk(String jenisPupuk) {
        this.jenisPupuk = jenisPupuk;
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