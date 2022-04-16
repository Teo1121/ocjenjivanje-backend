INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

INSERT INTO users(username,email,password) VALUES('test','test@test.com','$2a$10$dIRAtTENMIstqDpW3YjH/ufSgyfuQMvJ7PTvz0QtijvwHKV3FeaCi');

INSERT INTO user_roles(user_id,role_id) VALUES(1,1);
INSERT INTO user_roles(user_id,role_id) VALUES(1,2);
INSERT INTO user_roles(user_id,role_id) VALUES(1,3);