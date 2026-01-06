# Projet Buy-01

Plateforme e-commerce Microservices (Spring Boot / Angular).

## Architecture
- **Infrastructure**: MongoDB, Kafka, Zookeeper (Docker)
- **Services**:
  - Discovery Service (Eureka): Port 8761
  - Gateway Service: Port 8080
  - User Service: Port 8081

## Démarrage Rapide
1. Lancer l'infrastructure : `docker-compose up -d` (dans /infrastructure)
2. Lancer Discovery Service
3. Lancer Gateway Service
4. Lancer User Service

## Identifiants de Test (Développement)

| ADMIN | Admin | admin@example.com | adminpassword123 |
| CLIENT | John | john.doe@example.com | securepassword123 |
| SELLER | Jane | jane.seller@example.com | sellerpassword123 |

## Modèles de Données (Collections MongoDB)

### Base `buy01-user`
Collection `users` :
- `_id`: String (Auto-généré)
- `firstname`: String
- `lastname`: String
- `email`: String (Unique)
- `password`: String (Hash BCrypt)
- `role`: Enum (CLIENT, SELLER, ADMIN)

### Base `buy01-product`
Collection `products` :
- `_id`: String (Auto-généré)
- `name`: String
- `description`: String
- `price`: BigDecimal
- `stockQuantity`: Integer
- `sellerId`: String (Référence vers User)
- `imageUrls`: List<String>

## Commandes Utiles (Mongo Shell)

### Accéder au Shell
```bash
docker exec -it buy01-mongodb mongosh -u admin -p password --authenticationDatabase admin
```

### Commandes Fréquentes
- **Lister les bases** : `show dbs`
- **Utiliser une base** : `use buy01-user` (ou `buy01-product`)
- **Lister les collections** : `show collections`
- **Voir les données** :
  ```javascript
  db.users.find() // Pour les utilisateurs
  db.products.find() // Pour les produits
  ```

  :)
