package DAO;

import model.TransaksiPupuk;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import model.Keranjang;
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
        String query = "INSERT INTO transaksipupuk (id_pengguna, id_perusahaan, id_pupuk, jumlah_kg, total_harga, tanggal_beli, tipe_saldo) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            if (transaksi.getUser() != null) {
            stmt.setObject(1, transaksi.getUser().getIdPengguna());
        } else {
            stmt.setNull(1, Types.INTEGER); // Jika null, atur kolom menjadi NULL
        }
        // Periksa apakah company null
        if (transaksi.getCompany() != null) {
            stmt.setObject(2, transaksi.getCompany().getIdPerusahaan());
        } else {
            stmt.setNull(2, Types.INTEGER); // Jika null, atur kolom menjadi NULL
        }
        if (transaksi.getPupuk()!= null) {
            stmt.setObject(3, transaksi.getPupuk().getIdPupuk());
        } else {
            stmt.setNull(3, Types.INTEGER); // Jika null, atur kolom menjadi NULL
        }
            stmt.setInt(4, transaksi.getJumlahKg());
            stmt.setDouble(5, transaksi.getTotalHarga());
            stmt.setTimestamp(6, transaksi.getTanggalBeli()); // Set tanggal saat ini
            stmt.setString(7, transaksi.getTipeSaldo());
            stmt.executeUpdate();
        }
    }
    
    public void checkout(int idPengguna, String tipeSaldo, ObservableList<Keranjang> keranjangList) throws SQLException {
    String sqlInsertTransaksi = "INSERT INTO transaksipupuk (id_pengguna, total_harga, tipe_saldo) VALUES (?, ?, ?)";
    String sqlInsertDetailTransaksi = "INSERT INTO detail_transaksi (id_transaksi, id_pupuk, jumlah, total_harga) VALUES (?, ?, ?, ?)";

    try (PreparedStatement psTransaksi = connection.prepareStatement(sqlInsertTransaksi, Statement.RETURN_GENERATED_KEYS);
         PreparedStatement psDetailTransaksi = connection.prepareStatement(sqlInsertDetailTransaksi)) {

        // Hitung total harga seluruh keranjang
        int totalHarga = keranjangList.stream().mapToInt(Keranjang::getTotalHarga).sum();

        // Simpan transaksi utama
        psTransaksi.setInt(1, idPengguna);
        psTransaksi.setInt(2, totalHarga);
        psTransaksi.setString(3, tipeSaldo);
        
        psTransaksi.executeUpdate();

        // Ambil ID transaksi yang baru dibuat
        ResultSet rs = psTransaksi.getGeneratedKeys();
        if (!rs.next()) {
            throw new SQLException("Gagal mendapatkan ID transaksi.");
        }
        int idTransaksi = rs.getInt(1);

        // Simpan detail transaksi untuk setiap item di keranjang
        for (Keranjang item : keranjangList) {
            psDetailTransaksi.setInt(1, idTransaksi);
            psDetailTransaksi.setInt(2, item.getIdPupuk());
            psDetailTransaksi.setInt(3, item.getJumlah());
            psDetailTransaksi.setInt(4, item.getTotalHarga());
            psDetailTransaksi.executeUpdate();
        }
    }
}
    
    public void checkout(int idPengguna, ObservableList<Keranjang> keranjangList) throws SQLException {
    String sqlInsertDetailTransaksi = "INSERT INTO detail_transaksi (id_pupuk, nama_pupuk, jenis_pupuk, jumlah, total_harga) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement psDetailTransaksi = connection.prepareStatement(sqlInsertDetailTransaksi)) {

        // Simpan detail transaksi untuk setiap item di keranjang
        for (Keranjang item : keranjangList) {
            psDetailTransaksi.setInt(1, item.getIdPupuk()); // ID pupuk
            psDetailTransaksi.setString(2, item.getNamaPupuk()); // Nama pupuk
            psDetailTransaksi.setString(3, item.getJenisPupuk()); // Jenis pupuk
            psDetailTransaksi.setInt(4, item.getJumlah()); // Kuantitas
            psDetailTransaksi.setInt(5, item.getTotalHarga()); // Total harga
            psDetailTransaksi.executeUpdate(); // Eksekusi pernyataan untuk menyimpan detail
        }
    }
}
   
    // Metode pembelian pupuk dengan transaksi SQL
   public void buyPupuk(Pengguna pengguna, Pupuk pupuk, int jumlahKg, int totalHarga, Perusahaan perusahaan) throws SQLException {
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
        int saldoPengguna = penggunaDAO.getSaldoPengguna(pengguna.getIdPengguna());
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
        Timestamp tanggalBeli = new Timestamp(System.currentTimeMillis());
        TransaksiPupuk transaksi = new TransaksiPupuk(0,
        pengguna,// ID transaksi baru
        null,
        pupuk,
        jumlahKg, // Pastikan ini tipe int
        totalHarga, // Pastikan ini tipe double
        tanggalBeli,
        "debit"
);

        addTransaksi(transaksi);
        
        TransaksiPupuk transaksi2 = new TransaksiPupuk(0,
        null,// ID transaksi baru
        perusahaan,
        pupuk,
        jumlahKg, // Pastikan ini tipe int
        totalHarga, // Pastikan ini tipe double
        tanggalBeli,
        "kredit"
);

        addTransaksi(transaksi2);
        pupukDAO.updateStatusPupuk(pupuk.getIdPupuk(), "unavailable");
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
                Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(rs.getInt("id_perusahaan"));
                Pupuk pupuk = pupukDAO.getPupukById(rs.getInt("id_pupuk"));
                transaksiList.add(new TransaksiPupuk(
                    rs.getInt("id_transaksi"),
                    pengguna,
                    perusahaan,
                    pupuk,
                    rs.getInt("jumlah_kg"),
                    rs.getDouble("total_harga"),
                    rs.getTimestamp("tanggal_beli"),
                    rs.getString("tipe_saldo")
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
                Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(rs.getInt("id_perusahaan"));
                Pupuk pupuk = pupukDAO.getPupukById(rs.getInt("id_pupuk"));
                transaksiList.add(new TransaksiPupuk(
                    rs.getInt("id_transaksi"),
                    pengguna,
                    perusahaan,
                    pupuk,
                    rs.getInt("jumlah_kg"),
                    rs.getDouble("total_harga"),
                    rs.getTimestamp("tanggal_beli"),
                    rs.getString("tipe_saldo"))   
            );
        }
    }
    
    // Kembalikan list yang berisi semua transaksi yang ditemukan
    return transaksiList;
}

}
