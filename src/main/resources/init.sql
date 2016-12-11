CREATE DATABASE `test` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE TABLE `zhihu_user` (
`id`  int(4) NOT NULL AUTO_INCREMENT ,
`url`  varchar(255) NOT NULL ,
`hash`  varchar(255) NOT NULL,
`followee`  int NOT NULL DEFAULT 0,
`last_check`  timestamp NOT NULL,
PRIMARY KEY (`id`),
INDEX `user_check` (`last_check`) USING BTREE,
UNIQUE INDEX `user_url` (`url`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8
;

CREATE TABLE `zhihu_answer` (
  `id`  int NOT NULL AUTO_INCREMENT ,
  `url`  varchar(255) NOT NULL ,
  `user_url`  varchar(255) NOT NULL ,
  `question_url`  varchar(255) NOT NULL ,
  `token`  varchar(255) NOT NULL ,
  `create_time`  timestamp NOT NULL ,
  `need_check`  int NOT NULL ,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `answer_url` (`url`) USING BTREE ,
  INDEX `answer_user_url` (`user_url`) USING BTREE ,
  INDEX `answer_question_url` (`question_url`) USING BTREE ,
  INDEX `answer_create_time` (`create_time`) USING BTREE ,
  INDEX `answer_need_check` (`need_check`) USING BTREE
)
;

CREATE TABLE `zhihu_meta` (
  `id`  int NOT NULL AUTO_INCREMENT ,
  `time`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP ,
  `key`  varchar(255) NOT NULL ,
  `data`  text NULL ,
  PRIMARY KEY (`id`),
  INDEX `meta_time` (`time`) USING BTREE ,
  INDEX `meta_key` (`key`) USING BTREE
)
;

