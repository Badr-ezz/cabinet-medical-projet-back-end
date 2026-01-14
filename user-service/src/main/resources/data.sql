-- Password is 'password'
INSERT INTO users (login, pwd, nom, prenom, role, num_tel, signature) 
VALUES ('medecin@test.com', '$2a$10$wPHxwfsfTnOJAdgYcerBt.utdAvC24B/DWfuXfzKBSDHO0etB1ica', 'Alami', 'Mohammed', 0, '0600000000', 'sig_medecin')
ON CONFLICT (login) DO NOTHING;
