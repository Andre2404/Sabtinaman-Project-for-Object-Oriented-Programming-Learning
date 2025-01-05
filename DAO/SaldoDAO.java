package DAO;

import model.Saldo;
import java.sql.*;

public class SaldoDAO {
    private final Connection connection;
    private PenggunaDAO penggunaDAO;
    private PerusahaanDAO perusahaanDAO;

    public SaldoDAO(Connection connection, PenggunaDAO penggunaDAO, PerusahaanDAO perusahaanDAO) {
        this.connection = connection;
        this.penggunaDAO = penggunaDAO;
        this.perusahaanDAO = perusahaanDAO;
    }

    public SaldoDAO(Connection connection) {
        this.connection = connection;
    }

    // Menambah transaksi saldo baru
    public void addSaldo(Saldo saldo) throws SQLException {
    String query = "INSERT INTO saldo (id_pengguna, id_perusahaan, jumlah, tipe_saldo, tanggal_transaksi, jenis_transaksi) "
                 + "VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
       
        if (saldo.getUser() != null) {
            stmt.setObject(1, saldo.getUser().getIdPengguna());
        } else {
            stmt.setNull(1, Types.INTEGER); // Jika null, atur kolom menjadi NULL
        }
        // Periksa apakah company null
        if (saldo.getCompany() != null) {
            stmt.setObject(2, saldo.getCompany().getIdPerusahaan());
        } else {
            stmt.setNull(2, Types.INTEGER); // Jika null, atur kolom menjadi NULL
        }
        
        stmt.setInt(3, saldo.getJumlah());
        stmt.setString(4, saldo.getTipeSaldo());
        stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis())); // tanggal transaksi saat ini
        stmt.setString(6, saldo.getJenisTransaksi());
        stmt.executeUpdate();
    }
}

    // Top-Up Saldo
    public void topUpSaldo(int currentUserId, int amount) {
        try {
            // Ambil saldo saat ini pengguna
            int existingSaldo = penggunaDAO.getSaldoPengguna(currentUserId);

            // Hitung saldo baru
            int newSaldo = existingSaldo + amount;

            // Perbarui saldo pengguna di database
            penggunaDAO.updateSaldoPengguna(currentUserId, newSaldo);

            // Catat transaksi Top-Up di database saldo
            Saldo saldo = new Saldo();
            saldo.setUser(penggunaDAO.getPenggunaById(currentUserId));
            saldo.setCompany(null); // jika tidak terkait dengan perusahaan
            saldo.setJumlah(amount);
            saldo.setTipeSaldo("kredit");
            saldo.setTanggalTransaksi(null);
            saldo.setJenisTransaksi("top up");
            addSaldo(saldo);

            // Tampilkan pesan sukses
            System.out.println("Top-up saldo berhasil untuk ID pengguna " + currentUserId + ". Saldo baru: " + newSaldo);
        } catch (SQLException e) {
            // Menangani error dan mencatat pesan error
            System.err.println("Error saat top-up saldo untuk ID pengguna " + currentUserId + ": " + e.getMessage());
            throw new RuntimeException("Error topping up saldo.", e);
        }
    }
public void updateSaldoPengguna(int idPengguna, int newSaldo) throws SQLException {
        String query = "UPDATE pengguna SET saldo = saldo - ? WHERE id_pengguna = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newSaldo);
            statement.setInt(2, idPengguna);
            statement.executeUpdate();
        }
    }
    // Withdraw Saldo
    public void withDraw(int currentCompanyId, int amount) {
        try {
            // Ambil saldo saat ini perusahaan
            int existingSaldo = perusahaanDAO.getSaldoPerusahaan(currentCompanyId);

            if (existingSaldo < amount) {
                throw new RuntimeException("Saldo perusahaan tidak mencukupi untuk withdraw.");
            }

            // Hitung saldo baru
            int newSaldo = existingSaldo - amount;

            // Perbarui saldo perusahaan di database
            perusahaanDAO.updateSaldoPerusahaan(currentCompanyId, newSaldo);

            // Catat transaksi Withdraw di database saldo
            Saldo saldo = new Saldo();
            saldo.setUser(null); // jika tidak terkait dengan pengguna
            saldo.setCompany(perusahaanDAO.getPerusahaanById(currentCompanyId));
            saldo.setJumlah(amount);
            saldo.setTipeSaldo("debit");
            saldo.setTanggalTransaksi(null);
            saldo.setJenisTransaksi("withdraw");
            addSaldo(saldo);

            // Tampilkan pesan sukses
            System.out.println("Withdraw saldo berhasil untuk ID perusahaan " + currentCompanyId + ". Saldo baru: " + newSaldo);
        } catch (SQLException e) {
            // Menangani error dan mencatat pesan error
            System.err.println("Error saat withdraw saldo untuk ID perusahaan " + currentCompanyId + ": " + e.getMessage());
            throw new RuntimeException("Error withdrawing saldo.", e);
        }
    }
public void updateSaldoPerusahaan(int idPerusahaan, int jumlah) throws SQLException {
        String query = "UPDATE perusahaan SET saldo_perusahaan = saldo_perusahaan + ? WHERE id_perusahaan = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, jumlah);
            stmt.setInt(2, idPerusahaan);
            stmt.executeUpdate();
        }
    }
    // Mengambil saldo berdasarkan id pengguna
    public int getSaldoByUserId(int idPengguna) throws SQLException {
        String query = "SELECT jumlah FROM saldo WHERE id_pengguna = ? ";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPengguna);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("jumlah");
            }
        }
        return 0;
    }
}
