--Insert basic data into the tables

insert into client (date_curr, id_client) values ('2008-01-01 00:00:01', 1);
insert into credit (id_credit, id_client, amt_credit, date_start, state_credit) values (1, 1, 3000, '2008-11-11', 'O');
insert into credit (id_credit, id_client, amt_credit, date_start, state_credit) values (2, 1, 5000, '2008-10-11', 'O');

insert into client (date_curr, id_client) values ('20010-01-01 00:00:01', 2);
insert into credit (id_credit, id_client, amt_credit, date_start, state_credit) values (3, 2, 4000, '2008-11-11', 'O');
insert into credit (id_credit, id_client, amt_credit, date_start, state_credit) values (4, 2, 8000, '2008-10-11', 'O');
