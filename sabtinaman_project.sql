-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 06 Okt 2024 pada 18.00
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
-- Database: `sabtinaman project`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `alat pertanian`
--

CREATE TABLE `alat pertanian` (
  `Id alat` int(255) NOT NULL,
  `Nama alat` text NOT NULL,
  `Kategori` varchar(100) NOT NULL,
  `Spesifikasi` varchar(20) DEFAULT NULL,
  `Harga sewa` int(20) DEFAULT NULL,
  `Status ketersediaan` varchar(200) NOT NULL,
  `Gambar` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `keluhan`
--

CREATE TABLE `keluhan` (
  `Id Keluhan` int(100) NOT NULL,
  `Id pengguna` int(100) NOT NULL,
  `Id alat` int(100) NOT NULL,
  `Deskripsi masalah` text NOT NULL,
  `Tanggal laporan` varchar(20) NOT NULL,
  `Status` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `pengguna`
--

CREATE TABLE `pengguna` (
  `Id pengguna` int(255) NOT NULL,
  `Nama` text NOT NULL,
  `Alamat` varchar(255) DEFAULT NULL,
  `Email` varchar(20) DEFAULT NULL,
  `Nomor kontak` int(20) DEFAULT NULL,
  `Saldo pengguna` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `perusahaan`
--

CREATE TABLE `perusahaan` (
  `Id perusahaan` int(100) NOT NULL,
  `Nama` text NOT NULL,
  `Alamat` varchar(255) DEFAULT NULL,
  `Email` varchar(20) DEFAULT NULL,
  `Nomor kontak` int(20) DEFAULT NULL,
  `Saldo perusahaan` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `pupuk`
--

CREATE TABLE `pupuk` (
  `Id Pupuk` int(255) NOT NULL,
  `Nama pupuk` text NOT NULL,
  `Kategori` varchar(100) NOT NULL,
  `Harga` int(20) NOT NULL,
  `Stok` int(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksi pupuk`
--

CREATE TABLE `transaksi pupuk` (
  `Id Transaksi` int(100) NOT NULL,
  `Id Pengguna` int(100) NOT NULL,
  `Id Pupuk` int(100) NOT NULL,
  `Tanggal mulai sewa` int(20) NOT NULL,
  `Tanggal akhir sewa` int(20) NOT NULL,
  `Status pembayaran` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksi sewa`
--

CREATE TABLE `transaksi sewa` (
  `Id Transaksi` int(100) NOT NULL,
  `Id Pengguna` int(100) NOT NULL,
  `Id alat` int(100) NOT NULL,
  `Tanggal mulai sewa` int(20) NOT NULL,
  `Tanggal akhir sewa` int(20) DEFAULT NULL,
  `Status pembayaran` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `alat pertanian`
--
ALTER TABLE `alat pertanian`
  ADD PRIMARY KEY (`Id alat`);

--
-- Indeks untuk tabel `keluhan`
--
ALTER TABLE `keluhan`
  ADD PRIMARY KEY (`Id Keluhan`),
  ADD KEY `keluhan_ibfk_1` (`Id pengguna`),
  ADD KEY `keluhan_ibfk_2` (`Id alat`);

--
-- Indeks untuk tabel `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`Id pengguna`);

--
-- Indeks untuk tabel `perusahaan`
--
ALTER TABLE `perusahaan`
  ADD PRIMARY KEY (`Id perusahaan`);

--
-- Indeks untuk tabel `pupuk`
--
ALTER TABLE `pupuk`
  ADD PRIMARY KEY (`Id Pupuk`);

--
-- Indeks untuk tabel `transaksi pupuk`
--
ALTER TABLE `transaksi pupuk`
  ADD PRIMARY KEY (`Id Transaksi`),
  ADD KEY `transaksi pupuk_ibfk_1` (`Id Pengguna`),
  ADD KEY `transaksi pupuk_ibfk_2` (`Id Pupuk`);

--
-- Indeks untuk tabel `transaksi sewa`
--
ALTER TABLE `transaksi sewa`
  ADD PRIMARY KEY (`Id Transaksi`),
  ADD KEY `transaksi sewa_ibfk_1` (`Id Pengguna`),
  ADD KEY `transaksi sewa_ibfk_2` (`Id alat`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `alat pertanian`
--
ALTER TABLE `alat pertanian`
  MODIFY `Id alat` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `keluhan`
--
ALTER TABLE `keluhan`
  MODIFY `Id Keluhan` int(100) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `pengguna`
--
ALTER TABLE `pengguna`
  MODIFY `Id pengguna` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `perusahaan`
--
ALTER TABLE `perusahaan`
  MODIFY `Id perusahaan` int(100) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `pupuk`
--
ALTER TABLE `pupuk`
  MODIFY `Id Pupuk` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `transaksi pupuk`
--
ALTER TABLE `transaksi pupuk`
  MODIFY `Id Transaksi` int(100) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `transaksi sewa`
--
ALTER TABLE `transaksi sewa`
  MODIFY `Id Transaksi` int(100) NOT NULL AUTO_INCREMENT;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `keluhan`
--
ALTER TABLE `keluhan`
  ADD CONSTRAINT `keluhan_ibfk_1` FOREIGN KEY (`Id pengguna`) REFERENCES `pengguna` (`Id pengguna`),
  ADD CONSTRAINT `keluhan_ibfk_2` FOREIGN KEY (`Id alat`) REFERENCES `alat pertanian` (`Id alat`);

--
-- Ketidakleluasaan untuk tabel `transaksi pupuk`
--
ALTER TABLE `transaksi pupuk`
  ADD CONSTRAINT `transaksi pupuk_ibfk_1` FOREIGN KEY (`Id Pengguna`) REFERENCES `pengguna` (`Id pengguna`),
  ADD CONSTRAINT `transaksi pupuk_ibfk_2` FOREIGN KEY (`Id Pupuk`) REFERENCES `pupuk` (`Id Pupuk`);

--
-- Ketidakleluasaan untuk tabel `transaksi sewa`
--
ALTER TABLE `transaksi sewa`
  ADD CONSTRAINT `transaksi sewa_ibfk_1` FOREIGN KEY (`Id Pengguna`) REFERENCES `pengguna` (`Id pengguna`),
  ADD CONSTRAINT `transaksi sewa_ibfk_2` FOREIGN KEY (`Id alat`) REFERENCES `alat pertanian` (`Id alat`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
