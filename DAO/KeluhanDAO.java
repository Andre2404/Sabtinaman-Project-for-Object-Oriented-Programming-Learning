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

    // Menambahkan keluhan baru ke database
    public void addKeluhan(Keluhan keluhan) throws SQLException {
        String query = "INSERT INTO keluhan (id_keluhan, id_pengguna, id_alat, deskripsi_masalah, tanggal_laporan, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, keluhan.getIdKeluhan());
            statement.setInt(2, keluhan.getIdPengguna());
            statement.setInt(3, keluhan.getIdAlat());
            statement.setString(4, keluhan.getDeskripsiMasalah());
            statement.setString(5, keluhan.getTanggalLaporan());
            statement.setString(6, keluhan.getStatus());
            statement.executeUpdate();
        }
    }

    // Mengambil keluhan berdasarkan ID
    public Keluhan getKeluhanById(int idKeluhan) throws SQLException {
        String query = "SELECT * FROM keluhan WHERE id_keluhan = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idKeluhan);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Keluhan(
                    resultSet.getInt("id_keluhan"),
                    resultSet.getInt("id_pengguna"),
                    resultSet.getInt("id_alat"),
                    resultSet.getString("deskripsi_masalah"),
                    resultSet.getString("tanggal_laporan"),
                    resultSet.getString("status")
                );
            }
        }
        return null;
    }

    // Mengambil semua keluhan dari database
    public List<Keluhan> getAllKeluhan() throws SQLException {
        List<Keluhan> keluhanList = new ArrayList<>();
        String query = "SELECT * FROM keluhan";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Keluhan keluhan = new Keluhan(
                    resultSet.getInt("id_keluhan"),
                    resultSet.getInt("id_pengguna"),
                    resultSet.getInt("id_alat"),
                    resultSet.getString("deskripsi_masalah"),
                    resultSet.getString("tanggal_laporan"),
                    resultSet.getString("status")
                );
                keluhanList.add(keluhan);
            }
        }
        return keluhanList;
    }

    // Mengupdate data keluhan
    public void updateKeluhan(Keluhan keluhan) throws SQLException {
        String query = "UPDATE keluhan SET id_pengguna = ?, id_alat = ?, deskripsi_masalah = ?, tanggal_laporan = ?, status = ? "
                + "WHERE id_keluhan = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, keluhan.getIdPengguna());
            statement.setInt(2, keluhan.getIdAlat());
            statement.setString(3, keluhan.getDeskripsiMasalah());
            statement.setString(4, keluhan.getTanggalLaporan());
            statement.setString(5, keluhan.getStatus());
            statement.setInt(6, keluhan.getIdKeluhan());
            statement.executeUpdate();
        }
    }

    // Menghapus keluhan berdasarkan ID
    public void deleteKeluhan(int idKeluhan) throws SQLException {
        String query = "DELETE FROM keluhan WHERE id_keluhan = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idKeluhan);
            statement.executeUpdate();
        }
    }
}
