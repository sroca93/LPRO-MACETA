-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	5.6.23-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `following`
--

LOCK TABLES `following` WRITE;
/*!40000 ALTER TABLE `following` DISABLE KEYS */;
INSERT INTO `following` VALUES (2,1),(3,2),(1,3),(1,4);
/*!40000 ALTER TABLE `following` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fotos`
--

LOCK TABLES `fotos` WRITE;
/*!40000 ALTER TABLE `fotos` DISABLE KEYS */;
/*!40000 ALTER TABLE `fotos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `mediciones`
--

LOCK TABLES `mediciones` WRITE;
/*!40000 ALTER TABLE `mediciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `mediciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `plantas`
--

LOCK TABLES `plantas` WRITE;
/*!40000 ALTER TABLE `plantas` DISABLE KEYS */;
INSERT INTO `plantas` VALUES (1,1,'GuillePlanta1','Rosa',4,2,NULL),(2,1,'GuillePlanta2','Petunia',3,5,NULL),(3,2,'SimonPlanta1','Baobab',0,17,NULL),(4,3,'LuisPlanta1','Nabo',5,69,NULL),(5,4,'AlvaroPlanta1','Petunia',5,8,NULL);
/*!40000 ALTER TABLE `plantas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Guille','never',NULL),(2,'Simon','gonna',NULL),(3,'Luis','give',NULL),(4,'Alvaro','you',NULL),(5,'Sara','up',NULL),(6,'Andres','!',NULL);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `usuariosplantas`
--

LOCK TABLES `usuariosplantas` WRITE;
/*!40000 ALTER TABLE `usuariosplantas` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuariosplantas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `valoraciones`
--

LOCK TABLES `valoraciones` WRITE;
/*!40000 ALTER TABLE `valoraciones` DISABLE KEYS */;
INSERT INTO `valoraciones` VALUES (1,3,4),(2,1,3);
/*!40000 ALTER TABLE `valoraciones` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-02-26 19:52:48
