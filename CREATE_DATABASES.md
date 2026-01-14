# 🗄️ Créer les Bases de Données PostgreSQL

## ⚠️ Problème
L'erreur indique que la base de données `consultation_service_db` n'existe pas.

## ✅ Solution : Créer les Bases de Données

### Méthode 1 : Via pgAdmin 4 (Recommandé)

1. **Ouvrir pgAdmin 4**

2. **Se connecter au serveur PostgreSQL**
   - Clic droit sur **Servers** → **Create** → **Server**
   - Ou utilisez le serveur existant
   - **Host**: `localhost`
   - **Port**: `5432`
   - **Username**: `postgres` (ou votre utilisateur)
   - **Password**: Votre mot de passe PostgreSQL

3. **Ouvrir Query Tool**
   - Clic droit sur la base de données **postgres** → **Query Tool**

4. **Exécuter le script SQL suivant** (copier-coller) :

```sql
-- Créer les bases de données
CREATE DATABASE consultation_service_db;
CREATE DATABASE patient_service_db;
CREATE DATABASE rendezvous_service_db;
CREATE DATABASE notification_service_db;

-- Créer l'utilisateur (si n'existe pas)
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
```

5. **Pour chaque base de données, donner les privilèges sur le schéma** :

   **Pour consultation_service_db :**
   - Clic droit sur `consultation_service_db` → **Query Tool**
   - Exécuter :
   ```sql
   GRANT ALL ON SCHEMA public TO cabinet_app;
   ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cabinet_app;
   ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cabinet_app;
   ```

   **Répéter pour :**
   - `patient_service_db`
   - `rendezvous_service_db`
   - `notification_service_db`

### Méthode 2 : Via psql Shell (Ligne de commande)

1. **Ouvrir psql Shell** (rechercher "psql" dans le menu Démarrer)

2. **Se connecter** :
   ```
   Server [localhost]: 
   Database [postgres]: postgres
   Port [5432]: 5432
   Username [postgres]: postgres
   Password: [votre mot de passe]
   ```

3. **Exécuter les commandes** :

```sql
-- Créer les bases de données
CREATE DATABASE consultation_service_db;
CREATE DATABASE patient_service_db;
CREATE DATABASE rendezvous_service_db;
CREATE DATABASE notification_service_db;

-- Créer l'utilisateur
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_user WHERE usename = 'cabinet_app') THEN
        CREATE USER cabinet_app WITH PASSWORD 'cabinet123';
    END IF;
END
$$;

-- Donner les privilèges
GRANT ALL PRIVILEGES ON DATABASE consultation_service_db TO cabinet_app;
GRANT ALL PRIVILEGES ON DATABASE patient_service_db TO cabinet_app;
GRANT ALL PRIVILEGES ON DATABASE rendezvous_service_db TO cabinet_app;
GRANT ALL PRIVILEGES ON DATABASE notification_service_db TO cabinet_app;

-- Pour consultation_service_db
\c consultation_service_db
GRANT ALL ON SCHEMA public TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cabinet_app;

-- Pour patient_service_db
\c patient_service_db
GRANT ALL ON SCHEMA public TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cabinet_app;

-- Pour rendezvous_service_db
\c rendezvous_service_db
GRANT ALL ON SCHEMA public TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cabinet_app;

-- Pour notification_service_db
\c notification_service_db
GRANT ALL ON SCHEMA public TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cabinet_app;

-- Retourner à postgres
\c postgres

-- Vérifier
\l
```

### Méthode 3 : Script Rapide (Juste consultation_service_db)

Si vous voulez juste créer la base manquante rapidement :

**Dans pgAdmin 4 Query Tool :**
```sql
CREATE DATABASE consultation_service_db;
GRANT ALL PRIVILEGES ON DATABASE consultation_service_db TO cabinet_app;

-- Puis se connecter à consultation_service_db et exécuter :
\c consultation_service_db
GRANT ALL ON SCHEMA public TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cabinet_app;
```

## ✅ Vérification

Après avoir créé les bases, vérifiez dans pgAdmin 4 :
- Vous devriez voir les 4 bases de données dans la liste
- Clic droit sur chaque base → **Properties** → **Privileges** → Vérifier que `cabinet_app` a les droits

## 🚀 Après la Création

Une fois les bases créées, relancez votre service :

```bash
cd cabinet-medical-projet-back-end/consultation-service
mvn spring-boot:run
```

Le service devrait maintenant démarrer correctement ! ✅

