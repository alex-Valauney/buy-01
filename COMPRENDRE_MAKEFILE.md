# Comprendre le Makefile

Un **Makefile** est un fichier spécial utilisé par l'outil `make` pour automatiser des tâches dans un projet informatique. C'est comme une "télécommande" qui regroupe toutes les commandes complexes que vous devriez sinon taper à la main.

## 1. Structure de base

Un Makefile est composé de **règles** (ou *targets*). Chaque règle suit cette structure :

```makefile
target: dépendances
    commande
```

- **target** : Le nom de la commande (ex: `build`, `start`).
- **dépendances** : (Optionnel) D'autres règles qui doivent être exécutées avant celle-ci.
- **commande** : La commande shell à exécuter. **Attention** : Elle doit impérativement être précédée d'une **tabulation**, pas d'espaces !

## 2. Exemple Concret (Celui de ce projet)

Regardons le `Makefile` de ce projet :

```makefile
# Définition de variables (pour éviter de répéter du texte)
SERVICES = discovery-service gateway-service ...

# Target par défaut (ce qui se passe si on tape juste 'make')
all: help

# La target 'build'
build:
    # Compile tous les services un par un
    @for service in $(SERVICES); do ... done

# La target 'start'
start:
    # Lance l'infrastructure, attend les DBs, et lance les services un par un
    @docker-compose up -d
    @nohup ...
```

## 3. Les Commandes Usuelles

Dans ce projet, voici les commandes que vous pouvez utiliser dans votre terminal :

| Commande | Description |
| :--- | :--- |
| `make` | Affiche l'aide (liste des commandes). |
| `make build` | Compile tout le projet (nettoie et installe les dépendances Maven). |
| `make start` | Lance toute l'infrastructure (Docker, microservices, frontend). |
| `make stop` | Arrête proprement tous les services et conteneurs. |
| `make clean` | Supprime les fichiers générés (dossiers `target/`) et les logs pour remettre le projet à neuf. |
| `make mongo` | Ouvre directement un shell MongoDB connecté à la base de données (déjà identifié en tant qu'admin). |

## 4. Pourquoi l'utiliser ?

1.  **Gain de temps** : Plus besoin de taper `./mvnw clean install` dans chaque dossier.
2.  **Standardisation** : Tout le monde dans l'équipe utilise les mêmes commandes pour lancer le projet.
3.  **Simplicité** : Masque la complexité des scripts sous-jacents.

## 5. Astuces

- Le `@` devant une commande (ex: `@echo "..."`) sert à dire à `make` de ne pas afficher la commande elle-même dans le terminal, juste son résultat. C'est plus propre.
- `.PHONY` est utilisé pour dire à `make` que les cibles (`build`, `clean`, etc.) ne sont pas des vrais fichiers, mais juste des noms d'actions.
