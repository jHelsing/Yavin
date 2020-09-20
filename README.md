# Yavin

## ToDo
- Add authentication including the additionally required services.
  https://spring.io/guides/gs/securing-web/

- Add HATEOAS support, see further https://spring.io/guides/tutorials/bookmarks/

- Create a documentation service which holds API documentation + error
  descriptions as stated in RFC-7807 *type* member. 
  https://tools.ietf.org/html/rfc7807
  https://spring.io/guides/gs/testing-restdocs/

- Extend the error handling controller for snippets.

- Different database configurations depending on test or production.
  - https://spring.io/guides/gs/accessing-data-mysql/
  - h2 database for tests
  - Don't mind production database yet
- Github Action for running checkstyle, unit tests and integration tests
  - Add integration test config in Gradle for XXXIT
  - Add Github Action for checkstyle
  - Add Github Action for unit tests
  - Add Github Action for integration tests
  - Add JaCoCo test coverage to Gradle
  - Add Github Action for SonarCloud

# Links
- https://spring.io/guides/gs/testing-restdocs/
- https://spring.io/guides/gs/rest-hateoas/
- https://spring.io/guides/tutorials/bookmarks/
- https://spring.io/guides/gs/securing-web/
- https://www.baeldung.com/spring-mvc-annotations
- https://medium.com/devops-dudes/securing-spring-boot-rest-apis-with-keycloak-1d760b2004e

# Development environment
## Starting it
1. Change into src/test/docker folder
2. Run docker-compose -f stack.yml -d up

## Available users
After it has read all the data you have the following users available to you:
- user1 -- Password: password1  -- Role: app-user
- user2 -- Password: password1  -- Role: app-admin
- user3 -- Password: password1  -- Role: app-admin, app-user

## Keycloak configuration Yavin
- Secret: b1aedee7-8ee3-4faa-a202-675750140838
- token_endpoint: http://localhost:8002/auth/realms/dev/protocol/openid-connect/token
- client-id: yavin
- grant_type: password

## Export Keycloak dev realm
*The information below is taken from https://hub.docker.com/r/jboss/keycloak/*

This should be done when you've made changes to the keycloak settings for the 
 realm that you want to save to the development environment.
 ```shell script
 docker exec -it ba991dffaf0c /opt/jboss/keycloak/bin/standalone.sh \
 -Djboss.socket.binding.port-offset=100 -Dkeycloak.migration.action=export \
 -Dkeycloak.migration.provider=singleFile \
 -Dkeycloak.migration.realmName=dev \
 -Dkeycloak.migration.usersExportStrategy=REALM_FILE \
 -Dkeycloak.migration.file=/tmp/realm/dev_realm.json
 ```