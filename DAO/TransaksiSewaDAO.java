package DAO;

import model.TransaksiSewa;
import model.Pengguna;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import model.Alat;
import model.Keranjang;
import model.Perusahaan;

public class TransaksiSewaDAO {
    private Connection connection;
    private SaldoDAO saldoDAO;
    private PenggunaDAO penggunaDAO;
    private AlatDAO alatDAO;
    private PerusahaanDAO perusahaanDAO;

     public TransaksiSewaDAO(Connection connection) {
        this.connection = connection;
     }
    // Konstruktor utama yang memastikan semua dependensi diinisialisasi
    public TransaksiSewaDAO(Connection connection, SaldoDAO saldoDAO, PenggunaDAO penggunaDAO, AlatDAO alatDAO, PerusahaanDAO perusahaanDAO) {
        this.connection = connection;
        this.saldoDAO = saldoDAO;
        this.penggunaDAO = penggunaDAO;
        this.alatDAO = alatDAO;
        this.perusahaanDAO = perusahaanDAO;
    }

    // Menambah transaksi sewa baru ke database
    public int addTransaksi(TransaksiSewa transaksi) throws SQLException {
        String query = "INSERT INTO transaksisewa (id_pengguna, total_harga, tanggal_transaksi) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, transaksi.getUser().getIdPengguna());
            stmt.setInt(2, transaksi.getTotalHarga());
            stmt.setDate(3, Date.valueOf(transaksi.getTanggalTransaksi()));

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // ID transaksi
            } else {
                throw new SQLException("Gagal menyimpan transaksi, ID tidak ditemukan.");
            }
        }
    }

    public void checkout(int idPengguna, String tipeSaldo, ObservableList<Keranjang> keranjangList) throws SQLException {
    String sqlInsertTransaksi = "INSERT INTO transaksisewa (id_pengguna, total_harga, tipe_saldo) VALUES (?, ?, ?)";
    String sqlInsertDetailTransaksi = "INSERT INTO detail_transaksi (id_transaksisewa, id_alat, jumlah, total_harga, tanggal_pinjam, tanggal_kembali, durasi) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement psTransaksi = connection.prepareStatement(sqlInsertTransaksi, Statement.RETURN_GENERATED_KEYS);
         PreparedStatement psDetailTransaksi = connection.prepareStatement(sqlInsertDetailTransaksi)) {

        // Hitung total harga seluruh keranjang
        int totalHarga = keranjangList.stream().mapToInt(Keranjang::getTotalHarga).sum();
        int saldoPengguna = penggunaDAO.getSaldoPengguna(idPengguna);

        if (saldoPengguna < totalHarga) {
            throw new IllegalStateException("Saldo pengguna tidak mencukupi.");
        }

        connection.setAutoCommit(false); // Mulai transaksi

        // 1. Kurangi saldo pengguna
        saldoDAO.updateSaldoPengguna(idPengguna, totalHarga);
        System.out.println("Saldo pengguna berhasil diperbarui.");

       
        // 2. Tambah saldo perusahaan (opsional, pastikan ini dibutuhkan)
        // saldoDAO.updateSaldoPerusahaan(perusahaan.getIdPerusahaan(), totalHarga);
        // System.out.println("Saldo perusahaan berhasil diperbarui.");

        // Simpan transaksi utama
        psTransaksi.setInt(1, idPengguna);
        psTransaksi.setInt(2, totalHarga);
        psTransaksi.setString(3, tipeSaldo);
        psTransaksi.executeUpdate();

        // Ambil ID transaksi yang baru dibuat
        try (ResultSet rs = psTransaksi.getGeneratedKeys()) {
            if (!rs.next()) {
                throw new SQLException("Gagal mendapatkan ID transaksi.");
            }
            int idTransaksi = rs.getInt(1);

            // Simpan detail transaksi untuk setiap item di keranjang
            for (Keranjang item : keranjangList) {
                psDetailTransaksi.setInt(1, idTransaksi);
                psDetailTransaksi.setInt(2, item.getAlat().getIdAlat());
                psDetailTransaksi.setInt(3, item.getJumlah());
                psDetailTransaksi.setInt(4, item.getTotalHarga());
                psDetailTransaksi.setDate(5, Date.valueOf(item.getTanggalPinjam()));
                psDetailTransaksi.setDate(6, Date.valueOf(item.getTanggalKembali()));
                psDetailTransaksi.setLong(7, item.getDurasi());
                psDetailTransaksi.executeUpdate();
            }
        }

        connection.commit(); // Konfirmasi perubahan
        System.out.println("Transaksi berhasil disimpan.");
    } catch (SQLException e) {
        if (connection != null) {
            try {
                connection.rollback(); // Kembalikan ke kondisi awal jika gagal
                System.out.println("Transaksi dibatalkan.");
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
        throw new SQLException("Gagal melakukan transaksi checkout.", e);
    } finally {
        connection.setAutoCommit(true); // Kembalikan ke mode auto-commit
    }
}


  

    public void simpanDetailTransaksi(int idTransaksi, int idAlat, int jumlah, int totalHarga) throws SQLException {
    String sql = "INSERT INTO detail_transaksi (id_transaksisewa, id_alat, jumlah, total_harga) VALUES (?, ?, ?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, idTransaksi);
        ps.setInt(2, idAlat);
        ps.setInt(3, jumlah);
        ps.setInt(4, totalHarga);
        ps.executeUpdate();
    }
}
    
    // Mengambil transaksi berdasarkan ID
    public TransaksiSewa getTransaksiById(int idTransaksi) throws SQLException {
        String query = "SELECT * FROM transaksisewa WHERE id_transaksi = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idTransaksi);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
                return new TransaksiSewa(
                    rs.getInt("id_transaksi"),
                    pengguna,
                    rs.getInt("total_harga"),
                    rs.getDate("tanggal_transaksi").toLocalDate()
                );
            }
        }
        return null;
    }

    // Mengembalikan alat yang telah disewa
    public void kembalikanAlat(int idTransaksi, int idAlat, int kuantitas) throws SQLException {
        String query = "UPDATE transaksisewa SET tanggal_pengembalian = ? WHERE id_transaksi = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            Timestamp tanggalPengembalian = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(1, tanggalPengembalian);
            stmt.setInt(2, idTransaksi);
            stmt.executeUpdate();
        }

        // Ubah status alat menjadi "available" kembali
        alatDAO.updateStokAlat(idAlat, kuantitas );
        
    }
    
    // Mengambil semua transaksi
    public List<TransaksiSewa> getAllTransaksi() throws SQLException {
        String query = "SELECT * FROM transaksisewa";
        List<TransaksiSewa> transaksiList = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
                TransaksiSewa transaksi = new TransaksiSewa(
                    rs.getInt("id_transaksi"),
                    pengguna,
                    rs.getInt("total_harga"),
                    rs.getDate("tanggal_transaksi").toLocalDate()
                );
                transaksiList.add(transaksi);
            }
        }
        return transaksiList;
    }

    // Mengambil transaksi sewa berdasarkan ID pengguna
    public List<Keranjang> getTransaksiByUserId(int idPengguna) throws SQLException {
        String query = "SELECT dt.* FROM detail_transaksi dt JOIN alat a ON dt.id_alat = a.id_alat WHERE dt.id_pengguna = ?";
        List<Keranjang> inventory = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPengguna);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
                Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(rs.getInt("id_perusahaan"));
                Alat alat = alatDAO.getAlatById(rs.getInt("id_alat"));
                LocalDate tanggalPinjam = rs.getDate("tanggal_pinjam").toLocalDate();
                LocalDate tanggalKembali = rs.getDate("tanggal_kembali").toLocalDate();
                inventory.add(new Keranjang(
                    rs.getInt("id_keranjang"),
                    rs.getInt("id_transaksi"),
                    pengguna,
                    perusahaan, 
                    alat,
                    rs.getInt("jumlah"),
                    rs.getLong("durasi"),
                    rs.getInt("total_harga"),
                    tanggalPinjam,
                    tanggalKembali
                ));
            }
        }
        return inventory;
    }
    
    // Mengupdate transaksi
    public void updateTransaksi(TransaksiSewa transaksi) throws SQLException {
        String query = "UPDATE transaksisewa SET id_pengguna = ?, total_harga = ?, tanggal_transaksi = ? WHERE id_transaksi = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, transaksi.getUser().getIdPengguna());
            stmt.setInt(2, transaksi.getTotalHarga());
            stmt.setDate(3, Date.valueOf(transaksi.getTanggalTransaksi()));
            stmt.setInt(4, transaksi.getIdTransaksi());

            stmt.executeUpdate();
        }
    }

    // Menghapus transaksi berdasarkan ID
    public void deleteTransaksi(int idTransaksi) throws SQLException {
        String query = "DELETE FROM transaksisewa WHERE id_transaksi = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idTransaksi);
            stmt.executeUpdate();
        }
    }

    // Mengambil transaksi berdasarkan ID pengguna
    public List<TransaksiSewa> alatDisewaUser(int idPengguna) throws SQLException {
        String query = "SELECT * FROM transaksisewa WHERE id_pengguna = ?";
        List<TransaksiSewa> transaksiList = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPengguna);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
                TransaksiSewa transaksi = new TransaksiSewa(
                    rs.getInt("id_transaksi"),
                    pengguna,
                    rs.getInt("total_harga"),
                    rs.getDate("tanggal_transaksi").toLocalDate()
                );
                transaksiList.add(transaksi);
            }
        }
        return transaksiList;
    }
   public List<Keranjang> getAlatDisewaByUser(int idPengguna) throws SQLException {
    String query = """
        SELECT 
            ts.id_transaksi, 
            ts.id_pengguna,
            ts.total_harga, 
            dt.jumlah,
            dt.*,   
            dt.tanggal_pinjam,
            dt.tanggal_kembali,
            dt.id_keranjang,
            dt.id_alat, 
            a.nama_alat,
            a.id_perusahaan,
            pr.nama AS nama_perusahaan
        FROM 
            transaksisewa ts 
        JOIN detail_transaksi dt ON dt.id_transaksisewa = ts.id_transaksi
        JOIN alat a ON dt.id_alat = a.id_alat
        JOIN perusahaan pr ON a.id_perusahaan = pr.id_perusahaan
        WHERE 
            ts.id_pengguna = ?
    """;

    List<Keranjang> inventory = new ArrayList<>();

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
    stmt.setInt(1, idPengguna);

    ResultSet rs = stmt.executeQuery();

    while (rs.next()) {
        LocalDate tanggalPinjam = rs.getDate("tanggal_pinjam") != null
                ? rs.getDate("tanggal_pinjam").toLocalDate()
                : null;

        LocalDate tanggalKembali = rs.getDate("tanggal_kembali") != null
                ? rs.getDate("tanggal_kembali").toLocalDate()
                : null;

        Pengguna pengguna = null;
        try {
            pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
        } catch (SQLException e) {
            System.err.println("Pengguna tidak ditemukan untuk ID: " + rs.getInt("id_pengguna"));
        }

        Perusahaan perusahaan = null;
        try {
            perusahaan = perusahaanDAO.getPerusahaanById(rs.getInt("id_perusahaan"));
        } catch (SQLException e) {
            System.err.println("Perusahaan tidak ditemukan untuk ID: " + rs.getInt("id_perusahaan"));
        }

        Alat alat = null;
        try {
            alat = alatDAO.getAlatById(rs.getInt("id_alat"));
        } catch (SQLException e) {
            System.err.println("Alat tidak ditemukan untuk ID: " + rs.getInt("id_alat"));
        }

        if (pengguna != null && perusahaan != null && alat != null) {
            Keranjang keranjang = new Keranjang(
                rs.getInt("id_keranjang"),
                rs.getInt("id_transaksi"),
                pengguna,
                perusahaan,
                alat,
                rs.getInt("jumlah"),
                rs.getLong("durasi"),
                rs.getInt("total_harga"),
                tanggalPinjam,
                tanggalKembali
            );

            inventory.add(keranjang);
        }
    }
} catch (SQLException e) {
    e.printStackTrace();
    throw new SQLException("Gagal mengambil daftar alat yang disewa pengguna dengan ID: " + idPengguna, e);
}


    return inventory;
}

    
}


