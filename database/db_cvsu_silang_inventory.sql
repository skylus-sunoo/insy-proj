-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 15, 2025 at 02:29 PM
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
(13, 'Catalog-C-13', 'Sports Equipment');

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

-- --------------------------------------------------------

--
-- Table structure for table `tb_item_batch`
--

CREATE TABLE `tb_item_batch` (
  `batch_id` int(11) NOT NULL,
  `batch_timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
('2025-01-15 13:24:26', 'CATALOG', 'ADD', 'Catalog-C-1', 'Catalog-C-1', '; Category Name: N/A', ''),
('2025-01-15 13:25:38', 'CATALOG', 'ADD', 'Catalog-C-2', 'Catalog-C-2', '; Category Name: Books and Educational Resources', ''),
('2025-01-15 13:26:07', 'CATALOG', 'ADD', 'Catalog-C-3', 'Catalog-C-3', '; Category Name: Cafeteria Supplies', ''),
('2025-01-15 13:26:19', 'CATALOG', 'ADD', 'Catalog-C-4', 'Catalog-C-4', '; Category Name: Classroom Supplies', ''),
('2025-01-15 13:26:30', 'CATALOG', 'ADD', 'Catalog-C-5', 'Catalog-C-5', '; Category Name: Electronics and IT Equipment', ''),
('2025-01-15 13:26:38', 'CATALOG', 'ADD', 'Catalog-C-6', 'Catalog-C-6', '; Category Name: Furniture', ''),
('2025-01-15 13:26:49', 'CATALOG', 'ADD', 'Catalog-C-7', 'Catalog-C-7', '; Category Name: Lab Equipment and Materials', ''),
('2025-01-15 13:27:00', 'CATALOG', 'ADD', 'Catalog-C-8', 'Catalog-C-8', '; Category Name: Maintenance / Facilities', ''),
('2025-01-15 13:27:09', 'CATALOG', 'ADD', 'Catalog-C-9', 'Catalog-C-9', '; Category Name: Medical Equipment', ''),
('2025-01-15 13:27:24', 'CATALOG', 'ADD', 'Catalog-C-10', 'Catalog-C-10', '; Category Name: Miscellaneous', ''),
('2025-01-15 13:27:35', 'CATALOG', 'ADD', 'Catalog-C-11', 'Catalog-C-11', '; Category Name: Office Supplies', ''),
('2025-01-15 13:27:45', 'CATALOG', 'ADD', 'Catalog-C-12', 'Catalog-C-12', '; Category Name: Security and Safety Equipment', ''),
('2025-01-15 13:27:51', 'CATALOG', 'ADD', 'Catalog-C-13', 'Catalog-C-13', '; Category Name: Sports Equipment', '');

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
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `tb_catalog_item`
--
ALTER TABLE `tb_catalog_item`
  MODIFY `item_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tb_item_batch`
--
ALTER TABLE `tb_item_batch`
  MODIFY `batch_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `tb_item_request`
--
ALTER TABLE `tb_item_request`
  MODIFY `request_id` int(11) NOT NULL AUTO_INCREMENT;

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
