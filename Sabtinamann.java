import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import dao.PenggunaDAO;
import dao.PerusahaanDAO;
import dao.AlatDAO;
import dao.PupukDAO;
import dao.TransaksiSewaDAO;
import dao.TransaksiPupukDAO;
import dao.KeluhanDAO;
import model.Pengguna;
import model.Perusahaan;
import model.Alat;
import model.Pupuk;
import model.TransaksiSewa;
import model.TransaksiPupuk;
import model.Keluhan;
import DAO.DatabaseConnection;
import dao.SaldoDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sabtinamann {
    static Scanner scanner = new Scanner(System.in);
    static String currentUserType;  
    static int currentUserId;      
    static PenggunaDAO penggunaDAO;
    static PerusahaanDAO perusahaanDAO;
    static AlatDAO alatDAO;
    static PupukDAO pupukDAO;
    static TransaksiSewaDAO transaksiSewaDAO;
    static TransaksiPupukDAO transaksiPupukDAO;
    static KeluhanDAO keluhanDAO;

   public static void main(String[] args) {
    try (Connection connection = DatabaseConnection.getCon()) {
        penggunaDAO = new PenggunaDAO(connection);
        perusahaanDAO = new PerusahaanDAO(connection);
        alatDAO = new AlatDAO(connection);
        pupukDAO = new PupukDAO(connection);
        transaksiSewaDAO = new TransaksiSewaDAO(connection);

        // Membuat instance SaldoDAO terlebih dahulu
        SaldoDAO saldoDAO = new SaldoDAO(connection);

        // Membuat instance TransaksiPupukDAO dengan SaldoDAO
        transaksiPupukDAO = new TransaksiPupukDAO(connection, saldoDAO);
        
        keluhanDAO = new KeluhanDAO(connection);
        showInitialPage();
    } catch (SQLException e) {
        System.out.println("Error connecting to the database: " + e.getMessage());
    }
}

    // Halaman awal sebelum masuk ke homepage (choose account)
    public static void showInitialPage() {
        System.out.println("Selamat Datang di Aplikasi SABTINAMAN");
        System.out.println("Pilih jenis akun Anda:");
        System.out.println("1. Pengguna");
        System.out.println("2. Perusahaan");
        int choice = scanner.nextInt();
        
        if (choice == 1) {
            currentUserType = "User";
            userLoginOrRegister();
        } else if (choice == 2) {
            currentUserType = "Company";
            companyLoginOrRegister();
        } else {
            System.out.println("Pilihan tidak valid, coba lagi.");
            showInitialPage();
        }
    }

    // Halaman registrasi/login untuk User
    public static void userLoginOrRegister() {
        System.out.println("1. Login");
        System.out.println("2. Registrasi");
        int choice = scanner.nextInt();
        
        if (choice == 1) {
            currentUserId = login("User");
            if (currentUserId > 0) showUserHomePage();
        } else if (choice == 2) {
            register("User");
        } else {
            System.out.println("Pilihan tidak valid, coba lagi.");
            userLoginOrRegister();
        }
    }

    // Halaman registrasi/login untuk Company
    public static void companyLoginOrRegister() {
        System.out.println("1. Login");
        System.out.println("2. Registrasi");
        int choice = scanner.nextInt();
        
        if (choice == 1) {
            currentUserId = login("Company");
            if (currentUserId > 0) showCompanyHomePage();
        } else if (choice == 2) {
            register("Company");
        } else {
            System.out.println("Pilihan tidak valid, coba lagi.");
            companyLoginOrRegister();
        }
    }

    // Login function for both user and company
    public static int login(String userType) {
        System.out.println("Masukkan email: ");
        String email = scanner.next();
        System.out.println("Masukkan password: ");
        String password = scanner.next();
        
        if (userType.equals("User")) {
            try {
                Pengguna pengguna = penggunaDAO.login(email, password);
                if (pengguna != null) {
                    System.out.println("Login berhasil! Selamat datang, " + pengguna.getNama());
                    return pengguna.getIdPengguna();
                }
            } catch (SQLException e) {
                System.out.println("Error during login: " + e.getMessage());
            }
        } else if (userType.equals("Company")) {
            try {
                Perusahaan perusahaan = perusahaanDAO.login(email, password);
                if (perusahaan != null) {
                    System.out.println("Login berhasil! Selamat datang, " + perusahaan.getNama());
                    return perusahaan.getIdPerusahaan();
                }
            } catch (SQLException e) {
                System.out.println("Error during login: " + e.getMessage());
            }
        }
        System.out.println("Login gagal, coba lagi.");
        return -1;
    }

    // Register function for both user and company
    public static void register(String userType) {
        System.out.println("Masukkan nama: ");
        String nama = scanner.next();
        System.out.println("Masukkan alamat: ");
        String alamat = scanner.next();
        System.out.println("Masukkan email: ");
        String email = scanner.next();
        System.out.println("Masukkan nomor kontak: ");
        int nomorKontak = scanner.nextInt();
        System.out.println("Masukkan password: ");
        String password = scanner.next();
        
        try {
            if (userType.equals("User")) {
                Pengguna pengguna = new Pengguna(0, nama, alamat, email, nomorKontak, 0, password);
                penggunaDAO.addPengguna(pengguna);
                System.out.println("Registrasi berhasil!");
                showInitialPage();
            } else if (userType.equals("Company")) {
                Perusahaan perusahaan = new Perusahaan(0, nama, alamat, email, nomorKontak, 0, password);
                perusahaanDAO.addPerusahaan(perusahaan);
                System.out.println("Registrasi berhasil!");
                showInitialPage();
            }
        } catch (SQLException e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }

    // Halaman utama untuk User
    public static void showUserHomePage() {
        System.out.println("----- Halaman Utama Pengguna -----");
        System.out.println("1. Profil");
        System.out.println("2. Lihat Saldo");
        System.out.println("3. Top Up Saldo");
        System.out.println("4. Cari dan Sewa Alat");
        System.out.println("5. Beli Pupuk");
        System.out.println("6. Inventory (Alat yang disewa)");
        System.out.println("7. History Transaksi");
        System.out.println("8. Logout");
        
        int choice = scanner.nextInt();
        
        switch (choice) {
            case 1:
                showUserProfile();
                break;
            case 2:
                showUserBalance();
                break;
            case 3:
                topUpUserBalance();
                break;
            case 4:
                showRentToolUserPage();
                break;
            case 5:
                buyPupuk();
                break;
            case 6:
                showInventoryPage();
                break;
            case 7:
                showUserTransactionHistory();
                break;
            case 8:
                showInitialPage();
                break;
            default:
                System.out.println("Pilihan tidak valid, coba lagi.");
                showUserHomePage();
                break;
        }
    }

    // Halaman utama untuk Company
    public static void showCompanyHomePage() {
        System.out.println("----- Halaman Utama Perusahaan -----");
                System.out.println("1. Profil");
        System.out.println("2. Lihat Saldo");
        System.out.println("3. Tentukan Harga Pupuk");
        System.out.println("4. Menambahkan Alat (Sewa)");
        System.out.println("5. Keluhan (Complaint)");
        System.out.println("6. History Transaksi");
        System.out.println("7. Logout");

        int choice = scanner.nextInt();
        
        switch (choice) {
            case 1:
                showCompanyProfile();
                break;
            case 2:
                showCompanyBalance();
                break;
            case 3:
                setPupukPrice();
                break;
            case 4:
                showRentToolCompanyPage();
                break;
            case 5:
                showComplaintPage();
                break;
            case 6:
                showCompanyTransactionHistory();
                break;
            case 7:
                showInitialPage();
                break;
            default:
                System.out.println("Pilihan tidak valid, coba lagi.");
                showCompanyHomePage();
                break;
        }
    }


    // Tampilkan profil pengguna
    public static void showUserProfile() {
        try {
            Pengguna pengguna = penggunaDAO.getPenggunaById(currentUserId);
            System.out.println("Nama: " + pengguna.getNama());
            System.out.println("Alamat: " + pengguna.getAlamat());
            System.out.println("Email: " + pengguna.getEmail());
            System.out.println("Nomor Kontak: " + pengguna.getNomorKontak());
        } catch (SQLException e) {
            System.out.println("Error fetching profile: " + e.getMessage());
        }
        showUserHomePage();
    }

    // Tampilkan profil perusahaan
    public static void showCompanyProfile() {
        try {
            Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(currentUserId);
            System.out.println("Nama: " + perusahaan.getNama());
            System.out.println("Alamat: " + perusahaan.getAlamat());
            System.out.println("Email: " + perusahaan.getEmail());
            System.out.println("Nomor Kontak: " + perusahaan.getNomorKontak());
        } catch (SQLException e) {
            System.out.println("Error fetching profile: " + e.getMessage());
        }
        showCompanyHomePage();
    }

    // Lihat saldo pengguna
    public static void showUserBalance() {
        try {
            Pengguna pengguna = penggunaDAO.getPenggunaById(currentUserId);
            System.out.println("Saldo Anda: " + pengguna.getSaldoPengguna());
        } catch (SQLException e) {
            System.out.println("Error fetching balance: " + e.getMessage());
        }
        showUserHomePage();
    }

    // Lihat saldo perusahaan
    public static void showCompanyBalance() {
        try {
            Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(currentUserId);
            System.out.println("Saldo Perusahaan Anda: " + perusahaan.getSaldoPerusahaan());
        } catch (SQLException e) {
            System.out.println("Error fetching balance: " + e.getMessage());
        }
        showCompanyHomePage();
    }

    // Top up saldo pengguna
    public static void topUpUserBalance() {
        System.out.println("Masukkan jumlah saldo yang ingin di-top up: ");
        int amount = scanner.nextInt();
        penggunaDAO.topUpSaldo(currentUserId, amount);
        System.out.println("Top up berhasil! Saldo Anda bertambah " + amount);
        showUserHomePage();
    }

    // Tentukan harga pupuk untuk perusahaan
    public static void setPupukPrice() {
        System.out.println("Masukkan nama pupuk: ");
        String namaPupuk = scanner.next();
        System.out.println("Masukkan harga per kg: ");
        double hargaPerKg = scanner.nextDouble();
        try {
            Pupuk pupuk = new Pupuk(0, namaPupuk, hargaPerKg, currentUserId);
            pupukDAO.addPupuk(pupuk);
            System.out.println("Harga pupuk berhasil ditentukan.");
        } catch (SQLException e) {
            System.out.println("Error setting pupuk price: " + e.getMessage());
        }
        showCompanyHomePage();
    }

    // Halaman sewa alat untuk pengguna
public static void showRentToolUserPage() {
    System.out.println("----- Sewa Alat (Pengguna) -----");
    System.out.println("Cari alat berdasarkan kategori:");
    // Lakukan pencarian alat dan proses sewa (interaksi dengan database AlatPertanian)
    try {
        // Simulasi daftar alat yang tersedia
        for (Alat alat : alatDAO.getAllAlat()) {
            System.out.println("Nama Alat: " + alat.getNamaAlat());
            System.out.println("Spesifikasi: " + alat.getSpesifikasi());
            System.out.println("Harga Sewa: " + alat.getHargaSewa() + " per bulan");
            System.out.println("-------------");
        }

        System.out.println("Masukkan nama alat yang ingin disewa:");
        String namaAlat = scanner.next();

        Alat selectedAlat = alatDAO.getAlatByName(namaAlat);
        if (selectedAlat != null) {
            // Mendapatkan saldo pengguna saat ini
            int saldoPengguna = penggunaDAO.getSaldoPengguna(currentUserId);

            // Memeriksa apakah saldo cukup untuk menyewa alat
            if (saldoPengguna >= selectedAlat.getHargaSewa()) {
                // Mengurangi saldo pengguna
                int newSaldo = (int) (saldoPengguna - selectedAlat.getHargaSewa());
                penggunaDAO.updateSaldoPengguna(currentUserId, newSaldo);

                // Melakukan transaksi sewa alat
                transaksiSewaDAO.sewaAlat(currentUserId, selectedAlat.getIdAlat());
                System.out.println("Alat berhasil disewa! Saldo baru Anda: " + newSaldo);
            } else {
                System.out.println("Saldo Anda tidak cukup untuk menyewa alat ini.");
            }
        } else {
            System.out.println("Alat tidak ditemukan.");
        }
    } catch (SQLException e) {
        System.out.println("Error during renting tool: " + e.getMessage());
    }
    showUserHomePage();
}

    // Halaman inventory untuk pengguna
    public static void showInventoryPage() {
        System.out.println("----- Inventory -----");
        try {
            // Simulasi melihat alat yang disewa
            for (TransaksiSewa transaksi : transaksiSewaDAO.getTransaksiByUserId(currentUserId)) {
                Alat alat = alatDAO.getAlatById(transaksi.getIdAlat());
                System.out.println("Nama Alat: " + alat.getNamaAlat());
                System.out.println("Tanggal Sewa: " + transaksi.getTanggalSewa());
                System.out.println("-------------");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching inventory: " + e.getMessage());
        }
        showUserHomePage();
    }

    // Halaman keluhan untuk perusahaan
    public static void showComplaintPage() {
        System.out.println("----- Keluhan (Complaint) -----");
        try {
            for (Keluhan keluhan : keluhanDAO.getKeluhanByCompanyId(currentUserId)) {
                System.out.println("Keluhan dari Pengguna ID: " + keluhan.getIdPengguna());
                System.out.println("Deskripsi: " + keluhan.getDeskripsi());
                System.out.println("Tanggal Pengajuan: " + keluhan.getTanggal());
                System.out.println("-------------");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Sabtinamann.class.getName()).log(Level.SEVERE, null, ex);
        }
        showCompanyHomePage();
    }

    // Halaman history transaksi pengguna
    public static void showUserTransactionHistory() {
        System.out.println("----- History Transaksi (Pengguna) -----");
        try {
            for (TransaksiSewa transaksi : transaksiSewaDAO.getTransaksiByUserId(currentUserId)) {
                Alat alat = alatDAO.getAlatById(transaksi.getIdAlat());
                System.out.println("Nama Alat: " + alat.getNamaAlat());
                System.out.println("Tanggal Sewa: " + transaksi.getTanggalSewa());
                System.out.println("-------------");
            }
            for (TransaksiPupuk transaksi : transaksiPupukDAO.getTransaksiByUserId(currentUserId)) {
                Pupuk pupuk = pupukDAO.getPupukById(transaksi.getIdPupuk());
                System.out.println("Nama Pupuk: " + pupuk.getNamaPupuk());
                System.out.println("Tanggal Pembelian: " + transaksi.getTanggalBeli());
                System.out.println("-------------");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching transaction history: " + e.getMessage());
        }
        showUserHomePage();
    }

    // Halaman history transaksi perusahaan
    public static void showCompanyTransactionHistory() {
        System.out.println("----- History Transaksi (Perusahaan) -----");
        try {
            for (TransaksiSewa transaksi : transaksiSewaDAO.getTransaksiByCompanyId(currentUserId)) {
                Alat alat = alatDAO.getAlatById(transaksi.getIdAlat());
                System.out.println("Nama Alat: " + alat.getNamaAlat());
                System.out.println("Tanggal Sewa: " + transaksi.getTanggalSewa());
                System.out.println("-------------");
            }
            for (TransaksiPupuk transaksi : transaksiPupukDAO.getTransaksiByCompanyId(currentUserId)) {
                Pupuk pupuk = pupukDAO.getPupukById(transaksi.getIdPupuk());
                System.out.println("Nama Pupuk: " + pupuk.getNamaPupuk());
                System.out.println("Tanggal Penjualan: " + transaksi.getTanggalBeli());
                System.out.println("-------------");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching transaction history: " + e.getMessage());
        }
        showCompanyHomePage();
    }

    private static void buyPupuk() {
    try {
        // Menampilkan daftar pupuk yang tersedia
        List<Pupuk> pupukList = pupukDAO.getAllPupuk();
        if (pupukList.isEmpty()) {
            System.out.println("Tidak ada pupuk yang tersedia.");
            return;
        }

        System.out.println("Daftar pupuk yang tersedia:");
        for (Pupuk pupuk : pupukList) {
            System.out.printf("ID: %d | Nama: %s | Harga per kg: %.2f | ID Perusahaan: %d\n",
                pupuk.getIdPupuk(), pupuk.getNamaPupuk(), pupuk.getHargaPerKg(), pupuk.getIdPerusahaan());
        }

        // Meminta pengguna memilih pupuk dan memasukkan jumlah pembelian
        System.out.println("Masukkan ID pupuk yang ingin dibeli:");
        int idPupuk = scanner.nextInt();
        Pupuk pupuk = pupukDAO.getPupukById(idPupuk);

        if (pupuk == null) {
            System.out.println("Pupuk dengan ID tersebut tidak ditemukan.");
            return;
        }

        System.out.println("Masukkan jumlah (kg) pupuk yang ingin dibeli:");
        int jumlahKg = scanner.nextInt();
        double totalHarga = jumlahKg * pupuk.getHargaPerKg();

        // Memeriksa saldo pengguna
        int saldoPengguna = penggunaDAO.getSaldoPengguna(currentUserId);
        if (saldoPengguna < totalHarga) {
            System.out.println("Saldo tidak mencukupi. Silakan top-up saldo terlebih dahulu.");
            return;
        }

        // Melakukan pembelian dan memperbarui saldo pengguna serta perusahaan
        penggunaDAO.updateSaldoPengguna(currentUserId, saldoPengguna - totalHarga);
        penggunaDAO.addSaldoPerusahaan(pupuk.getIdPerusahaan(), totalHarga);

        // Mencatat transaksi pembelian
        TransaksiPupuk transaksi = new TransaksiPupuk(0, currentUserId, pupuk.getIdPupuk(), jumlahKg, totalHarga);
        transaksiPupukDAO.addTransaksi(transaksi);

        System.out.println("Pembelian pupuk berhasil. Total harga: " + totalHarga);
    } catch (SQLException e) {
    System.out.println("Terjadi kesalahan saat membeli pupuk: " + e.getMessage());
    e.printStackTrace(); // Cetak stack trace untuk detail error

}
}
    private static void showRentToolCompanyPage() {
    System.out.println("Masukkan nama alat: ");
    String namaAlat = scanner.next();
    System.out.println("Masukkan spesifikasi alat: ");
    scanner.nextLine(); // Konsumsi new line
    String spesifikasi = scanner.nextLine();
    System.out.println("Masukkan harga sewa per bulan: ");
    double hargaSewa = scanner.nextDouble();

    try {
        Alat alat = new Alat(0, namaAlat, currentUserId, hargaSewa);
        alatDAO.addAlat(alat);
        System.out.println("Alat berhasil ditambahkan.");
    } catch (SQLException e) {
        System.out.println("Error menambahkan alat: " + e.getMessage());
    }

    // Kembali ke halaman utama perusahaan
    showCompanyHomePage();
    }
}

