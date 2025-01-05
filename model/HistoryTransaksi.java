/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

/**
 *
 * @author User
 */
public class HistoryTransaksi {
    private int id;
    private String jenis;
    private int jumlah;
    private String keterangan;
    private LocalDate tanggal;
    private String tipeSaldo;

    public HistoryTransaksi(int id, String jenis, int jumlah, String keterangan, LocalDate tanggal, String tipeSaldo) {
        this.id = id;
        this.jenis = jenis;
        this.jumlah = jumlah;
        this.keterangan = keterangan;
        this.tanggal = tanggal;
        this.tipeSaldo = tipeSaldo;
    }

    public String getTipeSaldo() {
        return tipeSaldo;
    }

    public void setTipeSaldo(String tipeSaldo) {
        this.tipeSaldo = tipeSaldo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis= jenis;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    
}

