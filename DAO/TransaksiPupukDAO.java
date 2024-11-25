package dao;

import model.TransaksiPupuk;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.sql.Date;
import java.time.LocalDate;
import model.Pengguna;
import model.Perusahaan;
import model.Pupuk;

public class TransaksiPupukDAO {
    private Connection connection;
    private SaldoDAO saldoDAO; // Tambahkan DAO saldo agar transaksi berjalan lancar
    private PenggunaDAO penggunaDAO;
    private PupukDAO pupukDAO;
    private PerusahaanDAO perusahaanDAO;
    
    public TransaksiPupukDAO(Connection connection, SaldoDAO saldoDAO) {
        this.connection = connection;
        this.saldoDAO = saldoDAO;

    }
    
    public TransaksiPupukDAO(Connection connection, SaldoDAO saldoDAO, PenggunaDAO penggunaDAO, PupukDAO pupukDAO, PerusahaanDAO perusahaanDAO) {
        this.connection = connection;
        this.saldoDAO = saldoDAO;
        this.penggunaDAO = penggunaDAO;
        this.pupukDAO = pupukDAO;
        this.perusahaanDAO = perusahaanDAO;
    }
    // Menambahkan transaksi pupuk baru ke database
    public void addTransaksi(TransaksiPupuk transaksi) throws SQLException {
        String query = "INSERT INTO transaksipupuk (id_pengguna, id_pupuk, jumlah_kg, total_harga, tanggal_beli) "
                     + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, transaksi.getUser().getIdPengguna());
            stmt.setInt(2, transaksi.getPupuk().getIdPupuk());
            stmt.setInt(3, transaksi.getJumlahKg());
            stmt.setDouble(4, transaksi.getTotalHarga());
            stmt.setDate(5, java.sql.Date.valueOf(transaksi.getTanggalBeli())); // Set tanggal saat ini
            stmt.executeUpdate();
        }
    }

    // Metode pembelian pupuk dengan transaksi SQL
   public void buyPupuk(Pengguna pengguna, Pupuk pupuk, int jumlahKg, double totalHarga, Perusahaan perusahaan) throws SQLException {
    try {
        // Validasi parameter
        if (pengguna == null || pupuk == null || perusahaan == null) {
            throw new IllegalArgumentException("Pengguna, pupuk, atau perusahaan tidak boleh null.");
        }
        if (jumlahKg <= 0) {
            throw new IllegalArgumentException("Jumlah kilogram harus lebih besar dari nol.");
        }
        if (totalHarga <= 0) {
            throw new IllegalArgumentException("Total harga harus positif.");
        }

        // Pastikan saldo cukup
        double saldoPengguna = saldoDAO.getSaldoByUserId(pengguna.getIdPengguna());
        if (saldoPengguna < totalHarga) {
            throw new SQLException("Saldo pengguna tidak mencukupi untuk pembelian.");
        }

        connection.setAutoCommit(false); // Mulai transaksi

        // 1. Kurangi saldo pengguna
        saldoDAO.updateSaldoPengguna(pengguna.getIdPengguna(), totalHarga);
        System.out.println("Saldo pengguna berhasil diperbarui.");

        // 2. Tambah saldo perusahaan
        saldoDAO.updateSaldoPerusahaan(perusahaan.getIdPerusahaan(), totalHarga);
        System.out.println("Saldo perusahaan berhasil diperbarui.");

        // 3. Tambahkan transaksi pembelian pupuk
        LocalDate tanggalBeli = LocalDate.now();
        TransaksiPupuk transaksi = new TransaksiPupuk(0,
        pengguna,// ID transaksi baru
        pupuk,
        jumlahKg, // Pastikan ini tipe int
        totalHarga, // Pastikan ini tipe double
        tanggalBeli
);

        addTransaksi(transaksi);
        System.out.println("Transaksi berhasil ditambahkan ke database.");

        connection.commit(); // Commit transaksi jika berhasil
        System.out.println("Pembelian pupuk berhasil!");

    } catch (SQLException e) {
        connection.rollback(); // Rollback jika ada kesalahan
        System.err.println("Pembelian pupuk gagal: " + e.getMessage());
        e.printStackTrace();
        throw e;
    } finally {
        connection.setAutoCommit(true); // Kembali ke autocommit default
    }
}




    // Mengambil transaksi berdasarkan id pengguna
    public List<TransaksiPupuk> getTransaksiByUserId(int idPengguna) throws SQLException {
        String query = "SELECT * FROM transaksipupuk WHERE id_pengguna = ?";
        List<TransaksiPupuk> transaksiList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPengguna);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
                Pupuk pupuk = pupukDAO.getPupukById(rs.getInt("id_pupuk"));
                transaksiList.add(new TransaksiPupuk(
                    rs.getInt("id_transaksi"),
                    pengguna,
                    pupuk,
                    rs.getInt("jumlah_kg"),
                    rs.getDouble("total_harga"),
                    rs.getDate("tanggal_beli").toLocalDate()
                ));
            }
        }
        return transaksiList;
    }
    public List<TransaksiPupuk> getTransaksiByCompanyId(int idPerusahaan) throws SQLException {
    // Query SQL untuk mengambil transaksi pupuk berdasarkan id perusahaan (companyId)
    String query = "SELECT * FROM transaksipupuk WHERE id_perusahaan = ?";
    
    // List untuk menampung hasil query sebagai objek TransaksiPupuk
    List<TransaksiPupuk> transaksiList = new ArrayList<>();
    
    // Menggunakan try-with-resources untuk memastikan PreparedStatement dan ResultSet ditutup setelah selesai digunakan
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        // Set nilai id_perusahaan ke dalam query
        stmt.setInt(1, idPerusahaan);
        
        // Eksekusi query dan simpan hasilnya dalam ResultSet
        ResultSet rs = stmt.executeQuery();
        
        // Loop melalui setiap baris dalam ResultSet dan tambahkan ke list transaksiList
        while (rs.next()) {
            // Buat objek TransaksiPupuk untuk setiap baris dalam ResultSet
            Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
                Pupuk pupuk = pupukDAO.getPupukById(rs.getInt("id_pupuk"));
                transaksiList.add(new TransaksiPupuk(
                    rs.getInt("id_transaksi"),
                    pengguna,
                    pupuk,
                    rs.getInt("jumlah_kg"),
                    rs.getDouble("total_harga"),
                    rs.getDate("tanggal_beli").toLocalDate())   // Ambil nilai tanggal_beli
            );
        }
    }
    
    // Kembalikan list yang berisi semua transaksi yang ditemukan
    return transaksiList;
}

}
