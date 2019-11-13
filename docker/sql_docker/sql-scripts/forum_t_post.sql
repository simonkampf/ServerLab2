-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: forum
-- ------------------------------------------------------
-- Server version	8.0.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_post`
--

DROP TABLE IF EXISTS `t_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_post` (
  `idt_post` int(11) NOT NULL AUTO_INCREMENT,
  `t_message` varchar(200) NOT NULL,
  `t_time` datetime NOT NULL,
  `t_senderid` int(11) NOT NULL,
  `t_receiverid` int(11) NOT NULL,
  `t_private` tinyint(4) NOT NULL,
  PRIMARY KEY (`idt_post`),
  UNIQUE KEY `idt_post_UNIQUE` (`idt_post`),
  KEY `idt_user_idx1` (`t_senderid`,`t_receiverid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_post`
--

LOCK TABLES `t_post` WRITE;
/*!40000 ALTER TABLE `t_post` DISABLE KEYS */;
INSERT INTO `t_post` VALUES (1,'Meddelande 1','2019-11-01 09:06:15',6,7,0),(2,'Meddelande 2','2019-11-01 09:06:34',7,6,0),(3,'Meddelande 2','2019-11-01 09:06:41',6,6,0),(4,'Meddelande 2','2019-11-01 09:06:51',5,8,0),(5,'Meddelande 2','2019-11-01 09:07:05',5,6,0),(6,'hejsan','2019-11-06 09:15:37',12,7,0),(7,'hejhej privat','2019-11-06 09:51:26',6,7,1),(8,'här kommer ett publikt meddelande','2019-11-06 11:34:33',7,6,0),(9,'här kommer ett privat meddelande','2019-11-06 11:34:53',7,6,1),(10,'hejsan detta är ett meddelande från post','2019-11-07 13:53:11',6,7,1);
/*!40000 ALTER TABLE `t_post` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-11-13  8:36:56
