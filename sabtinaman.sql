-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 23 Okt 2024 pada 12.32
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
-- Struktur dari tabel `alat`
--

CREATE TABLE `alat` (
  `id_alat` int(11) NOT NULL,
  `nama_alat` varchar(255) NOT NULL,
  `spesifikasi` text NOT NULL,
  `harga_sewa` double NOT NULL,
  `id_perusahaan` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `alat`
--

INSERT INTO `alat` (`id_alat`, `nama_alat`, `spesifikasi`, `harga_sewa`, `id_perusahaan`) VALUES
(2, 'sabtina', 'bisa nyangkul', 300, 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `keluhan`
--

CREATE TABLE `keluhan` (
  `id_keluhan` int(11) NOT NULL,
  `id_pengguna` int(11) DEFAULT NULL,
  `id_alat` int(11) DEFAULT NULL,
  `id_transaksi_sewa` int(11) DEFAULT NULL,
  `deskripsi_masalah` text DEFAULT NULL,
  `tanggal_laporan` timestamp NOT NULL DEFAULT current_timestamp(),
  `status` varchar(50) DEFAULT 'Pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `pengguna`
--

CREATE TABLE `pengguna` (
  `id_pengguna` int(11) NOT NULL,
  `nama` varchar(100) DEFAULT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `nomor_kontak` varchar(15) DEFAULT NULL,
  `saldo` decimal(10,2) DEFAULT 0.00,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `pengguna`
--

INSERT INTO `pengguna` (`id_pengguna`, `nama`, `alamat`, `email`, `nomor_kontak`, `saldo`, `password`) VALUES
(1, 'ass', 'ass', 'ass', '123', 12820.00, '123');

-- --------------------------------------------------------

--
-- Struktur dari tabel `perusahaan`
--

CREATE TABLE `perusahaan` (
  `id_perusahaan` int(11) NOT NULL,
  `nama` varchar(100) DEFAULT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `nomor_kontak` varchar(15) DEFAULT NULL,
  `saldo_perusahaan` decimal(10,2) DEFAULT 0.00,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `perusahaan`
--

INSERT INTO `perusahaan` (`id_perusahaan`, `nama`, `alamat`, `email`, `nomor_kontak`, `saldo_perusahaan`, `password`) VALUES
(1, 'ass', 'ass', 'ass', '123', 0.00, '123');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pupuk`
--

CREATE TABLE `pupuk` (
  `id_pupuk` int(11) NOT NULL,
  `nama_pupuk` varchar(100) DEFAULT NULL,
  `harga_per_kg` decimal(10,2) DEFAULT NULL,
  `id_perusahaan` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `pupuk`
--

INSERT INTO `pupuk` (`id_pupuk`, `nama_pupuk`, `harga_per_kg`, `id_perusahaan`) VALUES
(1, 'ija', 50.00, 1),
(2, 'toriq', 200.00, 1),
(3, 'sab', 230.00, 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `saldo`
--

CREATE TABLE `saldo` (
  `id_saldo` int(11) NOT NULL,
  `id_pengguna` int(11) DEFAULT NULL,
  `id_perusahaan` int(11) DEFAULT NULL,
  `jumlah` decimal(10,2) DEFAULT NULL,
  `tipe_saldo` enum('topup','payment','refund') NOT NULL,
  `tanggal_transaksi` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksipupuk`
--

CREATE TABLE `transaksipupuk` (
  `id_transaksi` int(11) NOT NULL,
  `id_pengguna` int(11) DEFAULT NULL,
  `id_pupuk` int(11) DEFAULT NULL,
  `jumlah_kg` decimal(10,2) DEFAULT NULL,
  `total_harga` int(100) NOT NULL,
  `tanggal_beli` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksisewa`
--

CREATE TABLE `transaksisewa` (
  `id_transaksi` int(11) NOT NULL,
  `id_pengguna` int(11) DEFAULT NULL,
  `id_alat` int(11) DEFAULT NULL,
  `tanggal_sewa` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `alat`
--
ALTER TABLE `alat`
  ADD PRIMARY KEY (`id_alat`),
  ADD KEY `id_perusahaan` (`id_perusahaan`);

--
-- Indeks untuk tabel `keluhan`
--
ALTER TABLE `keluhan`
  ADD PRIMARY KEY (`id_keluhan`),
  ADD KEY `id_pengguna` (`id_pengguna`),
  ADD KEY `id_alat` (`id_alat`),
  ADD KEY `id_transaksi_sewa` (`id_transaksi_sewa`);

--
-- Indeks untuk tabel `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`id_pengguna`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indeks untuk tabel `perusahaan`
--
ALTER TABLE `perusahaan`
  ADD PRIMARY KEY (`id_perusahaan`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indeks untuk tabel `pupuk`
--
ALTER TABLE `pupuk`
  ADD PRIMARY KEY (`id_pupuk`),
  ADD KEY `id_perusahaan` (`id_perusahaan`);

--
-- Indeks untuk tabel `saldo`
--
ALTER TABLE `saldo`
  ADD PRIMARY KEY (`id_saldo`),
  ADD KEY `id_pengguna` (`id_pengguna`),
  ADD KEY `id_perusahaan` (`id_perusahaan`);

--
-- Indeks untuk tabel `transaksipupuk`
--
ALTER TABLE `transaksipupuk`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `id_pengguna` (`id_pengguna`),
  ADD KEY `id_pupuk` (`id_pupuk`);

--
-- Indeks untuk tabel `transaksisewa`
--
ALTER TABLE `transaksisewa`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `id_pengguna` (`id_pengguna`),
  ADD KEY `id_alat` (`id_alat`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `alat`
--
ALTER TABLE `alat`
  MODIFY `id_alat` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT untuk tabel `keluhan`
--
ALTER TABLE `keluhan`
  MODIFY `id_keluhan` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `pengguna`
--
ALTER TABLE `pengguna`
  MODIFY `id_pengguna` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `perusahaan`
--
ALTER TABLE `perusahaan`
  MODIFY `id_perusahaan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `pupuk`
--
ALTER TABLE `pupuk`
  MODIFY `id_pupuk` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `saldo`
--
ALTER TABLE `saldo`
  MODIFY `id_saldo` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `transaksipupuk`
--
ALTER TABLE `transaksipupuk`
  MODIFY `id_transaksi` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `transaksisewa`
--
ALTER TABLE `transaksisewa`
  MODIFY `id_transaksi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `alat`
--
ALTER TABLE `alat`
  ADD CONSTRAINT `alat_ibfk_1` FOREIGN KEY (`id_perusahaan`) REFERENCES `perusahaan` (`id_perusahaan`);

--
-- Ketidakleluasaan untuk tabel `keluhan`
--
ALTER TABLE `keluhan`
  ADD CONSTRAINT `keluhan_ibfk_1` FOREIGN KEY (`id_pengguna`) REFERENCES `pengguna` (`id_pengguna`) ON DELETE CASCADE,
  ADD CONSTRAINT `keluhan_ibfk_2` FOREIGN KEY (`id_alat`) REFERENCES `alat` (`id_alat`) ON DELETE CASCADE,
  ADD CONSTRAINT `keluhan_ibfk_3` FOREIGN KEY (`id_transaksi_sewa`) REFERENCES `transaksisewa` (`id_transaksi`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `pupuk`
--
ALTER TABLE `pupuk`
  ADD CONSTRAINT `pupuk_ibfk_1` FOREIGN KEY (`id_perusahaan`) REFERENCES `perusahaan` (`id_perusahaan`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `saldo`
--
ALTER TABLE `saldo`
  ADD CONSTRAINT `saldo_ibfk_1` FOREIGN KEY (`id_pengguna`) REFERENCES `pengguna` (`id_pengguna`) ON DELETE CASCADE,
  ADD CONSTRAINT `saldo_ibfk_2` FOREIGN KEY (`id_perusahaan`) REFERENCES `perusahaan` (`id_perusahaan`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `transaksipupuk`
--
ALTER TABLE `transaksipupuk`
  ADD CONSTRAINT `transaksipupuk_ibfk_1` FOREIGN KEY (`id_pengguna`) REFERENCES `pengguna` (`id_pengguna`) ON DELETE CASCADE,
  ADD CONSTRAINT `transaksipupuk_ibfk_2` FOREIGN KEY (`id_pupuk`) REFERENCES `pupuk` (`id_pupuk`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `transaksisewa`
--
ALTER TABLE `transaksisewa`
  ADD CONSTRAINT `transaksisewa_ibfk_1` FOREIGN KEY (`id_pengguna`) REFERENCES `pengguna` (`id_pengguna`) ON DELETE CASCADE,
  ADD CONSTRAINT `transaksisewa_ibfk_2` FOREIGN KEY (`id_alat`) REFERENCES `alat` (`id_alat`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
