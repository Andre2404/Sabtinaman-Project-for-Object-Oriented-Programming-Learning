package dao;

import model.TransaksiPupuk;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaksiPupukDAO {
    private Connection connection;
    private SaldoDAO saldoDAO; // Tambahkan DAO saldo agar transaksi berjalan lancar

    public TransaksiPupukDAO(Connection connection, SaldoDAO saldoDAO) {
        this.connection = connection;
        this.saldoDAO = saldoDAO;
    }

    // Menambahkan transaksi pupuk baru ke database
    public void addTransaksi(TransaksiPupuk transaksi) throws SQLException {
        String query = "INSERT INTO transaksi_pupuk (id_pengguna, id_pupuk, jumlah_kg, tanggal_beli) "
                     + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, transaksi.getIdPengguna());
            stmt.setInt(2, transaksi.getIdPupuk());
            stmt.setInt(3, transaksi.getJumlahKg());
            stmt.setDate(4, new Date(System.currentTimeMillis())); // Set tanggal saat ini
            stmt.executeUpdate();
        }
    }

    // Metode pembelian pupuk dengan transaksi SQL
    public void buyPupuk(int idPengguna, int idPupuk, int jumlahKg, double totalHarga, int idPerusahaan) throws SQLException {
        try {
            connection.setAutoCommit(false); // Mulai transaksi

            // 1. Kurangi saldo pengguna
            saldoDAO.updateSaldoPengguna(idPengguna, totalHarga);

            // 2. Tambah saldo perusahaan
            saldoDAO.updateSaldoPerusahaan(idPerusahaan, totalHarga);

            // 3. Tambahkan transaksi pembelian pupuk
            TransaksiPupuk transaksi = new TransaksiPupuk(0, idPengguna, idPupuk, jumlahKg, totalHarga);
            addTransaksi(transaksi);

            connection.commit(); // Commit transaksi jika berhasil
            System.out.println("Pembelian pupuk berhasil!");

        } catch (SQLException e) {
            connection.rollback(); // Rollback jika ada kesalahan
            throw e;
        } finally {
            connection.setAutoCommit(true); // Kembali ke autocommit default
        }
    }

    // Mengambil transaksi berdasarkan id pengguna
    public List<TransaksiPupuk> getTransaksiByUserId(int idPengguna) throws SQLException {
        String query = "SELECT * FROM transaksi_pupuk WHERE id_pengguna = ?";
        List<TransaksiPupuk> transaksiList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPengguna);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transaksiList.add(new TransaksiPupuk(
                    rs.getInt("id_transaksi"),
                    rs.getInt("id_pengguna"),
                    rs.getInt("id_pupuk"),
                    rs.getInt("jumlah_kg"),
                    rs.getDouble("total_harga"),
                    rs.getDate("tanggal_beli")
                ));
            }
        }
        return transaksiList;
    }
    public List<TransaksiPupuk> getTransaksiByCompanyId(int companyId) throws SQLException {
    // Query SQL untuk mengambil transaksi pupuk berdasarkan id perusahaan (companyId)
    String query = "SELECT * FROM transaksi_pupuk WHERE id_perusahaan = ?";
    
    // List untuk menampung hasil query sebagai objek TransaksiPupuk
    List<TransaksiPupuk> transaksiList = new ArrayList<>();
    
    // Menggunakan try-with-resources untuk memastikan PreparedStatement dan ResultSet ditutup setelah selesai digunakan
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        // Set nilai id_perusahaan ke dalam query
        stmt.setInt(1, companyId);
        
        // Eksekusi query dan simpan hasilnya dalam ResultSet
        ResultSet rs = stmt.executeQuery();
        
        // Loop melalui setiap baris dalam ResultSet dan tambahkan ke list transaksiList
        while (rs.next()) {
            // Buat objek TransaksiPupuk untuk setiap baris dalam ResultSet
            TransaksiPupuk transaksi = new TransaksiPupuk(
                rs.getInt("id_transaksi"),   // Ambil nilai id_transaksi
                rs.getInt("id_pengguna"),    // Ambil nilai id_pengguna
                rs.getInt("id_pupuk"),       // Ambil nilai id_pupuk
                rs.getInt("jumlah_kg"),      // Ambil nilai jumlah_kg
                rs.getDouble("total_harga"), // Ambil nilai total_harga
                rs.getDate("tanggal_beli")   // Ambil nilai tanggal_beli
            );
            
            // Tambahkan objek TransaksiPupuk ke dalam list
            transaksiList.add(transaksi);
        }
    }
    
    // Kembalikan list yang berisi semua transaksi yang ditemukan
    return transaksiList;
}

}
