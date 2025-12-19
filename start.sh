#!/bin/bash

# Créer le dossier de logs
mkdir -p logs

echo "=================================================="
echo "      Démarrage du Projet Buy-01 - Automatisé     "
echo "=================================================="

# 1. Infrastructure
echo "[1/4] Démarrage de l'infrastructure Docker..."
cd infrastructure
docker-compose up -d
cd ..

echo "⏳ Attente de 10s pour l'initialisation des bases de données..."
sleep 10

# 2. Discovery Service
echo "[2/4] Démarrage du Discovery Service..."
# On force le chemin du pom.xml pour être sûr
nohup ./microservices/discovery-service/mvnw -f microservices/discovery-service/pom.xml spring-boot:run > logs/discovery.log 2>&1 &
DISCOVERY_PID=$!
echo $DISCOVERY_PID > .pids
echo "   -> Discovery Service lancé (PID: $DISCOVERY_PID). Logs: logs/discovery.log"

echo "⏳ Attente de 20s pour que le Discovery Service soit prêt..."
sleep 20

# 3. Microservices
echo "[3/4] Démarrage des autres microservices..."

echo "   -> Gateway Service..."
nohup ./microservices/gateway-service/mvnw -f microservices/gateway-service/pom.xml spring-boot:run > logs/gateway.log 2>&1 &
echo $! >> .pids

echo "   -> User Service..."
nohup ./microservices/user-service/mvnw -f microservices/user-service/pom.xml spring-boot:run > logs/user.log 2>&1 &
echo $! >> .pids

echo "   -> Product Service..."
nohup ./microservices/product-service/mvnw -f microservices/product-service/pom.xml spring-boot:run > logs/product.log 2>&1 &
echo $! >> .pids

echo "   -> Media Service..."
nohup ./microservices/media-service/mvnw -f microservices/media-service/pom.xml spring-boot:run > logs/media.log 2>&1 &
echo $! >> .pids

# 4. Frontend
echo "[4/4] Démarrage du Frontend (Angular)..."
cd frontend
nohup npm start > ../logs/frontend.log 2>&1 &
echo $! >> ../.pids
cd ..

echo "=================================================="
echo "✅ TOUT EST LANCÉ !"
echo "=================================================="
echo "👉 Frontend : http://localhost:4200"
echo "👉 Gateway  : http://localhost:8080"
echo "👉 Eureka   : http://localhost:8761"
echo ""
echo "📝 Les logs sont disponibles dans le dossier 'logs/'"
echo "🛑 Pour arrêter le projet : ./stop.sh"
