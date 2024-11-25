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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.UUID;
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
    static SaldoDAO saldoDAO;
    static TransaksiSewaDAO transaksiSewaDAO;
    static TransaksiPupukDAO transaksiPupukDAO;
    static KeluhanDAO keluhanDAO;

   public static void main(String[] args) {
    try (Connection connection = DatabaseConnection.getCon()) {
        penggunaDAO = new PenggunaDAO(connection);
        perusahaanDAO = new PerusahaanDAO(connection);
        alatDAO = new AlatDAO(connection, perusahaanDAO);
        pupukDAO = new PupukDAO(connection, perusahaanDAO);
        transaksiSewaDAO = new TransaksiSewaDAO(connection, saldoDAO, penggunaDAO, alatDAO, perusahaanDAO);

        // Membuat instance SaldoDAO terlebih dahulu
        saldoDAO = new SaldoDAO(connection);

        // Membuat instance TransaksiPupukDAO dengan SaldoDAO
        transaksiPupukDAO = new TransaksiPupukDAO(connection, saldoDAO, penggunaDAO, pupukDAO, perusahaanDAO);
        
        keluhanDAO = new KeluhanDAO(connection, penggunaDAO, alatDAO, transaksiSewaDAO);
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
                    currentUserId = pengguna.getIdPengguna();
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
                    currentUserId = perusahaan.getIdPerusahaan();
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
            Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(currentUserId); // Ambil perusahaan terkait
            Pupuk pupuk = new Pupuk(0,namaPupuk, hargaPerKg, perusahaan);
            pupukDAO.addPupuk(pupuk);
            System.out.println("Harga pupuk berhasil ditentukan.");
        } catch (SQLException e) {
            System.out.println("Error setting pupuk price: " + e.getMessage());
        }
        showCompanyHomePage();
    }

    // Halaman sewa alat untuk pengguna
public static void showRentToolUserPage() {
    try {
        // Menampilkan daftar alat yang tersedia
        List<Alat> alatList = alatDAO.getAllAlat();
        if (alatList.isEmpty()) {
            System.out.println("Tidak ada alat yang tersedia untuk disewa.");
            return;
        }

        System.out.println("Daftar alat yang tersedia:");
        for (Alat alat : alatList) {
            System.out.printf("ID: %d | Nama: %s | Harga Sewa per Bulan: %d | ID Perusahaan: %d\n",
                alat.getIdAlat(), alat.getNamaAlat(), alat.getHargaSewa(), alat.getCompany().getIdPerusahaan());
        }

        // Meminta pengguna memilih alat yang ingin disewa
        System.out.println("Masukkan ID alat yang ingin disewa:");
        int idAlat = scanner.nextInt();
        scanner.nextLine(); // Bersihkan buffer

        Alat alat = alatDAO.getAlatById(idAlat);
        if (alat == null) {
            System.out.println("Alat dengan ID tersebut tidak ditemukan.");
            return;
        }

        // Memeriksa saldo pengguna
        int saldoPengguna = (int) penggunaDAO.getSaldoPengguna(currentUserId);
        if (saldoPengguna < alat.getHargaSewa()) {
            System.out.println("Saldo Anda tidak mencukupi untuk menyewa alat ini. Silakan top-up saldo terlebih dahulu.");
            return;
        }

        // Proses penyewaan
        int newSaldoPengguna = saldoPengguna - alat.getHargaSewa();
        penggunaDAO.updateSaldoPengguna(currentUserId, newSaldoPengguna);
        penggunaDAO.addSaldoPerusahaan(alat.getCompany().getIdPerusahaan(), alat.getHargaSewa());

        // Catat transaksi
        Timestamp tanggalSewa = new Timestamp(System.currentTimeMillis());
        Pengguna pengguna = penggunaDAO.getPenggunaById(currentUserId);
        Alat alatObj = alatDAO.getAlatById(alat.getIdAlat());
        TransaksiSewa transaksi = new TransaksiSewa(0, pengguna, alatObj, tanggalSewa);
        transaksiSewaDAO.addTransaksi(transaksi);

        System.out.println("Alat berhasil disewa! Saldo baru Anda: " + newSaldoPengguna);

    } catch (SQLException e) {
        System.out.println("Terjadi kesalahan saat memproses penyewaan alat: " + e.getMessage());
    } catch (InputMismatchException e) {
        System.out.println("Masukkan input yang valid. Harap gunakan angka untuk ID alat.");
        scanner.nextLine(); // Bersihkan buffer input
    }
}


    // Halaman inventory untuk pengguna
    public static void showInventoryPage() {
        System.out.println("----- Inventory -----");
        try {
            // Simulasi melihat alat yang disewa
            for (TransaksiSewa transaksi : transaksiSewaDAO.getTransaksiByUserId(currentUserId)) {
            Alat alat = alatDAO.getAlatById(transaksi.getAlat().getIdAlat());
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
                System.out.println("Keluhan dari Pengguna ID: " + keluhan.getUser().getIdPengguna());
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
                Alat alat = alatDAO.getAlatById(transaksi.getAlat().getIdAlat());
                System.out.println("Nama Alat: " + alat.getNamaAlat());
                System.out.println("Tanggal Sewa: " + transaksi.getTanggalSewa());
                System.out.println("-------------");
            }
            for (TransaksiPupuk transaksi : transaksiPupukDAO.getTransaksiByUserId(currentUserId)) {
                Pupuk pupuk = pupukDAO.getPupukById(transaksi.getPupuk().getIdPupuk());
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
                Alat alat = alatDAO.getAlatById(transaksi.getAlat().getIdAlat());
                System.out.println("Nama Alat: " + alat.getNamaAlat());
                System.out.println("Tanggal Sewa: " + transaksi.getTanggalSewa());
                System.out.println("-------------");
            }
            for (TransaksiPupuk transaksi : transaksiPupukDAO.getTransaksiByCompanyId(currentUserId)) {
                Pupuk pupuk = pupukDAO.getPupukById(transaksi.getPupuk().getIdPupuk());
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
                pupuk.getIdPupuk(), pupuk.getNamaPupuk(), pupuk.getHargaPerKg(), pupuk.getCompany().getIdPerusahaan());
        }

        // Meminta pengguna memilih pupuk dan memasukkan jumlah pembelian
        System.out.println("Masukkan ID pupuk yang ingin dibeli:");
        int idPupuk = scanner.nextInt(); // Bersihkan spasi dan newline
        scanner.nextLine();
        

        try {// Parsing input
            Pupuk pupuk = pupukDAO.getPupukById(idPupuk);

            if (pupuk == null) {
                System.out.println("Pupuk dengan ID tersebut tidak ditemukan.");
                return;
            }

            // Lanjutkan ke logika pembelian
            System.out.println("Masukkan jumlah (kg) pupuk yang ingin dibeli:");
            if (scanner.hasNextInt()) {
                int jumlahKg = scanner.nextInt();
                double totalHarga = jumlahKg * pupuk.getHargaPerKg();

                // Memeriksa saldo pengguna
                int saldoPengguna = (int) penggunaDAO.getSaldoPengguna(currentUserId);
                if (saldoPengguna < totalHarga) {
                    System.out.println("Saldo tidak mencukupi. Silakan top-up saldo terlebih dahulu.");
                    return;
                }

                // Proses pembelian
                penggunaDAO.updateSaldoPengguna(currentUserId, saldoPengguna - totalHarga);
                penggunaDAO.addSaldoPerusahaan(pupuk.getCompany().getIdPerusahaan(), totalHarga);

                // Catat transaksi
                LocalDate tanggalBeli = LocalDate.now();
                Pengguna pengguna = penggunaDAO.getPenggunaById(currentUserId);
                Pupuk pupukObj = pupukDAO.getPupukById(pupuk.getIdPupuk());
                TransaksiPupuk transaksi = new TransaksiPupuk(0, pengguna, pupukObj, jumlahKg, totalHarga, tanggalBeli);
                transaksiPupukDAO.addTransaksi(transaksi);

                System.out.println("Pembelian pupuk berhasil. Total harga: " + totalHarga);
            } else {
                System.out.println("Masukkan jumlah dalam angka.");
            }
            scanner.nextLine(); // Bersihkan buffer

        } catch (NumberFormatException e) {
            System.out.println("Input ID pupuk tidak valid. Harap masukkan angka.");
        }

    } catch (SQLException ex) {
        Logger.getLogger(Sabtinamann.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    private static void showRentToolCompanyPage() {
    System.out.println("Masukkan nama alat: ");
    String namaAlat = scanner.next();
    scanner.nextLine(); // Bersihkan buffer setelah next()

    System.out.println("Masukkan spesifikasi alat: ");
    String spesifikasi = scanner.nextLine();

    System.out.println("Masukkan harga sewa per bulan: ");
    int hargaSewa = scanner.nextInt();
    scanner.nextLine(); // Bersihkan buffer setelah nextInt()

    try {
        System.out.println("Masukkan ID perusahaan: ");
        int idPerusahaan = scanner.nextInt();
        scanner.nextLine();

        // Ambil perusahaan dari database
        Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(idPerusahaan);
        if (perusahaan == null) {
            System.out.println("ID perusahaan tidak ditemukan. Silakan periksa kembali.");
            showCompanyHomePage();
            return;
        }

        // Buat objek alat dan tambahkan ke database
        Alat alat = new Alat(0, namaAlat, spesifikasi, perusahaan, hargaSewa);
        alatDAO.addAlat(alat);

        System.out.println("Alat berhasil ditambahkan.");
    } catch (SQLException e) {
        System.out.println("Error menambahkan alat: " + e.getMessage());
    } catch (NumberFormatException e) {
        System.out.println("ID perusahaan harus berupa angka. Silakan coba lagi.");
    }

    // Kembali ke halaman utama perusahaan
    showCompanyHomePage();
}


}

