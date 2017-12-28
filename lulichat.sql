/*
SQLyog Ultimate v12.4.3 (64 bit)
MySQL - 5.5.58-0ubuntu0.14.04.1 : Database - mychat
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mychat` /*!40100 DEFAULT CHARACTER SET latin1 */;

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
) ENGINE=InnoDB AUTO_INCREMENT=2642 DEFAULT CHARSET=utf8 COMMENT='聊天记录表';

/*Data for the table `chatmessage` */

insert  into `chatmessage`(`id`,`username`,`avatar`,`timestamp`,`content`,`unreadpoint`,`type`,`toid`,`from`,`unreadnumbers`) values 
(526,'wendal','imgs/5.jpg','1513148180462','1',0,1,1,29,''),
(527,'wendal','imgs/5.jpg','1513148758016','111',0,1,1,29,''),
(528,'wendal','imgs/5.jpg','1513149633046','ww',0,1,31,29,''),
(529,'蛋蛋的忧伤','imgs/8.jpg','1513153689994','face[抱抱] ',0,1,29,1,NULL),
(530,'wendal','imgs/5.jpg','1513153695406','哦哦哦',0,1,1,29,NULL),
(531,'蛋蛋的忧伤','imgs/8.jpg','1513153699946','face[亲亲] ',0,1,29,1,NULL),
(2641,'chaozi','imgs/2.jpg','1514427829515','dadwdqw',0,1,1,417,NULL);

/*Table structure for table `flock` */

DROP TABLE IF EXISTS `flock`;

CREATE TABLE `flock` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` int(11) DEFAULT NULL COMMENT '创建人',
  `groupname` varchar(30) DEFAULT NULL COMMENT '群名称',
  `avatar` varchar(100) DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='群组';

/*Data for the table `flock` */

insert  into `flock`(`id`,`userid`,`groupname`,`avatar`) values 
(2,1,'老铁一家人','imgs/laotie.jpg'),
(3,1,'实现你的梦想','imgs/8.jpg'),
(4,1,'Nutz社区群','imgs/10.jpg'),
(5,1,'LayUI/LayIm社区群','imgs/layuilogo.png'),
(6,1,'T-io社区群','imgs/tiologo.png');

/*Table structure for table `flockrefuser` */

DROP TABLE IF EXISTS `flockrefuser`;

CREATE TABLE `flockrefuser` (
  `uid` int(11) DEFAULT NULL COMMENT '用户id',
  `fid` int(11) DEFAULT NULL COMMENT '群组id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户群组多对多';

/*Data for the table `flockrefuser` */

insert  into `flockrefuser`(`uid`,`fid`) values 
(2,2),
(1,2),
(1,3),
(1,4),
(1,5),
(1,6);

/*Table structure for table `friends` */

DROP TABLE IF EXISTS `friends`;

CREATE TABLE `friends` (
  `me` int(11) DEFAULT NULL COMMENT '我的id',
  `friend` int(11) DEFAULT NULL COMMENT '对方id',
  `groupid` int(11) DEFAULT NULL COMMENT '所在分组'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='好友关系表';

/*Data for the table `friends` */

insert  into `friends`(`me`,`friend`,`groupid`) values 
(2,1,813),
(1,2,1);

/*Table structure for table `group` */

DROP TABLE IF EXISTS `group`;

CREATE TABLE `group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) DEFAULT NULL COMMENT '用户编号',
  `groupname` varchar(30) DEFAULT NULL COMMENT '分组名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=814 DEFAULT CHARSET=utf8 COMMENT='分组';

/*Data for the table `group` */

insert  into `group`(`id`,`user_id`,`groupname`) values 
(1,1,'亲人们'),
(2,1,'Ji友们'),
(3,2,'亲人们'),
(813,2,'Ji友们');

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
) ENGINE=InnoDB AUTO_INCREMENT=163 DEFAULT CHARSET=utf8 COMMENT='加群消息';

/*Data for the table `message` */

insert  into `message`(`id`,`content`,`uid`,`from`,`from_group`,`type`,`remark`,`href`,`read`,`time`) values 
(161,'申请添加你为好友',2,1,1,3,'1',NULL,0,'2017-12-28 14:03:00'),
(162,'成功添加对方为好友!',1,2,1,3,'',NULL,1,'2017-12-28 14:04:06');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(20) NOT NULL COMMENT '用户名,可中文',
  `pwd` varchar(20) DEFAULT NULL COMMENT '密码',
  `sign` varchar(200) DEFAULT NULL COMMENT '签名',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `status` varchar(20) DEFAULT NULL COMMENT '在线状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=420 DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Data for the table `user` */

insert  into `user`(`id`,`username`,`pwd`,`sign`,`avatar`,`status`) values 
(1,'蛋蛋的忧伤','123456','nutz是世界上最好的java编程框架','imgs/8.jpg','online'),
(2,'wendal','123456','我是nutz的作者','imgs/5.jpg','online');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
