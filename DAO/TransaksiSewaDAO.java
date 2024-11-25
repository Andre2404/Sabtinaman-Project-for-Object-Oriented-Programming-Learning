package dao;

import model.TransaksiSewa;
import model.Pengguna;
import model.Alat;
import model.Perusahaan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaksiSewaDAO {
    private Connection connection;
    private SaldoDAO saldoDAO;
    private PenggunaDAO penggunaDAO;
    private AlatDAO alatDAO;
    private PerusahaanDAO perusahaanDAO;

    public TransaksiSewaDAO(Connection connection, SaldoDAO saldoDAO, PenggunaDAO penggunaDAO, AlatDAO alatDAO, PerusahaanDAO perusahaanDAO) {
        this.connection = connection;
        this.saldoDAO = saldoDAO;
        this.penggunaDAO = penggunaDAO;
        this.alatDAO = alatDAO;
        this.perusahaanDAO = perusahaanDAO;
    }

    // Menambah transaksi sewa alat baru ke database
    public void addTransaksi(TransaksiSewa transaksi) throws SQLException {
    String query = "INSERT INTO transaksisewa (id_pengguna, id_alat, tanggal_sewa) VALUES (?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, transaksi.getUser().getIdPengguna());
        stmt.setInt(2, transaksi.getAlat().getIdAlat());
        stmt.setTimestamp(3, transaksi.getTanggalSewa()); // Konversi LocalDateTime ke Timestamp
        stmt.executeUpdate();
    }
}


    // Melakukan transaksi sewa alat dengan validasi saldo
    public void sewaAlat(Pengguna pengguna, Alat alat, Perusahaan perusahaan) throws SQLException {
        try {
            // Validasi parameter
            if (pengguna == null || alat == null || perusahaan == null) {
                throw new IllegalArgumentException("Pengguna, alat, atau perusahaan tidak boleh null.");
            }

            // Pastikan saldo cukup
            double saldoPengguna = saldoDAO.getSaldoByUserId(pengguna.getIdPengguna());
            if (saldoPengguna < alat.getHargaSewa()) {
                throw new SQLException("Saldo pengguna tidak mencukupi untuk menyewa alat.");
            }

            connection.setAutoCommit(false); // Mulai transaksi

            // 1. Kurangi saldo pengguna
            saldoDAO.updateSaldoPengguna(pengguna.getIdPengguna(), alat.getHargaSewa());
            System.out.println("Saldo pengguna berhasil diperbarui.");

            // 2. Tambah saldo perusahaan
            saldoDAO.updateSaldoPerusahaan(perusahaan.getIdPerusahaan(), alat.getHargaSewa());
            System.out.println("Saldo perusahaan berhasil diperbarui.");

            // 3. Tambahkan transaksi sewa alat
            Timestamp tanggalSewa = new Timestamp(System.currentTimeMillis());
            TransaksiSewa transaksi = new TransaksiSewa(0, pengguna, alat, tanggalSewa);

            addTransaksi(transaksi);
            System.out.println("Transaksi sewa berhasil ditambahkan ke database.");

            connection.commit(); // Commit transaksi jika berhasil
            System.out.println("Sewa alat berhasil!");

        } catch (SQLException e) {
            connection.rollback(); // Rollback jika ada kesalahan
            System.err.println("Sewa alat gagal: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            connection.setAutoCommit(true); // Kembali ke autocommit default
        }
    }

    // Mengambil transaksi sewa berdasarkan id pengguna
    public List<TransaksiSewa> getTransaksiByUserId(int idPengguna) throws SQLException {
        String query = "SELECT ts.* FROM transaksisewa ts JOIN alat a ON ts.id_alat = a.id_alat WHERE ts.id_pengguna = ?";
        List<TransaksiSewa> transaksiList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPengguna);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
                Alat alat = alatDAO.getAlatById(rs.getInt("id_alat"));
                transaksiList.add(new TransaksiSewa(
                    rs.getInt("id_transaksi"),
                    pengguna,
                    alat,
                    rs.getTimestamp("tanggal_sewa")
                ));
            }
        }
        return transaksiList;
    }

    // Mengambil transaksi sewa berdasarkan id perusahaan
    public List<TransaksiSewa> getTransaksiByCompanyId(int idPerusahaan) throws SQLException {
        String query = "SELECT ts.* FROM transaksisewa ts JOIN alat a ON ts.id_alat = a.id_alat WHERE a.id_perusahaan = ?";
        List<TransaksiSewa> transaksiList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPerusahaan);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
                Alat alat = alatDAO.getAlatById(rs.getInt("id_alat"));
                transaksiList.add(new TransaksiSewa(
                    rs.getInt("id_transaksi"),
                    pengguna,
                    alat,
                    rs.getTimestamp("tanggal_sewa")
                ));
            }
        }
        return transaksiList;
    }
}
