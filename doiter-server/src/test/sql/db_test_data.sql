delete from goals;
insert into goals (id, name, image_cover_id) values (1, "test goal one", 1), (2, "test goal two", 2);

delete from messages;
insert into messages(id, goal_id, text, order_index, type) values
  (1, 1, "some first advice", 0, 'FIRST'), (2, 1, "some other advice", 1, 'OTHER'), (3, 1, "some last advice", null, 'LAST'),
  (4, 1, "some first advice", 0, 'FIRST'), (5, 1, "some other advice", 1, 'OTHER'), (6, 1, "some last advice", null, 'LAST');