--使用的mysql数据库是5.7版本的，mysql5.5版本和之前版本是不支持表中出现多列timestamp类型

--创建数据库
CREATE DATABASE seckill;

--使用数据库
use seckill;

--设置默认的mysql引擎为InnoDB,mysql中只有这种引擎支持事务处理，同时设置主键id自增，且从1000开始
--key 用来创建索引，key是数据库模式中物理层面上的定义，它有两层含义，一是约束，二是索引。
--一般的索引都是为了优化查询速度，所以我们把最经常出现在查询条件或者排序条件中的数据列字段加上索引
CREATE TABLE seckill(
	`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '秒杀商品id',
	`name`  varchar(120) NOT NULL COMMENT '秒杀商品名称',
	`number` int NOT NULL COMMENT '秒杀商品库存数量',
	`start_time`  timestamp NOT NULL COMMENT '秒杀开始时间',
	`end_time`    timestamp NOT NULL COMMENT '秒杀结束时间',
	`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	PRIMARY KEY (seckill_id),
	key idx_start_time(start_time),
	key idx_end_time(end_time),
	key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='商品库存表';

--初始化数据
insert into 
	seckill(name,number,start_time,end_time) 
values
	('1000元秒杀iphone6',100,'2017-06-18 00:00:00','2017-06-19 00:00:00'),
	('500元秒杀ipad2',200,'2017-06-18 00:00:00','2017-11-11 00:00:00'),
	('300元秒杀小米4',300,'2017-06-18 00:00:00','2017-06-19 00:00:00'),
	('200元秒杀红米note',400,'2017-06-18 00:00:00','2017-06-19 00:00:00');
	
	
--创建秒杀成功明细表
CREATE TABLE success_killed(
	`seckill_id` bigint NOT NULL COMMENT '秒杀商品id',
	`user_phone` bigint NOT NULL COMMENT '用户手机号',
	`state` tinyint NOT NULL DEFAULT -1 COMMENT '状态标志，默认为-1   -1:没有秒杀成功     0:秒杀成功    1:已付款   2:已发货',
	`create_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	PRIMARY KEY(seckill_id,user_phone),/*联合主键 ，因为商品id并不能唯一标识一次秒杀成功记录，一件商品可以被多个用户秒杀成功，我们需要商品id和用户手机号联合主键来标识*/
	key idx_create_time(create_time)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';