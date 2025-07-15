-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 15, 2025 at 03:41 AM
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
-- Database: `db_demo_paulash`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_catalog_item`
--

CREATE TABLE `tb_catalog_item` (
  `item_id` int(11) NOT NULL,
  `code` varchar(256) NOT NULL,
  `name` varchar(256) NOT NULL,
  `uom` varchar(32) NOT NULL,
  `price` float NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_catalog_item`
--

INSERT INTO `tb_catalog_item` (`item_id`, `code`, `name`, `uom`, `price`, `created_at`, `updated_at`) VALUES
(1, '2507118227072', 'Airlash', 'SET', 559, '2025-07-11 10:04:43', '2025-07-11 10:04:43'),
(2, '2507113168688', 'Applicator', 'PIECE', 259, '2025-07-11 10:04:55', '2025-07-11 10:04:55'),
(3, '2507115031207', 'Bond and Bind', 'PIECE', 649, '2025-07-11 10:05:08', '2025-07-11 10:05:08'),
(4, '2507119670547', 'Clear Lash Glue', 'PIECE', 584, '2025-07-11 10:05:42', '2025-07-11 10:05:42'),
(5, '2507118571917', 'Curl Supreme', 'PIECE', 909, '2025-07-11 10:06:17', '2025-07-11 10:06:17'),
(6, '2507110501899', 'Meltdown', 'PIECE', 399, '2025-07-11 10:06:40', '2025-07-11 10:06:40'),
(7, '2507118972013', 'Mini Lash Glue', 'PIECE', 199, '2025-07-11 10:07:22', '2025-07-11 10:07:22'),
(8, '2507118623746', 'Black Lash Glue', 'PIECE', 584, '2025-07-11 10:07:32', '2025-07-11 10:07:32'),
(9, '2507111800926', 'Beauty Blade', 'PIECE', 249, '2025-07-11 10:08:02', '2025-07-11 10:08:02'),
(10, '2507115204580', 'Blade Replacement', 'SET', 249, '2025-07-11 10:08:14', '2025-07-11 10:08:14'),
(11, '2507115287727', 'Lash Curler', 'PIECE', 399, '2025-07-11 10:08:35', '2025-07-11 10:08:35'),
(12, '2507114726296', 'Makeup Tweezers', 'PIECE', 149, '2025-07-11 10:09:19', '2025-07-11 10:09:19'),
(13, '2507119028599', 'Makeup Scissors', 'PIECE', 249, '2025-07-11 10:09:29', '2025-07-11 10:09:29'),
(14, '2507117120714', 'Pro', 'SET', 399, '2025-07-11 10:10:04', '2025-07-15 00:02:40'),
(15, '2507111224061', 'Pro Mini', 'SET', 279, '2025-07-11 10:10:13', '2025-07-11 10:10:13');

-- --------------------------------------------------------

--
-- Table structure for table `tb_inventory_balance`
--

CREATE TABLE `tb_inventory_balance` (
  `item_id` int(11) NOT NULL,
  `location` varchar(256) NOT NULL,
  `quantity` int(11) NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_inventory_balance`
--

INSERT INTO `tb_inventory_balance` (`item_id`, `location`, `quantity`, `updated_at`) VALUES
(1, 'MAIN SUPPLY ROOM', 99, '2025-07-15 01:33:58'),
(2, 'MAIN SUPPLY ROOM', 96, '2025-07-15 01:33:58'),
(3, 'MAIN SUPPLY ROOM', 97, '2025-07-15 00:10:15'),
(4, 'MAIN SUPPLY ROOM', 93, '2025-07-15 00:10:15'),
(5, 'MAIN SUPPLY ROOM', 116, '2025-07-15 00:10:15'),
(6, 'MAIN SUPPLY ROOM', 97, '2025-07-15 00:10:15'),
(7, 'MAIN SUPPLY ROOM', 98, '2025-07-15 00:10:15'),
(8, 'MAIN SUPPLY ROOM', 100, '2025-07-11 10:35:32'),
(9, 'MAIN SUPPLY ROOM', 100, '2025-07-11 10:35:30'),
(10, 'MAIN SUPPLY ROOM', 75, '2025-07-14 14:07:18'),
(11, 'MAIN SUPPLY ROOM', 84, '2025-07-15 00:10:15'),
(12, 'MAIN SUPPLY ROOM', 99, '2025-07-11 10:38:59'),
(13, 'MAIN SUPPLY ROOM', 97, '2025-07-15 00:10:15'),
(14, 'MAIN SUPPLY ROOM', 100, '2025-07-15 01:36:09'),
(15, 'MAIN SUPPLY ROOM', 98, '2025-07-15 00:10:15');

-- --------------------------------------------------------

--
-- Table structure for table `tb_inventory_transaction`
--

CREATE TABLE `tb_inventory_transaction` (
  `transaction_id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `item_id` int(11) NOT NULL,
  `location` varchar(64) NOT NULL,
  `type` varchar(32) NOT NULL,
  `quantity_change` int(11) NOT NULL,
  `created_by` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_inventory_transaction`
--

INSERT INTO `tb_inventory_transaction` (`transaction_id`, `timestamp`, `item_id`, `location`, `type`, `quantity_change`, `created_by`) VALUES
(4, '2025-07-11 10:35:22', 1, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 100, 1),
(5, '2025-07-11 10:35:26', 2, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 100, 1),
(6, '2025-07-11 10:35:30', 9, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 100, 1),
(7, '2025-07-11 10:35:32', 8, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 100, 1),
(8, '2025-07-11 10:35:36', 10, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 100, 1),
(9, '2025-07-11 10:35:40', 3, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 100, 1),
(10, '2025-07-11 10:35:44', 4, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 100, 1),
(11, '2025-07-11 10:35:48', 5, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 100, 1),
(12, '2025-07-11 10:35:51', 11, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 100, 1),
(13, '2025-07-11 10:35:54', 13, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 100, 1),
(14, '2025-07-11 10:35:59', 12, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 100, 1),
(15, '2025-07-11 10:36:02', 6, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 100, 1),
(16, '2025-07-11 10:36:05', 7, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 100, 1),
(17, '2025-07-11 10:36:21', 14, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 100, 1),
(18, '2025-07-11 10:36:25', 15, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 100, 1),
(19, '2025-07-11 10:38:59', 11, 'MAIN SUPPLY ROOM', 'SALE', -3, 1),
(20, '2025-07-11 10:38:59', 5, 'MAIN SUPPLY ROOM', 'SALE', -1, 1),
(21, '2025-07-11 10:38:59', 4, 'MAIN SUPPLY ROOM', 'SALE', -2, 1),
(22, '2025-07-11 10:38:59', 3, 'MAIN SUPPLY ROOM', 'SALE', -1, 1),
(23, '2025-07-11 10:38:59', 10, 'MAIN SUPPLY ROOM', 'SALE', -1, 1),
(24, '2025-07-11 10:38:59', 12, 'MAIN SUPPLY ROOM', 'SALE', -1, 1),
(25, '2025-07-11 10:38:59', 15, 'MAIN SUPPLY ROOM', 'SALE', -1, 1),
(26, '2025-07-11 10:38:59', 2, 'MAIN SUPPLY ROOM', 'SALE', -3, 1),
(27, '2025-07-11 10:44:55', 13, 'MAIN SUPPLY ROOM', 'SALE', -1, 1),
(28, '2025-07-11 10:44:55', 11, 'MAIN SUPPLY ROOM', 'SALE', -3, 1),
(29, '2025-07-11 10:44:55', 5, 'MAIN SUPPLY ROOM', 'SALE', -1, 1),
(30, '2025-07-11 10:44:55', 4, 'MAIN SUPPLY ROOM', 'SALE', -3, 1),
(31, '2025-07-11 11:37:16', 10, 'MAIN SUPPLY ROOM', 'SALE', -24, 1),
(32, '2025-07-14 14:05:12', 10, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 15, 1),
(33, '2025-07-14 14:05:32', 10, 'MAIN SUPPLY ROOM', 'LOST (OUT)', -5, 1),
(34, '2025-07-14 14:07:18', 10, 'MAIN SUPPLY ROOM', 'SALE', -10, 1),
(35, '2025-07-15 00:04:50', 5, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 20, 1),
(36, '2025-07-15 00:10:15', 3, 'MAIN SUPPLY ROOM', 'SALE', -2, 1),
(37, '2025-07-15 00:10:15', 4, 'MAIN SUPPLY ROOM', 'SALE', -2, 1),
(38, '2025-07-15 00:10:15', 5, 'MAIN SUPPLY ROOM', 'SALE', -2, 1),
(39, '2025-07-15 00:10:15', 11, 'MAIN SUPPLY ROOM', 'SALE', -10, 1),
(40, '2025-07-15 00:10:15', 13, 'MAIN SUPPLY ROOM', 'SALE', -2, 1),
(41, '2025-07-15 00:10:15', 6, 'MAIN SUPPLY ROOM', 'SALE', -3, 1),
(42, '2025-07-15 00:10:15', 7, 'MAIN SUPPLY ROOM', 'SALE', -2, 1),
(43, '2025-07-15 00:10:15', 15, 'MAIN SUPPLY ROOM', 'SALE', -1, 1),
(44, '2025-07-15 01:25:36', 14, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 60, 2),
(45, '2025-07-15 01:29:50', 14, 'MAIN SUPPLY ROOM', 'LOST (OUT)', -60, 2),
(46, '2025-07-15 01:33:58', 14, 'MAIN SUPPLY ROOM', 'SALE', -20, 2),
(47, '2025-07-15 01:33:58', 1, 'MAIN SUPPLY ROOM', 'SALE', -1, 2),
(48, '2025-07-15 01:33:58', 2, 'MAIN SUPPLY ROOM', 'SALE', -1, 2),
(49, '2025-07-15 01:36:09', 14, 'MAIN SUPPLY ROOM', 'RECEIPT (IN)', 20, 2);

-- --------------------------------------------------------

--
-- Table structure for table `tb_sales`
--

CREATE TABLE `tb_sales` (
  `sale_id` int(11) NOT NULL,
  `customer_name` varchar(100) DEFAULT NULL,
  `channel` varchar(64) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `total_amount` decimal(10,2) DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_sales`
--

INSERT INTO `tb_sales` (`sale_id`, `customer_name`, `channel`, `created_at`, `total_amount`, `created_by`) VALUES
(1, 'John Patrick Skidmore', 'Lazada', '2025-07-11 10:38:59', 5377.00, 1),
(2, 'Danel Dave Barbuco', 'Lazada', '2025-07-11 10:44:55', 4107.00, 1),
(4, 'Danel Dave Barbuco', 'Lazada', '2025-07-11 11:37:16', 5976.00, 1),
(5, 'Kenny Guilaran', 'Shopee', '2025-07-14 14:07:18', 2490.00, 1),
(6, 'Danel Dave Barbuco', 'Lazada', '2025-07-15 00:10:15', 10646.00, 1),
(7, 'Tyrone', 'Lazada', '2025-07-15 01:33:58', 8798.00, 2);

-- --------------------------------------------------------

--
-- Table structure for table `tb_sales_items`
--

CREATE TABLE `tb_sales_items` (
  `sale_item_id` int(11) NOT NULL,
  `sale_id` int(11) DEFAULT NULL,
  `item_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `unit_price` decimal(10,2) DEFAULT NULL,
  `total_price` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_sales_items`
--

INSERT INTO `tb_sales_items` (`sale_item_id`, `sale_id`, `item_id`, `quantity`, `unit_price`, `total_price`) VALUES
(1, 1, 11, 3, 399.00, 1197.00),
(2, 1, 5, 1, 909.00, 909.00),
(3, 1, 4, 2, 584.00, 1168.00),
(4, 1, 3, 1, 649.00, 649.00),
(5, 1, 10, 1, 249.00, 249.00),
(6, 1, 12, 1, 149.00, 149.00),
(7, 1, 15, 1, 279.00, 279.00),
(8, 1, 2, 3, 259.00, 777.00),
(9, 2, 13, 1, 249.00, 249.00),
(10, 2, 11, 3, 399.00, 1197.00),
(11, 2, 5, 1, 909.00, 909.00),
(12, 2, 4, 3, 584.00, 1752.00),
(13, 4, 10, 24, 249.00, 5976.00),
(14, 5, 10, 10, 249.00, 2490.00),
(15, 6, 3, 2, 649.00, 1298.00),
(16, 6, 4, 2, 584.00, 1168.00),
(17, 6, 5, 2, 909.00, 1818.00),
(18, 6, 11, 10, 399.00, 3990.00),
(19, 6, 13, 2, 249.00, 498.00),
(20, 6, 6, 3, 399.00, 1197.00),
(21, 6, 7, 2, 199.00, 398.00),
(22, 6, 15, 1, 279.00, 279.00),
(23, 7, 14, 20, 399.00, 7980.00),
(24, 7, 1, 1, 559.00, 559.00),
(25, 7, 2, 1, 259.00, 259.00);

--
-- Triggers `tb_sales_items`
--
DELIMITER $$
CREATE TRIGGER `trg_after_delete_sales_item` AFTER DELETE ON `tb_sales_items` FOR EACH ROW BEGIN
    UPDATE tb_sales
    SET total_amount = (
        SELECT IFNULL(SUM(total_price), 0)
        FROM tb_sales_items
        WHERE sale_id = OLD.sale_id
    )
    WHERE sale_id = OLD.sale_id;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_after_insert_sales_item` AFTER INSERT ON `tb_sales_items` FOR EACH ROW BEGIN
    UPDATE tb_sales
    SET total_amount = (
        SELECT IFNULL(SUM(total_price), 0)
        FROM tb_sales_items
        WHERE sale_id = NEW.sale_id
    )
    WHERE sale_id = NEW.sale_id;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_after_update_sales_item` AFTER UPDATE ON `tb_sales_items` FOR EACH ROW BEGIN
    UPDATE tb_sales
    SET total_amount = (
        SELECT IFNULL(SUM(total_price), 0)
        FROM tb_sales_items
        WHERE sale_id = NEW.sale_id
    )
    WHERE sale_id = NEW.sale_id;
END
$$
DELIMITER ;

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
(1, 'johnpatrick.skidmore@cvsu.edu.ph', 'BYjzi3wsD+ut2HBZawXVXg==', 'e4eba840eddfe58491f744d223fc289fcd4b416c72d4f375cd81cc889dcec53d', 'John Patrick', 'Skidmore'),
(2, 'admin@gmail.com', 'JBbFC6bPltKPm22572udrg==', 'fff47ef7cb1226df67a4a36e67486792cb62ce9a2baafe7fc6ca526a2a1a61b3', 'John Patrick', 'Skidmore');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_catalog_item`
--
ALTER TABLE `tb_catalog_item`
  ADD PRIMARY KEY (`item_id`),
  ADD UNIQUE KEY `catalog_code` (`code`);

--
-- Indexes for table `tb_inventory_balance`
--
ALTER TABLE `tb_inventory_balance`
  ADD PRIMARY KEY (`item_id`,`location`);

--
-- Indexes for table `tb_inventory_transaction`
--
ALTER TABLE `tb_inventory_transaction`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `fk_item_id_to_transaction` (`item_id`),
  ADD KEY `fk_created_by_to_user` (`created_by`);

--
-- Indexes for table `tb_sales`
--
ALTER TABLE `tb_sales`
  ADD PRIMARY KEY (`sale_id`),
  ADD KEY `fk_sales_to_user` (`created_by`);

--
-- Indexes for table `tb_sales_items`
--
ALTER TABLE `tb_sales_items`
  ADD PRIMARY KEY (`sale_item_id`),
  ADD KEY `tb_sales_items_ibfk_1` (`sale_id`),
  ADD KEY `tb_sales_items_ibfk_2` (`item_id`);

--
-- Indexes for table `tb_user`
--
ALTER TABLE `tb_user`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tb_catalog_item`
--
ALTER TABLE `tb_catalog_item`
  MODIFY `item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `tb_inventory_transaction`
--
ALTER TABLE `tb_inventory_transaction`
  MODIFY `transaction_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;

--
-- AUTO_INCREMENT for table `tb_sales`
--
ALTER TABLE `tb_sales`
  MODIFY `sale_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `tb_sales_items`
--
ALTER TABLE `tb_sales_items`
  MODIFY `sale_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `tb_user`
--
ALTER TABLE `tb_user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tb_inventory_balance`
--
ALTER TABLE `tb_inventory_balance`
  ADD CONSTRAINT `fk_item_id_to_inventory` FOREIGN KEY (`item_id`) REFERENCES `tb_catalog_item` (`item_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tb_inventory_transaction`
--
ALTER TABLE `tb_inventory_transaction`
  ADD CONSTRAINT `fk_created_by_to_user` FOREIGN KEY (`created_by`) REFERENCES `tb_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_item_id_to_transaction` FOREIGN KEY (`item_id`) REFERENCES `tb_catalog_item` (`item_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tb_sales`
--
ALTER TABLE `tb_sales`
  ADD CONSTRAINT `fk_sales_to_user` FOREIGN KEY (`created_by`) REFERENCES `tb_user` (`user_id`);

--
-- Constraints for table `tb_sales_items`
--
ALTER TABLE `tb_sales_items`
  ADD CONSTRAINT `tb_sales_items_ibfk_1` FOREIGN KEY (`sale_id`) REFERENCES `tb_sales` (`sale_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tb_sales_items_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `tb_catalog_item` (`item_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
