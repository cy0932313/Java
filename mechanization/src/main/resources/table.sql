CREATE TABLE `transaction` (
  `reason` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `price` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `transactionType` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `transactionTime` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`reason`,`transactionTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;