package DAO;

import model.TransaksiSewa;
import model.Pengguna;
import model.Alat;
import model.Perusahaan;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import model.Keranjang;
import model.Saldo;

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

    // Menambah transaksi sewa alat baru ke database
    public int addTransaksi(TransaksiSewa transaksi) throws SQLException {
        String query = "INSERT INTO transaksisewa (id_pengguna, id_perusahaan, id_alat, tanggal_sewa, tanggal_pengembalian, status, tipe_saldo, total_harga) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
            stmt.setInt(3, transaksi.getAlat().getIdAlat());
            stmt.setString(7, transaksi.getTipeSaldo());
            stmt.setInt(8, transaksi.getTotalHarga());
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
    String sqlInsertDetailTransaksi = "INSERT INTO detail_transaksi (id_transaksi, id_alat, jumlah, total_harga, tanggal_pinjam, tanggal_kembali, durasi) VALUES (?, ?, ?, ?, ?, ?, ?)";

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
            psDetailTransaksi.setInt(2, item.getIdAlat());
            psDetailTransaksi.setInt(3, item.getJumlah());
            psDetailTransaksi.setInt(4, item.getTotalHarga());
            psDetailTransaksi.setDate(5, Date.valueOf(item.getTanggalPinjam()));
            psDetailTransaksi.setDate(6, Date.valueOf(item.getTanggalKembali()));
            psDetailTransaksi.setLong(7, item.getDurasi());
            psDetailTransaksi.executeUpdate();
        }
    }
}
    
//    // Proses penyewaan alat
//    public void sewaAlat(Pengguna pengguna, Alat alat, Perusahaan perusahaan, long jumlahHari, int totalHarga) throws SQLException {
//    if (pengguna == null || alat == null || perusahaan == null) {
//        throw new IllegalArgumentException("Pengguna, alat, atau perusahaan tidak boleh null.");
//    }
//
//    if (jumlahHari <= 0) {
//        throw new IllegalArgumentException("Jumlah hari sewa harus lebih dari 0.");
//    }
//
//
//    // 2. Periksa saldo pengguna
//    int saldoPengguna = penggunaDAO.getSaldoPengguna(pengguna.getIdPengguna());
//    if (saldoPengguna < totalHarga) {
//        throw new SQLException("Saldo pengguna tidak mencukupi untuk menyewa alat.");
//    }
//
//    // 3. Pastikan alat tersedia
//    String statusAlat = alatDAO.getStatusAlat(alat.getIdAlat());
//    if (!statusAlat.equals("available")) {
//        throw new SQLException("Alat tidak tersedia untuk disewa.");
//    }
//
//    connection.setAutoCommit(false); // Mulai transaksi
//
//    try {
//        // 4. Kurangi saldo pengguna
//        saldoDAO.updateSaldoPengguna(pengguna.getIdPengguna(), totalHarga);
//
//        // 5. Tambahkan saldo ke perusahaan
//        saldoDAO.updateSaldoPerusahaan(perusahaan.getIdPerusahaan(), totalHarga);
//        
//        // 6. Tambah transaksi penyewaan alat
//        Timestamp tanggalSewa = new Timestamp(System.currentTimeMillis());
//        LocalDateTime tanggalPengembalian = tanggalSewa.toLocalDateTime().plusDays(jumlahHari);
//        Timestamp tanggalPengembalianTimestamp = Timestamp.valueOf(tanggalPengembalian);
//
//        TransaksiSewa transaksi = new TransaksiSewa(0, pengguna, null, alat, tanggalSewa, tanggalPengembalianTimestamp, "rented", "debit", totalHarga);
//        addTransaksi(transaksi);
//        TransaksiSewa transaksi2 = new TransaksiSewa(0, null, perusahaan, alat, tanggalSewa, tanggalPengembalianTimestamp, null, "kredit", totalHarga);
//        addTransaksi(transaksi2);
//        // 7. Ubah status alat menjadi "rented"
//        alatDAO.updateStatusAlat(alat.getIdAlat(), "rented");
//
//        connection.commit();
//        System.out.println("Sewa alat berhasil! Alat disewa selama " + jumlahHari + " hari.");
//    } catch (SQLException e) {
//        connection.rollback();
//        System.err.println("Sewa alat gagal: " + e.getMessage());
//        throw e; // Re-throw untuk penanganan lebih lanjut
//    } finally {
//        connection.setAutoCommit(true); // Kembalikan ke pengaturan default
//    }
//}

    public void simpanDetailTransaksi(int idTransaksi, int idAlat, int jumlah, int totalHarga) throws SQLException {
    String sql = "INSERT INTO detail_transaksi (id_transaksi, id_alat, jumlah, total_harga) VALUES (?, ?, ?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, idTransaksi);
        ps.setInt(2, idAlat);
        ps.setInt(3, jumlah);
        ps.setInt(4, totalHarga);
        ps.executeUpdate();
    }
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
        updateStatusTransaksi(idTransaksi, "returned");
    }

    public void updateStatusTransaksi(int idTransaksi, String status){
       String sql = "UPDATE transaksisewa SET status = ? WHERE id_transaksi = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, status);
        stmt.setInt(2, idTransaksi);
        stmt.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(AlatDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
   }
    
    // Mengambil transaksi sewa berdasarkan ID pengguna
    public List<TransaksiSewa> getTransaksiByUserId(int idPengguna) throws SQLException {
        String query = "SELECT ts.* FROM transaksisewa ts JOIN alat a ON ts.id_alat = a.id_alat WHERE ts.id_pengguna = ?";
        List<TransaksiSewa> transaksiList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPengguna);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
                Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(rs.getInt("id_perusahaan"));
                Alat alat = alatDAO.getAlatById(rs.getInt("id_alat"));
                transaksiList.add(new TransaksiSewa(
                    rs.getInt("id_transaksi"),
                    pengguna,
                    perusahaan,    
                    alat,
                    rs.getString("tipe_saldo"),
                    rs.getInt("total_harga")
                ));
            }
        }
        return transaksiList;
    }

    public TransaksiSewa getTransaksiById(int idTransaksi) throws SQLException {
        String query = "SELECT * FROM transaksisewa WHERE id_transaksi = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idTransaksi);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
                Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(rs.getInt("id_perusahaan"));
                Alat alat = alatDAO.getAlatById(rs.getInt("id_alat"));
                return new TransaksiSewa(
                    rs.getInt("id_transaksi"),
                    pengguna,
                    perusahaan,
                    alat,
                    rs.getString("tipe_saldo"),
                    rs.getInt("total_harga")
                );
            }
        }
        return null;
    }
    // Mengambil transaksi sewa berdasarkan ID perusahaan
    public List<TransaksiSewa> getTransaksiByCompanyId(int idPerusahaan) throws SQLException {
        String query = "SELECT ts.* FROM transaksisewa ts JOIN alat a ON ts.id_alat = a.id_alat WHERE a.id_perusahaan = ?";
        List<TransaksiSewa> transaksiList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPerusahaan);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
                Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(rs.getInt("id_perusahaan"));
                Alat alat = alatDAO.getAlatById(rs.getInt("id_alat"));
                transaksiList.add(new TransaksiSewa(
                    rs.getInt("id_transaksi"),
                    pengguna,
                    perusahaan,
                    alat,
                    rs.getString("tipe_saldo"),
                    rs.getInt("total_harga")
                ));
            }
        }
        return transaksiList;
    }
    

// Get daftar alat yang disewa pengguna
public List<TransaksiSewa> getAlatDisewaByUser(int idPengguna) throws SQLException {
    // SQL Query untuk mengambil semua transaksi sewa dengan status 'rented'
    String query = """
        SELECT 
            ts.id_transaksi, 
            ts.id_pengguna,
            ts.tipe_saldo,
            ts.total_harga,
            a.nama_alat 
        FROM 
            transaksisewa ts 
        JOIN 
            alat a ON ts.id_alat = a.id_alat 
        WHERE 
            ts.id_pengguna = ? 
            AND ts.status = 'rented';
    """;

    // List untuk menampung daftar transaksi yang diambil dari database
    List<TransaksiSewa> transaksiList = new ArrayList<>();

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        // Set parameter query dengan ID pengguna
        stmt.setInt(1, idPengguna);

        // Eksekusi query dan ambil hasilnya
        ResultSet rs = stmt.executeQuery();

        // Iterasi hasil query
        while (rs.next()) {
            // Ambil data pengguna, alat, dan properti dari tabel transaksi
            Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
            Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(rs.getInt("id_perusahaan"));
            Alat alat = alatDAO.getAlatById(rs.getInt("id_alat"));

            // Buat objek TransaksiSewa dan masukkan ke dalam daftar
            TransaksiSewa transaksi = new TransaksiSewa(
                rs.getInt("id_transaksi"),
                pengguna,
                perusahaan, 
                alat,
                rs.getString("tipe_saldo"),
                rs.getInt("total_harga")
            );

            // Tambahkan ke daftar transaksi
            transaksiList.add(transaksi);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new SQLException("Gagal mengambil daftar alat yang disewa pengguna dengan ID: " + idPengguna, e);
    }

    return transaksiList;
    }
}
