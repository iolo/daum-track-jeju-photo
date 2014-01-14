create database geophoto;

USE geophoto;

CREATE TABLE `sample` (
  `articleId` int(11) NOT NULL,  
  `title` varchar(255) COLLATE utf8_bin DEFAULT NULL,  
  PRIMARY KEY (`articleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;