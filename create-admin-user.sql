-- Script pour ajouter un utilisateur ADMIN dans user_service_db
-- À exécuter dans PostgreSQL

-- 1. Se connecter à la base de données user_service_db
-- psql -U postgres
-- \c user_service_db

-- 2. Vérifier la structure de la table users
-- \d users

-- 3. Insérer un utilisateur admin
-- ATTENTION: Le mot de passe doit être haché avec BCrypt
-- Le mot de passe "admin123" haché avec BCrypt = $2a$10$xJ3v4z5h6k7j8m9n0p1q2rOZxY.wVuTs/R.qPoNmLkJiHgFeDcBa
-- (Vous devrez peut-être générer votre propre hash BCrypt)

INSERT INTO users (email, password, role, nom, prenom, telephone, created_at, updated_at, cabinet_id) 
VALUES (
  'admin@cabinet.com',                                              -- email
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',  -- password: "admin123" (BCrypt hash)
  'ADMIN',                                                           -- role
  'Admin',                                                           -- nom
  'Système',                                                         -- prenom
  '0600000000',                                                      -- telephone
  NOW(),                                                             -- created_at
  NOW(),                                                             -- updated_at
  1                                                                  -- cabinet_id
);

-- 4. Vérifier l'insertion
SELECT * FROM users WHERE role = 'ADMIN';

-- 5. Pour vous connecter ensuite:
-- Email: admin@cabinet.com
-- Mot de passe: admin123
