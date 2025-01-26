-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 26, 2025 at 01:51 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_cvsu_silang_inventory`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_catalog_category`
--

CREATE TABLE `tb_catalog_category` (
  `category_id` int(11) NOT NULL,
  `category_code` varchar(255) DEFAULT NULL,
  `category_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_catalog_category`
--

INSERT INTO `tb_catalog_category` (`category_id`, `category_code`, `category_name`) VALUES
(1, 'Catalog-C-1', 'N/A'),
(2, 'Catalog-C-2', 'Books and Educational Resources'),
(3, 'Catalog-C-3', 'Cafeteria Supplies'),
(4, 'Catalog-C-4', 'Classroom Supplies'),
(5, 'Catalog-C-5', 'Electronics and IT Equipment'),
(6, 'Catalog-C-6', 'Furniture'),
(7, 'Catalog-C-7', 'Lab Equipment and Materials'),
(8, 'Catalog-C-8', 'Maintenance / Facilities'),
(9, 'Catalog-C-9', 'Medical Equipment'),
(10, 'Catalog-C-10', 'Miscellaneous'),
(11, 'Catalog-C-11', 'Office Supplies'),
(12, 'Catalog-C-12', 'Security and Safety Equipment'),
(13, 'Catalog-C-13', 'Sports Equipment'),
(14, 'Catalog-C-14', 'Appliances'),
(15, 'Catalog-C-15', 'Garbage DIsposal');

-- --------------------------------------------------------

--
-- Table structure for table `tb_catalog_item`
--

CREATE TABLE `tb_catalog_item` (
  `item_id` int(11) NOT NULL,
  `item_code` varchar(255) DEFAULT NULL,
  `item_category` varchar(255) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `item_uom` enum('PIECE','UNIT','SET') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_catalog_item`
--

INSERT INTO `tb_catalog_item` (`item_id`, `item_code`, `item_category`, `item_name`, `item_uom`) VALUES
(1, 'Catalog-I-13-1', 'Sports Equipment', 'Volley Ball', 'PIECE'),
(2, 'Catalog-I-13-2', 'Sports Equipment', 'Volleyball Ball', 'PIECE'),
(3, 'Catalog-I-13-3', 'Sports Equipment', 'Volleyball Net', 'PIECE'),
(4, 'Catalog-I-5-4', 'Electronics and IT Equipment', 'VOM', 'PIECE'),
(9, 'Catalog-I-11-9', 'Office Supplies', 'Wall Clock', 'PIECE'),
(10, 'Catalog-I-14-10', 'Appliances', 'Washing Machine', 'UNIT'),
(11, 'Catalog-I-14-11', 'Appliances', 'Water Dispenser', 'UNIT'),
(12, 'Catalog-I-9-12', 'Medical Equipment', 'Weighing Scale', 'PIECE'),
(13, 'Catalog-I-13-13', 'Sports Equipment', 'Weigh Sets', 'SET'),
(14, 'Catalog-I-9-14', 'Medical Equipment', 'Wheel Chair', 'PIECE'),
(15, 'Catalog-I-8-15', 'Maintenance / Facilities', 'Wheel Barrow', 'PIECE'),
(16, 'Catalog-I-13-16', 'Sports Equipment', 'Whistle', 'PIECE'),
(17, 'Catalog-I-4-17', 'Classroom Supplies', 'White Board', 'PIECE'),
(18, 'Catalog-I-5-18', 'Electronics and IT Equipment', 'Wifi Dongle', 'PIECE'),
(19, 'Catalog-I-5-19', 'Electronics and IT Equipment', 'Wifi Repeater', 'UNIT'),
(20, 'Catalog-I-3-20', 'Cafeteria Supplies', 'Wine Glass', 'PIECE'),
(21, 'Catalog-I-5-21', 'Electronics and IT Equipment', 'Wire Cutter', 'UNIT'),
(22, 'Catalog-I-15-22', 'Garbage DIsposal', 'Waste Basket', 'PIECE'),
(23, 'Catalog-I-14-23', 'Appliances', 'Water & Coffee Boiler', 'UNIT'),
(24, 'Catalog-I-12-24', 'Security and Safety Equipment', 'Helmet', 'PIECE');

-- --------------------------------------------------------

--
-- Table structure for table `tb_item_batch`
--

CREATE TABLE `tb_item_batch` (
  `batch_id` int(11) NOT NULL,
  `batch_timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_item_batch`
--

INSERT INTO `tb_item_batch` (`batch_id`, `batch_timestamp`) VALUES
(30, '2025-01-14 22:13:07'),
(31, '2025-01-14 22:26:34'),
(32, '2025-01-14 22:30:46'),
(33, '2025-01-14 22:32:11'),
(34, '2025-01-14 22:34:34'),
(35, '2025-01-14 22:36:59'),
(36, '2025-01-14 22:40:53'),
(37, '2025-01-14 22:43:56'),
(38, '2025-01-14 22:46:04'),
(39, '2025-01-14 22:47:48'),
(40, '2025-01-14 23:00:57'),
(41, '2025-01-14 23:02:18'),
(42, '2025-01-14 23:03:38'),
(43, '2025-01-14 23:07:44'),
(44, '2025-01-14 23:07:53'),
(45, '2025-01-14 23:08:10'),
(46, '2025-01-14 23:10:28'),
(47, '2025-01-14 23:13:52'),
(48, '2025-01-14 23:15:33'),
(49, '2025-01-14 23:16:19'),
(50, '2025-01-14 23:17:39'),
(51, '2025-01-14 23:20:56'),
(52, '2025-01-14 23:23:32'),
(53, '2025-01-14 23:24:49'),
(54, '2025-01-14 23:26:01'),
(55, '2025-01-14 23:28:45'),
(56, '2025-01-14 23:29:32'),
(57, '2025-01-14 23:31:02'),
(58, '2025-01-16 18:45:25');

-- --------------------------------------------------------

--
-- Table structure for table `tb_item_history`
--

CREATE TABLE `tb_item_history` (
  `history_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `history_frame` varchar(255) NOT NULL,
  `history_type` varchar(255) NOT NULL,
  `history_item_code_start` varchar(1023) NOT NULL,
  `history_item_code_end` varchar(255) NOT NULL,
  `history_desc` varchar(1023) NOT NULL,
  `history_user` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_item_history`
--

INSERT INTO `tb_item_history` (`history_timestamp`, `history_frame`, `history_type`, `history_item_code_start`, `history_item_code_end`, `history_desc`, `history_user`) VALUES
('2025-01-14 20:39:26', 'CATALOG', 'ADD', 'Catalog-C-1', 'Catalog-C-1', '; Category Name: N/A', ''),
('2025-01-14 20:40:38', 'CATALOG', 'ADD', 'Catalog-C-2', 'Catalog-C-2', '; Category Name: Books and Educational Resources', ''),
('2025-01-14 20:41:07', 'CATALOG', 'ADD', 'Catalog-C-3', 'Catalog-C-3', '; Category Name: Cafeteria Supplies', ''),
('2025-01-14 20:41:19', 'CATALOG', 'ADD', 'Catalog-C-4', 'Catalog-C-4', '; Category Name: Classroom Supplies', ''),
('2025-01-14 20:41:30', 'CATALOG', 'ADD', 'Catalog-C-5', 'Catalog-C-5', '; Category Name: Electronics and IT Equipment', ''),
('2025-01-14 20:41:38', 'CATALOG', 'ADD', 'Catalog-C-6', 'Catalog-C-6', '; Category Name: Furniture', ''),
('2025-01-14 20:41:49', 'CATALOG', 'ADD', 'Catalog-C-7', 'Catalog-C-7', '; Category Name: Lab Equipment and Materials', ''),
('2025-01-14 20:42:00', 'CATALOG', 'ADD', 'Catalog-C-8', 'Catalog-C-8', '; Category Name: Maintenance / Facilities', ''),
('2025-01-14 20:42:09', 'CATALOG', 'ADD', 'Catalog-C-9', 'Catalog-C-9', '; Category Name: Medical Equipment', ''),
('2025-01-14 20:42:24', 'CATALOG', 'ADD', 'Catalog-C-10', 'Catalog-C-10', '; Category Name: Miscellaneous', ''),
('2025-01-14 20:42:35', 'CATALOG', 'ADD', 'Catalog-C-11', 'Catalog-C-11', '; Category Name: Office Supplies', ''),
('2025-01-14 20:42:45', 'CATALOG', 'ADD', 'Catalog-C-12', 'Catalog-C-12', '; Category Name: Security and Safety Equipment', ''),
('2025-01-14 20:42:51', 'CATALOG', 'ADD', 'Catalog-C-13', 'Catalog-C-13', '; Category Name: Sports Equipment', ''),
('2025-01-14 21:52:03', 'CATALOG', 'ADD', 'Catalog-I-13-1', 'Catalog-I-13-1', '; Item Name: Volley Ball; Category: Sports Equipment; UOM: PIECE', ''),
('2025-01-14 21:54:28', 'CATALOG', 'ADD', 'Catalog-I-13-2', 'Catalog-I-13-2', '; Item Name: Volleyball Ball; Category: Sports Equipment; UOM: PIECE', ''),
('2025-01-14 21:54:52', 'CATALOG', 'ADD', 'Catalog-I-13-3', 'Catalog-I-13-3', '; Item Name: Volleyball Net; Category: Sports Equipment; UOM: PIECE', ''),
('2025-01-14 21:55:13', 'CATALOG', 'ADD', 'Catalog-I-5-4', 'Catalog-I-5-4', '; Item Name: VOM; Category: Electronics and IT Equipment; UOM: PIECE', ''),
('2025-01-14 21:59:17', 'CATALOG', 'ADD', 'Catalog-I-11-9', 'Catalog-I-11-9', '; Item Name: Wall Clock; Category: Office Supplies; UOM: PIECE', ''),
('2025-01-14 22:00:35', 'CATALOG', 'ADD', 'Catalog-C-14', 'Catalog-C-14', '; Category Name: Appliances', ''),
('2025-01-14 22:00:56', 'CATALOG', 'ADD', 'Catalog-I-14-10', 'Catalog-I-14-10', '; Item Name: Washing Machine; Category: Appliances; UOM: UNIT', ''),
('2025-01-14 22:01:12', 'CATALOG', 'ADD', 'Catalog-I-14-11', 'Catalog-I-14-11', '; Item Name: Water Dispenser; Category: Appliances; UOM: UNIT', ''),
('2025-01-14 22:01:42', 'CATALOG', 'ADD', 'Catalog-I-9-12', 'Catalog-I-9-12', '; Item Name: Weighing Scale; Category: Medical Equipment; UOM: PIECE', ''),
('2025-01-14 22:03:06', 'CATALOG', 'ADD', 'Catalog-I-13-13', 'Catalog-I-13-13', '; Item Name: Weigh Sets; Category: Sports Equipment; UOM: SET', ''),
('2025-01-14 22:03:39', 'CATALOG', 'ADD', 'Catalog-I-9-14', 'Catalog-I-9-14', '; Item Name: Wheel Chair; Category: Medical Equipment; UOM: PIECE', ''),
('2025-01-14 22:05:15', 'CATALOG', 'ADD', 'Catalog-I-8-15', 'Catalog-I-8-15', '; Item Name: Wheel Barrow; Category: Maintenance / Facilities; UOM: PIECE', ''),
('2025-01-14 22:05:36', 'CATALOG', 'ADD', 'Catalog-I-13-16', 'Catalog-I-13-16', '; Item Name: Whistle; Category: Sports Equipment; UOM: PIECE', ''),
('2025-01-14 22:05:55', 'CATALOG', 'ADD', 'Catalog-I-4-17', 'Catalog-I-4-17', '; Item Name: White Board; Category: Classroom Supplies; UOM: PIECE', ''),
('2025-01-14 22:06:16', 'CATALOG', 'ADD', 'Catalog-I-5-18', 'Catalog-I-5-18', '; Item Name: Wifi Dongle; Category: Electronics and IT Equipment; UOM: PIECE', ''),
('2025-01-14 22:06:27', 'CATALOG', 'ADD', 'Catalog-I-5-19', 'Catalog-I-5-19', '; Item Name: Wifi Repeater; Category: Electronics and IT Equipment; UOM: PIECE', ''),
('2025-01-14 22:06:53', 'CATALOG', 'UPDATE', 'Catalog-I-5-19', 'Catalog-I-5-19', '; UOM: PIECE -> UNIT', ''),
('2025-01-14 22:07:17', 'CATALOG', 'ADD', 'Catalog-I-3-20', 'Catalog-I-3-20', '; Item Name: Wine Glass; Category: Cafeteria Supplies; UOM: PIECE', ''),
('2025-01-14 22:07:36', 'CATALOG', 'ADD', 'Catalog-I-5-21', 'Catalog-I-5-21', '; Item Name: Wire Cutter; Category: Electronics and IT Equipment; UOM: UNIT', ''),
('2025-01-14 22:13:07', 'MANAGEMENT', 'ADD', 'Silang-25-24194', 'Silang-25-24198', '; Name: Volleyball Ball; Description: Mikasa, MVA 390; Price: 1250; Quantity: 5; DOD: 2025-01-15; Benefactor: Joseph S. Callanta', 'N/A'),
('2025-01-14 22:26:34', 'MANAGEMENT', 'ADD', 'Silang-18-35912', 'Silang-18-35919', '; Name: Volleyball Ball; Description: Mikasa, MVA310; Price: 2200; Quantity: 8; DOD: 2018-08-23; Benefactor: Roselyn A. Ymana', 'N/A'),
('2025-01-14 22:30:46', 'MANAGEMENT', 'ADD', 'Silang-18-35920', 'Silang-18-35921', '; Name: Volleyball Net; Description: Volleyball Net - GTO; Price: 850; Quantity: 2; DOD: 2018-08-23; Benefactor: Roselyn A. Ymana', 'N/A'),
('2025-01-14 22:32:11', 'MANAGEMENT', 'ADD', 'Silang-17-20797', 'Silang-17-20798', '; Name: Volleyball Net; Price: 2330; Quantity: 2; DOD: 2017-05-25; Benefactor: Hazelyn H. Dela Cruz', 'N/A'),
('2025-01-14 22:32:46', 'MANAGEMENT', 'UPDATE', 'Silang-25-24194', 'Silang-25-24198', '; DOD: 2025-01-15 -> 2017-10-25', 'N/A'),
('2025-01-14 22:34:34', 'MANAGEMENT', 'ADD', 'Silang-22-104483', 'Silang-22-104483', '; Name: VOM; Description: VOM Multitester Ingco; Price: 500; Quantity: 1; DOD: 2022-10-26; Benefactor: Elena B. Telmo', 'N/A'),
('2025-01-14 22:36:59', 'MANAGEMENT', 'ADD', 'Silang-21-75399', 'Silang-21-75402', '; Name: Wall Clock; Description: 16\" / Stainless / Asahi; Price: 1073; Quantity: 4; DOD: 2021-06-16; Benefactor: Jenny Beb F. Ebo', 'N/A'),
('2025-01-14 22:37:54', 'MANAGEMENT', 'UPDATE', 'Silang-25-24194', 'Silang-25-24198', '', 'N/A'),
('2025-01-14 22:40:53', 'MANAGEMENT', 'ADD', 'Silang-17-27621', 'Silang-17-27630', '; Name: Wall Clock; Description: big; Price: 400; Quantity: 10; DOD: 2017-06-12; Benefactor: Elena B. Telmo', 'N/A'),
('2025-01-14 22:43:56', 'MANAGEMENT', 'ADD', 'Silang-19-41366', 'Silang-19-41366', '; Name: Wall Clock; Price: 88; Quantity: 1; DOD: 2019-10-12; Benefactor: Elena B. Telmo', 'N/A'),
('2025-01-14 22:46:04', 'MANAGEMENT', 'ADD', 'Silang-17-58348', 'Silang-17-58348', '; Name: Washing Machine; Description: Fully Auto LG, WF-S120V 12kg; Price: 32750; Quantity: 1; DOD: 2017-12-05; Benefactor: Beverly A. Malabag', 'N/A'),
('2025-01-14 22:47:48', 'MANAGEMENT', 'ADD', 'Silang-15-55561', 'Silang-15-55561', '; Name: Washing Machine; Description: Washing Machine w/ Dryer, HD, 11kg./Whirlpool WWT 110x; Price: 13125; Quantity: 1; DOD: 2015-12-01; Benefactor: Beverly A. Malabag', 'N/A'),
('2025-01-14 22:48:43', 'CATALOG', 'ADD', 'Catalog-C-15', 'Catalog-C-15', '; Category Name: Garbage DIsposal', ''),
('2025-01-14 22:48:54', 'CATALOG', 'ADD', 'Catalog-I-15-22', 'Catalog-I-15-22', '; Item Name: Waste Basket; Category: Garbage DIsposal; UOM: PIECE', ''),
('2025-01-14 22:59:17', 'CATALOG', 'ADD', 'Catalog-I-14-23', 'Catalog-I-14-23', '; Item Name: Water & Coffee Boiler; Category: Appliances; UOM: UNIT', ''),
('2025-01-14 23:00:57', 'MANAGEMENT', 'ADD', 'Silang-19-55662', 'Silang-19-55662', '; Name: Water & Coffee Boiler; Description: Imarflex IWB-15008; Price: 5499; Quantity: 1; DOD: 2019-07-22; Benefactor: Elena B. Telmo', 'N/A'),
('2025-01-14 23:02:18', 'MANAGEMENT', 'ADD', 'Silang-21-92149', 'Silang-21-92149', '; Name: Water Dispenser; Description: Hot and Cold, Camel; Price: 5700; Quantity: 1; DOD: 2021-07-09; Benefactor: Elena B. Telmo', 'N/A'),
('2025-01-14 23:03:38', 'MANAGEMENT', 'ADD', 'Silang-22-36071', 'Silang-22-36071', '; Name: Water Dispenser; Description: Table Top / Kyowa KW1501 / Hot / Cold; Price: 1958; Quantity: 1; DOD: 2022-07-15; Benefactor: Elena B. Telmo', 'N/A'),
('2025-01-14 23:10:28', 'MANAGEMENT', 'ADD', 'Silang-18-41973', 'Silang-18-41974', '; Name: Weigh Sets; Description: Stainless Steel Hooked, 1000g; Price: 1975; Quantity: 2; DOD: 2018-12-19; Benefactor: Joseph S. Callanta', 'N/A'),
('2025-01-14 23:13:52', 'MANAGEMENT', 'ADD', 'Silang-22-99953', 'Silang-22-99954', '; Name: Weighing Scale; Description: Digital / 120kg. / Aquadry; Price: 1250; Quantity: 2; DOD: 2022-05-24; Benefactor: Elena B. Telmo', 'N/A'),
('2025-01-14 23:15:33', 'MANAGEMENT', 'ADD', 'Silang-18-36076', 'Silang-18-36076', '; Name: Wheel Chair; Description: Foldable MAX Weight Cap 120kg. ; Price: 3900; Quantity: 1; DOD: 2018-09-13; Benefactor: Elena B. Telmo', 'N/A'),
('2025-01-14 23:16:19', 'MANAGEMENT', 'ADD', 'Silang-22-102638', 'Silang-22-102638', '; Name: Wheel Barrow; Price: 3700; Quantity: 1; DOD: 2022-09-05; Benefactor: Elena B. Telmo', 'N/A'),
('2025-01-14 23:17:39', 'MANAGEMENT', 'ADD', 'Silang-18-35976', 'Silang-18-35978', '; Name: Whistle; Description: Molten Dolfim; Price: 650; Quantity: 3; DOD: 2018-08-23; Benefactor: Roselyn A. Ymana', 'N/A'),
('2025-01-14 23:20:56', 'MANAGEMENT', 'ADD', 'Silang-17-19118', 'Silang-17-19137', '; Name: White Board; Description: 4x8ft.; Price: 3000; Quantity: 20; DOD: 2017-04-13; Benefactor: Beverly A. Malabag', 'N/A'),
('2025-01-14 23:23:32', 'MANAGEMENT', 'ADD', 'Silang-17-25860', 'Silang-17-25869', '; Name: White Board; Description: big, 4x8ft. wall type; Price: 3480; Quantity: 10; DOD: 2017-11-27; Benefactor: Merlina B. Castro', 'N/A'),
('2025-01-14 23:24:49', 'MANAGEMENT', 'ADD', 'Silang-19-55660', 'Silang-19-55661', '; Name: Wifi Dongle; Description: EP-150mbps; Price: 450; Quantity: 2; DOD: 2019-11-20; Benefactor: Merlina B. Castro', 'N/A'),
('2025-01-14 23:26:01', 'MANAGEMENT', 'ADD', 'Silang-20-70297', 'Silang-20-70346', '; Name: Wifi Dongle; Description: TP-Link 150mbps (branded); Price: 350; Quantity: 50; DOD: 2020-12-03; Benefactor: Elena B. Telmo', 'N/A'),
('2025-01-14 23:28:45', 'MANAGEMENT', 'ADD', 'Silang-22-125136', 'Silang-22-125145', '; Name: Wifi Repeater; Description: Repeater, WiFi Outdoor Repeater, WiFi Transmission standard 802.11ac, Wired transfer rate 10/00/1000Mbps, 2.4 WiFi Transmission 300Mbps, TP Link; Price: 4500; Quantity: 10; DOD: 2022-09-06; Benefactor: Elena B. Telmo', 'N/A'),
('2025-01-14 23:29:32', 'MANAGEMENT', 'ADD', 'Silang-18-42046', 'Silang-18-42048', '; Name: Wine Glass; Price: 44; Quantity: 3; DOD: 2018-11-28; Benefactor: Merlina B. Castro', 'N/A'),
('2025-01-14 23:31:02', 'MANAGEMENT', 'ADD', 'Silang-22-70356', 'Silang-22-70356', '; Name: Wire Cutter; Description: Multifunctional Automatic Cable Wire Stripper, Crimping and Cutter Pliers; Price: 680; Quantity: 1; DOD: 2022-12-03; Benefactor: Elena B. Telmo', 'N/A'),
('2025-01-16 18:42:48', 'REQUEST', 'ADD', 'Request-4', 'Request-4', '; Item Name: Helmet; Description: ; Quantity: 2', 'Danel Dave Barbuco'),
('2025-01-16 18:43:44', 'REQUEST', 'UPDATE', 'Request-4', 'Request-4', '; Item Name: Helmet -> Volleyball Ball', 'Danel Dave Barbuco'),
('2025-01-16 18:44:21', 'REQUEST', 'UPDATE', 'Request-4', 'Request-4', '; Item Name: Volleyball Ball -> Helmet', 'Danel Dave Barbuco'),
('2025-01-16 18:44:53', 'CATALOG', 'ADD', 'Catalog-I-12-24', 'Catalog-I-12-24', '; Item Name: Helmet; Category: Security and Safety Equipment; UOM: PIECE', ''),
('2025-01-16 18:45:26', 'MANAGEMENT', 'ADD', 'Silang-25-125147', 'Silang-25-125148', '; Name: Helmet; Price: 750; Quantity: 2; DOD: 2025-01-17; Benefactor: Danel Dave Barbuco', 'N/A'),
('2025-01-16 18:45:34', 'REQUEST', 'UPDATE', 'Request-4', 'Request-4', '; Status: PENDING -> RECEIVED', 'Danel Dave Barbuco'),
('2025-01-16 18:48:27', 'REQUEST', 'UPDATE', 'Request-4', 'Request-4', '; Status: RECEIVED -> PENDING', 'Danel Dave Barbuco'),
('2025-01-16 18:58:52', 'REQUEST', 'ADD', 'Request-5', 'Request-5', '; Item Name: Motor; Description: mio; Quantity: 1', 'Danel Dave Barbuco'),
('2025-01-16 18:59:05', 'REQUEST', 'UPDATE', 'Request-4', 'Request-4', '; Status: PENDING -> DENIED', 'Danel Dave Barbuco'),
('2025-01-16 19:00:22', 'REQUEST', 'UPDATE', 'Request-5', 'Request-5', '; Status: PENDING -> RECEIVED', 'Danel Dave Barbuco');

-- --------------------------------------------------------

--
-- Table structure for table `tb_item_report`
--

CREATE TABLE `tb_item_report` (
  `report_id` int(11) NOT NULL,
  `report_timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `report_code` varchar(50) NOT NULL,
  `report_condition` varchar(1023) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tb_item_request`
--

CREATE TABLE `tb_item_request` (
  `request_id` int(11) NOT NULL,
  `request_timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `request_name` varchar(255) NOT NULL,
  `request_item` varchar(255) NOT NULL,
  `request_desc` varchar(1023) NOT NULL,
  `request_quantity` int(16) NOT NULL DEFAULT 1,
  `request_status` enum('PENDING','DENIED','RECEIVED') NOT NULL DEFAULT 'PENDING'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_item_request`
--

INSERT INTO `tb_item_request` (`request_id`, `request_timestamp`, `request_name`, `request_item`, `request_desc`, `request_quantity`, `request_status`) VALUES
(1, '2025-01-16 18:59:05', 'Danel Dave Barbuco', 'Helmet', '', 2, 'DENIED'),
(2, '2025-01-16 19:00:22', 'Danel Dave Barbuco', 'Motor', 'mio', 1, 'RECEIVED');

-- --------------------------------------------------------

--
-- Table structure for table `tb_item_stock`
--

CREATE TABLE `tb_item_stock` (
  `stock_timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `stock_id` int(11) NOT NULL,
  `stock_name` varchar(255) NOT NULL,
  `stock_category` varchar(255) NOT NULL,
  `stock_desc` varchar(255) DEFAULT NULL,
  `stock_price` float NOT NULL,
  `stock_dod` date NOT NULL,
  `stock_benefactor` varchar(255) NOT NULL,
  `stock_location` varchar(255) NOT NULL DEFAULT 'Supply Room',
  `stock_holder` varchar(255) NOT NULL DEFAULT 'N/A',
  `stock_code` varchar(50) DEFAULT NULL,
  `stock_batch` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_item_stock`
--

INSERT INTO `tb_item_stock` (`stock_timestamp`, `stock_id`, `stock_name`, `stock_category`, `stock_desc`, `stock_price`, `stock_dod`, `stock_benefactor`, `stock_location`, `stock_holder`, `stock_code`, `stock_batch`) VALUES
('2025-01-14 23:20:56', 19118, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19118', 51),
('2025-01-14 23:20:56', 19119, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19119', 51),
('2025-01-14 23:20:56', 19120, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19120', 51),
('2025-01-14 23:20:56', 19121, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19121', 51),
('2025-01-14 23:20:56', 19122, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19122', 51),
('2025-01-14 23:20:56', 19123, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19123', 51),
('2025-01-14 23:20:56', 19124, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19124', 51),
('2025-01-14 23:20:56', 19125, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19125', 51),
('2025-01-14 23:20:56', 19126, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19126', 51),
('2025-01-14 23:20:56', 19127, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19127', 51),
('2025-01-14 23:20:56', 19128, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19128', 51),
('2025-01-14 23:20:56', 19129, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19129', 51),
('2025-01-14 23:20:56', 19130, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19130', 51),
('2025-01-14 23:20:56', 19131, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19131', 51),
('2025-01-14 23:20:56', 19132, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19132', 51),
('2025-01-14 23:20:56', 19133, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19133', 51),
('2025-01-14 23:20:56', 19134, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19134', 51),
('2025-01-14 23:20:56', 19135, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19135', 51),
('2025-01-14 23:20:56', 19136, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19136', 51),
('2025-01-14 23:20:56', 19137, 'White Board', 'Classroom Supplies', '4x8ft.', 3000, '2017-04-13', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-19137', 51),
('2025-01-14 22:32:11', 20797, 'Volleyball Net', 'Sports Equipment', '', 2330, '2017-05-25', 'Hazelyn H. Dela Cruz', 'Supply Room', 'N/A', 'Silang-17-20797', 33),
('2025-01-14 22:32:11', 20798, 'Volleyball Net', 'Sports Equipment', '', 2330, '2017-05-25', 'Hazelyn H. Dela Cruz', 'Supply Room', 'N/A', 'Silang-17-20798', 33),
('2025-01-14 22:13:07', 24194, 'Volleyball Ball', 'Sports Equipment', 'Mikasa, MVA 390', 1250, '2017-10-25', 'Joseph S. Callanta', 'Supply Room', 'N/A', 'Silang-25-24194', 30),
('2025-01-14 22:13:07', 24195, 'Volleyball Ball', 'Sports Equipment', 'Mikasa, MVA 390', 1250, '2017-10-25', 'Joseph S. Callanta', 'Supply Room', 'N/A', 'Silang-25-24195', 30),
('2025-01-14 22:13:07', 24196, 'Volleyball Ball', 'Sports Equipment', 'Mikasa, MVA 390', 1250, '2017-10-25', 'Joseph S. Callanta', 'Supply Room', 'N/A', 'Silang-25-24196', 30),
('2025-01-14 22:13:07', 24197, 'Volleyball Ball', 'Sports Equipment', 'Mikasa, MVA 390', 1250, '2017-10-25', 'Joseph S. Callanta', 'Supply Room', 'N/A', 'Silang-25-24197', 30),
('2025-01-14 22:13:07', 24198, 'Volleyball Ball', 'Sports Equipment', 'Mikasa, MVA 390', 1250, '2017-10-25', 'Joseph S. Callanta', 'Supply Room', 'N/A', 'Silang-25-24198', 30),
('2025-01-14 23:23:32', 25860, 'White Board', 'Classroom Supplies', 'big, 4x8ft. wall type', 3480, '2017-11-27', 'Merlina B. Castro', 'Supply Room', 'N/A', 'Silang-17-25860', 52),
('2025-01-14 23:23:32', 25861, 'White Board', 'Classroom Supplies', 'big, 4x8ft. wall type', 3480, '2017-11-27', 'Merlina B. Castro', 'Supply Room', 'N/A', 'Silang-17-25861', 52),
('2025-01-14 23:23:32', 25862, 'White Board', 'Classroom Supplies', 'big, 4x8ft. wall type', 3480, '2017-11-27', 'Merlina B. Castro', 'Supply Room', 'N/A', 'Silang-17-25862', 52),
('2025-01-14 23:23:32', 25863, 'White Board', 'Classroom Supplies', 'big, 4x8ft. wall type', 3480, '2017-11-27', 'Merlina B. Castro', 'Supply Room', 'N/A', 'Silang-17-25863', 52),
('2025-01-14 23:23:32', 25864, 'White Board', 'Classroom Supplies', 'big, 4x8ft. wall type', 3480, '2017-11-27', 'Merlina B. Castro', 'Supply Room', 'N/A', 'Silang-17-25864', 52),
('2025-01-14 23:23:32', 25865, 'White Board', 'Classroom Supplies', 'big, 4x8ft. wall type', 3480, '2017-11-27', 'Merlina B. Castro', 'Supply Room', 'N/A', 'Silang-17-25865', 52),
('2025-01-14 23:23:32', 25866, 'White Board', 'Classroom Supplies', 'big, 4x8ft. wall type', 3480, '2017-11-27', 'Merlina B. Castro', 'Supply Room', 'N/A', 'Silang-17-25866', 52),
('2025-01-14 23:23:32', 25867, 'White Board', 'Classroom Supplies', 'big, 4x8ft. wall type', 3480, '2017-11-27', 'Merlina B. Castro', 'Supply Room', 'N/A', 'Silang-17-25867', 52),
('2025-01-14 23:23:32', 25868, 'White Board', 'Classroom Supplies', 'big, 4x8ft. wall type', 3480, '2017-11-27', 'Merlina B. Castro', 'Supply Room', 'N/A', 'Silang-17-25868', 52),
('2025-01-14 23:23:32', 25869, 'White Board', 'Classroom Supplies', 'big, 4x8ft. wall type', 3480, '2017-11-27', 'Merlina B. Castro', 'Supply Room', 'N/A', 'Silang-17-25869', 52),
('2025-01-14 22:40:53', 27621, 'Wall Clock', 'Office Supplies', 'big', 400, '2017-06-12', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-17-27621', 36),
('2025-01-14 22:40:53', 27622, 'Wall Clock', 'Office Supplies', 'big', 400, '2017-06-12', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-17-27622', 36),
('2025-01-14 22:40:53', 27623, 'Wall Clock', 'Office Supplies', 'big', 400, '2017-06-12', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-17-27623', 36),
('2025-01-14 22:40:53', 27624, 'Wall Clock', 'Office Supplies', 'big', 400, '2017-06-12', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-17-27624', 36),
('2025-01-14 22:40:53', 27625, 'Wall Clock', 'Office Supplies', 'big', 400, '2017-06-12', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-17-27625', 36),
('2025-01-14 22:40:53', 27626, 'Wall Clock', 'Office Supplies', 'big', 400, '2017-06-12', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-17-27626', 36),
('2025-01-14 22:40:53', 27627, 'Wall Clock', 'Office Supplies', 'big', 400, '2017-06-12', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-17-27627', 36),
('2025-01-14 22:40:53', 27628, 'Wall Clock', 'Office Supplies', 'big', 400, '2017-06-12', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-17-27628', 36),
('2025-01-14 22:40:53', 27629, 'Wall Clock', 'Office Supplies', 'big', 400, '2017-06-12', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-17-27629', 36),
('2025-01-14 22:40:53', 27630, 'Wall Clock', 'Office Supplies', 'big', 400, '2017-06-12', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-17-27630', 36),
('2025-01-14 22:26:34', 35912, 'Volleyball Ball', 'Sports Equipment', 'Mikasa, MVA310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35912', 31),
('2025-01-14 22:26:34', 35913, 'Volleyball Ball', 'Sports Equipment', 'Mikasa, MVA310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35913', 31),
('2025-01-14 22:26:34', 35914, 'Volleyball Ball', 'Sports Equipment', 'Mikasa, MVA310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35914', 31),
('2025-01-14 22:26:34', 35915, 'Volleyball Ball', 'Sports Equipment', 'Mikasa, MVA310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35915', 31),
('2025-01-14 22:26:34', 35916, 'Volleyball Ball', 'Sports Equipment', 'Mikasa, MVA310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35916', 31),
('2025-01-14 22:26:34', 35917, 'Volleyball Ball', 'Sports Equipment', 'Mikasa, MVA310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35917', 31),
('2025-01-14 22:26:34', 35918, 'Volleyball Ball', 'Sports Equipment', 'Mikasa, MVA310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35918', 31),
('2025-01-14 22:26:34', 35919, 'Volleyball Ball', 'Sports Equipment', 'Mikasa, MVA310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35919', 31),
('2025-01-14 22:30:46', 35920, 'Volleyball Net', 'Sports Equipment', 'Volleyball Net - GTO', 850, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35920', 32),
('2025-01-14 22:30:46', 35921, 'Volleyball Net', 'Sports Equipment', 'Volleyball Net - GTO', 850, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35921', 32),
('2025-01-14 23:17:39', 35976, 'Whistle', 'Sports Equipment', 'Molten Dolfim', 650, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35976', 50),
('2025-01-14 23:17:39', 35977, 'Whistle', 'Sports Equipment', 'Molten Dolfim', 650, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35977', 50),
('2025-01-14 23:17:39', 35978, 'Whistle', 'Sports Equipment', 'Molten Dolfim', 650, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35978', 50),
('2025-01-14 23:03:38', 36071, 'Water Dispenser', 'Appliances', 'Table Top / Kyowa KW1501 / Hot / Cold', 1958, '2022-07-15', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-36071', 42),
('2025-01-14 23:15:33', 36076, 'Wheel Chair', 'Medical Equipment', 'Foldable MAX Weight Cap 120kg. ', 3900, '2018-09-13', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-18-36076', 48),
('2025-01-14 22:43:56', 41366, 'Wall Clock', 'Office Supplies', '', 88, '2019-10-12', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-19-41366', 37),
('2025-01-14 23:10:28', 41973, 'Weigh Sets', 'Sports Equipment', 'Stainless Steel Hooked, 1000g', 1975, '2018-12-19', 'Joseph S. Callanta', 'Supply Room', 'N/A', 'Silang-18-41973', 46),
('2025-01-14 23:10:28', 41974, 'Weigh Sets', 'Sports Equipment', 'Stainless Steel Hooked, 1000g', 1975, '2018-12-19', 'Joseph S. Callanta', 'Supply Room', 'N/A', 'Silang-18-41974', 46),
('2025-01-14 23:29:32', 42046, 'Wine Glass', 'Cafeteria Supplies', '', 44, '2018-11-28', 'Merlina B. Castro', 'Supply Room', 'N/A', 'Silang-18-42046', 56),
('2025-01-14 23:29:32', 42047, 'Wine Glass', 'Cafeteria Supplies', '', 44, '2018-11-28', 'Merlina B. Castro', 'Supply Room', 'N/A', 'Silang-18-42047', 56),
('2025-01-14 23:29:32', 42048, 'Wine Glass', 'Cafeteria Supplies', '', 44, '2018-11-28', 'Merlina B. Castro', 'Supply Room', 'N/A', 'Silang-18-42048', 56),
('2025-01-14 22:47:48', 55561, 'Washing Machine', 'Appliances', 'Washing Machine w/ Dryer, HD, 11kg./Whirlpool WWT 110x', 13125, '2015-12-01', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-15-55561', 39),
('2025-01-14 23:24:49', 55660, 'Wifi Dongle', 'Electronics and IT Equipment', 'EP-150mbps', 450, '2019-11-20', 'Merlina B. Castro', 'Supply Room', 'N/A', 'Silang-19-55660', 53),
('2025-01-14 23:24:49', 55661, 'Wifi Dongle', 'Electronics and IT Equipment', 'EP-150mbps', 450, '2019-11-20', 'Merlina B. Castro', 'Supply Room', 'N/A', 'Silang-19-55661', 53),
('2025-01-14 23:00:57', 55662, 'Water & Coffee Boiler', 'Appliances', 'Imarflex IWB-15008', 5499, '2019-07-22', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-19-55662', 40),
('2025-01-14 22:46:04', 58348, 'Washing Machine', 'Appliances', 'Fully Auto LG, WF-S120V 12kg', 32750, '2017-12-05', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-58348', 38),
('2025-01-14 23:26:01', 70297, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70297', 54),
('2025-01-14 23:26:01', 70298, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70298', 54),
('2025-01-14 23:26:01', 70299, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70299', 54),
('2025-01-14 23:26:01', 70300, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70300', 54),
('2025-01-14 23:26:01', 70301, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70301', 54),
('2025-01-14 23:26:01', 70302, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70302', 54),
('2025-01-14 23:26:01', 70303, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70303', 54),
('2025-01-14 23:26:01', 70304, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70304', 54),
('2025-01-14 23:26:01', 70305, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70305', 54),
('2025-01-14 23:26:01', 70306, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70306', 54),
('2025-01-14 23:26:01', 70307, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70307', 54),
('2025-01-14 23:26:01', 70308, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70308', 54),
('2025-01-14 23:26:01', 70309, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70309', 54),
('2025-01-14 23:26:01', 70310, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70310', 54),
('2025-01-14 23:26:01', 70311, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70311', 54),
('2025-01-14 23:26:01', 70312, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70312', 54),
('2025-01-14 23:26:01', 70313, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70313', 54),
('2025-01-14 23:26:01', 70314, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70314', 54),
('2025-01-14 23:26:01', 70315, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70315', 54),
('2025-01-14 23:26:01', 70316, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70316', 54),
('2025-01-14 23:26:01', 70317, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70317', 54),
('2025-01-14 23:26:01', 70318, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70318', 54),
('2025-01-14 23:26:01', 70319, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70319', 54),
('2025-01-14 23:26:01', 70320, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70320', 54),
('2025-01-14 23:26:01', 70321, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70321', 54),
('2025-01-14 23:26:01', 70322, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70322', 54),
('2025-01-14 23:26:01', 70323, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70323', 54),
('2025-01-14 23:26:01', 70324, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70324', 54),
('2025-01-14 23:26:01', 70325, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70325', 54),
('2025-01-14 23:26:01', 70326, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70326', 54),
('2025-01-14 23:26:01', 70327, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70327', 54),
('2025-01-14 23:26:01', 70328, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70328', 54),
('2025-01-14 23:26:01', 70329, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70329', 54),
('2025-01-14 23:26:01', 70330, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70330', 54),
('2025-01-14 23:26:01', 70331, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70331', 54),
('2025-01-14 23:26:01', 70332, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70332', 54),
('2025-01-14 23:26:01', 70333, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70333', 54),
('2025-01-14 23:26:01', 70334, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70334', 54),
('2025-01-14 23:26:01', 70335, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70335', 54),
('2025-01-14 23:26:01', 70336, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70336', 54),
('2025-01-14 23:26:01', 70337, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70337', 54),
('2025-01-14 23:26:01', 70338, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70338', 54),
('2025-01-14 23:26:01', 70339, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70339', 54),
('2025-01-14 23:26:01', 70340, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70340', 54),
('2025-01-14 23:26:01', 70341, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70341', 54),
('2025-01-14 23:26:01', 70342, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70342', 54),
('2025-01-14 23:26:01', 70343, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70343', 54),
('2025-01-14 23:26:01', 70344, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70344', 54),
('2025-01-14 23:26:01', 70345, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70345', 54),
('2025-01-14 23:26:01', 70346, 'Wifi Dongle', 'Electronics and IT Equipment', 'TP-Link 150mbps (branded)', 350, '2020-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-20-70346', 54),
('2025-01-14 23:31:02', 70356, 'Wire Cutter', 'Electronics and IT Equipment', 'Multifunctional Automatic Cable Wire Stripper, Crimping and Cutter Pliers', 680, '2022-12-03', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-70356', 57),
('2025-01-14 22:36:59', 75399, 'Wall Clock', 'Office Supplies', '16\" / Stainless / Asahi', 1073, '2021-06-16', 'Jenny Beb F. Ebo', 'Supply Room', 'N/A', 'Silang-21-75399', 35),
('2025-01-14 22:36:59', 75400, 'Wall Clock', 'Office Supplies', '16\" / Stainless / Asahi', 1073, '2021-06-16', 'Jenny Beb F. Ebo', 'Supply Room', 'N/A', 'Silang-21-75400', 35),
('2025-01-14 22:36:59', 75401, 'Wall Clock', 'Office Supplies', '16\" / Stainless / Asahi', 1073, '2021-06-16', 'Jenny Beb F. Ebo', 'Supply Room', 'N/A', 'Silang-21-75401', 35),
('2025-01-14 22:36:59', 75402, 'Wall Clock', 'Office Supplies', '16\" / Stainless / Asahi', 1073, '2021-06-16', 'Jenny Beb F. Ebo', 'Supply Room', 'N/A', 'Silang-21-75402', 35),
('2025-01-14 23:02:18', 92149, 'Water Dispenser', 'Appliances', 'Hot and Cold, Camel', 5700, '2021-07-09', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-21-92149', 41),
('2025-01-14 23:13:52', 99953, 'Weighing Scale', 'Medical Equipment', 'Digital / 120kg. / Aquadry', 1250, '2022-05-24', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-99953', 47),
('2025-01-14 23:13:52', 99954, 'Weighing Scale', 'Medical Equipment', 'Digital / 120kg. / Aquadry', 1250, '2022-05-24', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-99954', 47),
('2025-01-14 23:16:19', 102638, 'Wheel Barrow', 'Maintenance / Facilities', '', 3700, '2022-09-05', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-102638', 49),
('2025-01-14 22:34:34', 104483, 'VOM', 'Electronics and IT Equipment', 'VOM Multitester Ingco', 500, '2022-10-26', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-104483', 34),
('2025-01-14 23:28:45', 125136, 'Wifi Repeater', 'Electronics and IT Equipment', 'Repeater, WiFi Outdoor Repeater, WiFi Transmission standard 802.11ac, Wired transfer rate 10/00/1000Mbps, 2.4 WiFi Transmission 300Mbps, TP Link', 4500, '2022-09-06', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-125136', 55),
('2025-01-14 23:28:45', 125137, 'Wifi Repeater', 'Electronics and IT Equipment', 'Repeater, WiFi Outdoor Repeater, WiFi Transmission standard 802.11ac, Wired transfer rate 10/00/1000Mbps, 2.4 WiFi Transmission 300Mbps, TP Link', 4500, '2022-09-06', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-125137', 55),
('2025-01-14 23:28:45', 125138, 'Wifi Repeater', 'Electronics and IT Equipment', 'Repeater, WiFi Outdoor Repeater, WiFi Transmission standard 802.11ac, Wired transfer rate 10/00/1000Mbps, 2.4 WiFi Transmission 300Mbps, TP Link', 4500, '2022-09-06', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-125138', 55),
('2025-01-14 23:28:45', 125139, 'Wifi Repeater', 'Electronics and IT Equipment', 'Repeater, WiFi Outdoor Repeater, WiFi Transmission standard 802.11ac, Wired transfer rate 10/00/1000Mbps, 2.4 WiFi Transmission 300Mbps, TP Link', 4500, '2022-09-06', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-125139', 55),
('2025-01-14 23:28:45', 125140, 'Wifi Repeater', 'Electronics and IT Equipment', 'Repeater, WiFi Outdoor Repeater, WiFi Transmission standard 802.11ac, Wired transfer rate 10/00/1000Mbps, 2.4 WiFi Transmission 300Mbps, TP Link', 4500, '2022-09-06', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-125140', 55),
('2025-01-14 23:28:45', 125141, 'Wifi Repeater', 'Electronics and IT Equipment', 'Repeater, WiFi Outdoor Repeater, WiFi Transmission standard 802.11ac, Wired transfer rate 10/00/1000Mbps, 2.4 WiFi Transmission 300Mbps, TP Link', 4500, '2022-09-06', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-125141', 55),
('2025-01-14 23:28:45', 125142, 'Wifi Repeater', 'Electronics and IT Equipment', 'Repeater, WiFi Outdoor Repeater, WiFi Transmission standard 802.11ac, Wired transfer rate 10/00/1000Mbps, 2.4 WiFi Transmission 300Mbps, TP Link', 4500, '2022-09-06', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-125142', 55),
('2025-01-14 23:28:45', 125143, 'Wifi Repeater', 'Electronics and IT Equipment', 'Repeater, WiFi Outdoor Repeater, WiFi Transmission standard 802.11ac, Wired transfer rate 10/00/1000Mbps, 2.4 WiFi Transmission 300Mbps, TP Link', 4500, '2022-09-06', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-125143', 55),
('2025-01-14 23:28:45', 125144, 'Wifi Repeater', 'Electronics and IT Equipment', 'Repeater, WiFi Outdoor Repeater, WiFi Transmission standard 802.11ac, Wired transfer rate 10/00/1000Mbps, 2.4 WiFi Transmission 300Mbps, TP Link', 4500, '2022-09-06', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-125144', 55),
('2025-01-14 23:28:45', 125145, 'Wifi Repeater', 'Electronics and IT Equipment', 'Repeater, WiFi Outdoor Repeater, WiFi Transmission standard 802.11ac, Wired transfer rate 10/00/1000Mbps, 2.4 WiFi Transmission 300Mbps, TP Link', 4500, '2022-09-06', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-125145', 55),
('2025-01-16 18:45:25', 125146, 'Helmet', 'Security and Safety Equipment', '', 750, '2025-01-17', 'Danel Dave Barbuco', 'Supply Room', 'N/A', 'Silang-25-125146', 58),
('2025-01-16 18:45:26', 125147, 'Helmet', 'Security and Safety Equipment', '', 750, '2025-01-17', 'Danel Dave Barbuco', 'Supply Room', 'N/A', 'Silang-25-125147', 58);

-- --------------------------------------------------------

--
-- Table structure for table `tb_user`
--

CREATE TABLE `tb_user` (
  `user_id` int(11) NOT NULL,
  `user_email` varchar(255) NOT NULL,
  `user_salt` varchar(255) NOT NULL,
  `user_password` varchar(255) NOT NULL,
  `user_fname` varchar(255) NOT NULL,
  `user_lname` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_user`
--

INSERT INTO `tb_user` (`user_id`, `user_email`, `user_salt`, `user_password`, `user_fname`, `user_lname`) VALUES
(1, 'johnpatrick.skidmore@cvsu.edu.ph', 'BYjzi3wsD+ut2HBZawXVXg==', 'e4eba840eddfe58491f744d223fc289fcd4b416c72d4f375cd81cc889dcec53d', 'John Patrick', 'Skidmore');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_catalog_category`
--
ALTER TABLE `tb_catalog_category`
  ADD PRIMARY KEY (`category_id`),
  ADD UNIQUE KEY `category_name` (`category_name`);

--
-- Indexes for table `tb_catalog_item`
--
ALTER TABLE `tb_catalog_item`
  ADD PRIMARY KEY (`item_id`),
  ADD UNIQUE KEY `item_name` (`item_name`),
  ADD KEY `fk_category` (`item_category`);

--
-- Indexes for table `tb_item_batch`
--
ALTER TABLE `tb_item_batch`
  ADD PRIMARY KEY (`batch_id`);

--
-- Indexes for table `tb_item_report`
--
ALTER TABLE `tb_item_report`
  ADD PRIMARY KEY (`report_id`),
  ADD KEY `fk_code` (`report_code`);

--
-- Indexes for table `tb_item_request`
--
ALTER TABLE `tb_item_request`
  ADD PRIMARY KEY (`request_id`);

--
-- Indexes for table `tb_item_stock`
--
ALTER TABLE `tb_item_stock`
  ADD PRIMARY KEY (`stock_id`),
  ADD UNIQUE KEY `stock_code` (`stock_code`),
  ADD KEY `fk_batch` (`stock_batch`),
  ADD KEY `fk_name` (`stock_name`),
  ADD KEY `fk_category_item` (`stock_category`);

--
-- Indexes for table `tb_user`
--
ALTER TABLE `tb_user`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tb_catalog_category`
--
ALTER TABLE `tb_catalog_category`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `tb_catalog_item`
--
ALTER TABLE `tb_catalog_item`
  MODIFY `item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `tb_item_batch`
--
ALTER TABLE `tb_item_batch`
  MODIFY `batch_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT for table `tb_item_report`
--
ALTER TABLE `tb_item_report`
  MODIFY `report_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tb_item_request`
--
ALTER TABLE `tb_item_request`
  MODIFY `request_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tb_item_stock`
--
ALTER TABLE `tb_item_stock`
  MODIFY `stock_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=125148;

--
-- AUTO_INCREMENT for table `tb_user`
--
ALTER TABLE `tb_user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tb_catalog_item`
--
ALTER TABLE `tb_catalog_item`
  ADD CONSTRAINT `fk_category` FOREIGN KEY (`item_category`) REFERENCES `tb_catalog_category` (`category_name`) ON UPDATE CASCADE;

--
-- Constraints for table `tb_item_report`
--
ALTER TABLE `tb_item_report`
  ADD CONSTRAINT `fk_code` FOREIGN KEY (`report_code`) REFERENCES `tb_item_stock` (`stock_code`) ON UPDATE CASCADE;

--
-- Constraints for table `tb_item_stock`
--
ALTER TABLE `tb_item_stock`
  ADD CONSTRAINT `fk_batch` FOREIGN KEY (`stock_batch`) REFERENCES `tb_item_batch` (`batch_id`),
  ADD CONSTRAINT `fk_category_item` FOREIGN KEY (`stock_category`) REFERENCES `tb_catalog_category` (`category_name`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_name` FOREIGN KEY (`stock_name`) REFERENCES `tb_catalog_item` (`item_name`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
