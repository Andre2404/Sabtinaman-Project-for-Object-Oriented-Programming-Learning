    package model;

public class Alat {
    private int idAlat;
    private String namaAlat;
    private String spesifikasi;
    private int hargaSewa;
    private Perusahaan company;  // Alat disewakan oleh perusahaan
    private String status;
    private int stok;
    private byte[] image;
    private String imageHash;
    private String imagePath; // Menyimpan path gambar

    public Alat(int idAlat, String namaAlat, String spesifikasi, Perusahaan company, int hargaSewa, String status, int stok, String imageHash) {
        this.idAlat = idAlat;
        this.namaAlat = namaAlat;
        this.spesifikasi = spesifikasi;
        this.hargaSewa = hargaSewa;
        this.company = company;
        this.status = status;
        this.stok = stok;
        this.imageHash = imageHash;
        this.imagePath = imagePath;
        this.image = image;
    }
    
    public int getIdAlat() {
        return idAlat;
    }

    public void setIdAlat(int idAlat) {
        this.idAlat = idAlat;
    }

    public String getNamaAlat() {
        return namaAlat;
    }

    public void setNamaAlat(String namaAlat) {
        this.namaAlat = namaAlat;
    }

    public String getSpesifikasi() {
        return spesifikasi;
    }

    public void setSpesifikasi(String spesifikasi) {
        this.spesifikasi = spesifikasi;
    }

    public int getHargaSewa() {
        return hargaSewa;
    }

    public void setHargaSewa(int hargaSewa) {
        this.hargaSewa = hargaSewa;
    }

    public Perusahaan getCompany() {
        return company;
    }

    public void setCompany(Perusahaan company) {
        this.company = company;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
    
    public byte[] getImage() {
        return image;
    }

    public String getImageHash() {
        return imageHash;
    }

    public void setImageHash(String imageHash) {
        this.imageHash = imageHash;
    }
    
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
    
