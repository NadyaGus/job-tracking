DELETE FROM job_tags;
DELETE FROM jobs;
DELETE FROM user_skills;
DELETE FROM users;

INSERT INTO users (name, experience) VALUES ('Bob', 4);
INSERT INTO user_skills (user_name, skill) VALUES ('Bob', 'java');
INSERT INTO user_skills (user_name, skill) VALUES ('Bob', 'sql');

INSERT INTO jobs (title, company, exp_required) VALUES ('Backend Developer', 'TechCorp', 3);
INSERT INTO job_tags (job_title, tag) VALUES ('Backend Developer', 'java');
INSERT INTO job_tags (job_title, tag) VALUES ('Backend Developer', 'spring');

INSERT INTO jobs (title, company, exp_required) VALUES ('Senior Java Developer', 'SuperCorp', 5);
INSERT INTO job_tags (job_title, tag) VALUES ('Senior Java Developer', 'java');
INSERT INTO job_tags (job_title, tag) VALUES ('Senior Java Developer', 'spring');
INSERT INTO job_tags (job_title, tag) VALUES ('Senior Java Developer', 'docker');