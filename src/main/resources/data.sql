INSERT INTO users(email, password, name) 
VALUES ('email1@email.com', '111', 'Пользователь1');

INSERT INTO users(email, password, name) 
VALUES ('email2@email.com', '222', 'Пользователь2');

INSERT INTO projects(name) VALUES ('Проект1');
INSERT INTO projects(name) VALUES ('Проект2');

INSERT INTO projects_users(projects_id, users_id)
VALUES (1, 1);
INSERT INTO projects_users(projects_id, users_id)
VALUES (1, 2);
INSERT INTO projects_users(projects_id, users_id)
VALUES (2, 1);

INSERT INTO groups(name) VALUES ('Группа1');
INSERT INTO groups(name) VALUES ('Группа2');


INSERT INTO roles(role) VALUES ('MEMBER');
INSERT INTO roles(role) VALUES ('ADMIN');

INSERT INTO members(group_id, role_id, user_id) 
VALUES (1, 2, 1);
INSERT INTO members(group_id, role_id, user_id) 
VALUES (1, 1, 2);
INSERT INTO members(group_id, role_id, user_id) 
VALUES (2, 2, 1);