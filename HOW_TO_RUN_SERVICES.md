# 🚀 Comment Exécuter les Services dans IntelliJ IDEA

## 📋 Méthode 1 : Terminal Intégré d'IntelliJ (Recommandé)

### Étape 1 : Ouvrir le Terminal
1. Dans IntelliJ, allez dans le menu : **View → Tool Windows → Terminal**
   - Ou utilisez le raccourci : **Alt + F12** (Windows/Linux) ou **Option + F12** (Mac)
   - Ou cliquez sur l'onglet **Terminal** en bas de l'écran

### Étape 2 : Naviguer vers le Service
```bash
cd cabinet-medical-projet-back-end/registry-service
```

### Étape 3 : Démarrer le Service
```bash
mvn spring-boot:run
```

### Étape 4 : Ouvrir de Nouveaux Terminaux pour les Autres Services

**Option A : Nouvel Onglet Terminal**
- Cliquez sur le **+** à côté de l'onglet Terminal
- Ou utilisez : **Alt + Shift + T** (Windows/Linux) ou **Option + Shift + T** (Mac)

**Option B : Split Terminal**
- Clic droit sur l'onglet Terminal → **Split Right** ou **Split Down**

### Exemple : Démarrer Tous les Services

**Terminal 1 - Registry Service (Eureka) :**
```bash
cd cabinet-medical-projet-back-end/registry-service
mvn spring-boot:run
```

**Terminal 2 - Patient Service :**
```bash
cd cabinet-medical-projet-back-end/patient-service
mvn spring-boot:run
```

**Terminal 3 - Consultation Service :**
```bash
cd cabinet-medical-projet-back-end/consultation-service
mvn spring-boot:run
```

**Terminal 4 - RendezVous Service :**
```bash
cd cabinet-medical-projet-back-end/rendezvous-service
mvn spring-boot:run
```

## 📋 Méthode 2 : Run Configuration dans IntelliJ

### Créer une Run Configuration

1. **Ouvrir Run Configurations**
   - Menu : **Run → Edit Configurations...**
   - Ou cliquez sur la liste déroulante en haut à droite → **Edit Configurations...**

2. **Ajouter une Nouvelle Configuration**
   - Cliquez sur **+** → **Maven**

3. **Configurer**
   - **Name** : `Registry Service` (ou nom du service)
   - **Working directory** : `$PROJECT_DIR$/cabinet-medical-projet-back-end/registry-service`
   - **Command line** : `spring-boot:run`
   - Cliquez sur **OK**

4. **Démarrer**
   - Sélectionnez la configuration dans la liste déroulante
   - Cliquez sur le bouton **Run** (▶️) ou **Debug** (🐛)

### Avantages de cette Méthode
- ✅ Plusieurs services peuvent être démarrés en parallèle
- ✅ Logs séparés pour chaque service
- ✅ Facile à redémarrer
- ✅ Peut être sauvegardé dans le projet

## 📋 Méthode 3 : Utiliser le Profil Dev (H2 - Sans PostgreSQL)

Si vous voulez tester sans PostgreSQL, utilisez le profil `dev` :

```bash
cd cabinet-medical-projet-back-end/patient-service
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## 🔍 Vérifier que le Service Démarre Correctement

### Indicateurs de Succès
- ✅ Vous voyez : `Started [ServiceName]Application`
- ✅ Pas d'erreurs de connexion PostgreSQL
- ✅ Le service s'enregistre dans Eureka (sauf registry-service)

### Vérifier les Ports
```bash
# Dans un nouveau terminal
netstat -ano | findstr ":8081 :8082 :8083 :8761"
```

### Vérifier Eureka Dashboard
Ouvrez dans le navigateur : http://localhost:8761

Vous devriez voir les services enregistrés.

## ⚠️ Problèmes Courants

### Port déjà utilisé
```
Port 8081 was already in use
```
**Solution :**
```bash
# Trouver le processus
netstat -ano | findstr :8081

# Tuer le processus (remplacer PID par le numéro trouvé)
taskkill /F /PID <PID>
```

### Erreur de connexion PostgreSQL
```
FATAL: password authentication failed
```
**Solution :**
- Vérifier que PostgreSQL est démarré
- Vérifier les credentials dans `application.yml`
- Exécuter `init-databases.sql` si les bases n'existent pas

### Eureka non accessible
```
Cannot execute request on any known server
```
**Solution :**
- Démarrer `registry-service` en premier
- Attendre qu'il soit complètement démarré avant de démarrer les autres services

## 💡 Astuces IntelliJ

### Raccourcis Utiles
- **Ctrl + C** : Arrêter le service en cours
- **Ctrl + D** : Dupliquer l'onglet terminal
- **Alt + F12** : Ouvrir/Fermer le terminal
- **Ctrl + Shift + F12** : Maximiser/Restaurer le terminal

### Organiser les Terminaux
- **Split Right** : Diviser horizontalement
- **Split Down** : Diviser verticalement
- **Close** : Fermer l'onglet terminal

### Voir les Logs
- Les logs apparaissent directement dans le terminal
- Vous pouvez faire défiler avec la molette de la souris
- Utilisez **Ctrl + F** pour rechercher dans les logs

## 📝 Ordre Recommandé de Démarrage

1. **Registry Service** (Eureka) - Port 8761
   - Attendre : `Started EurekaServerApplication`

2. **Patient Service** - Port 8081
   - Attendre : `Started PatientServiceApplication`

3. **Consultation Service** - Port 8082
   - Attendre : `Started ConsultationServiceApplication`

4. **RendezVous Service** - Port 8083 (optionnel)
   - Attendre : `Started RendezvousServiceApplication`

5. **Notification Service** - Port 8084 (optionnel)
   - Attendre : `Started NotificationServiceApplication`

## ✅ Checklist de Démarrage

- [ ] PostgreSQL est démarré
- [ ] Bases de données créées (`init-databases.sql` exécuté)
- [ ] Registry Service démarré et accessible sur http://localhost:8761
- [ ] Autres services démarrés et enregistrés dans Eureka
- [ ] Pas d'erreurs dans les logs
- [ ] Swagger UI accessible pour chaque service


