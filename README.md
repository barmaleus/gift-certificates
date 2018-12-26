**Server application for Gift Certificates system with Certificate and Tag entities.**

System exposes REST API:
- CRD operations for Tag
- CRUD operations for Certificate, search by part of name/description and by tag

Database implemented in docker (postgres and pgAdmin).

Custom Connection Pool.

Testing with JUnit & Mockito

Repository is tested in embedded database.

Multi-module project

JSON includes HATEOAS links

There are no:
- Sprig Boot
- JPA/Hibernate
- Lombok
