package DAO;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeluhanDAO {

    private Connection connection;
    private PenggunaDAO penggunaDAO;
    private AlatDAO alatDAO;
    private TransaksiSewaDAO transaksiSewaDAO;
    private PerusahaanDAO perusahaanDAO;

    public KeluhanDAO(Connection connection) {
        this.connection = connection;
    }
    
    public KeluhanDAO(Connection connection, PenggunaDAO penggunaDAO, AlatDAO alatDAO, TransaksiSewaDAO transaksiSewaDAO, PerusahaanDAO perusahaanDAO) {
        this.connection = connection;
        this.penggunaDAO = penggunaDAO;
        this.alatDAO = alatDAO;
        this.transaksiSewaDAO = transaksiSewaDAO;
        this.perusahaanDAO = perusahaanDAO;
    }

    public void addKeluhan(Keluhan keluhan) throws SQLException {
        String query = "INSERT INTO keluhan(id_pengguna, id_perusahaan, id_alat, id_transaksi, deskripsi_masalah, tanggal_laporan, status) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, keluhan.getUser().getIdPengguna());
            stmt.setInt(2, keluhan.getCompany().getIdPerusahaan());
            stmt.setInt(3, keluhan.getAlat().getIdAlat());
            stmt.setInt(4, keluhan.getSewa().getIdTransaksi());
            stmt.setString(5, keluhan.getDeskripsi());
            stmt.setTimestamp(6, keluhan.getTanggal());
            stmt.setString(7, keluhan.getStatus());
            stmt.executeUpdate();
        }
    }

    public boolean updateTanggapan(int idKeluhan, String tanggapan) {
        String query = "UPDATE keluhan SET tanggapan = ? WHERE id_keluhan = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tanggapan);
            stmt.setInt(2, idKeluhan);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(KeluhanDAO.class.getName()).log(Level.SEVERE, "Gagal memperbarui tanggapan", e);
            return false;
        }
    }

    public void laporkanKeluhan(Perusahaan perusahaan, Pengguna pengguna, Alat alat, TransaksiSewa transaksiSewa, String deskripsi) {
        try {
            Timestamp tanggalLaporan = new Timestamp(System.currentTimeMillis());
            Keluhan keluhan = new Keluhan(
                0,
                perusahaan,
                pengguna,
                alat,
                transaksiSewa,
                deskripsi,
                tanggalLaporan,
                "Belum Ditangani",
                null
            );
            addKeluhan(keluhan);
        } catch (SQLException ex) {
            Logger.getLogger(KeluhanDAO.class.getName()).log(Level.SEVERE, "Gagal melaporkan keluhan", ex);
        }
    }

    public List<Keluhan> getKeluhanByCompanyId(int idPerusahaan) throws SQLException {
        String query = "SELECT k.id_keluhan, k.id_pengguna, k.id_perusahaan, k.id_alat, k.id_transaksi, " +
                       "k.deskripsi_masalah, k.tanggal_laporan, k.status, k.tanggapan, " +
                       "p.nama, u.nama, a.nama_alat " +
                       "FROM keluhan k " +
                       "JOIN perusahaan p ON k.id_perusahaan = p.id_perusahaan " +
                       "JOIN pengguna u ON k.id_pengguna = u.id_pengguna " +
                       "JOIN alat a ON k.id_alat = a.id_alat " +
                       "WHERE k.id_perusahaan = ?";
        List<Keluhan> keluhanList = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPerusahaan);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
                Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(rs.getInt("id_perusahaan"));
                Alat alat = alatDAO.getAlatById(rs.getInt("id_alat"));
                TransaksiSewa transaksi = transaksiSewaDAO.getTransaksiById(rs.getInt("id_transaksi"));
                    Keluhan keluhan = new Keluhan(
                        rs.getInt("id_keluhan"),
                        perusahaan,
                        pengguna,
                        alat,
                        transaksi,
                        rs.getString("deskripsi_masalah"),
                        rs.getTimestamp("tanggal_laporan"),
                        rs.getString("status"),
                        rs.getString("tanggapan")
                    );

                    keluhanList.add(keluhan);
                }
            }
        }
        return keluhanList;
    }
}
