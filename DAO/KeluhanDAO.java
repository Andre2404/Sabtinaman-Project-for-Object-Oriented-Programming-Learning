package DAO;

import model.Keluhan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import model.Perusahaan;
import model.Alat;
import model.TransaksiSewa;
import model.Pengguna;

public class KeluhanDAO {
    private Connection connection;
    private PenggunaDAO penggunaDAO;
    private AlatDAO alatDAO;
    private TransaksiSewaDAO transaksiSewaDAO;

    public KeluhanDAO(Connection connection) {
        this.connection = connection;
    }
    
    public KeluhanDAO(Connection connection, PenggunaDAO penggunaDAO, AlatDAO alatDAO, TransaksiSewaDAO transaksiSewaDAO) {
        this.connection = connection;
        this.penggunaDAO = penggunaDAO;
        this.alatDAO = alatDAO;
        this.transaksiSewaDAO = transaksiSewaDAO;
    }

    public void addKeluhan(Keluhan keluhan) throws SQLException {
        String query = "INSERT INTO Keluhan(id_pengguna, id_perusahaan, id_alat, id_transaksi_sewa, deskripsi_masalah, tanggal_laporan, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, keluhan.getUser().getIdPengguna());
            stmt.setObject(1, keluhan.getCompany().getIdPerusahaan());
            stmt.setObject(2, keluhan.getAlat().getIdAlat());
            stmt.setObject(3, keluhan.getSewa().getIdTransaksi());
            stmt.setString(4, keluhan.getDeskripsi());
            stmt.setTimestamp(5, keluhan.getTanggal());
            stmt.setString(6, keluhan.getStatus());
            stmt.executeUpdate();
        }
    }
public List<Keluhan> getKeluhanByCompanyId(int idPerusahaan) throws SQLException {
    String query = "SELECT * FROM Keluhan k JOIN Alat a ON k.id_alat = a.id_alat WHERE a.id_perusahaan = ?";
    List<Keluhan> keluhanList = new ArrayList<>();
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, idPerusahaan);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
            Alat alat = alatDAO.getAlatById(rs.getInt("id_alat"));
            List<TransaksiSewa> transaksiList = transaksiSewaDAO.getTransaksiByUserId(rs.getInt("id_transaksi"));
            TransaksiSewa sewa = transaksiList.isEmpty() ? null : transaksiList.get(0);  // Ambil transaksi pertama jika ada
            Keluhan keluhan = new Keluhan(
                rs.getInt("id_keluhan"),
                pengguna,
                alat,
                sewa,
                rs.getString("deskripsi"),
                rs.getTimestamp("tanggal"),
                rs.getString("status")
            );
            keluhanList.add(keluhan);
        }
    }
    return keluhanList;
}

}


