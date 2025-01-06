package model;

public class Pupuk {
    private int idPupuk;
    private String namaPupuk;
    private int hargaPerKg;
    private int stok;
    private String jenisPupuk;
    private Perusahaan company;
    private String imageHash;

    // Constructor
    public Pupuk(int idPupuk, String namaPupuk, int hargaPerKg,int stok, String jenisPupuk, Perusahaan company, String imageHash) {
        this.idPupuk = idPupuk;
        this.namaPupuk = namaPupuk;
        this.hargaPerKg = hargaPerKg;
        this.company = company;
        this.stok = stok;
        this.jenisPupuk = jenisPupuk;
         this.imageHash = imageHash;       
    }
    
    // Getter dan Setter
    public int getIdPupuk() {
        return idPupuk;
    }

    public void setIdPupuk(int idPupuk) {
        this.idPupuk = idPupuk;
    }

    public String getNamaPupuk() {
        return namaPupuk;
    }

    public void setNamaPupuk(String namaPupuk) {
        this.namaPupuk = namaPupuk;
    }

    public int getHargaPerKg() {
        return hargaPerKg;
    }

    public void setHargaPerKg(int hargaPerKg) {
        this.hargaPerKg = hargaPerKg;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getJenisPupuk() {
        return jenisPupuk;
    }

    public void setJenisPupuk(String jenisPupuk) {
        this.jenisPupuk = jenisPupuk;
    }
    
    public Perusahaan getCompany() {
        return company;
    }

    public void setCompany(Perusahaan company) {
        this.company = company;
    }
      public String getImageHash() {
        return imageHash;
    }

    public void setImageHash(String imageHash) {
        this.imageHash = imageHash;
    }
   
}