# TrocSkillHub — Back-end

API REST d'une plateforme d'**échange de compétences entre particuliers** : chaque utilisateur indique ce qu'il sait faire et ce qu'il veut apprendre.

**Stack** : Java 21 · Spring Boot 3.4 · Spring Security + JWT · PostgreSQL 16 · Flyway · MapStruct · Lombok · Docker · GitHub Actions

---

## 🚀 Démarrage rapide

> Prérequis : **Docker Desktop** lancé. Toutes les commandes se font depuis le dossier `TrocSkillHub/`.

**1. Créer le fichier `TrocSkillHub/.env`** (déjà ignoré par Git) :

```dotenv
POSTGRES_DB=trocskillhubdb
POSTGRES_USER=*****
POSTGRES_PASSWORD=*****
JWT_SECRET=une_cle_secrete_d_au_moins_32_caracteres
JWT_EXPIRATION=86400000   # durée de validité du token en ms (ici 24 h)
```

**2. Lancer l'application :**

```bash
cd TrocSkillHub
docker compose up --build
```

**3. C'est prêt :** API sur `http://localhost:8080` · PostgreSQL sur `localhost:5432`

| Action | Commande |
|---|---|
| Arrêter (garder les données) | `docker compose down` |
| Arrêter + reset complet de la BDD | `docker compose down -v` |

---

## 🔐 Authentification

- `POST /api/auth/login` renvoie un **JWT dans un cookie `jwt`**.
- Côté front, toujours envoyer les cookies : `credentials: "include"` (fetch) ou `withCredentials: true` (axios).
- CORS autorisé pour `http://localhost:5173` (front Vite).

---

## 📁 Structure

```
TrocSkillHub/
├── src/main/java/RNCP/TrocSkillHub/
│   ├── Config/        # Sécurité, JWT, CORS
│   ├── Controllers/   # Endpoints REST
│   ├── Services/      # Logique métier
│   ├── Repositories/  # Accès aux données (JPA)
│   ├── Models/        # Entités
│   ├── DTOs/          # Objets échangés avec le front
│   ├── Mappers/       # Entité ↔ DTO (MapStruct)
│   └── Utils/         # Constantes
├── src/main/resources/
│   ├── application.yaml   # Configuration
│   ├── db/migration/      # Migrations Flyway
│   └── static/avatars/    # Avatars SVG
└── src/test/              # Tests unitaires (JUnit 5 + Mockito)
```

---

## ✅ Tests & CI

```bash
./mvnw test          # Windows : mvnw.cmd test
```

Environnement de test isolé (BDD sur le port **5434**, API sur le port **8081**) :

```bash
docker compose -f docker-compose.test.yml up -d --build
docker compose -f docker-compose.test.yml down -v
```

**GitHub Actions** compile et lance les tests à chaque PR vers `main`/`develop`.

---

## 🤝 Contribuer

**Git**
- `main` : version stable · `develop` : intégration
- Une branche par ticket (ex. `back-32_endpoint_homepage`) → Pull Request vers `develop`
- Commits préfixés : `feat:`, `fix:`, `refacto:`…

**Migrations Flyway**
- Nommage : `V{numéro}__{description}.sql` (ex. `V13__add_table_message.sql`)
- ⚠️ Ne **jamais modifier** une migration déjà appliquée : en créer une nouvelle.
- Les entités Java doivent correspondre au schéma SQL (Hibernate en mode `validate`).

---

## 🛠️ Dépannage

| Problème | Solution |
|---|---|
| Les conteneurs ne démarrent pas | Vérifier que Docker Desktop est lancé et que `.env` est dans `TrocSkillHub/` |
| Port 8080 ou 5432 déjà utilisé | Windows : `netstat -ano \| findstr :8080` puis `taskkill /PID <PID> /F` — Mac/Linux : `lsof -i :8080` puis `kill -9 <PID>` |
| Erreur Flyway / « Schema validation » | `docker compose down -v && docker compose up --build` |
| Erreur 401 | Se connecter d'abord et vérifier que le cookie `jwt` est envoyé |
| Erreur CORS | Le front doit tourner sur `localhost:5173` et envoyer les cookies |

---

## 👥 Auteurs

Sarra Baehl - Projet RNCP niveau 6, Ada Tech School Nantes 2026-2027
