# Guide de Démarrage du Projet Buy-01

Ce guide détaille les étapes pour lancer l'application complète (Infrastructure, Backend, Frontend).

## 1. Prérequis

Assurez-vous d'avoir installé les outils suivants :
- **Java JDK 17** ou supérieur
- **Docker** et **Docker Compose**
- **Node.js** (v18+ recommandé) et **npm**
- **Maven** (optionnel, le wrapper `mvnw` est fourni)
- **Bash** (pour les scripts d'automatisation)

## 2. Démarrage Automatisé (Recommandé)

Pour simplifier le lancement et réduire le nombre de terminaux ouverts, utilisez les scripts fournis à la racine.

### Tout Lancer
Exécutez cette commande à la racine du projet :
```bash
./start.sh
```
Ce script va automatiquement :
1. Lancer l'infrastructure Docker (MongoDB, Kafka, Zookeeper).
2. Lancer tous les microservices en arrière-plan (logs redirigés vers le dossier `logs/`).
3. Lancer le frontend Angular.

### Tout Arrêter
Pour arrêter proprement tous les services et conteneurs :
```bash
./stop.sh
```

## 3. URLs d'Accès

Une fois le projet lancé, accédez aux services via ces URLs :

| Service | URL | Description |
|---------|-----|-------------|
| **Frontend** | http://localhost:4200 | Application E-commerce (Angular) |
| **API Gateway** | http://localhost:8080 | Point d'entrée unique des APIs |
| **Eureka** | http://localhost:8761 | Dashboard du registre des services |

## 4. Identifiants de Test

| Rôle | Email | Mot de passe |
|------|-------|--------------|
| **Client** | john.doe@example.com | securepassword123 |
| **Vendeur** | jane.seller@example.com | sellerpassword123 |
| **Admin** | admin@example.com | adminpassword123 |

---

## Annexe : Démarrage Manuel (Pas à pas)

Si vous rencontrez des problèmes avec les scripts ou avez besoin de débugger un service spécifique, suivez ces étapes manuelles.

### 1. Infrastructure
```bash
cd infrastructure
docker-compose up -d
```

### 2. Backend (Microservices)
Ouvrez un terminal par service et lancez-les **dans cet ordre** :

1. **Discovery Service** :
   ```bash
   cd microservices/discovery-service
   ./mvnw spring-boot:run
   ```
2. **Gateway Service** :
   ```bash
   cd microservices/gateway-service
   ./mvnw spring-boot:run
   ```
3. **Autres Services** (User, Product, Media) :
   ```bash
   cd microservices/[service-name]
   ./mvnw spring-boot:run
   ```

### 3. Frontend
```bash
cd frontend
npm install # Première fois seulement
npm start
```
