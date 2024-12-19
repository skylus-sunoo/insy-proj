-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 19, 2024 at 02:54 PM
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
  `category_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_catalog_category`
--

INSERT INTO `tb_catalog_category` (`category_id`, `category_name`) VALUES
(1, 'Books and Educational Resources'),
(2, 'Cafeteria Supplies'),
(3, 'Classroom Supplies'),
(4, 'Cleaning Supplies'),
(5, 'Electronics and IT Equipment'),
(6, 'Furniture'),
(7, 'Lab Equipment and Materials'),
(23, 'Maintenance / Facilities'),
(22, 'Medical Equipment'),
(8, 'Miscellaneous'),
(9, 'N/A'),
(10, 'Office Supplies'),
(11, 'Security and Safety Equipment'),
(12, 'Sports Equipment');

-- --------------------------------------------------------

--
-- Table structure for table `tb_catalog_item`
--

CREATE TABLE `tb_catalog_item` (
  `item_id` int(11) NOT NULL,
  `item_category` varchar(255) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `item_uom` enum('PIECE','UNIT','SET') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_catalog_item`
--

INSERT INTO `tb_catalog_item` (`item_id`, `item_category`, `item_name`, `item_uom`) VALUES
(9, 'Sports Equipment', 'Volleyball Ball', 'PIECE'),
(10, 'Sports Equipment', 'Volleyball Net', 'PIECE'),
(11, 'Electronics and IT Equipment', 'VOM (Volt Ohm Meter)', 'PIECE'),
(12, 'Furniture', 'Wall Clock', 'UNIT'),
(13, 'Miscellaneous', 'Washing Machine', 'UNIT'),
(14, 'Cleaning Supplies', 'Waste Basket', 'PIECE'),
(15, 'Cafeteria Supplies', 'Water and Coffee Boiler', 'UNIT'),
(16, 'Cafeteria Supplies', 'Water Dispenser', 'PIECE'),
(17, 'Lab Equipment and Materials', 'Weighing Scale', 'PIECE'),
(18, 'Lab Equipment and Materials', 'Weight Sets', 'SET'),
(19, 'Medical Equipment', 'Wheelchair', 'PIECE'),
(20, 'Maintenance / Facilities', 'Wheelbarrow', 'PIECE'),
(21, 'Sports Equipment', 'Whistle', 'PIECE'),
(22, 'Classroom Supplies', 'Whiteboard', 'PIECE'),
(23, 'Electronics and IT Equipment', 'Wifi Dongle', 'UNIT'),
(24, 'Electronics and IT Equipment', 'Wifi Repeater', 'PIECE'),
(25, 'Cafeteria Supplies', 'Wine Glass', 'PIECE'),
(26, 'Maintenance / Facilities', 'Wire Cutter', 'UNIT');

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
(16, '2024-12-19 13:52:56');

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
  `stock_user` varchar(255) NOT NULL,
  `stock_code` varchar(50) DEFAULT NULL,
  `stock_batch` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_item_stock`
--

INSERT INTO `tb_item_stock` (`stock_timestamp`, `stock_id`, `stock_category`, `stock_name`, `stock_desc`, `stock_price`, `stock_dod`, `stock_user`, `stock_code`, `stock_batch`) VALUES
('2024-12-19 13:31:58', 20797, 'Sports Equipment', 'Volleyball Net', '', 2330, '2017-05-25', 'Hazelyn H. Dela Cruz', 'Silang-17-20797', 5),
('2024-12-19 13:31:58', 20798, 'Sports Equipment', 'Volleyball Net', '', 2330, '2017-05-25', 'Hazelyn H. Dela Cruz', 'Silang-17-20798', 5),
('2024-12-19 13:31:58', 24914, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 390', 1250, '2017-10-25', 'Joseph S. Callanta', 'Silang-17-24914', 2),
('2024-12-19 13:31:58', 24915, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 390', 1250, '2017-10-25', 'Joseph S. Callanta', 'Silang-17-24915', 2),
('2024-12-19 13:31:58', 24916, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 390', 1250, '2017-10-25', 'Joseph S. Callanta', 'Silang-17-24916', 2),
('2024-12-19 13:31:58', 24917, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 390', 1250, '2017-10-25', 'Joseph S. Callanta', 'Silang-17-24917', 2),
('2024-12-19 13:31:58', 24918, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 390', 1250, '2017-10-25', 'Joseph S. Callanta', 'Silang-17-24918', 2),
('2024-12-19 13:38:11', 27621, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Silang-17-27621', 8),
('2024-12-19 13:38:11', 27622, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Silang-17-27622', 8),
('2024-12-19 13:38:11', 27623, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Silang-17-27623', 8),
('2024-12-19 13:38:11', 27624, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Silang-17-27624', 8),
('2024-12-19 13:38:11', 27625, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Silang-17-27625', 8),
('2024-12-19 13:38:11', 27626, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Silang-17-27626', 8),
('2024-12-19 13:38:11', 27627, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Silang-17-27627', 8),
('2024-12-19 13:38:11', 27628, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Silang-17-27628', 8),
('2024-12-19 13:38:11', 27629, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Silang-17-27629', 8),
('2024-12-19 13:38:11', 27630, 'Furniture', 'Wall Clock', 'big', 400, '2017-12-06', 'Elena B. Telmo', 'Silang-17-27630', 8),
('2024-12-19 13:31:58', 35912, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Silang-18-35912', 3),
('2024-12-19 13:31:58', 35913, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Silang-18-35913', 3),
('2024-12-19 13:31:58', 35914, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Silang-18-35914', 3),
('2024-12-19 13:31:58', 35915, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Silang-18-35915', 3),
('2024-12-19 13:31:58', 35916, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Silang-18-35916', 3),
('2024-12-19 13:31:58', 35917, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Silang-18-35917', 3),
('2024-12-19 13:31:58', 35918, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Silang-18-35918', 3),
('2024-12-19 13:31:58', 35919, 'Sports Equipment', 'Volleyball Ball', 'Mikasa MVA 310', 2200, '2018-08-23', 'Roselyn A. Ymana', 'Silang-18-35919', 3),
('2024-12-19 13:31:58', 35920, 'Sports Equipment', 'Volleyball Net', 'Voleyball Net GTO', 850, '2018-08-23', 'Roselyn A. Ymana', 'Silang-18-35920', 4),
('2024-12-19 13:31:58', 35921, 'Sports Equipment', 'Volleyball Net', 'Voleyball Net GTO', 850, '2018-08-23', 'Roselyn A. Ymana', 'Silang-18-35921', 4),
('2024-12-19 13:49:32', 36071, 'Lab Equipment and Materials', 'Weighing Scale', 'Digital, 120kg.', 1100, '2018-09-13', 'Elena B. Telmo', 'Silang-18-36071', 15),
('2024-12-19 13:39:34', 41366, 'Furniture', 'Wall Clock', '', 88, '2017-12-06', 'Elena B. Telmo', 'Silang-17-41366', 9),
('2024-12-19 13:52:56', 41973, 'Lab Equipment and Materials', 'Weight Sets', 'Stainless steel hooked, 1000g', 1975, '2018-12-19', 'Joseph S. Callanta', 'Silang-18-41973', 16),
('2024-12-19 13:52:56', 41974, 'Lab Equipment and Materials', 'Weight Sets', 'Stainless steel hooked, 1000g', 1975, '2018-12-19', 'Joseph S. Callanta', 'Silang-18-41974', 16),
('2024-12-19 13:43:17', 55561, 'Miscellaneous', 'Washing Machine', 'With dryer, HD, 11kg. / Whirlpool WWT 110x', 13125, '2015-01-12', 'Beverly A. Malabag', 'Silang-24-55561', 11),
('2024-12-19 13:44:57', 55662, 'Cafeteria Supplies', 'Water and Coffee Boiler', 'Imarflex IWB-15008', 5499.75, '2019-07-22', 'Elena B. Telmo', 'Silang-19-55662', 12),
('2024-12-19 13:41:05', 58348, 'Miscellaneous', 'Washing Machine', 'fully auto LG, WF-S120V 12kg', 32750, '2017-05-12', 'Beverly A. Malabag', 'Silang-17-58348', 10),
('2024-12-19 13:36:00', 75399, 'Furniture', 'Wall Clock', '16\" / Stainless / Asahi', 1073.5, '2021-06-16', 'Jenny Beb F. Ebo', 'Silang-24-75399', 7),
('2024-12-19 13:36:00', 75400, 'Furniture', 'Wall Clock', '16\" / Stainless / Asahi', 1073.5, '2021-06-16', 'Jenny Beb F. Ebo', 'Silang-24-75400', 7),
('2024-12-19 13:36:00', 75401, 'Furniture', 'Wall Clock', '16\" / Stainless / Asahi', 1073.5, '2021-06-16', 'Jenny Beb F. Ebo', 'Silang-24-75401', 7),
('2024-12-19 13:36:00', 75402, 'Furniture', 'Wall Clock', '16\" / Stainless / Asahi', 1073.5, '2021-06-16', 'Jenny Beb F. Ebo', 'Silang-24-75402', 7),
('2024-12-19 13:47:12', 92149, 'Cafeteria Supplies', 'Water Dispenser', 'hot and cold, camel', 5700, '2021-09-07', 'Elena B. Telmo', 'Silang-21-92149', 13),
('2024-12-19 13:48:37', 98660, 'Cafeteria Supplies', 'Water Dispenser', 'table top/ Kyowa KQ1501 / hot / cold', 1958, '2022-01-01', 'Roselyn A. Ymana', 'Silang-22-98660', 14),
('2024-12-19 13:34:36', 104483, 'Electronics and IT Equipment', 'VOM (Volt Ohm Meter)', 'VOM Multitester Ingco', 500, '2022-10-26', 'Elena B. Telmo', 'Silang-22-104483', 6);

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
  ADD UNIQUE KEY `item_name` (`item_name`);

--
-- Indexes for table `tb_item_batch`
--
ALTER TABLE `tb_item_batch`
  ADD PRIMARY KEY (`batch_id`);

--
-- Indexes for table `tb_item_stock`
--
ALTER TABLE `tb_item_stock`
  ADD PRIMARY KEY (`stock_id`),
  ADD KEY `fk_batch` (`stock_batch`);

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
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `tb_catalog_item`
--
ALTER TABLE `tb_catalog_item`
  MODIFY `item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `tb_item_batch`
--
ALTER TABLE `tb_item_batch`
  MODIFY `batch_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `tb_item_stock`
--
ALTER TABLE `tb_item_stock`
  MODIFY `stock_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=104484;

--
-- AUTO_INCREMENT for table `tb_user`
--
ALTER TABLE `tb_user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tb_item_stock`
--
ALTER TABLE `tb_item_stock`
  ADD CONSTRAINT `fk_batch` FOREIGN KEY (`stock_batch`) REFERENCES `tb_item_batch` (`batch_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
