import java.util.Scanner;
import model.Pengguna;
import model.Perusahaan;

public class Sabtinamann {
    static Scanner scanner = new Scanner(System.in);
    static String currentUserType;  // "User" or "Company"
    static int currentUserId;       // Store user or company ID after login
    
    public static void main(String[] args) {
        showInitialPage();
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
            // Check credentials in the database (Pengguna table)
            Pengguna pengguna = Pengguna.login(email, password);
            if (pengguna != null) {
                System.out.println("Login berhasil! Selamat datang, " + pengguna.getNama());
                return pengguna.getIdPengguna();
            }
        } else if (userType.equals("Company")) {
            // Check credentials in the database (Perusahaan table)
            Perusahaan perusahaan = Perusahaan.login(email, password);
            if (perusahaan != null) {
                System.out.println("Login berhasil! Selamat datang, " + perusahaan.getNama());
                return perusahaan.getIdPerusahaan();
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
        
        if (userType.equals("User")) {
            Pengguna pengguna = new Pengguna(0, nama, alamat, email, nomorKontak, 0);
            pengguna.saveToDatabase();
            System.out.println("Registrasi berhasil!");
            showInitialPage();
        } else if (userType.equals("Company")) {
            Perusahaan perusahaan = new Perusahaan(0, nama, alamat, email, nomorKontak, 0);
            perusahaan.saveToDatabase();
            System.out.println("Registrasi berhasil!");
            showInitialPage();
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
                // Tampilkan profil pengguna
                break;
            case 2:
                // Lihat saldo
                break;
            case 3:
                // Top up saldo (page melayang)
                break;
            case 4:
                // Cari dan sewa alat (rent tool user)
                showRentToolUserPage();
                break;
            case 5:
                // Beli pupuk (page melayang)
                break;
            case 6:
                // Inventory
                showInventoryPage();
                break;
            case 7:
                // History transaksi
                break;
            case 8:
                // Logout
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
        System.out.println("4. Pasang Alat (Sewa)");
        System.out.println("5. Keluhan (Complaint)");
        System.out.println("6. History Transaksi");
        System.out.println("7. Logout");

        int choice = scanner.nextInt();
        
        switch (choice) {
            case 1:
                // Tampilkan profil perusahaan
                break;
            case 2:
                // Lihat saldo
                break;
            case 3:
                // Tentukan harga pupuk
                break;
            case 4:
                // Pasang alat sewa (rent tool company)
                showRentToolCompanyPage();
                break;
            case 5:
                // Daftar keluhan (complaint)
                showComplaintPage();
                break;
            case 6:
                // History transaksi
                break;
            case 7:
                // Logout
                showInitialPage();
                break;
            default:
                System.out.println("Pilihan tidak valid, coba lagi.");
                showCompanyHomePage();
                break;
        }
    }

    // Halaman sewa alat untuk pengguna
    public static void showRentToolUserPage() {
        System.out.println("----- Sewa Alat (Pengguna) -----");
        System.out.println("Cari alat berdasarkan kategori:");
        // Lakukan pencarian alat dan proses sewa (interaksi dengan database AlatPertanian)
    }

    // Halaman inventory untuk pengguna
    public static void showInventoryPage() {
        System.out.println("----- Inventory -----");
        // Lihat alat yang disewa, waktu sewa, ajukan keluhan (interaksi dengan Keluhan table)
    }

    // Halaman sewa alat untuk perusahaan
    public static void showRentToolCompanyPage() {
        System.out.println("----- Pasang Alat Sewa (Perusahaan) -----");
        // Upload alat baru untuk disewakan (interaksi dengan AlatPertanian)
    }

    // Halaman keluhan untuk perusahaan
    public static void showComplaintPage() {
        System.out.println("----- Keluhan (Complaint) -----");
        // Lihat keluhan dari pengguna terkait alat yang disewa (interaksi dengan Keluhan)
    }
}