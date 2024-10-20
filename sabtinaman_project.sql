-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 20 Okt 2024 pada 19.35
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sabtinamann`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `alat_pertanian`
--

CREATE TABLE `alat_pertanian` (
  `id_alat` int(255) NOT NULL,
  `nama_alat` text NOT NULL,
  `kategori` varchar(100) NOT NULL,
  `spesifikasi` varchar(20) DEFAULT NULL,
  `harga_sewa` int(20) DEFAULT NULL,
  `status_ketersediaan` varchar(200) NOT NULL,
  `gambar` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `kategori_alat_pertanian`
--

CREATE TABLE `kategori_alat_pertanian` (
  `id_kategori_alat` int(100) NOT NULL,
  `id_alat_pertanian` int(100) NOT NULL,
  `nama_kategori` varchar(100) NOT NULL,
  `deskripsi` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `kategori_pupuk`
--

CREATE TABLE `kategori_pupuk` (
  `id_kategori_pupuk` int(100) NOT NULL,
  `id_pupuk` int(11) NOT NULL,
  `nama_kategori` varchar(100) NOT NULL,
  `deskripsi` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `keluhan`
--

CREATE TABLE `keluhan` (
  `Id_keluhan` int(100) NOT NULL,
  `id_transaksi_sewa` int(100) NOT NULL,
  `deskripsi_masalah` text NOT NULL,
  `tanggal_laporan` varchar(20) NOT NULL,
  `status` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `pengguna`
--

CREATE TABLE `pengguna` (
  `id_pengguna` int(255) NOT NULL,
  `nama` text NOT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `email` varchar(20) DEFAULT NULL,
  `nomor_kontak` int(20) DEFAULT NULL,
  `saldo_pengguna` int(200) NOT NULL,
  `password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `pengguna`
--

INSERT INTO `pengguna` (`id_pengguna`, `nama`, `alamat`, `email`, `nomor_kontak`, `saldo_pengguna`, `password`) VALUES
(1, 'wa', 'aaw', 'awa@', 569877, 0, '');

-- --------------------------------------------------------

--
-- Struktur dari tabel `perusahaan`
--

CREATE TABLE `perusahaan` (
  `id_perusahaan` int(100) NOT NULL,
  `nama` text NOT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `email` varchar(20) DEFAULT NULL,
  `nomor_kontak` int(20) DEFAULT NULL,
  `saldo_perusahaan` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `pupuk`
--

CREATE TABLE `pupuk` (
  `id_pupuk` int(255) NOT NULL,
  `nama_pupuk` text NOT NULL,
  `kategori` varchar(100) NOT NULL,
  `harga` int(20) NOT NULL,
  `stok` int(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksi pupuk`
--

CREATE TABLE `transaksi pupuk` (
  `id_transaksi` int(100) NOT NULL,
  `id_pengguna` int(100) NOT NULL,
  `id_pupuk` int(100) NOT NULL,
  `tanggal_pembelian` int(20) NOT NULL,
  `jumlah` int(20) NOT NULL,
  `total_pembayaran` int(100) NOT NULL,
  `total_harga` int(100) NOT NULL,
  `status_pembayaran` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksi sewa`
--

CREATE TABLE `transaksi sewa` (
  `id_transaksi` int(100) NOT NULL,
  `id_pengguna` int(100) NOT NULL,
  `id_alat` int(100) NOT NULL,
  `tanggal_mulai_sewa` int(20) NOT NULL,
  `tanggal_akhir_sewa` int(20) DEFAULT NULL,
  `status_pembayaran` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksi_pengguna_saldo`
--

CREATE TABLE `transaksi_pengguna_saldo` (
  `id_transaksi_saldo` int(100) NOT NULL,
  `id_pengguna` int(100) NOT NULL,
  `tanggal_transaksi` int(100) NOT NULL,
  `jenis_transaksi` varchar(200) NOT NULL,
  `jumlah` int(200) NOT NULL,
  `saldo` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `kategori_alat_pertanian`
--
ALTER TABLE `kategori_alat_pertanian`
  ADD PRIMARY KEY (`id_kategori_alat`),
  ADD KEY `id_alat_pertanian` (`id_alat_pertanian`);

--
-- Indeks untuk tabel `kategori_pupuk`
--
ALTER TABLE `kategori_pupuk`
  ADD PRIMARY KEY (`id_kategori_pupuk`),
  ADD KEY `id_pupuk` (`id_pupuk`);

--
-- Indeks untuk tabel `keluhan`
--
ALTER TABLE `keluhan`
  ADD PRIMARY KEY (`Id_keluhan`),
  ADD KEY `id_transaksi_sewa` (`id_transaksi_sewa`);

--
-- Indeks untuk tabel `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`id_pengguna`);

--
-- Indeks untuk tabel `perusahaan`
--
ALTER TABLE `perusahaan`
  ADD PRIMARY KEY (`id_perusahaan`);

--
-- Indeks untuk tabel `pupuk`
--
ALTER TABLE `pupuk`
  ADD PRIMARY KEY (`id_pupuk`);

--
-- Indeks untuk tabel `transaksi pupuk`
--
ALTER TABLE `transaksi pupuk`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `transaksi pupuk_ibfk_1` (`id_pengguna`),
  ADD KEY `transaksi pupuk_ibfk_2` (`id_pupuk`);

--
-- Indeks untuk tabel `transaksi sewa`
--
ALTER TABLE `transaksi sewa`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `transaksi sewa_ibfk_1` (`id_pengguna`),
  ADD KEY `transaksi sewa_ibfk_2` (`id_alat`);

--
-- Indeks untuk tabel `transaksi_pengguna_saldo`
--
ALTER TABLE `transaksi_pengguna_saldo`
  ADD PRIMARY KEY (`id_transaksi_saldo`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `kategori_alat_pertanian`
--
ALTER TABLE `kategori_alat_pertanian`
  MODIFY `id_kategori_alat` int(100) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `kategori_pupuk`
--
ALTER TABLE `kategori_pupuk`
  MODIFY `id_kategori_pupuk` int(100) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `keluhan`
--
ALTER TABLE `keluhan`
  MODIFY `Id_keluhan` int(100) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `pengguna`
--
ALTER TABLE `pengguna`
  MODIFY `id_pengguna` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `perusahaan`
--
ALTER TABLE `perusahaan`
  MODIFY `id_perusahaan` int(100) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `pupuk`
--
ALTER TABLE `pupuk`
  MODIFY `id_pupuk` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `transaksi pupuk`
--
ALTER TABLE `transaksi pupuk`
  MODIFY `id_transaksi` int(100) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `transaksi sewa`
--
ALTER TABLE `transaksi sewa`
  MODIFY `id_transaksi` int(100) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `transaksi_pengguna_saldo`
--
ALTER TABLE `transaksi_pengguna_saldo`
  MODIFY `id_transaksi_saldo` int(100) NOT NULL AUTO_INCREMENT;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `kategori_alat_pertanian`
--
ALTER TABLE `kategori_alat_pertanian`
  ADD CONSTRAINT `kategori_alat_pertanian_ibfk_1` FOREIGN KEY (`id_alat_pertanian`) REFERENCES `alat pertanian` (`id_alat`);

--
-- Ketidakleluasaan untuk tabel `kategori_pupuk`
--
ALTER TABLE `kategori_pupuk`
  ADD CONSTRAINT `kategori_pupuk_ibfk_1` FOREIGN KEY (`id_pupuk`) REFERENCES `pupuk` (`id_pupuk`);

--
-- Ketidakleluasaan untuk tabel `keluhan`
--
ALTER TABLE `keluhan`
  ADD CONSTRAINT `keluhan_ibfk_1` FOREIGN KEY (`id_transaksi_sewa`) REFERENCES `transaksi sewa` (`id_transaksi`);

--
-- Ketidakleluasaan untuk tabel `transaksi pupuk`
--
ALTER TABLE `transaksi pupuk`
  ADD CONSTRAINT `transaksi pupuk_ibfk_1` FOREIGN KEY (`id_pengguna`) REFERENCES `pengguna` (`id_pengguna`),
  ADD CONSTRAINT `transaksi pupuk_ibfk_2` FOREIGN KEY (`id_pupuk`) REFERENCES `pupuk` (`id_pupuk`);

--
-- Ketidakleluasaan untuk tabel `transaksi sewa`
--
ALTER TABLE `transaksi sewa`
  ADD CONSTRAINT `transaksi sewa_ibfk_1` FOREIGN KEY (`id_pengguna`) REFERENCES `pengguna` (`id_pengguna`),
  ADD CONSTRAINT `transaksi sewa_ibfk_2` FOREIGN KEY (`id_alat`) REFERENCES `alat pertanian` (`id_alat`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
