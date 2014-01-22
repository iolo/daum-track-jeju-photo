create database geophoto;

USE geophoto;

CREATE TABLE `article` (
  `no` int(10) auto_increment,
  `id` varchar(255) NOT NULL,
  `fbid` varchar(255),
  `lat` int(10) NOT NULL,
  `lng` int(10) NOT NULL,
  `likecnt` int(10) default 0,
  `regdttm` varchar(14) NOT NULL,
  `avgcolor` int(10) default 16581375,
  PRIMARY KEY (`no`, `id`),
  KEY (`lat`,`lng`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;






