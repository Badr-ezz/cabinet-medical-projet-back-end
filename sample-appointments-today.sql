-- Script pour ajouter des rendez-vous pour aujourd'hui (14/01/2026)
-- À exécuter dans la base de données rendezvous_db

-- Rendez-vous pour aujourd'hui
INSERT INTO rendez_vous (id_cabinet, id_medecin, id_patient, date_rdv, heure_rdv, statut, motif, created_at, updated_at) VALUES
-- Rendez-vous à 08:30 - Patient 1
(1, 1, 1, '2026-01-14', '08:30:00', 'TERMINE', 'Consultation de routine', NOW(), NOW()),

-- Rendez-vous à 09:00 - Patient 2
(1, 1, 2, '2026-01-14', '09:00:00', 'TERMINE', 'Suivi médical', NOW(), NOW()),

-- Rendez-vous à 09:30 - Patient 3
(1, 1, 3, '2026-01-14', '09:30:00', 'EN_COURS', 'Consultation urgente', NOW(), NOW()),

-- Rendez-vous à 10:00 - Patient 4
(1, 1, 4, '2026-01-14', '10:00:00', 'CONFIRME', 'Contrôle annuel', NOW(), NOW()),

-- Rendez-vous à 10:30 - Patient 5
(1, 1, 5, '2026-01-14', '10:30:00', 'CONFIRME', 'Renouvellement ordonnance', NOW(), NOW()),

-- Rendez-vous à 11:00 - Patient 6
(1, 1, 6, '2026-01-14', '11:00:00', 'CONFIRME', 'Vaccination', NOW(), NOW()),

-- Rendez-vous à 14:00 - Patient 7
(1, 1, 7, '2026-01-14', '14:00:00', 'CONFIRME', 'Consultation première fois', NOW(), NOW()),

-- Rendez-vous à 14:30 - Patient 8
(1, 1, 8, '2026-01-14', '14:30:00', 'CONFIRME', 'Résultats analyses', NOW(), NOW()),

-- Rendez-vous à 15:00 - Patient 9
(1, 1, 9, '2026-01-14', '15:00:00', 'CONFIRME', 'Suivi traitement', NOW(), NOW()),

-- Rendez-vous à 15:30 - Patient 10
(1, 1, 10, '2026-01-14', '15:30:00', 'CONFIRME', 'Consultation générale', NOW(), NOW()),

-- Rendez-vous à 16:00 - Patient 1
(1, 1, 1, '2026-01-14', '16:00:00', 'CONFIRME', 'Contrôle post-traitement', NOW(), NOW()),

-- Rendez-vous à 16:30 - Patient 2
(1, 1, 2, '2026-01-14', '16:30:00', 'CONFIRME', 'Consultation de suivi', NOW(), NOW());

-- Vérification
SELECT COUNT(*) as total_rdv_today FROM rendez_vous WHERE date_rdv = '2026-01-14';
