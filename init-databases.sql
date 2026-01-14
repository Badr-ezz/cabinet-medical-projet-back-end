-- Script SQL pour créer les bases de données PostgreSQL
-- Exécuter avec: psql -U postgres -p 5432 -f init-databases.sql
-- Ou copier-coller ce contenu dans psql Shell ou pgAdmin 4

-- Créer la base de données pour consultation-service
CREATE DATABASE consultation_service_db;

-- Créer la base de données pour patient-service
CREATE DATABASE patient_service_db;

-- Créer la base de données pour rendezvous-service
CREATE DATABASE rendezvous_service_db;

-- Créer la base de données pour notification-service
CREATE DATABASE notification_service_db;

-- Créer un utilisateur pour les services (optionnel mais recommandé)
-- Si l'utilisateur existe déjà, cette commande échouera silencieusement
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_user WHERE usename = 'cabinet_app') THEN
        CREATE USER cabinet_app WITH PASSWORD 'cabinet123';
    END IF;
END
$$;

-- Donner les privilèges sur les bases de données
GRANT ALL PRIVILEGES ON DATABASE consultation_service_db TO cabinet_app;
GRANT ALL PRIVILEGES ON DATABASE patient_service_db TO cabinet_app;
GRANT ALL PRIVILEGES ON DATABASE rendezvous_service_db TO cabinet_app;
GRANT ALL PRIVILEGES ON DATABASE notification_service_db TO cabinet_app;

-- Se connecter à la base consultation_service_db et donner les privilèges sur le schéma
\c consultation_service_db
GRANT ALL ON SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cabinet_app;

-- Se connecter à la base patient_service_db et donner les privilèges sur le schéma
\c patient_service_db
GRANT ALL ON SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cabinet_app;

-- Se connecter à la base rendezvous_service_db et donner les privilèges sur le schéma
\c rendezvous_service_db
GRANT ALL ON SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cabinet_app;

-- Se connecter à la base notification_service_db et donner les privilèges sur le schéma
\c notification_service_db
GRANT ALL ON SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cabinet_app;

-- Créer la base de données pour user-service
CREATE DATABASE user_service_db;
GRANT ALL PRIVILEGES ON DATABASE user_service_db TO cabinet_app;

-- Se connecter à la base user_service_db et donner les privilèges sur le schéma
\c user_service_db
GRANT ALL ON SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cabinet_app;

-- Retourner à la base postgres
\c postgres

-- Afficher un message de confirmation
SELECT 'Bases de données créées avec succès!' AS message;

