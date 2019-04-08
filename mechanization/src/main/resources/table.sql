CREATE TABLE `transaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `coinName` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `price` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `transactionType` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `transactionTime` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;