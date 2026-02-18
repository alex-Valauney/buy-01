# Makefile pour le projet Buy-01
# Ce fichier permet d'automatiser les tâches courantes du projet.

# Définition des dossiers
SERVICES_DIR = microservices
INFRA_DIR = infrastructure
FRONT_DIR = frontend
SERVICES_LIST = discovery-service gateway-service user-service product-service media-service

# Commande Maven Wrapper
MVNW = ./mvnw
# Fichier pour stocker les PIDs
PIDS_FILE = .pids

.PHONY: all build start stop clean help mongo

all: help

# ------------------------------------------------------------------------------
# COMMANDES
# ------------------------------------------------------------------------------

help: ## Affiche l'aide
	@echo "=================================================="
	@echo "      🛠️  Commandes Makefile - Buy-01"
	@echo "=================================================="
	@echo "Commandes disponibles :"
	@echo "  make build    : Compile tous les microservices"
	@echo "  make start    : Lance tout le projet (Infrastructure + Services + Frontend)"
	@echo "  make stop     : Arrête tout le projet"
	@echo "  make clean    : Supprime les fichiers temporaires et les logs"
	@echo "  make mongo    : Connecte au shell MongoDB (admin/password)"
	@echo "  make sonar    : Lance l'analyse SonarQube et les tests"
	@echo "  make help     : Affiche ce message"
	@echo ""

build: ## Compile tous les microservices
	@echo "🏗️  [1/1] Compilation des microservices..."
	@for service in $(SERVICES_LIST); do \
		echo "   👉 $$service..."; \
		(cd $(SERVICES_DIR)/$$service && $(MVNW) clean install -DskipTests -q) || exit 1; \
	done
	@echo "✅  Compilation terminée avec succès !"

sonar: ## Lance l'analyse SonarQube et les tests
	@echo "🔍  [1/1] Analyse SonarQube & Coverage..."
	@for service in $(SERVICES_LIST); do \
		echo "   👉 $$service..."; \
		(cd $(SERVICES_DIR)/$$service && $(MVNW) verify sonar:sonar) || exit 1; \
	done
	@echo "✅  Analyse terminée ! Voir http://localhost:9000"

start: ## Lance le projet (Infra -> Discovery -> Services -> Frontend)
	@echo "=================================================="
	@echo "      Démarrage du Projet Buy-01 - Automatisé     "
	@echo "=================================================="
	@mkdir -p logs
	
	@echo "[1/4] Démarrage de l'infrastructure Docker..."
	@cd $(INFRA_DIR) && docker-compose up -d
	
	@echo "⏳ Attente de 10s pour l'initialisation des bases de données..."
	@sleep 10
	
	@echo "[2/4] Démarrage du Discovery Service..."
	@nohup $(SERVICES_DIR)/discovery-service/mvnw -f $(SERVICES_DIR)/discovery-service/pom.xml spring-boot:run > logs/discovery.log 2>&1 & echo $$! > $(PIDS_FILE)
	@echo "   -> Discovery Service lancé. Logs: logs/discovery.log"
	
	@echo "⏳ Attente de 20s pour que le Discovery Service soit prêt..."
	@sleep 20
	
	@echo "[3/4] Démarrage des autres microservices..."
	
	@echo "   -> Gateway Service..."
	@nohup $(SERVICES_DIR)/gateway-service/mvnw -f $(SERVICES_DIR)/gateway-service/pom.xml spring-boot:run > logs/gateway.log 2>&1 & echo $$! >> $(PIDS_FILE)
	
	@echo "   -> User Service..."
	@nohup $(SERVICES_DIR)/user-service/mvnw -f $(SERVICES_DIR)/user-service/pom.xml spring-boot:run > logs/user.log 2>&1 & echo $$! >> $(PIDS_FILE)
	
	@echo "   -> Product Service..."
	@nohup $(SERVICES_DIR)/product-service/mvnw -f $(SERVICES_DIR)/product-service/pom.xml spring-boot:run > logs/product.log 2>&1 & echo $$! >> $(PIDS_FILE)
	
	@echo "   -> Media Service..."
	@nohup $(SERVICES_DIR)/media-service/mvnw -f $(SERVICES_DIR)/media-service/pom.xml spring-boot:run > logs/media.log 2>&1 & echo $$! >> $(PIDS_FILE)
	
	@echo "[4/4] Démarrage du Frontend (Angular)..."
	@cd $(FRONT_DIR) && nohup npm start > ../logs/frontend.log 2>&1 & echo $$! >> ../$(PIDS_FILE)
	
	@echo "=================================================="
	@echo "✅ TOUT EST LANCÉ !"
	@echo "=================================================="
	@echo "👉 Frontend : http://localhost:4200"
	@echo "👉 Gateway  : http://localhost:8080"
	@echo "👉 Eureka   : http://localhost:8761"
	@echo "📝 Les logs sont disponibles dans le dossier 'logs/'"
	@echo "🛑 Pour arrêter le projet : make stop"

stop: ## Arrête le projet
	@echo "=================================================="
	@echo "        Arrêt du Projet Buy-01                    "
	@echo "=================================================="
	@if [ -f $(PIDS_FILE) ]; then \
		echo "[1/2] Arrêt des services (Backend & Frontend)..."; \
		while read pid; do \
			if kill -0 $$pid 2>/dev/null; then \
				echo "   -> Arrêt du processus $$pid"; \
				kill $$pid; \
			else \
				echo "   -> Processus $$pid déjà arrêté"; \
			fi; \
		done < $(PIDS_FILE); \
		rm $(PIDS_FILE); \
	else \
		echo "[1/2] Aucun fichier .pids trouvé (services déjà arrêtés ?)"; \
	fi
	
	@echo "[2/2] Arrêt de l'infrastructure Docker..."
	@cd $(INFRA_DIR) && docker-compose down
	
	@echo "=================================================="
	@echo "✅ PROJET ARRÊTÉ"
	@echo "=================================================="

mongo: ## Accède au shell MongoDB
	@echo "🍃 Connexion à MongoDB..."
	@docker exec -it buy01-mongodb mongosh -u admin -p password

clean: ## Nettoie les fichiers temporaires et de compilation
	@echo "🧹  Nettoyage du projet..."
	@for service in $(SERVICES_LIST); do \
		echo "   👉 Nettoyage de $$service..."; \
		(cd $(SERVICES_DIR)/$$service && $(MVNW) clean -q) || exit 1; \
	done
	@echo "   👉 Suppression des logs..."
	@rm -rf logs
	@echo "✅  Nettoyage terminé !"
