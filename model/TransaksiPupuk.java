/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rizar
 */
public class TransaksiPupuk {
    private int idTransaksi;
    private int idPengguna;
    private int idPupuk;
    private int tanggalMulaiSewa;
    private int tanggalAkhirSewa;
    private String statusPembayaran;

    public TransaksiPupuk(int idTransaksi, int idPengguna, int idPupuk, int tanggalMulaiSewa, int tanggalAkhirSewa, String statusPembayaran) {
        this.idTransaksi = idTransaksi;
        this.idPengguna = idPengguna;
        this.idPupuk = idPupuk;
        this.tanggalMulaiSewa = tanggalMulaiSewa;
        this.tanggalAkhirSewa = tanggalAkhirSewa;
        this.statusPembayaran = statusPembayaran;
    }

    public int getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(int idTransaksi) { this.idTransaksi = idTransaksi; }

    public int getIdPengguna() { return idPengguna; }
    public void setIdPengguna(int idPengguna) { this.idPengguna = idPengguna; }

    public int getIdPupuk() { return idPupuk; }
    public void setIdPupuk(int idPupuk) { this.idPupuk = idPupuk; }

    public int getTanggalMulaiSewa() { return tanggalMulaiSewa; }
    public void setTanggalMulaiSewa(int tanggalMulaiSewa) { this.tanggalMulaiSewa = tanggalMulaiSewa; }

    public int getTanggalAkhirSewa() { return tanggalAkhirSewa; }
    public void setTanggalAkhirSewa(int tanggalAkhirSewa) { this.tanggalAkhirSewa = tanggalAkhirSewa; }

    public String getStatusPembayaran() { return statusPembayaran; }
    public void setStatusPembayaran(String statusPembayaran) { this.statusPembayaran = statusPembayaran; }
}
