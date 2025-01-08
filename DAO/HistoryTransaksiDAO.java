package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Alat;
import model.HistoryTransaksi;
import model.Keranjang;
import model.Pengguna;
import model.Perusahaan;
import model.Pupuk;

public class HistoryTransaksiDAO {
    private Connection connection;
    private PenggunaDAO penggunaDAO;
    private AlatDAO alatDAO;
    private PerusahaanDAO perusahaanDAO;
    private PupukDAO pupukDAO;

     public HistoryTransaksiDAO(Connection connection) {
        this.connection = connection;
     }
    // Konstruktor utama yang memastikan semua dependensi diinisialisasi
    public HistoryTransaksiDAO(Connection connection, PenggunaDAO penggunaDAO, AlatDAO alatDAO, PerusahaanDAO perusahaanDAO, PupukDAO pupukDAO) {
        this.connection = connection;
        this.penggunaDAO = penggunaDAO;
        this.alatDAO = alatDAO;
        this.perusahaanDAO = perusahaanDAO;
        this.pupukDAO = pupukDAO;
    }
    // Ambil semua history transaksi (Top Up, Sewa Alat, Beli Pupuk)
    public List<HistoryTransaksi> getAllData(int userId) throws SQLException {
        List<HistoryTransaksi> list = new ArrayList<>();
        list.addAll(getTopUpData(userId));
        list.addAll(getSewaAlatData(userId));
        list.addAll(getPupukData(userId));
        return list;
    }

    public List<HistoryTransaksi> getFilteredDataCom(int userId, boolean withdraw, boolean sewa, boolean pupuk) throws SQLException {
        List<HistoryTransaksi> list = new ArrayList<>();
        if (withdraw) list.addAll(getWithdrawCompanyData(userId));
        if (sewa) list.addAll(getSewaAlatCompanyData(userId));
        if (pupuk) list.addAll(getPupukCompanyData(userId));
        return list;
    }
    
    // Filter data berdasarkan jenis transaksi yang dipilih
    public List<HistoryTransaksi> getFilteredData(int userId, boolean topUp, boolean sewa, boolean pupuk) throws SQLException {
        List<HistoryTransaksi> list = new ArrayList<>();
        if (topUp) list.addAll(getTopUpData(userId));
        if (sewa) list.addAll(getSewaAlatData(userId));
        if (pupuk) list.addAll(getPupukData(userId));
        return list;
    }

    // Data Top Up Saldo
    private List<HistoryTransaksi> getTopUpData(int userId) throws SQLException {
        String query = """
            SELECT s.id_saldo AS id, 
                   s.jumlah AS jumlah, 
                   'Top Up' AS jenis, 
                   p.nama AS keterangan, 
                   s.tanggal_transaksi AS tanggal,
                   s.tipe_saldo AS tipeSaldo
            FROM saldo s
            JOIN pengguna p ON s.id_pengguna = p.id_pengguna
            WHERE s.id_pengguna = ?
        """;
        return executeQuery(query, userId);
    }

    // Data Sewa Alat
    private List<HistoryTransaksi> getSewaAlatData(int userId) throws SQLException {
        String query = """
            SELECT ts.id_transaksi AS id, 
                   ts.total_harga AS jumlah, 
                   'Sewa Alat' AS jenis, 
                   p.nama AS keterangan, 
                   ts.tanggal_transaksi AS tanggal,
                   'Debit' AS tipeSaldo
            FROM transaksisewa ts
            JOIN pengguna p ON ts.id_pengguna = p.id_pengguna
            WHERE ts.id_pengguna = ?
        """;
        return executeQuery(query, userId);
    }

    // Data Beli Pupuk
    private List<HistoryTransaksi> getPupukData(int userId) throws SQLException {
        String query = """
            SELECT tp.id_transaksi AS id, 
                   tp.total_harga AS jumlah, 
                   'Beli Pupuk' AS jenis, 
                   p.nama AS keterangan, 
                   tp.tanggal_beli AS tanggal,
                   'Debit' AS tipeSaldo
            FROM transaksipupuk tp
            JOIN pengguna p ON tp.id_pengguna = p.id_pengguna
            WHERE tp.id_pengguna = ?
        """;
        return executeQuery(query, userId);
    }

    // Eksekusi Query
    private List<HistoryTransaksi> executeQuery(String query, int userId) throws SQLException {
        List<HistoryTransaksi> list = new ArrayList<>();
        System.out.println("Executing query: " + query + " | User ID: " + userId);
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new HistoryTransaksi(
                        rs.getInt("id"),
                        rs.getString("jenis"),
                        rs.getInt("jumlah"),
                        rs.getString("keterangan"),
                        rs.getDate("tanggal").toLocalDate(),
                        rs.getString("tipeSaldo")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            throw e;
        }
        return list;
    }
    
    public List<HistoryTransaksi> getAllCompanyData(int companyId) throws SQLException {
    List<HistoryTransaksi> list = new ArrayList<>();
    list.addAll(getWithdrawCompanyData(companyId));
    list.addAll(getSewaAlatCompanyData(companyId));
    list.addAll(getPupukCompanyData(companyId));
    return list;
}

public List<HistoryTransaksi> getAllCompanyHistory() throws SQLException {
    List<HistoryTransaksi> list = new ArrayList<>();
    list.addAll(getAllTopUpData());
    list.addAll(getAllSewaAlatData());
    list.addAll(getAllPupukData());
    return list;
}

private List<HistoryTransaksi> getWithdrawCompanyData(int companyId) throws SQLException {
    String query = """
        SELECT s.id_saldo AS id, 
                           s.jumlah AS jumlah, 
                           'withdraw' AS jenis, 
                           p.nama AS keterangan, 
                           s.tanggal_transaksi AS tanggal,
                           s.tipe_saldo AS tipeSaldo
                    FROM saldo s
                    JOIN perusahaan p ON s.id_perusahaan = p.id_perusahaan
                    WHERE s.id_perusahaan = ?
    """;
    return executeQuery(query, companyId);
}

private List<HistoryTransaksi> getSewaAlatCompanyData(int companyId) throws SQLException {
    String query = """
        SELECT dt.id_transaksisewa AS id, 
                                   dt.total_harga AS jumlah, 
                                   'Sewa Alat' AS jenis, 
                                   a.nama_alat AS keterangan, 
                                   dt.tanggal_pinjam AS tanggal,
                                   "kredit" AS tipeSaldo
                            FROM detail_transaksi dt
                            JOIN alat a ON dt.id_alat = a.id_alat
                           JOIN perusahaan p ON a.id_perusahaan = p.id_perusahaan
                            WHERE p.id_perusahaan = ?
    """;
    return executeQuery(query, companyId);
}

private List<HistoryTransaksi> getPupukCompanyData(int companyId) throws SQLException {
    String query = """
        SELECT dt.id_transaksi AS id, 
                           dt.total_harga AS jumlah, 
                           'Beli Pupuk' AS jenis, 
                           p.nama_pupuk AS keterangan, 
                           tp.tanggal_beli AS tanggal,
                           "kredit" AS tipeSaldo
                    FROM detail_transaksi dt
                   JOIN transaksipupuk tp ON tp.id_transaksi = dt.id_transaksi
                    JOIN pupuk p ON tp.id_pupuk = p.id_pupuk
                   JOIN perusahaan pr ON p.id_perusahaan = pr.id_perusahaan
                    WHERE pr.id_perusahaan = ?
    """;
    return executeQuery(query, companyId);
}

private List<HistoryTransaksi> getAllTopUpData() throws SQLException {
    String query = """
        SELECT s.id_saldo AS id, 
               s.jumlah AS jumlah, 
               s.jenis_transaksi AS jenis, 
               p.nama AS keterangan, 
               s.tanggal_transaksi AS tanggal,
               s.tipe_saldo AS tipeSaldo
        FROM saldo s
        JOIN pengguna p ON s.id_pengguna = p.id_pengguna
    """;
    return executeQueryNoParam(query);
}

private List<HistoryTransaksi> getAllSewaAlatData() throws SQLException {
    String query = """
        SELECT ts.id_transaksi AS id, 
               ts.total_harga AS jumlah, 
               'Sewa Alat' AS jenis, 
               a.nama_alat AS keterangan, 
               ts.tanggal_sewa AS tanggal,
               ts.tipe_saldo AS tipeSaldo
        FROM transaksisewa ts
        JOIN alat a ON ts.id_alat = a.id_alat
    """;
    return executeQueryNoParam(query);
}

private List<HistoryTransaksi> getAllPupukData() throws SQLException {
    String query = """
        SELECT tp.id_transaksi AS id, 
               tp.total_harga AS jumlah, 
               'Beli Pupuk' AS jenis, 
               p.nama_pupuk AS keterangan, 
               tp.tanggal_beli AS tanggal,
               tp.tipe_saldo AS tipeSaldo
        FROM transaksipupuk tp
        JOIN pupuk p ON tp.id_pupuk = p.id_pupuk
    """;
    return executeQueryNoParam(query);
}

private List<HistoryTransaksi> executeQueryNoParam(String query) throws SQLException {
    List<HistoryTransaksi> list = new ArrayList<>();
    System.out.println("Executing query: " + query);
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            list.add(new HistoryTransaksi(
                    rs.getInt("id"),
                    rs.getString("jenis"),
                    rs.getInt("jumlah"),
                    rs.getString("keterangan"),
                    rs.getDate("tanggal").toLocalDate(),
                    rs.getString("tipeSaldo")
            ));
        }
    } catch (SQLException e) {
        System.err.println("Error executing query: " + e.getMessage());
        throw e;
    }
    return list;
}

public List<Keranjang> getDetailTransaksisewa (int idTransaksi) throws SQLException{
    List<Keranjang> details = new ArrayList<>();
        String query = """
            SELECT 
               dt.*, dt.id_keranjang, dt.id_transaksisewa, a.nama_alat, dt.jumlah, dt.total_harga, pe.id_pengguna, p.id_perusahaan
            FROM 
                detail_transaksi dt
            JOIN transaksisewa ts ON dt.id_transaksisewa = ts.id_transaksi
            JOIN alat a ON dt.id_alat = a.id_alat
            JOIN perusahaan p ON a.id_perusahaan = p.id_perusahaan
            JOIN pengguna pe ON ts.id_pengguna = pe.id_pengguna
            WHERE 
                dt.id_transaksisewa = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idTransaksi); // Set ID transaksi yang dipilih
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
                Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(rs.getInt("id_perusahaan"));
                Alat alat = alatDAO.getAlatById(rs.getInt("id_alat"));
                LocalDate tanggalPinjam = rs.getDate("tanggal_pinjam").toLocalDate();
                LocalDate tanggalKembali = rs.getDate("tanggal_kembali").toLocalDate();
                details.add(new Keranjang(
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
                System.out.println("ID Transaksi: " + idTransaksi);
System.out.println("ID Keranjang: " + rs.getInt("id_keranjang"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        return details;
    }

    public List<Keranjang> getDetailTransaksipupuk (int idTransaksi) throws SQLException{
    List<Keranjang> details = new ArrayList<>();
        String query = """
            SELECT 
               dt.*, dt.id_keranjang, dt.id_transaksi, pu.nama_pupuk, dt.jumlah, dt.total_harga,  pe.id_pengguna, p.id_perusahaan
               FROM 
                detail_transaksi dt
               JOIN transaksipupuk tp ON dt.id_transaksi = tp.id_transaksi
               JOIN pupuk pu ON dt.id_pupuk = pu.id_pupuk
               JOIN perusahaan p ON p.id_perusahaan = p.id_perusahaan
               JOIN pengguna pe ON tp.id_pengguna = pe.id_pengguna
               WHERE 
               dt.id_transaksi = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idTransaksi); // Set ID transaksi yang dipilih
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pengguna pengguna = penggunaDAO.getPenggunaById(rs.getInt("id_pengguna"));
                Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(rs.getInt("id_perusahaan"));
                Pupuk pupuk = pupukDAO.getPupukById(rs.getInt("id_pupuk"));
                
                details.add(new Keranjang(
                    rs.getInt("id_keranjang"),
                    rs.getInt("id_transaksi"),
                    pengguna,
                    perusahaan, 
                    pupuk,
                    rs.getInt("jumlah"),
                    rs.getInt("total_harga")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }
    
    public List<HistoryTransaksi> getFilteredDataByDate(int userId, LocalDate startDate, LocalDate endDate) throws SQLException {
    List<HistoryTransaksi> filteredData = new ArrayList<>();
    String query = """
        SELECT id, jumlah, jenis, tipeSaldo, tanggal, keterangan FROM (
            SELECT s.id_saldo AS id, 
                                      s.jumlah AS jumlah, 
                                      'Top Up' AS jenis, 
                                      p.nama AS keterangan, 
                                      s.tanggal_transaksi AS tanggal,
                                      s.tipe_saldo AS tipeSaldo
                               FROM saldo s
                               JOIN pengguna p ON s.id_pengguna = p.id_pengguna
            WHERE s.id_pengguna = ? AND s.tanggal_transaksi BETWEEN ? AND ?

            UNION ALL
SELECT ts.id_transaksi AS id, 
                   ts.total_harga AS jumlah, 
                   'Sewa Alat' AS jenis, 
                   p.nama AS keterangan, 
                   ts.tanggal_transaksi AS tanggal,
                   'Debit' AS tipeSaldo
            FROM transaksisewa ts
            JOIN pengguna p ON ts.id_pengguna = p.id_pengguna
            WHERE ts.id_pengguna = ? AND ts.tanggal_transaksi BETWEEN ? AND ?

            UNION ALL

           SELECT tp.id_transaksi AS id, 
                               tp.total_harga AS jumlah, 
                               'Beli Pupuk' AS jenis, 
                               p.nama AS keterangan, 
                               tp.tanggal_beli AS tanggal,
                               'Debit' AS tipeSaldo
                        FROM transaksipupuk tp
                        JOIN pengguna p ON tp.id_pengguna = p.id_pengguna
            WHERE tp.id_pengguna = ? AND tp.tanggal_beli BETWEEN ? AND ?
        ) AS combined
        ORDER BY tanggal ASC
    """;

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, userId);
        stmt.setDate(2, java.sql.Date.valueOf(startDate));
        stmt.setDate(3, java.sql.Date.valueOf(endDate));

        stmt.setInt(4, userId);
        stmt.setDate(5, java.sql.Date.valueOf(startDate));
        stmt.setDate(6, java.sql.Date.valueOf(endDate));

        stmt.setInt(7, userId);
        stmt.setDate(8, java.sql.Date.valueOf(startDate));
        stmt.setDate(9, java.sql.Date.valueOf(endDate));

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            filteredData.add(new HistoryTransaksi(
                rs.getInt("id"),
                rs.getString("jenis"),
                rs.getInt("jumlah"),
                rs.getString("keterangan"),
                rs.getDate("tanggal").toLocalDate(),
                rs.getString("tipeSaldo")
            ));
        }
    } catch (SQLException e) {
        System.err.println("Error executing query: " + e.getMessage());
        throw e;
    }

    return filteredData;
}

    
}