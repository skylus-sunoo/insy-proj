-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 14, 2025 at 05:03 PM
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
(1, 'Catalog-C-1', 'Books and Educational Resources'),
(2, 'Catalog-C-2', 'Cafeteria Supplies'),
(3, 'Catalog-C-3', 'Classroom Supplies'),
(4, 'Catalog-C-4', 'Cleaning Supplies'),
(5, 'Catalog-C-5', 'Electronics and IT Equipment'),
(6, 'Catalog-C-6', 'Furniture'),
(7, 'Catalog-C-7', 'Lab Equipment and Materials'),
(8, 'Catalog-C-8', 'Maintenance / Facilities'),
(9, 'Catalog-C-9', 'Medical Equipment'),
(10, 'Catalog-C-10', 'Miscellaneous'),
(11, 'Catalog-C-11', 'N/A'),
(12, 'Catalog-C-12', 'Office Supplies'),
(13, 'Catalog-C-13', 'Security and Safety Equipment'),
(14, 'Catalog-C-14', 'Sports Equipment');

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
(1, 'Catalog-I-14-1', 'Sports Equipment', 'Volleyball Ball', 'PIECE'),
(2, 'Catalog-I-14-2', 'Sports Equipment', 'Volleyball Net', 'PIECE'),
(3, 'Catalog-I-5-3', 'Electronics and IT Equipment', 'VOM (Volt Ohm Meter)', 'PIECE'),
(4, 'Catalog-I-6-4', 'Furniture', 'Wall Clock', 'UNIT'),
(5, 'Catalog-I-10-5', 'Miscellaneous', 'Washing Machine', 'UNIT'),
(6, 'Catalog-I-4-6', 'Cleaning Supplies', 'Waste Basket', 'PIECE'),
(7, 'Catalog-I-2-7', 'Cafeteria Supplies', 'Water and Coffee Boiler', 'UNIT'),
(8, 'Catalog-I-2-8', 'Cafeteria Supplies', 'Water Dispenser', 'PIECE'),
(9, 'Catalog-I-7-9', 'Lab Equipment and Materials', 'Weighing Scale', 'PIECE'),
(10, 'Catalog-I-7-10', 'Lab Equipment and Materials', 'Weight Sets', 'SET'),
(11, 'Catalog-I-9-11', 'Medical Equipment', 'Wheelchair', 'PIECE'),
(12, 'Catalog-I-8-12', 'Maintenance / Facilities', 'Wheelbarrow', 'PIECE'),
(13, 'Catalog-I-14-13', 'Sports Equipment', 'Whistle', 'PIECE'),
(14, 'Catalog-I-3-14', 'Classroom Supplies', 'Whiteboard', 'PIECE'),
(15, 'Catalog-I-5-15', 'Electronics and IT Equipment', 'Wifi Dongle', 'UNIT'),
(16, 'Catalog-I-5-16', 'Electronics and IT Equipment', 'Wifi Repeater', 'PIECE'),
(17, 'Catalog-I-2-17', 'Cafeteria Supplies', 'Wine Glass', 'PIECE'),
(18, 'Catalog-I-8-18', 'Maintenance / Facilities', 'Wire Cutter', 'UNIT'),
(19, 'Catalog-I-13-19', 'Security and Safety Equipment', 'PPE', 'UNIT'),
(20, 'Catalog-I-5-20', 'Electronics and IT Equipment', 'Monitor', 'PIECE'),
(21, 'Catalog-I-10-21', 'Miscellaneous', 'Uniforms', 'PIECE');

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
(1, '2024-12-19 13:20:31'),
(2, '2024-12-19 13:22:54'),
(3, '2024-12-19 13:26:03'),
(4, '2024-12-19 13:29:46'),
(5, '2024-12-19 13:30:46'),
(6, '2024-12-19 13:34:36'),
(7, '2024-12-19 13:36:00'),
(8, '2024-12-19 13:38:11'),
(9, '2024-12-19 13:39:34'),
(10, '2024-12-19 13:41:05'),
(11, '2024-12-19 13:43:17'),
(12, '2024-12-19 13:44:57'),
(13, '2024-12-19 13:47:12'),
(14, '2024-12-19 13:48:37'),
(15, '2024-12-19 13:49:32'),
(16, '2024-12-19 13:52:56'),
(17, '2025-01-07 02:31:35'),
(18, '2025-01-07 02:32:10'),
(19, '2025-01-07 02:46:28'),
(20, '2025-01-13 00:48:29'),
(21, '2025-01-13 08:52:09'),
(22, '2025-01-13 09:01:03'),
(23, '2025-01-13 09:23:17'),
(24, '2025-01-13 09:26:04'),
(25, '2025-01-13 09:30:38'),
(26, '2025-01-13 09:33:45'),
(27, '2025-01-13 09:37:04'),
(28, '2025-01-13 09:53:43'),
(29, '2025-01-13 10:01:53');

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
('2025-01-13 12:27:52', 'MANAGEMENT', 'DELETE', 'Silang-25-500', 'Silang-25-501', '', ''),
('2025-01-13 12:28:32', 'MANAGEMENT', 'DELETE', 'Silang-25-105007', 'Silang-25-105007', '', ''),
('2025-01-13 12:28:39', 'CATALOG', 'ADD', '', '', '; Category Name: A', ''),
('2025-01-13 12:28:42', 'CATALOG', 'DELETE', '', '', '; Category Name: A', ''),
('2025-01-13 12:38:59', 'CATALOG', 'ADD', '', '', '; Item Name: Volleyball Balls; Category: Sports Equipment; UOM: PIECE', ''),
('2025-01-13 12:40:14', 'CATALOG', 'UPDATE', '', '', '; Item Name: Volleyball Balls -> Volleyball Ball', ''),
('2025-01-13 12:40:22', 'CATALOG', 'UPDATE', '', '', '; Item Name: Volleyball Balls -> Volleyball Ba; Category: Sports Equipment -> Electronics and IT Equipment; UOM: PIECE -> SET', ''),
('2025-01-13 12:40:39', 'CATALOG', 'DELETE', '', '', '; Item Name: Volleyball Ba', ''),
('2025-01-13 12:41:39', 'CATALOG', 'ADD', '', '', '; Item Name: Keyboarda; Category: Electronics and IT Equipment; UOM: UNIT', ''),
('2025-01-13 12:41:49', 'CATALOG', 'DELETE', '', '', '; Item Name: Keyboarda', ''),
('2025-01-13 23:09:33', 'MANAGEMENT', 'UPDATE', 'Silang-25-800', 'Silang-25-800', '; Price: 250.00 -> 300.00', 'N/A'),
('2025-01-14 05:57:47', 'REQUEST', 'ADD', '', '', '; Item Name: Laptop; Description: ASUS; Quantity: 1', 'John'),
('2025-01-14 06:01:50', 'REQUEST', 'UPDATE', '', '', '; Quantity: 1 -> 2', 'John'),
('2025-01-14 06:02:24', 'REQUEST', 'UPDATE', '', '', '; Status: PENDING -> DENIED', 'John'),
('2025-01-14 06:02:30', 'REQUEST', 'DELETE', '', '', '; Item Name: Laptop', 'John'),
('2025-01-14 06:13:14', 'REQUEST', 'ADD', '', '', '; Item Name: laptop; Description: big; Quantity: 1', 'john'),
('2025-01-14 06:13:30', 'REQUEST', 'ADD', '', '', '; Item Name: computer; Description: small; Quantity: 1', 'john'),
('2025-01-14 06:42:24', 'REQUEST', 'ADD', '', '', '; Item Name: Bulb; Description: yellow; Quantity: 6', 'Danel'),
('2025-01-14 06:42:52', 'REQUEST', 'UPDATE', '', '', '; Status: PENDING -> RECEIVED', 'Danel'),
('2025-01-14 06:45:04', 'REQUEST', 'UPDATE', '', '', '; Status: PENDING -> RECEIVED', 'john'),
('2025-01-14 06:49:41', 'REQUEST', 'ADD', 'laptop', 'laptop', '; Description: big; Quantity: 1', 'john'),
('2025-01-14 06:51:14', 'REQUEST', 'UPDATE', 'laptop', 'laptop', '; Description: big -> medium', 'john'),
('2025-01-14 06:51:58', 'REQUEST', 'DELETE', 'laptop', 'laptop', '', 'john'),
('2025-01-14 06:57:25', 'REQUEST', 'DELETE', 'Request-5', 'Request-5', '; Item Name: laptop', 'john'),
('2025-01-14 06:57:28', 'REQUEST', 'DELETE', 'Request-4', 'Request-4', '; Item Name: Bulb', 'Danel'),
('2025-01-14 06:57:30', 'REQUEST', 'DELETE', 'Request-3', 'Request-3', '; Item Name: computer', 'john'),
('2025-01-14 06:57:40', 'REQUEST', 'ADD', 'Request-6', 'Request-6', '; Item Name: Bulb; Description: yellow; Quantity: 1', 'John'),
('2025-01-14 06:58:32', 'REQUEST', 'UPDATE', 'Request-6', 'Request-6', '; Description: yellow -> blue', 'John'),
('2025-01-14 06:58:37', 'REQUEST', 'UPDATE', 'Request-6', 'Request-6', '; Status: PENDING -> RECEIVED', 'John'),
('2025-01-14 06:58:58', 'REQUEST', 'DELETE', 'Request-6', 'Request-6', '; Item Name: Bulb', 'John'),
('2025-01-14 07:05:56', 'CATALOG', 'UPDATE', 'Catalog-C1', 'Catalog-C1', '; Category Name: Books and Educational Resources -> Books and Educational Resourcess', ''),
('2025-01-14 07:06:10', 'CATALOG', 'UPDATE', 'Catalog-C1', 'Catalog-C1', '; Category Name: Books and Educational Resourcess -> Books and Educational Resources', ''),
('2025-01-14 07:06:48', 'CATALOG', 'ADD', 'Catalog-C-27', 'Catalog-C-27', '; Category Name: aa', ''),
('2025-01-14 07:06:51', 'CATALOG', 'DELETE', 'Catalog-C-', 'Catalog-C-', '; Category Name: aa', ''),
('2025-01-14 07:09:27', 'CATALOG', 'ADD', 'Catalog-C-28', 'Catalog-C-28', '; Category Name: ss', ''),
('2025-01-14 07:09:31', 'CATALOG', 'UPDATE', 'Catalog-C-28', 'Catalog-C-28', '; Category Name: ss -> ssa', ''),
('2025-01-14 07:09:34', 'CATALOG', 'DELETE', 'Catalog-C-28', 'Catalog-C-28', '; Category Name: ssa', ''),
('2025-01-14 07:09:58', 'CATALOG', 'ADD', 'Catalog-I-33', 'Catalog-I-33', '; Item Name: Cable; Category: N/A; UOM: PIECE', ''),
('2025-01-14 07:10:07', 'CATALOG', 'UPDATE', 'Catalog-I-', 'Catalog-I-', '; Item Name: Cable -> Cables', ''),
('2025-01-14 07:10:14', 'CATALOG', 'UPDATE', 'Catalog-I-', 'Catalog-I-', '', ''),
('2025-01-14 07:11:52', 'CATALOG', 'ADD', 'Catalog-C-29', 'Catalog-C-29', '; Category Name: Alergy', ''),
('2025-01-14 07:12:03', 'CATALOG', 'ADD', 'Catalog-I-34', 'Catalog-I-34', '; Item Name: Algae; Category: Alergy; UOM: PIECE', ''),
('2025-01-14 07:12:10', 'CATALOG', 'UPDATE', 'Catalog-I-34', 'Catalog-I-34', '; Item Name: Algae -> Algaea; UOM: PIECE -> SET', ''),
('2025-01-14 07:12:14', 'CATALOG', 'DELETE', 'Catalog-I-34', 'Catalog-I-34', '; Item Name: Algaea', ''),
('2025-01-14 07:12:18', 'CATALOG', 'DELETE', 'Catalog-C-29', 'Catalog-C-29', '; Category Name: Alergy', ''),
('2025-01-14 07:14:15', 'REQUEST', 'ADD', 'Request-7', 'Request-7', '; Item Name: Algae; Description: green; Quantity: 64', 'John'),
('2025-01-14 07:14:52', 'REQUEST', 'UPDATE', 'Request-7', 'Request-7', '; Description: green -> greenblue', 'John'),
('2025-01-14 07:14:58', 'REQUEST', 'UPDATE', 'Request-7', 'Request-7', '; Status: PENDING -> DENIED', 'John'),
('2025-01-14 07:15:10', 'REQUEST', 'DELETE', 'Request-7', 'Request-7', '; Item Name: Algae', 'John'),
('2025-01-14 07:16:46', 'CATALOG', 'DELETE', 'Catalog-I-33', 'Catalog-I-33', '; Item Name: Cables', ''),
('2025-01-14 12:21:26', 'CATALOG', 'UPDATE', 'Catalog-I-30', 'Catalog-I-30', '; Item Name: Uniform -> Uniforms', ''),
('2025-01-14 12:21:34', 'CATALOG', 'DELETE', 'Catalog-I-30', 'Catalog-I-30', '; Item Name: Uniforms', ''),
('2025-01-14 12:24:52', 'CATALOG', 'DELETE', 'Catalog-I-29', 'Catalog-I-29', '; Item Name: Keyboard', ''),
('2025-01-14 12:24:58', 'CATALOG', 'DELETE', 'Catalog-I-30', 'Catalog-I-30', '; Item Name: Uniforms', ''),
('2025-01-14 12:25:33', 'CATALOG', 'DELETE', 'Catalog-I-30', 'Catalog-I-30', '; Item Name: Uniforms', ''),
('2025-01-14 13:53:47', 'REQUEST', 'ADD', 'Request-8', 'Request-8', '; Item Name: hi; Description: hi; Quantity: 1', 'hi'),
('2025-01-14 13:54:00', 'REQUEST', 'DELETE', 'Request-8', 'Request-8', '; Item Name: hi', 'hi'),
('2025-01-14 14:10:41', 'CATALOG', 'ADD', 'Catalog-C-32', 'Catalog-C-32', '; Category Name: a', ''),
('2025-01-14 14:12:53', 'CATALOG', 'DELETE', 'Catalog-C-32', 'Catalog-C-32', '; Category Name: a', ''),
('2025-01-14 14:38:21', 'CATALOG', 'ADD', 'Catalog-C-43', 'Catalog-C-43', '; Category Name: xx', ''),
('2025-01-14 14:44:09', 'CATALOG', 'ADD', 'Catalog-I-43-35', 'Catalog-I-43-35', '; Item Name: as; Category: xx; UOM: PIECE', ''),
('2025-01-14 15:16:21', 'CATALOG', 'DELETE', 'Catalog-I-21', 'Catalog-I-21', '; Item Name: Uniforms', ''),
('2025-01-14 15:16:26', 'CATALOG', 'DELETE', 'Catalog-C-15', 'Catalog-C-15', '; Category Name: xx', ''),
('2025-01-14 15:16:29', 'CATALOG', 'DELETE', 'Catalog-I-22', 'Catalog-I-22', '; Item Name: as', ''),
('2025-01-14 15:16:32', 'CATALOG', 'DELETE', 'Catalog-C-15', 'Catalog-C-15', '; Category Name: xx', ''),
('2025-01-14 15:21:32', 'CATALOG', 'ADD', 'Catalog-C-16', 'Catalog-C-16', '; Category Name: as', ''),
('2025-01-14 15:21:38', 'CATALOG', 'ADD', 'Catalog-I-16-23', 'Catalog-I-16-23', '; Item Name: aaa; Category: as; UOM: PIECE', ''),
('2025-01-14 15:21:56', 'CATALOG', 'UPDATE', 'Catalog-I-16-23', 'Catalog-I-16-23', '; Item Name: aaa -> bbb', ''),
('2025-01-14 15:22:09', 'CATALOG', 'UPDATE', 'Catalog-I-16-23', 'Catalog-I-16-23', '; Category: as -> Books and Educational Resources', ''),
('2025-01-14 15:31:48', 'CATALOG', 'UPDATE', 'Catalog-I-2-23', 'Catalog-I-2-23', '; Category: Books and Educational Resources -> Cafeteria Supplies', ''),
('2025-01-14 15:32:18', 'CATALOG', 'UPDATE', 'Catalog-I-2-23', 'Catalog-I-2-23', '; Item Name: bbb -> asd', ''),
('2025-01-14 15:32:32', 'CATALOG', 'UPDATE', 'Catalog-I-11-23', 'Catalog-I-11-23', '; Item Name: asd -> cables; Category: Cafeteria Supplies -> N/A', ''),
('2025-01-14 15:32:53', 'CATALOG', 'UPDATE', 'Catalog-I-16-23', 'Catalog-I-16-23', '; Category: N/A -> as', ''),
('2025-01-14 15:32:57', 'CATALOG', 'DELETE', '', '', '; Category Name: as', ''),
('2025-01-14 15:33:36', 'CATALOG', 'DELETE', '', '', '; Category Name: as', ''),
('2025-01-14 15:33:51', 'CATALOG', 'DELETE', 'Catalog-C-16', 'Catalog-C-16', '; Category Name: as', ''),
('2025-01-14 15:33:55', 'CATALOG', 'DELETE', 'Catalog-I-16-23', 'Catalog-I-16-23', '; Item Name: cables', ''),
('2025-01-14 15:33:58', 'CATALOG', 'DELETE', 'Catalog-C-16', 'Catalog-C-16', '; Category Name: as', ''),
('2025-01-14 15:34:42', 'CATALOG', 'DELETE', 'Catalog-C-14', 'Catalog-C-14', '; Category Name: Sports Equipment', ''),
('2025-01-14 15:36:05', 'CATALOG', 'UPDATE', 'Catalog-I-5-20', 'Catalog-I-5-20', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `tb_item_request`
--

CREATE TABLE `tb_item_request` (
  `request_id` int(11) NOT NULL,
  `request_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `request_name` varchar(255) NOT NULL,
  `request_item` varchar(255) NOT NULL,
  `request_desc` varchar(1023) NOT NULL,
  `request_quantity` int(16) NOT NULL DEFAULT 1,
  `request_status` enum('PENDING','DENIED','RECEIVED') NOT NULL DEFAULT 'PENDING'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tb_item_stock`
--

CREATE TABLE `tb_item_stock` (
  `stock_timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `stock_id` int(11) NOT NULL,
  `stock_category` varchar(255) NOT NULL,
  `stock_name` varchar(255) NOT NULL,
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

INSERT INTO `tb_item_stock` (`stock_timestamp`, `stock_id`, `stock_category`, `stock_name`, `stock_desc`, `stock_price`, `stock_dod`, `stock_benefactor`, `stock_location`, `stock_holder`, `stock_code`, `stock_batch`) VALUES
('2025-01-13 09:37:04', 800, 'Sports Equipment', 'Volleyball Ball', 'rounded', 300, '2025-01-13', 'Clarisse Penus', 'Supply Room', 'N/A', 'Silang-25-800', 27),
('2025-01-13 08:52:09', 1000, 'Electronics and IT Equipment', 'Monitor', '', 500, '2025-01-13', 'Joseph S. Callanta', 'Supply Room', 'N/A', 'Silang-25-1000', 21),
('2025-01-13 08:52:09', 1001, 'Electronics and IT Equipment', 'Monitor', '', 500, '2025-01-13', 'Joseph S. Callanta', 'Supply Room', 'N/A', 'Silang-25-1001', 21),
('2024-12-19 13:31:58', 20797, 'Sports Equipment', 'Volleyball Net', '', 2330, '2017-05-25', 'Hazelyn H. Dela Cruz', 'Gym', 'Gabriel Bautista', 'Silang-17-20797', 5),
('2024-12-19 13:31:58', 20798, 'Sports Equipment', 'Volleyball Net', '', 2330, '2017-05-25', 'Hazelyn H. Dela Cruz', 'Gym', 'Gabriel Bautista', 'Silang-17-20798', 5),
('2024-12-19 13:31:58', 24914, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 390', 1250, '2017-10-25', 'Joseph S. Callanta', 'Library', 'N/A', 'Silang-17-24914', 2),
('2024-12-19 13:31:58', 24915, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 390', 1250, '2017-10-25', 'Joseph S. Callanta', 'Library', 'N/A', 'Silang-17-24915', 2),
('2024-12-19 13:31:58', 24916, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 390', 1250, '2017-10-25', 'Joseph S. Callanta', 'Library', 'N/A', 'Silang-17-24916', 2),
('2024-12-19 13:31:58', 24917, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 390', 1250, '2017-10-25', 'Joseph S. Callanta', 'Library', 'N/A', 'Silang-17-24917', 2),
('2024-12-19 13:31:58', 24918, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 390', 1250, '2017-10-25', 'Joseph S. Callanta', 'Library', 'N/A', 'Silang-17-24918', 2),
('2024-12-19 13:38:11', 27621, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Gym', 'Clarisse Penus', 'Silang-17-27621', 8),
('2024-12-19 13:38:11', 27622, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Gym', 'Clarisse Penus', 'Silang-17-27622', 8),
('2024-12-19 13:38:11', 27623, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Gym', 'Clarisse Penus', 'Silang-17-27623', 8),
('2024-12-19 13:38:11', 27624, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Gym', 'Clarisse Penus', 'Silang-17-27624', 8),
('2024-12-19 13:38:11', 27625, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Gym', 'Clarisse Penus', 'Silang-17-27625', 8),
('2024-12-19 13:38:11', 27626, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Gym', 'Clarisse Penus', 'Silang-17-27626', 8),
('2024-12-19 13:38:11', 27627, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Gym', 'Clarisse Penus', 'Silang-17-27627', 8),
('2024-12-19 13:38:11', 27628, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Gym', 'Clarisse Penus', 'Silang-17-27628', 8),
('2024-12-19 13:38:11', 27629, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Gym', 'Clarisse Penus', 'Silang-17-27629', 8),
('2024-12-19 13:38:11', 27630, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Gym', 'Clarisse Penus', 'Silang-17-27630', 8),
('2024-12-19 13:31:58', 35912, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35912', 3),
('2024-12-19 13:31:58', 35913, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35913', 3),
('2024-12-19 13:31:58', 35914, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35914', 3),
('2024-12-19 13:31:58', 35915, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35915', 3),
('2024-12-19 13:31:58', 35916, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35916', 3),
('2024-12-19 13:31:58', 35917, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35917', 3),
('2024-12-19 13:31:58', 35918, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35918', 3),
('2024-12-19 13:31:58', 35919, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35919', 3),
('2024-12-19 13:31:58', 35920, 'Sports Equipment', 'Volleyball Net', 'Voleyball Net GTO', 850, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35920', 4),
('2024-12-19 13:31:58', 35921, 'Sports Equipment', 'Volleyball Net', 'Voleyball Net GTO', 850, '2018-08-23', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-18-35921', 4),
('2024-12-19 13:49:32', 36071, 'Lab Equipment and Materials', 'Weighing Scale', 'Digital, 120kg.', 1100, '2018-09-13', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-18-36071', 15),
('2024-12-19 13:39:34', 41366, 'Furniture', 'Wall Clock', '', 90, '2017-12-06', 'Elena B. Telmo', 'OSAS', 'Beverly Malabag', 'Silang-17-41366', 9),
('2024-12-19 13:52:56', 41973, 'Lab Equipment and Materials', 'Weight Sets', 'Stainless steel hooked, 1000g', 1975, '2018-12-19', 'Joseph S. Callanta', 'Supply Room', 'N/A', 'Silang-18-41973', 16),
('2024-12-19 13:52:56', 41974, 'Lab Equipment and Materials', 'Weight Sets', 'Stainless steel hooked, 1000g', 1975, '2018-12-19', 'Joseph S. Callanta', 'Supply Room', 'N/A', 'Silang-18-41974', 16),
('2024-12-19 13:43:17', 55561, 'Miscellaneous', 'Washing Machine', 'With dryer, HD, 11kg. / Whirlpool WWT 110x', 13125, '2015-01-12', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-24-55561', 11),
('2024-12-19 13:44:57', 55662, 'Cafeteria Supplies', 'Water and Coffee Boiler', 'Imarflex IWB-15008', 5499.75, '2019-07-22', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-19-55662', 12),
('2024-12-19 13:41:05', 58348, 'Miscellaneous', 'Washing Machine', 'fully auto LG, WF-S120V 12kg', 32750, '2017-05-12', 'Beverly A. Malabag', 'Supply Room', 'N/A', 'Silang-17-58348', 10),
('2024-12-19 13:36:00', 75399, 'Furniture', 'Wall Clock', '16\" / Stainless / Asahi', 1073.5, '2021-06-16', 'Jenny Beb F. Ebo', 'Supply Room', 'N/A', 'Silang-24-75399', 7),
('2024-12-19 13:36:00', 75400, 'Furniture', 'Wall Clock', '16\" / Stainless / Asahi', 1073.5, '2021-06-16', 'Jenny Beb F. Ebo', 'Supply Room', 'N/A', 'Silang-24-75400', 7),
('2024-12-19 13:36:00', 75401, 'Furniture', 'Wall Clock', '16\" / Stainless / Asahi', 1073.5, '2021-06-16', 'Jenny Beb F. Ebo', 'Supply Room', 'N/A', 'Silang-24-75401', 7),
('2024-12-19 13:36:00', 75402, 'Furniture', 'Wall Clock', '16\" / Stainless / Asahi', 1073.5, '2021-06-16', 'Jenny Beb F. Ebo', 'Supply Room', 'N/A', 'Silang-24-75402', 7),
('2024-12-19 13:47:12', 92149, 'Cafeteria Supplies', 'Water Dispenser', 'hot and cold, camel', 5700, '2021-09-07', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-21-92149', 13),
('2024-12-19 13:48:37', 98660, 'Cafeteria Supplies', 'Water Dispenser', 'table top/ Kyowa KQ1501 / hot / cold', 1958, '2022-01-01', 'Roselyn A. Ymana', 'Supply Room', 'N/A', 'Silang-22-98660', 14),
('2024-12-19 13:34:36', 104483, 'Electronics and IT Equipment', 'VOM (Volt Ohm Meter)', 'VOM Multitester Ingco', 500, '2022-10-26', 'Elena B. Telmo', 'Supply Room', 'N/A', 'Silang-22-104483', 6),
('2025-01-07 02:32:10', 104984, 'Sports Equipment', 'Volleyball Ball', 'bigg', 200, '2025-01-07', 'Clarisse Penus', 'DIT Faculty', 'N/A', 'Silang-25-104984', 18),
('2025-01-07 02:32:10', 104985, 'Sports Equipment', 'Volleyball Ball', 'bigg', 200, '2025-01-07', 'Clarisse Penus', 'Supply Room', 'N/A', 'Silang-25-104985', 18),
('2025-01-07 02:46:28', 104986, 'Electronics and IT Equipment', 'Monitor', 'aorus', 7000, '2025-01-07', 'Danel Dave Barbuco', 'CL2', 'Danel Dave Barbuco', 'Silang-25-104986', 19),
('2025-01-07 02:46:28', 104987, 'Electronics and IT Equipment', 'Monitor', 'aorus', 7000, '2025-01-07', 'Danel Dave Barbuco', 'CL2', 'Danel Dave Barbuco', 'Silang-25-104987', 19),
('2025-01-13 00:48:29', 104988, 'Security and Safety Equipment', 'PPE', '', 5000, '2025-01-13', 'Drixen Paul Calzado', 'Supply Room', 'N/A', 'Silang-25-104988', 20),
('2025-01-13 00:48:29', 104989, 'Security and Safety Equipment', 'PPE', '', 5000, '2025-01-13', 'Drixen Paul Calzado', 'Supply Room', 'N/A', 'Silang-25-104989', 20),
('2025-01-13 00:48:29', 104990, 'Security and Safety Equipment', 'PPE', '', 5000, '2025-01-13', 'Drixen Paul Calzado', 'Supply Room', 'N/A', 'Silang-25-104990', 20),
('2025-01-13 00:48:29', 104991, 'Security and Safety Equipment', 'PPE', '', 5000, '2025-01-13', 'Drixen Paul Calzado', 'Supply Room', 'N/A', 'Silang-25-104991', 20),
('2025-01-13 00:48:29', 104992, 'Security and Safety Equipment', 'PPE', '', 5000, '2025-01-13', 'Drixen Paul Calzado', 'Supply Room', 'N/A', 'Silang-25-104992', 20),
('2025-01-13 09:01:03', 104993, 'Electronics and IT Equipment', 'Monitor', 'small', 5000, '2025-01-15', 'Kenny Guillaran', 'CL1', 'Danel Dave Barbuco', 'Silang-25-104993', 22),
('2025-01-13 09:01:03', 104994, 'Electronics and IT Equipment', 'Monitor', 'small', 5000, '2025-01-15', 'Kenny Guillaran', 'CL1', 'Danel Dave Barbuco', 'Silang-25-104994', 22),
('2025-01-13 09:01:03', 104995, 'Electronics and IT Equipment', 'Monitor', 'small', 5000, '2025-01-15', 'Kenny Guillaran', 'CL1', 'Danel Dave Barbuco', 'Silang-25-104995', 22),
('2025-01-13 09:01:03', 104996, 'Electronics and IT Equipment', 'Monitor', 'small', 5000, '2025-01-15', 'Kenny Guillaran', 'CL1', 'Danel Dave Barbuco', 'Silang-25-104996', 22),
('2025-01-13 09:01:03', 104997, 'Electronics and IT Equipment', 'Monitor', 'small', 5000, '2025-01-15', 'Kenny Guillaran', 'CL1', 'Danel Dave Barbuco', 'Silang-25-104997', 22),
('2025-01-13 10:01:53', 105008, 'Miscellaneous', 'Uniforms', 'yellow', 400, '2025-01-13', 'Aljon Pilar', 'RM 334B', 'Aljon Pilar', 'Silang-25-105008', 29),
('2025-01-13 10:01:53', 105009, 'Miscellaneous', 'Uniforms', 'yellow', 400, '2025-01-13', 'Aljon Pilar', 'RM 334A', 'Aljon Pilar', 'Silang-25-105009', 29),
('2025-01-13 10:01:53', 105010, 'Miscellaneous', 'Uniforms', 'yellow', 400, '2025-01-13', 'Aljon Pilar', 'RM 334B', 'Aljon Pilar', 'Silang-25-105010', 29),
('2025-01-13 10:01:53', 105011, 'Miscellaneous', 'Uniforms', 'yellow', 400, '2025-01-13', 'Aljon Pilar', 'RM 334B', 'Aljon Pilar', 'Silang-25-105011', 29),
('2025-01-13 10:01:53', 105012, 'Miscellaneous', 'Uniforms', 'yellow', 400, '2025-01-13', 'Aljon Pilar', 'RM 334B', 'Aljon Pilar', 'Silang-25-105012', 29),
('2025-01-13 10:01:53', 105013, 'Miscellaneous', 'Uniforms', 'yellow', 400, '2025-01-13', 'Aljon Pilar', 'RM 334B', 'Aljon Pilar', 'Silang-25-105013', 29);

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
-- Indexes for table `tb_item_request`
--
ALTER TABLE `tb_item_request`
  ADD PRIMARY KEY (`request_id`);

--
-- Indexes for table `tb_item_stock`
--
ALTER TABLE `tb_item_stock`
  ADD PRIMARY KEY (`stock_id`),
  ADD KEY `fk_batch` (`stock_batch`),
  ADD KEY `fk_name` (`stock_name`);

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
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `tb_catalog_item`
--
ALTER TABLE `tb_catalog_item`
  MODIFY `item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `tb_item_batch`
--
ALTER TABLE `tb_item_batch`
  MODIFY `batch_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `tb_item_request`
--
ALTER TABLE `tb_item_request`
  MODIFY `request_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `tb_item_stock`
--
ALTER TABLE `tb_item_stock`
  MODIFY `stock_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=105014;

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
-- Constraints for table `tb_item_stock`
--
ALTER TABLE `tb_item_stock`
  ADD CONSTRAINT `fk_batch` FOREIGN KEY (`stock_batch`) REFERENCES `tb_item_batch` (`batch_id`),
  ADD CONSTRAINT `fk_name` FOREIGN KEY (`stock_name`) REFERENCES `tb_catalog_item` (`item_name`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
