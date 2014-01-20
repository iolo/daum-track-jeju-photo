create database geophoto;

USE geophoto;

CREATE TABLE `article` (
  `id` varchar(255) NOT NULL,
  `fbid` varchar(255),
  `lat` int(10) NOT NULL,
  `lng` int(10) NOT NULL,
  `likecnt` int(10) default 0,
  `regdttm` varchar(14) NOT NULL,
  PRIMARY KEY (`id`),
  KEY (`lat`,`lng`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;






