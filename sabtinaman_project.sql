-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 20, 2024 at 02:39 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sabtinaman_project`
--

-- --------------------------------------------------------

--
-- Table structure for table `alat_pertanian`
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
-- Table structure for table `kategori_alat_pertanian`
--

CREATE TABLE `kategori_alat_pertanian` (
  `id_kategori_alat` int(100) NOT NULL,
  `id_alat_pertanian` int(100) NOT NULL,
  `nama_kategori` varchar(100) NOT NULL,
  `deskripsi` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `kategori_pupuk`
--

CREATE TABLE `kategori_pupuk` (
  `id_kategori_pupuk` int(100) NOT NULL,
  `id_pupuk` int(11) NOT NULL,
  `nama_kategori` varchar(100) NOT NULL,
  `deskripsi` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `keluhan`
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
-- Table structure for table `pengguna`
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
-- Table structure for table `perusahaan`
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
-- Table structure for table `pupuk`
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
-- Table structure for table `transaksi_pupuk`
--

CREATE TABLE `transaksi_pupuk` (
  `Id Transaksi` int(100) NOT NULL,
  `Id Pengguna` int(100) NOT NULL,
  `Id Pupuk` int(100) NOT NULL,
  `Tanggal mulai sewa` int(20) NOT NULL,
  `Tanggal akhir sewa` int(20) NOT NULL,
  `Status pembayaran` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `transaksi_sewa`
--

CREATE TABLE `transaksi_sewa` (
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
-- Indexes for table `keluhan`
--
ALTER TABLE `keluhan`
  ADD PRIMARY KEY (`Id Keluhan`),
  ADD KEY `keluhan_ibfk_1` (`Id pengguna`),
  ADD KEY `keluhan_ibfk_2` (`Id alat`);

--
-- Indexes for table `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`Id pengguna`);

--
-- Indexes for table `perusahaan`
--
ALTER TABLE `perusahaan`
  ADD PRIMARY KEY (`Id perusahaan`);

--
-- Indexes for table `pupuk`
--
ALTER TABLE `pupuk`
  ADD PRIMARY KEY (`Id Pupuk`);

--
-- Indexes for table `transaksi_pupuk`
--
ALTER TABLE `transaksi_pupuk`
  ADD PRIMARY KEY (`Id Transaksi`),
  ADD KEY `transaksi pupuk_ibfk_1` (`Id Pengguna`),
  ADD KEY `transaksi pupuk_ibfk_2` (`Id Pupuk`);

--
-- Indexes for table `transaksi_sewa`
--
ALTER TABLE `transaksi_sewa`
  ADD PRIMARY KEY (`Id Transaksi`),
  ADD KEY `transaksi sewa_ibfk_1` (`Id Pengguna`),
  ADD KEY `transaksi sewa_ibfk_2` (`Id alat`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `keluhan`
--
ALTER TABLE `keluhan`
  MODIFY `Id Keluhan` int(100) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pengguna`
--
ALTER TABLE `pengguna`
  MODIFY `Id pengguna` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `perusahaan`
--
ALTER TABLE `perusahaan`
  MODIFY `Id perusahaan` int(100) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pupuk`
--
ALTER TABLE `pupuk`
  MODIFY `Id Pupuk` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `transaksi_pupuk`
--
ALTER TABLE `transaksi_pupuk`
  MODIFY `Id Transaksi` int(100) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `transaksi_sewa`
--
ALTER TABLE `transaksi_sewa`
  MODIFY `Id Transaksi` int(100) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `keluhan`
--
ALTER TABLE `keluhan`
  ADD CONSTRAINT `keluhan_ibfk_1` FOREIGN KEY (`Id pengguna`) REFERENCES `pengguna` (`Id pengguna`),
  ADD CONSTRAINT `keluhan_ibfk_2` FOREIGN KEY (`Id alat`) REFERENCES `alat pertanian` (`Id alat`);

--
-- Constraints for table `transaksi_pupuk`
--
ALTER TABLE `transaksi_pupuk`
  ADD CONSTRAINT `transaksi_pupuk_ibfk_1` FOREIGN KEY (`Id Pengguna`) REFERENCES `pengguna` (`Id pengguna`),
  ADD CONSTRAINT `transaksi_pupuk_ibfk_2` FOREIGN KEY (`Id Pupuk`) REFERENCES `pupuk` (`Id Pupuk`);

--
-- Constraints for table `transaksi_sewa`
--
ALTER TABLE `transaksi_sewa`
  ADD CONSTRAINT `transaksi_sewa_ibfk_1` FOREIGN KEY (`Id Pengguna`) REFERENCES `pengguna` (`Id pengguna`),
  ADD CONSTRAINT `transaksi_sewa_ibfk_2` FOREIGN KEY (`Id alat`) REFERENCES `alat pertanian` (`Id alat`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
