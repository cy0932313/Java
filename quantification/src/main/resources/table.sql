CREATE TABLE `HoldSymbol` (
  `symbolCode` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `symbolName` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `buyPrice` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `buyTime` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `isETF` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



CREATE TABLE `MonitorSymbol` (
  `symbolCode` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `symbolName` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE `HoldRecord` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `symbolCode` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `symbolName` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `open` float DEFAULT NULL,
  `close` float DEFAULT NULL,
  `high` float DEFAULT NULL,
  `low` float DEFAULT NULL,
  `previous_cci` float DEFAULT NULL,
  `cci` float DEFAULT NULL,
  `time` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `groupID` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



CREATE TABLE `MonitorRecord` (
  `symbolCode` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `symbolName` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cci` float DEFAULT NULL,
  `time` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
