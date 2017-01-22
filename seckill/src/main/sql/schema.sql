CREATE DATABASE seckill;
use seckill;
create table seckill(
    seckill_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '当前库存id',
    name varchar(120) NOT NULL ,
  number int NOT NULL ,
  start_time TIMESTAMP NOT NULL ,
  end_time TIMESTAMP NOT NULL ,
  create_time TIMESTAMP NOT NULL DEFAULT current_timestamp,
  PRIMARY KEY (seckill_id),
  key idx_start_time(start_time),
  key idx_end_time(end_time),
  key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET =utf8 COMMENT ='秒杀库存表';

INSERT into
  seckill(name,number,start_time,end_time)
values('700元秒杀电视',100,'2016-12-17 18:00:00','2016-12-18 18:00:00'),
('2000元秒杀iphone',100,'2016-12-17 18:00:00','2016-12-18 18:00:00'),
('500元秒杀小米',100,'2016-12-17 18:00:00','2016-12-18 18:00:00'),
('800元秒杀自行车',100,'2016-12-17 18:00:00','2016-12-18 18:00:00');

CREATE table success_killed(
  seckill_id BIGINT NOT NULL ,
  user_phone BIGINT NOT NULL ,
  state TINYINT NOT NULL DEFAULT -1,
  create_time TIMESTAMP NOT NULL ,
  PRIMARY KEY (seckill_id,user_phone),
  key idx_create_time(create_time),
  FOREIGN KEY (seckill_id) REFERENCES seckill(seckill_id)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET =utf8;

