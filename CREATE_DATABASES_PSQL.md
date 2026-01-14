# 🗄️ Créer les Bases de Données avec psql Shell

## 📋 Instructions pour psql Shell

### Étape 1 : Ouvrir psql Shell

1. **Rechercher "psql"** dans le menu Démarrer Windows
2. **Ouvrir "SQL Shell (psql)"**

### Étape 2 : Se Connecter

Lorsque psql Shell s'ouvre, il vous demandera :

```
Server [localhost]: 
Database [postgres]: postgres
Port [5432]: 5432
Username [postgres]: postgres
Password for user postgres: [Tapez votre mot de passe et appuyez sur Entrée]
```

**Note :** Le mot de passe ne s'affichera pas à l'écran (c'est normal pour la sécurité).

### Étape 3 : Exécuter le Script SQL

Une fois connecté, vous verrez :
```
postgres=#
```

**Copiez-collez tout le script suivant** (ou tapez ligne par ligne) :

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

-- Se connecter à consultation_service_db et donner les privilèges sur le schéma
\c consultation_service_db
GRANT ALL ON SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cabinet_app;

-- Se connecter à patient_service_db et donner les privilèges sur le schéma
\c patient_service_db
GRANT ALL ON SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cabinet_app;

-- Se connecter à rendezvous_service_db et donner les privilèges sur le schéma
\c rendezvous_service_db
GRANT ALL ON SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cabinet_app;

-- Se connecter à notification_service_db et donner les privilèges sur le schéma
\c notification_service_db
GRANT ALL ON SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO cabinet_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cabinet_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cabinet_app;

-- Retourner à la base postgres
\c postgres

-- Vérifier que tout est créé
\l
```

### Étape 4 : Vérifier

Après avoir exécuté le script, vous devriez voir la liste des bases de données avec `\l` :

```
                                  List of databases
     Name              |  Owner   | Encoding |   Collate   |    Ctype    | 
-----------------------+----------+----------+-------------+-------------+
 consultation_service_db | postgres | UTF8     | ... | ... |
 patient_service_db    | postgres | UTF8     | ... | ... |
 rendezvous_service_db | postgres | UTF8     | ... | ... |
 notification_service_db| postgres | UTF8     | ... | ... |
 postgres             | postgres | UTF8     | ... | ... |
```

### Étape 5 : Quitter psql

```sql
\q
```

## 🚀 Alternative : Exécuter le Script depuis un Fichier

Si vous préférez exécuter le script depuis un fichier :

1. **Ouvrir PowerShell** dans le dossier du projet
2. **Exécuter** :
   ```powershell
   psql -U postgres -d postgres -f init-databases.sql
   ```
   (Vous devrez entrer votre mot de passe)

## ⚠️ Si vous avez des Problèmes de Connexion

### Erreur : "password authentication failed"

**Solution 1 :** Vérifier le mot de passe
- Assurez-vous d'utiliser le bon mot de passe pour l'utilisateur `postgres`

**Solution 2 :** Utiliser un autre utilisateur
- Si vous avez créé un autre utilisateur avec des privilèges, utilisez-le :
  ```
  Username [postgres]: votre_utilisateur
  ```

### Erreur : "could not connect to server"

**Solution :**
- Vérifier que le service PostgreSQL est démarré
- Vérifier le port (par défaut 5432)
- Vérifier que PostgreSQL écoute sur localhost

## ✅ Après la Création

Une fois les bases créées, relancez votre service :

```bash
cd cabinet-medical-projet-back-end/consultation-service
mvn spring-boot:run
```

Le service devrait maintenant démarrer correctement ! ✅

## 📝 Commandes psql Utiles

- `\l` : Lister toutes les bases de données
- `\c nom_base` : Se connecter à une base de données
- `\du` : Lister tous les utilisateurs
- `\dt` : Lister toutes les tables dans la base actuelle
- `\q` : Quitter psql
- `\?` : Aide sur les commandes psql
- `\h` : Aide sur les commandes SQL

