package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.HistoryTransaksi;

public class HistoryTransaksiDAO {

    private final Connection connection;

    public HistoryTransaksiDAO(Connection connection) {
        this.connection = connection;
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
            JOIN detail_transaksi dt ON ts.id_transaksi = dt.id_transaksisewa
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
            JOIN detail_transaksi dt ON tp.id_transaksi = dt.id_transaksi
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
        SELECT ts.id_transaksi AS id, 
                           ts.total_harga AS jumlah, 
                           'Sewa Alat' AS jenis, 
                           a.nama_alat AS keterangan, 
                           ts.tanggal_sewa AS tanggal,
                           ts.tipe_saldo AS tipeSaldo
                    FROM transaksisewa ts
                    JOIN alat a ON ts.id_alat = a.id_alat
                    WHERE ts.id_perusahaan = ?
    """;
    return executeQuery(query, companyId);
}

private List<HistoryTransaksi> getPupukCompanyData(int companyId) throws SQLException {
    String query = """
        SELECT tp.id_transaksi AS id, 
                           tp.total_harga AS jumlah, 
                           'Beli Pupuk' AS jenis, 
                           p.nama_pupuk AS keterangan, 
                           tp.tanggal_beli AS tanggal,
                           tp.tipe_saldo AS tipeSaldo
                    FROM transaksipupuk tp
                    JOIN pupuk p ON tp.id_pupuk = p.id_pupuk
                    WHERE tp.id_perusahaan = ?
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

    
}
