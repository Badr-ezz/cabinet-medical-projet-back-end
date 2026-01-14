-- Patients Data
INSERT INTO patients (nom, prenom, cin, date_naissance, sexe, num_tel) VALUES 
('Alami', 'Ahmed', 'AB12345', '1980-05-15', 'HOMME', '0611223344'),
('Benjelloun', 'Fatima', 'CD67890', '1992-08-22', 'FEMME', '0622334455'),
('Tazi', 'Karim', 'EF13579', '1975-03-10', 'HOMME', '0633445566'),
('Berrada', 'Layla', 'GH24680', '1988-11-05', 'FEMME', '0644556677'),
('Chraibi', 'Omar', 'IJ97531', '2000-01-30', 'HOMME', '0655667788'),
('El Fassi', 'Khadija', 'KL86420', '1965-09-12', 'FEMME', '0666778899'),
('Bennani', 'Youssef', 'MN11223', '1982-04-18', 'HOMME', '0677889900'),
('Ouazzani', 'Samira', 'OP33445', '1995-07-25', 'FEMME', '0688990011'),
('Idrissi', 'Hassan', 'QR55667', '1970-12-08', 'HOMME', '0699001122'),
('Mansouri', 'Nadia', 'ST77889', '1985-06-14', 'FEMME', '0600112233');

-- Dossiers Medicaux for each patient (assuming simple 1-to-1 mapping logic or separate inserts)
-- Note: Depending on your entity structure 'dossier_medical', you might need inserts here too if it's not auto-created.
-- But usually patient service creates it. If not, these patients might lack dossiers initially.
