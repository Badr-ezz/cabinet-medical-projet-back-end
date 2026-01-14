-- Script SQL pour créer les bases de données PostgreSQL
-- À exécuter dans pgAdmin 4 Query Tool
-- Connectez-vous d'abord à la base de données "postgres"

-- ============================================
-- ÉTAPE 1 : Créer les bases de données
-- ============================================
CREATE DATABASE consultation_service_db;
CREATE DATABASE patient_service_db;
CREATE DATABASE rendezvous_service_db;
CREATE DATABASE notification_service_db;
CREATE DATABASE user_service_db;

-- ============================================
-- ÉTAPE 2 : Créer l'utilisateur (si n'existe pas)
-- ============================================
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_user WHERE usename = 'cabinet_app') THEN
        CREATE USER cabinet_app WITH PASSWORD 'cabinet123';
    END IF;
END
$$;

-- ============================================
-- ÉTAPE 3 : Donner les privilèges sur les bases de données
-- ============================================
GRANT ALL PRIVILEGES ON DATABASE consultation_service_db TO cabinet_app;
GRANT ALL PRIVILEGES ON DATABASE patient_service_db TO cabinet_app;
GRANT ALL PRIVILEGES ON DATABASE rendezvous_service_db TO cabinet_app;
GRANT ALL PRIVILEGES ON DATABASE notification_service_db TO cabinet_app;
GRANT ALL PRIVILEGES ON DATABASE user_service_db TO cabinet_app;

-- ============================================
-- IMPORTANT : Les commandes suivantes doivent être exécutées
-- dans chaque base de données individuellement
-- ============================================
-- 
-- Pour chaque base de données (consultation_service_db, patient_service_db, etc.) :
-- 1. Clic droit sur la base → Query Tool
-- 2. Exécuter ce script :
--
-- GRANT ALL ON SCHEMA public TO cabinet_app;
-- ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cabinet_app;
-- ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cabinet_app;
--
-- ============================================

