insert into users (name, username, password)
values ('Anton', 'anbudo@taltech.ee', '$2a$10$FWCXM1qiIAGnof2Gryntm.D7XxpTGGE0JlEdtJryuO0u/sRTwspm6'),
       ('Nikita', 'nikita@gmail.com', '$2a$10$merfnWlnYt58jzSw8AsmLONH1TfJrz2/MNWM6UEcaVzmORHrtScNq');

insert into tasks (title, description, status, expiration_date)
values ('Sleep', 'Go to bed', 'TODO', '2024-05-03 12:00:00'),
       ('Do homework', 'Math', 'IN_PROGRESS', '2022-04-12 13:00:00'),
       ('Clean rooms', null, 'DONE', '2022-01-01 00:00:00'),
       ('Call Mike', null, 'TODO', '2022-01-01 00:00:00');

insert into users_tasks (task_id, user_id)
values (1, 2),
       (2, 2),
       (3, 2),
       (4, 1);

insert into users_roles (user_id, role)
values (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER');