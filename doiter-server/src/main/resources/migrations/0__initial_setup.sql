create table goals (
  id              bigint unsigned not null auto_increment,
  name            varchar(100) not null,
  primary key (id)
);

create table messages (
  id             bigint unsigned not null auto_increment,
  goal_id        bigint unsigned not null,
  text           varchar(1000) not null,
  type           varchar(100) NOT NULL DEFAULT 'OTHER',
  order_index    bigint unsigned,

  primary key   (id),
  foreign key (goal_id) references goals(id) on delete cascade on update cascade
);

create index goal_id on goals(id);

insert into goals (id, name) values
    (0, 'Меньше Сидеть в Интернете'),
    (1, 'Убраться в Квартире'),
    (2, 'Научиться Готовить'),
    (3, 'Завести Домашнее Животное'),
    (4, 'Научиться Отдыхать'),
    (5, 'Провести Отпуск в Зимбабве'),
    (6, 'Уволиться с Работы'),
    (7, 'Расстаться с Девушкой'),
    (8, 'Бросить Курить'),
    (9, 'Выйти Замуж'),
    (10, 'Научиться Танцевать'),
    (11, 'Улучшить Карму'),
    (12, 'Разыграть Друга');