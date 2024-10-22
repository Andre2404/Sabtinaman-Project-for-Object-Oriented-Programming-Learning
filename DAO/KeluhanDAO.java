package dao;

import model.Keluhan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KeluhanDAO {
    private Connection connection;

    public KeluhanDAO(Connection connection) {
        this.connection = connection;
    }

    public void addKeluhan(Keluhan keluhan) throws SQLException {
        String query = "INSERT INTO Keluhan(id_pengguna, id_alat, id_transaksi_sewa, deskripsi_masalah, tanggal_laporan, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, keluhan.getIdPengguna());
            stmt.setInt(2, keluhan.getIdAlat());
            stmt.setInt(3, keluhan.getIdTransaksiSewa());
            stmt.setString(4, keluhan.getDeskripsi());
            stmt.setTimestamp(5, keluhan.getTanggal());
            stmt.setString(6, keluhan.getStatus());
            stmt.executeUpdate();
        }
    }

    public List<Keluhan> getKeluhanByCompanyId(int companyId) throws SQLException {
        String query = "SELECT * FROM Keluhan k JOIN Alat a ON k.id_alat = a.id_alat WHERE a.id_perusahaan = ?";
        List<Keluhan> keluhanList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                keluhanList.add(new Keluhan(rs.getInt("id_keluhan"),
                        rs.getInt("id_pengguna"),
                        rs.getInt("id_alat"),
                        rs.getInt("id_transaksi_sewa"),
                        rs.getString("deskripsi_masalah"),
                        rs.getTimestamp("tanggal_laporan"),
                        rs.getString("status")));
            }
        }
        return keluhanList;
    }
}
