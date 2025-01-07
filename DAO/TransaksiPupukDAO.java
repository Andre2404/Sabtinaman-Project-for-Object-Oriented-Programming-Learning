package DAO;

import model.TransaksiPupuk;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
        String query = "INSERT INTO transaksipupuk (id_pengguna, total_harga, tanggal_beli) "
                     + "VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            if (transaksi.getUser() != null) {
                stmt.setInt(1, transaksi.getUser().getIdPengguna());
            } else {
                stmt.setNull(1, Types.INTEGER); // Jika null, atur kolom menjadi NULL
            }
            stmt.setInt(2, transaksi.getTotalHarga());
            stmt.setTimestamp(3, transaksi.getTanggalBeli());
            stmt.executeUpdate();
        }
    }

    // Proses checkout
    public void checkout(int idPengguna, ObservableList<Keranjang> keranjangList) throws SQLException {
        String sqlInsertTransaksi = "INSERT INTO transaksipupuk (id_pengguna, total_harga, tanggal_beli) VALUES (?, ?, ?)";
        String sqlInsertDetailTransaksi = "INSERT INTO detail_transaksi (id_transaksi, id_pupuk, jumlah, total_harga) VALUES (?, ?, ?, ?)";

        try (PreparedStatement psTransaksi = connection.prepareStatement(sqlInsertTransaksi, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psDetailTransaksi = connection.prepareStatement(sqlInsertDetailTransaksi)) {

            // Hitung total harga seluruh keranjang
            int totalHarga = keranjangList.stream().mapToInt(Keranjang::getTotalHarga).sum();
            int saldoPengguna = penggunaDAO.getSaldoPengguna(idPengguna);
            if (saldoPengguna < totalHarga) {
            throw new SQLException("Saldo pengguna tidak mencukupi untuk pembelian.");
        }

        connection.setAutoCommit(false); // Mulai transaksi

        // 1. Kurangi saldo pengguna
        saldoDAO.updateSaldoPengguna(idPengguna, totalHarga);
        System.out.println("Saldo pengguna berhasil diperbarui.");

        // 2. Tambah saldo perusahaan
//        saldoDAO.updateSaldoPerusahaan(perusahaan.getIdPerusahaan(), totalHarga);
//        System.out.println("Saldo perusahaan berhasil diperbarui.");

            // Simpan transaksi utama
            psTransaksi.setInt(1, idPengguna);
            psTransaksi.setInt(2, totalHarga);
            psTransaksi.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
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
                psDetailTransaksi.setInt(2, item.getPupuk().getIdPupuk());
                psDetailTransaksi.setInt(3, item.getJumlah());
                psDetailTransaksi.setInt(4, item.getTotalHarga());
                psDetailTransaksi.executeUpdate();
            }
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
                transaksiList.add(new TransaksiPupuk(
                    rs.getInt("id_transaksi"),
                    pengguna,
                    rs.getInt("total_harga"),
                    rs.getTimestamp("tanggal_beli")
                ));
            }
        }
        return transaksiList;
    }

    // Mengambil transaksi berdasarkan id transaksi
    public TransaksiPupuk getTransaksiById(int idTransaksi) throws SQLException {
        String query = "SELECT * FROM transaksipupuk WHERE id_transaksi = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idTransaksi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
                return new TransaksiPupuk(
                    rs.getInt("id_transaksi"),
                    pengguna,
                    rs.getInt("total_harga"),
                    rs.getTimestamp("tanggal_beli")
                );
            }
        }
        return null; // Jika tidak ditemukan
    }
}
