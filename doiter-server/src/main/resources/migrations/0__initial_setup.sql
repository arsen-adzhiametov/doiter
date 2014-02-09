create table goals (
  id              bigint unsigned not null,
  name            varchar(100) not null,
  primary key (id)
);

create table messages (
  id             bigint unsigned not null,
  goal_id        bigint unsigned not null,
  text           varchar(1000) not null,
  type           varchar(100) NOT NULL DEFAULT 'OTHER',
  order_index    bigint unsigned,

  primary key   (id),
  foreign key (goal_id) references goals(id) on delete cascade on update cascade
);

create index goal_id on goals(id);

insert into goals (id, name) values
    (0, 'Меньше сидеть в интернете'),
    (1, 'Убраться в квартире'),
    (2, 'Научиться готовить'),
    (3, 'Завести домашнее животное'),
    (4, 'Научиться отдыхать'),
    (5, 'Провести отпуск в Зимбабве'),
    (6, 'Уволиться с работы'),
    (7, 'Расстаться с девушкой'),
    (8, 'Бросить курить'),
    (9, 'Выйти замуж'),
    (10, 'Научиться танцевать');