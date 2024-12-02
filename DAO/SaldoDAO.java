package DAO;

import model.Saldo;
import java.sql.*;
import model.Pengguna;
import model.Perusahaan;

public class SaldoDAO {
    private final Connection connection;

    public SaldoDAO(Connection connection) {
        this.connection = connection;
    }

    // Menambah transaksi saldo baru
    public void addSaldo(Saldo saldo) throws SQLException {
        String query = "INSERT INTO saldo (id_pengguna, id_perusahaan, jumlah, tipe_saldo, tanggal_transaksi) "
                     + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, saldo.getUser().getIdPengguna());
            stmt.setObject(2, saldo.getCompany().getIdPerusahaan());
            stmt.setDouble(3, saldo.getJumlah());
            stmt.setString(4, saldo.getTipeSaldo());
            stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis())); // tanggal transaksi saat ini
            stmt.executeUpdate();
        }
    }

    // Mengurangi saldo pengguna
    public void updateSaldoPengguna(int idPengguna, double jumlah) throws SQLException {
    String query = "UPDATE saldo SET jumlah = jumlah - ? WHERE id_pengguna = ? AND tipe_saldo = 'debit'";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setDouble(1, jumlah);
        stmt.setInt(2, idPengguna);
        stmt.executeUpdate();
    }
}


    // Menambah saldo ke perusahaan
    public void updateSaldoPerusahaan(int idPerusahaan, double jumlah) throws SQLException {
        String query = "UPDATE saldo SET jumlah = jumlah + ? WHERE id_perusahaan = ? AND tipe_saldo = 'kredit'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, jumlah);
            stmt.setInt(2, idPerusahaan);
            stmt.executeUpdate();
        }
    }

    // Mengambil saldo berdasarkan id pengguna
    public double getSaldoByUserId(int idPengguna) throws SQLException {
        String query = "SELECT jumlah FROM saldo WHERE id_pengguna = ? AND tipe_saldo = 'debit'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(2, idPengguna);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("jumlah");
            }
        }
        return 0;
    }
}
