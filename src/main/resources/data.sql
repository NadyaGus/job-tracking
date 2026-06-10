INSERT INTO users (name, experience) VALUES
('Иван Иванов', 2),
('Елена Петрова', 0),
('Алексей Сидоров', 5),
('Мария Николаева', 1)
ON CONFLICT (name) DO NOTHING;

INSERT INTO user_skills (user_name, skill) VALUES
('Иван Иванов', 'Java'),
('Иван Иванов', 'SQL'),
('Иван Иванов', 'Docker'),
('Елена Петрова', 'Java'),
('Елена Петрова', 'Git'),
('Алексей Сидоров', 'Java'),
('Алексей Сидоров', 'Spring Boot'),
('Алексей Сидоров', 'PostgreSQL'),
('Алексей Сидоров', 'Docker'),
('Алексей Сидоров', 'Kubernetes'),
('Мария Николаева', 'Python'),
('Мария Николаева', 'Django')
ON CONFLICT (user_name, skill) DO NOTHING;

INSERT INTO jobs (title, company, exp_required) VALUES
('Junior Java Developer', 'VK', 0),
('Middle Java Engineer', 'Сбер', 2),
('Senior Java Architect', 'Яндекс', 5),
('Python Data Engineer', 'МТС', 1)
ON CONFLICT (title) DO NOTHING;

INSERT INTO job_tags (job_title, tag) VALUES
('Junior Java Developer', 'Java'),
('Junior Java Developer', 'Git'),
('Middle Java Engineer', 'Java'),
('Middle Java Engineer', 'SQL'),
('Middle Java Engineer', 'Docker'),
('Senior Java Architect', 'Java'),
('Senior Java Architect', 'Spring Boot'),
('Senior Java Architect', 'PostgreSQL'),
('Senior Java Architect', 'Docker'),
('Python Data Engineer', 'Python'),
('Python Data Engineer', 'Django')
ON CONFLICT (job_title, tag) DO NOTHING;
