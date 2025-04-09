backend/
├── src/
│   ├── main/
│   │   ├── java/com/example/project/
│   │   │   ├── controllers/  # REST API Controllers
│   │   │   ├── services/     # Business logic
│   │   │   ├── models/       # Entity classes for database
│   │   │   ├── repositories/ # Database interaction
│   │   │   ├── config/       # App configuration
│   │   │   ├── ProjectApplication.java # Main entry point
│   ├── resources/
│   │   ├── application.properties  # Config settings
├── scripts/
│   ├── migrate_db.sql  # Database migration script
│   ├── seed_db.sh      # Script to seed data
├── pom.xml  # Maven dependencies

