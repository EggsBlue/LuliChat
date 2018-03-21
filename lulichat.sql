/*
SQLyog Ultimate v12.4.3 (64 bit)
MySQL - 5.7.18-log : Database - mychat
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mychat` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `mychat`;

/*Table structure for table `chatmessage` */

DROP TABLE IF EXISTS `chatmessage`;

CREATE TABLE `chatmessage` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `avatar` varchar(300) DEFAULT NULL COMMENT '头像',
  `timestamp` varchar(50) DEFAULT NULL COMMENT '时间戳,用此时间获取标准时间',
  `content` text COMMENT '消息内容',
  `unreadpoint` int(11) DEFAULT NULL COMMENT '是否未读,用于离线消息,0已读,1未读',
  `type` int(11) DEFAULT NULL COMMENT '消息类型,1.用户消息  2.群组消息',
  `toid` int(255) DEFAULT NULL COMMENT '对方/接受消息的id',
  `from` int(255) DEFAULT NULL COMMENT '我的id',
  `unreadnumbers` text COMMENT '针对群组,此消息的未读人员',
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1142 DEFAULT CHARSET=utf8 COMMENT='聊天记录表';

/*Table structure for table `flock` */

DROP TABLE IF EXISTS `flock`;

CREATE TABLE `flock` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` int(11) DEFAULT NULL COMMENT '创建人',
  `groupname` varchar(30) DEFAULT NULL COMMENT '群名称',
  `avatar` varchar(100) DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='群组';

/*Table structure for table `flockrefuser` */

DROP TABLE IF EXISTS `flockrefuser`;

CREATE TABLE `flockrefuser` (
  `uid` int(11) DEFAULT NULL COMMENT '用户id',
  `fid` int(11) DEFAULT NULL COMMENT '群组id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户群组多对多';

/*Table structure for table `friends` */

DROP TABLE IF EXISTS `friends`;

CREATE TABLE `friends` (
  `me` int(11) DEFAULT NULL COMMENT '我的id',
  `friend` int(11) DEFAULT NULL COMMENT '对方id',
  `groupid` int(11) DEFAULT NULL COMMENT '所在分组'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='好友关系表';

/*Table structure for table `group` */

DROP TABLE IF EXISTS `group`;

CREATE TABLE `group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) DEFAULT NULL COMMENT '用户编号',
  `groupname` varchar(30) DEFAULT NULL COMMENT '分组名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=181 DEFAULT CHARSET=utf8 COMMENT='分组';

/*Table structure for table `message` */

DROP TABLE IF EXISTS `message`;

CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `uid` int(11) DEFAULT NULL COMMENT '发送给谁啊',
  `from` int(11) DEFAULT NULL COMMENT '谁发的',
  `from_group` int(11) DEFAULT NULL COMMENT '分组ID',
  `type` int(11) DEFAULT NULL COMMENT '1.请求加好友.  2.已拒绝  3.已同意 ,4.请求加群',
  `remark` varchar(200) DEFAULT NULL COMMENT '附带留言',
  `href` varchar(100) DEFAULT NULL COMMENT '未知意义',
  `read` int(11) DEFAULT NULL COMMENT '是否已读 1.已读. 0.未读',
  `time` datetime DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8 COMMENT='加群消息';

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(20) NOT NULL COMMENT '用户名,可中文',
  `pwd` varchar(20) DEFAULT NULL COMMENT '密码',
  `sign` varchar(200) DEFAULT NULL COMMENT '签名',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `status` varchar(20) DEFAULT NULL COMMENT '在线状态',
  `create_time datatime DEFAULT NULL COMMENT '创建时间'
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8 COMMENT='用户表';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
