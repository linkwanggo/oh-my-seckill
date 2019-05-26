-- 创建数据库
CREATE DATABASE seckill;

-- 使用数据库
use seckill;

-- 创建库存秒杀表
CREATE TABLE `seckill`(
`seckill_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` VARCHAR(120) NOT NULL COMMENT '商品名称',
`number` BIGINT NOT NULL COMMENT '库存数量',
`start_time` TIMESTAMP NOT NULL COMMENT '开始时间',
`end_time` TIMESTAMP NOT NULL COMMENT '结束时间',
`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (seckill_id),
KEY idx_start_time(start_time),
KEY idx_end_time(end_time),
KEY idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT '库存秒杀表';

-- 初始化数据
INSERT INTO `seckill`(name, number, start_time, end_time)
VALUES
  ('1000元秒杀iphone6', 100, '2019-5-21 12:00:00', '2019-5-21 13:00:00'),
  ('300元秒杀小米4', 200, '2019-5-21 12:00:00', '2019-5-21 13:00:00'),
  ('200元秒杀红米note', 300, '2019-5-21 12:00:00', '2019-5-21 13:00:00'),
  ('500元秒杀华为nova', 400, '2019-5-21 12:00:00', '2019-5-21 13:00:00');

-- 创建秒杀明细表
CREATE TABLE `success_killed` (
`seckill_id` BIGINT NOT NULL COMMENT '商品id',
`user_phone` INT(11) NOT NULL COMMENT '用户手机号',
`state` TINYINT NOT NULL DEFAULT -1 COMMENT '秒杀状态 默认-1:无效状态 0:成功 1:失败',
`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY(seckill_id, user_phone), /*联合主键*/
key idx_create_time(create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';
