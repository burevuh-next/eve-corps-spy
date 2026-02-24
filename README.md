# EVE CorpEsp

<div align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" />
  <img src="https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=black" />
  <img src="https://img.shields.io/badge/TypeScript-5.4-3178C6?style=for-the-badge&logo=typescript&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" />
</div>

<div align="center">
  <h3>🚀 Симулятор корпоративного шпионажа во вселенной EVE Online</h3>
  <p>Внедряйся, кради чертежи, саботируй производства и избегай разоблачения</p>
</div>

---

## 📋 О проекте

**EVE CorpEsp** — это браузерная стратегия, в которой игрок выступает в роли агента, внедрённого во враждебную корпорацию. 

### 🎮 Ключевые особенности

- **🕵️ Шпионаж** — внедрение в корпорации, кража данных, саботаж
- **📊 Реальная экономика EVE** — синхронизация с рынком EVE Online
- **🧠 Прокачка навыков** — хакинг, социальная инженерия, скрытность
- **⚠️ Система риска** — чем активнее агент, тем выше шанс разоблачения
- **🌐 Киберпанк-стиль** — атмосферный интерфейс в стиле хакерского терминала

---

## 🛠️ Технологический стек

### Backend
- **Java 21** — основной язык
- **Spring Boot 3.2** — фреймворк
- **Spring Security** — аутентификация и авторизация
- **Spring Data JPA** — работа с базой данных
- **PostgreSQL 16** — база данных
- **Maven** — сборка проекта

### Frontend
- **React 19** — библиотека для UI
- **TypeScript 5.4** — типизация
- **Vite** — сборщик и dev-сервер
- **React Router** — навигация
- **Fetch API** — работа с бэкендом

---

## 🚀 Запуск проекта

### Предварительные требования

- **Java 21** или выше
- **Node.js 18+** и npm
- **PostgreSQL 16**
- **Maven** (или используйте ./mvnw)

### 1. Настройка базы данных

```sql
CREATE USER evecorpesp_user WITH PASSWORD 'secure_password';
CREATE DATABASE evecorpesp_db OWNER evecorpesp_user;
2. Backend
bash
cd backend
./mvnw spring-boot:run
Бэкенд будет доступен на http://localhost:8080

3. Frontend
bash
cd frontend
npm install
npm run dev
Фронтенд будет доступен на http://localhost:5173

📁 Структура проекта
text
eve-corpspy/
├── backend/                    # Java Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/backend/
│   │   │   │   ├── config/     # Конфигурации (Security, CORS)
│   │   │   │   ├── controller/ # REST контроллеры
│   │   │   │   ├── dto/        # Data Transfer Objects
│   │   │   │   ├── entity/     # JPA сущности
│   │   │   │   ├── repository/ # Репозитории
│   │   │   │   └── service/    # Бизнес-логика
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
│
└── frontend/                   # React + TypeScript
    ├── src/
    │   ├── pages/              # Страницы приложения
    │   │   └── RegisterPage.tsx
    │   ├── App.tsx              # Маршрутизация
    │   └── main.tsx
    ├── index.html
    ├── vite.config.ts           # Настройка прокси
    └── package.json
🔌 API Endpoints
Регистрация
http
POST /api/auth/register
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "123456"
}
Успешный ответ (201):

json
{
    "id": 1,
    "email": "user@example.com",
    "createdAt": "2026-02-24T20:15:30Z"
}
Ошибка (400):

json
{
    "message": "Email already in use"
}
✅ Реализованный функционал
Регистрация пользователей

Валидация на фронтенде и бэкенде

Хеширование паролей (BCrypt)

Интеграция с PostgreSQL

Обработка ошибок с понятными сообщениями

Прокси для разработки (Vite)

📝 В планах
JWT аутентификация

Защита маршрутов

Создание агента

Выбор корпорации-цели

Система миссий

Прокачка навыков

WebSocket уведомления

Интеграция с EVE Online API

🤝 Как внести вклад
Форкните репозиторий

Создайте ветку для фичи (git checkout -b feature/amazing-feature)

Закоммитьте изменения (git commit -m 'Add amazing feature')

Запушьте ветку (git push origin feature/amazing-feature)

Откройте Pull Request

📄 Лицензия
MIT

👨‍💻 Автор
Burevuh

<div align="center"> <sub>Built with ☕ and ⚛️ for EVE Online fans</sub> </div> ```
Как добавить:
Откройте файл README.md в корне проекта

Вставьте весь этот код

Сохраните (Ctrl+S)