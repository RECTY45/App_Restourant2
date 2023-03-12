/*
Navicat MySQL Data Transfer

Source Server         : db
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : db_resto2

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2023-03-12 19:20:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tbl_menu`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_menu`;
CREATE TABLE `tbl_menu` (
  `id_masakan` int(11) NOT NULL AUTO_INCREMENT,
  `nama_masakan` varchar(25) DEFAULT NULL,
  `kategori` enum('Makanan','Minuman') DEFAULT 'Makanan',
  `harga` int(11) DEFAULT NULL,
  `status` enum('Tersedia','Habis') DEFAULT 'Habis',
  PRIMARY KEY (`id_masakan`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tbl_menu
-- ----------------------------
INSERT INTO `tbl_menu` VALUES ('9', 'Ratatoille Original', 'Makanan', '10000', 'Tersedia');
INSERT INTO `tbl_menu` VALUES ('10', 'Ratatoille Coklat', 'Makanan', '10000', 'Tersedia');
INSERT INTO `tbl_menu` VALUES ('11', 'Ratatoille Keju', 'Makanan', '10000', 'Tersedia');
INSERT INTO `tbl_menu` VALUES ('12', 'Rum', 'Minuman', '5000', 'Tersedia');
INSERT INTO `tbl_menu` VALUES ('13', 'Tequila', 'Minuman', '5000', 'Habis');
INSERT INTO `tbl_menu` VALUES ('14', 'Vodka', 'Minuman', '5000', 'Tersedia');

-- ----------------------------
-- Table structure for `tbl_transaksi`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_transaksi`;
CREATE TABLE `tbl_transaksi` (
  `id_transaksi` int(11) NOT NULL AUTO_INCREMENT,
  `nama_pelanggan` varchar(50) DEFAULT NULL,
  `id_masakan` int(15) DEFAULT NULL,
  `jumlah` int(15) DEFAULT NULL,
  `tanggal` date DEFAULT NULL,
  `total` int(15) DEFAULT NULL,
  PRIMARY KEY (`id_transaksi`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tbl_transaksi
-- ----------------------------
INSERT INTO `tbl_transaksi` VALUES ('3', 'Arman', '11', '2', '2023-03-12', '20000');
INSERT INTO `tbl_transaksi` VALUES ('4', 'Salim', '9', '2', '2023-03-09', '20000');
INSERT INTO `tbl_transaksi` VALUES ('5', 'Fery', '13', '2', '2023-03-12', '20000');
INSERT INTO `tbl_transaksi` VALUES ('6', 'Muh.Aris', '9', '2', '2023-03-09', '20000');
INSERT INTO `tbl_transaksi` VALUES ('9', 'adaw', '9', '2', '2023-03-01', '20000');

-- ----------------------------
-- Table structure for `tbl_users`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_users`;
CREATE TABLE `tbl_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nama_users` varchar(50) NOT NULL,
  `username` char(25) NOT NULL,
  `password` char(25) NOT NULL,
  `level` enum('kasir','pelanggan') NOT NULL DEFAULT 'kasir',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tbl_users
-- ----------------------------
INSERT INTO `tbl_users` VALUES ('2', 'Bintang Kun', 'pelanggan', 'pelanggan', 'pelanggan');
INSERT INTO `tbl_users` VALUES ('3', 'Syahru', 'kasir', 'kasir', 'kasir');

-- ----------------------------
-- View structure for `viewtransaksi`
-- ----------------------------
DROP VIEW IF EXISTS `viewtransaksi`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `viewtransaksi` AS select `tbl_transaksi`.`id_transaksi` AS `id_transaksi`,`tbl_transaksi`.`nama_pelanggan` AS `nama_pelanggan`,`tbl_menu`.`kategori` AS `kategori`,`tbl_menu`.`nama_masakan` AS `nama_masakan`,`tbl_transaksi`.`jumlah` AS `jumlah`,`tbl_transaksi`.`tanggal` AS `tanggal`,`tbl_transaksi`.`total` AS `total`,`tbl_menu`.`status` AS `status` from (`tbl_transaksi` join `tbl_menu` on(`tbl_transaksi`.`id_masakan` = `tbl_menu`.`id_masakan`)) ;
