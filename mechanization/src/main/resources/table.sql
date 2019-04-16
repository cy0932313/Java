CREATE TABLE `back_test_result` (
  `symbolCode` varchar(32) COLLATE utf8mb4_general_ci NOT NULL,
  `symbolName` varchar(32) COLLATE utf8mb4_general_ci NOT NULL,
  `plan` varchar(32) COLLATE utf8mb4_general_ci NOT NULL,
  `countProfit` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `transactionNum` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `holdDay` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `md` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `lossNum` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `winProfit` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `t` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `immobility` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`symbolCode`,`symbolName`,`plan`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE `transaction` (
  `symbolCode` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `symbolName` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `buyTime` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `buyPrice` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `buyNextPrice` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `buyReason` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sellTime` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sellPrice` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sellReason` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `profit` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;