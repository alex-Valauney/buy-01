#!/bin/bash

echo "=================================================="
echo "        Arrêt du Projet Buy-01                    "
echo "=================================================="

# 1. Arrêt des processus Java/Node
if [ -f .pids ]; then
    echo "[1/2] Arrêt des services (Backend & Frontend)..."
    while read pid; do
        if kill -0 $pid 2>/dev/null; then
            echo "   -> Arrêt du processus $pid"
            kill $pid
        else
            echo "   -> Processus $pid déjà arrêté"
        fi
    done < .pids
    rm .pids
else
    echo "[1/2] Aucun fichier .pids trouvé (services déjà arrêtés ?)"
fi

# 2. Arrêt de Docker
echo "[2/2] Arrêt de l'infrastructure Docker..."
cd infrastructure
docker-compose down
cd ..

echo "=================================================="
echo "✅ PROJET ARRÊTÉ"
echo "=================================================="
