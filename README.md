# TrocSkillHub — Back-end

Plateforme d'échange de compétences entre particuliers — API REST.

**Stack** : Java 21 · Spring Boot 3.4 · PostgreSQL 16 · Flyway · Maven

---

## 🚀 Démarrer le projet

1. Créer un fichier `.env` à la racine :
   ```dotenv
   POSTGRES_DB=trocskillhubdb
   POSTGRES_USER=*****
   POSTGRES_PASSWORD=*****
   ```

2. Lancer les conteneurs :
   ```bash
   docker compose up --build
   ```

3. L'API est disponible sur : `http://localhost:8080`

**Arrêter le projet**
```bash
docker compose down        # garde les données
docker compose down -v     # reset complet de la BDD
```

---

## 🗄️ Base de données

- Migrations SQL dans `src/main/resources/db/migration/`
- Nommage : `V{version}__{description}.sql` (ex: `V1__create_users_table.sql`)
- Après une nouvelle migration :
  ```bash
  docker compose down -v && docker compose up --build
  ```

---

## 📁 Structure

```
src/main/java/RNCP/TrocSkillHub/
├── controllers/    # Endpoints REST
├── services/       # Logique métier
├── repositories/   # Accès aux données (JPA)
├── models/         # Entités
├── dtos/           # Objets de transfert
└── mappers/        # Mappers MapStruct
```
---

## ✅ Tests

```bash
./mvnw test

# Base de test dédiée
docker compose -f docker-compose.test.yml up -d
docker compose -f docker-compose.test.yml down -v
```

---

## 🛠️ Dépannage

| Problème | Solution |
|----------|----------|
| Le conteneur ne démarre pas | Vérifier que Docker Desktop est lancé |
| Port 8080 occupé | `lsof -i :8080` puis `kill -9 <PID>` |
| Erreur BDD | `docker compose down -v` puis `docker compose up --build` |

---

## 👥 Auteurs

Sarra.B & Molid.A — Projet RNCP niveau 6, Ada Tech School Nantes 2026-2027