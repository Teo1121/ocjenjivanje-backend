INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

INSERT INTO users(username,email,password) VALUES('test','test@test.com','$2a$10$dIRAtTENMIstqDpW3YjH/ufSgyfuQMvJ7PTvz0QtijvwHKV3FeaCi');

INSERT INTO user_roles(user_id,role_id) VALUES(1,1);
INSERT INTO user_roles(user_id,role_id) VALUES(1,2);
INSERT INTO user_roles(user_id,role_id) VALUES(1,3);

INSERT INTO professors(name,details) VALUES('Test Testovic','Nezna nista');

INSERT INTO reviews(score,comment,anonymous,professor_id,user_id) VALUES(2,'Doista nezna nista',0,1,1);
INSERT INTO reviews(score,comment,anonymous,professor_id,user_id) VALUES(1,'Uistinu, nista nezna',1,1,1);