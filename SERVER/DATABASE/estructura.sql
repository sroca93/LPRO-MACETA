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
-- Table structure for table `following`
--

DROP TABLE IF EXISTS `following`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `following` (
  `Follower` int(11) NOT NULL,
  `Followed` int(11) NOT NULL,
  PRIMARY KEY (`Follower`,`Followed`),
  KEY `fk_Plantas_has_Plantas_Plantas2_idx` (`Followed`),
  KEY `fk_Following_Usuarios1_idx` (`Follower`),
  CONSTRAINT `fk_Following_Usuarios1` FOREIGN KEY (`Follower`) REFERENCES `usuarios` (`idUsuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Plantas_has_Plantas_Plantas2` FOREIGN KEY (`Followed`) REFERENCES `plantas` (`idPlanta`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fotos`
--

DROP TABLE IF EXISTS `fotos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fotos` (
  `idPlanta` int(11) NOT NULL,
  `Timestamp` datetime NOT NULL,
  `Foto` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idPlanta`,`Timestamp`),
  KEY `fk_Fotos_Plantas1_idx` (`idPlanta`),
  CONSTRAINT `fk_Fotos_Plantas1` FOREIGN KEY (`idPlanta`) REFERENCES `plantas` (`idPlanta`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mediciones`
--

DROP TABLE IF EXISTS `mediciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mediciones` (
  `Plantas_idPlanta` int(11) NOT NULL,
  `Timestamp` datetime NOT NULL,
  `Temperatura` decimal(5,0) DEFAULT NULL,
  `Luminosidad` decimal(5,0) DEFAULT NULL,
  `Humedad` decimal(5,0) DEFAULT NULL,
  PRIMARY KEY (`Plantas_idPlanta`,`Timestamp`),
  KEY `fk_Mediciones_Plantas1_idx` (`Plantas_idPlanta`),
  CONSTRAINT `fk_Mediciones_Plantas1` FOREIGN KEY (`Plantas_idPlanta`) REFERENCES `plantas` (`idPlanta`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `plantas`
--

DROP TABLE IF EXISTS `plantas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `plantas` (
  `idPlanta` int(11) NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) NOT NULL,
  `Nombre` varchar(45) DEFAULT NULL,
  `Tipo` varchar(45) DEFAULT NULL,
  `ValoracionMedia` decimal(5,0) DEFAULT NULL,
  `NValoraciones` int(11) DEFAULT NULL,
  `Thumbnail` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idPlanta`),
  KEY `fk_Plantas_Usuarios1_idx` (`idUsuario`),
  CONSTRAINT `fk_Plantas_Usuarios1` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios` (`idUsuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuarios` (
  `idUsuario` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  `Password` varchar(45) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuariosplantas`
--

DROP TABLE IF EXISTS `usuariosplantas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuariosplantas` (
  `idUsuario` int(11) NOT NULL,
  `idPlanta` int(11) NOT NULL,
  PRIMARY KEY (`idUsuario`,`idPlanta`),
  KEY `fk_table1_Plantas_idx` (`idPlanta`),
  KEY `fk_UsuariosPlantas_Usuarios1_idx` (`idUsuario`),
  CONSTRAINT `fk_UsuariosPlantas_Usuarios1` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios` (`idUsuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_table1_Plantas` FOREIGN KEY (`idPlanta`) REFERENCES `plantas` (`idPlanta`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `valoraciones`
--

DROP TABLE IF EXISTS `valoraciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `valoraciones` (
  `idUsuario` int(11) NOT NULL,
  `idPlanta` int(11) NOT NULL,
  `Valoracion` int(11) DEFAULT NULL,
  PRIMARY KEY (`idUsuario`,`idPlanta`),
  KEY `fk_Valoraciones_Plantas1_idx` (`idPlanta`),
  CONSTRAINT `fk_Valoraciones_Plantas1` FOREIGN KEY (`idPlanta`) REFERENCES `plantas` (`idPlanta`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Valoraciones_Usuarios1` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios` (`idUsuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-02 12:33:24
