SET FOREIGN_KEY_CHECKS=0;

drop table if exists messages;
create table messages (
  id             bigint unsigned not null,
  goal_id        bigint unsigned not null,
  text           varchar(1000) not null,
  type           varchar(100) NOT NULL DEFAULT 'OTHER',
  order_index    bigint unsigned,

  primary key   (id),
  constraint fk_goal_id foreign key (goal_id) references goals(id) on delete cascade on update cascade,
  index fk_goal_id (goal_id asc)
)
character set utf8
collate = utf8_general_ci
engine=innodb;

drop table if exists goals;
create table goals (
    id              bigint unsigned not null,
    name            varchar(100) not null,
    image_cover_id  bigint unsigned not null,
    primary key (id)
)
character set utf8
collate = utf8_general_ci
engine=innodb;

SET FOREIGN_KEY_CHECKS=1;