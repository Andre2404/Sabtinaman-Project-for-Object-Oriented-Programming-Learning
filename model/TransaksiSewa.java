/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rizar
 */
public class TransaksiSewa {
    private int idTransaksi;
    private int idPengguna;
    private int idAlat;
    private int tanggalMulaiSewa;
    private int tanggalAkhirSewa;
    private String statusPembayaran;

    public TransaksiSewa(int idTransaksi, int idPengguna, int idAlat, int tanggalMulaiSewa, int tanggalAkhirSewa, String statusPembayaran) {
        this.idTransaksi = idTransaksi;
        this.idPengguna = idPengguna;
        this.idAlat = idAlat;
        this.tanggalMulaiSewa = tanggalMulaiSewa;
        this.tanggalAkhirSewa = tanggalAkhirSewa;
        this.statusPembayaran = statusPembayaran;
    }

    public int getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(int idTransaksi) { this.idTransaksi = idTransaksi; }

    public int getIdPengguna() { return idPengguna; }
    public void setIdPengguna(int idPengguna) { this.idPengguna = idPengguna; }

    public int getIdAlat() { return idAlat; }
    public void setIdAlat(int idAlat) { this.idAlat = idAlat; }

    public int getTanggalMulaiSewa() { return tanggalMulaiSewa; }
    public void setTanggalMulaiSewa(int tanggalMulaiSewa) { this.tanggalMulaiSewa = tanggalMulaiSewa; }

    public int getTanggalAkhirSewa() { return tanggalAkhirSewa; }
    public void setTanggalAkhirSewa(int tanggalAkhirSewa) { this.tanggalAkhirSewa = tanggalAkhirSewa; }

    public String getStatusPembayaran() { return statusPembayaran; }
    public void setStatusPembayaran(String statusPembayaran) { this.statusPembayaran = statusPembayaran; }
}
