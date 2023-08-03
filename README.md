# Instruction to run the application

1. Uncomment `spring.jpa.hibernate.ddl-auto = create` and edit/choose JDBC/Postgres properties in application.properties
   file.

2. Run the application by the main method in `PostAppApplication` class. After the first running spring will create
   tables and mappings automatically.

3. Stop the application and comment `spring.jpa.hibernate.ddl-auto = create` again, then fill the data using
   /resources/db/data.sql

4. Run the application by the main method in `PostAppApplication` class.
