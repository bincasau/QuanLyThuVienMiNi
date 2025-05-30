-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Máy chủ: localhost
-- Thời gian đã tạo: Th5 21, 2025 lúc 02:58 PM
-- Phiên bản máy phục vụ: 10.4.28-MariaDB
-- Phiên bản PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `IS216`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `DocGia`
--

CREATE TABLE `DocGia` (
  `maNguoiDung` char(5) NOT NULL,
  `tenNguoiDung` varchar(50) NOT NULL,
  `taiKhoan` varchar(50) NOT NULL,
  `matKhau` varchar(100) NOT NULL,
  `email` varchar(50) NOT NULL,
  `soDienThoai` varchar(10) NOT NULL,
  `ngayTao` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `LichSuMuonSach`
--

CREATE TABLE `LichSuMuonSach` (
  `maLichSu` char(5) NOT NULL,
  `ngayMuon` date NOT NULL,
  `ngayTra` date NOT NULL,
  `trangThai` varchar(50) NOT NULL,
  `maSach` char(5) DEFAULT NULL,
  `maThuThu` char(5) NOT NULL,
  `maDocGia` char(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Cấu trúc bảng cho bảng `PhieuPhat`
--

CREATE TABLE `PhieuPhat` (
  `maPhieuPhat` char(5) NOT NULL,
  `loi` varchar(50) NOT NULL,
  `giaTien` double NOT NULL,
  `maDocGia` char(5) NOT NULL,
  `maSach` char(5) DEFAULT NULL,
  `ngayPhieu` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `Sach`
--

CREATE TABLE `Sach` (
  `maSach` char(5) NOT NULL,
  `tenSach` varchar(50) NOT NULL,
  `nhaXB` varchar(50) NOT NULL,
  `namXB` int(11) NOT NULL,
  `gia` double NOT NULL,
  `anh` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Cấu trúc bảng cho bảng `Sach_TheLoai`
--

CREATE TABLE `Sach_TheLoai` (
  `maSach` char(5) NOT NULL,
  `maTheLoai` char(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Cấu trúc bảng cho bảng `TheLoai`
--

CREATE TABLE `TheLoai` (
  `maTheLoai` char(5) NOT NULL,
  `tenTheLoai` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Cấu trúc bảng cho bảng `ThuThu`
--

CREATE TABLE `ThuThu` (
  `maNguoiDung` char(5) NOT NULL,
  `tenNguoiDung` varchar(50) NOT NULL,
  `taiKhoan` varchar(50) NOT NULL,
  `matKhau` varchar(100) NOT NULL,
  `email` varchar(50) NOT NULL,
  `soDienThoai` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `DocGia`
--
ALTER TABLE `DocGia`
  ADD PRIMARY KEY (`maNguoiDung`);

--
-- Chỉ mục cho bảng `LichSuMuonSach`
--
ALTER TABLE `LichSuMuonSach`
  ADD PRIMARY KEY (`maLichSu`),
  ADD KEY `FK_LS1` (`maDocGia`),
  ADD KEY `FK_LS2` (`maThuThu`),
  ADD KEY `FK_LS3` (`maSach`);

--
-- Chỉ mục cho bảng `PhieuPhat`
--
ALTER TABLE `PhieuPhat`
  ADD PRIMARY KEY (`maPhieuPhat`),
  ADD KEY `FK_PP1` (`maDocGia`);

--
-- Chỉ mục cho bảng `Sach`
--
ALTER TABLE `Sach`
  ADD PRIMARY KEY (`maSach`);

--
-- Chỉ mục cho bảng `Sach_TheLoai`
--
ALTER TABLE `Sach_TheLoai`
  ADD PRIMARY KEY (`maSach`,`maTheLoai`);

--
-- Chỉ mục cho bảng `TheLoai`
--
ALTER TABLE `TheLoai`
  ADD PRIMARY KEY (`maTheLoai`);

--
-- Chỉ mục cho bảng `ThuThu`
--
ALTER TABLE `ThuThu`
  ADD PRIMARY KEY (`maNguoiDung`);

--
-- Ràng buộc đối với các bảng kết xuất
--

--
-- Ràng buộc cho bảng `LichSuMuonSach`
--
ALTER TABLE `LichSuMuonSach`
  ADD CONSTRAINT `FK_LS1` FOREIGN KEY (`maDocGia`) REFERENCES `DocGia` (`maNguoiDung`),
  ADD CONSTRAINT `FK_LS2` FOREIGN KEY (`maThuThu`) REFERENCES `ThuThu` (`maNguoiDung`),
  ADD CONSTRAINT `FK_LS3` FOREIGN KEY (`maSach`) REFERENCES `Sach` (`maSach`);

--
-- Ràng buộc cho bảng `PhieuPhat`
--
ALTER TABLE `PhieuPhat`
  ADD CONSTRAINT `FK_PP1` FOREIGN KEY (`maDocGia`) REFERENCES `DocGia` (`maNguoiDung`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
