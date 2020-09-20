# Development environment
## Starting it
1. Change into src/test/docker folder
2. Run docker-compose -f stack.yml -d up

## URLS
- Yavin:    localhost:8000
- Database: localhost:8001
- Keycloak: localhost:8002

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
*The information below is from [Keycloak on Docker Hub](https://hub.docker.com/r/jboss/keycloak/*).

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

## Information on Keycloak integration
The following [guide](https://medium.com/devops-dudes/securing-spring-boot-rest-apis-with-keycloak-1d760b2004e "A guide at Medium by Devops Dudes")
was used during development of the authentication and Keycloak integration.

# ToDo
- [x] Introduce traceability to Snippets and Users
  - [x] Add created and modified date to Snippets
  - [x] Add firstAccess and mostRecentAccess date to users
- [x] Implement the endpoints that we have now, so that they are correct and 
  only show what they should.
- [] Add HATEOAS support, see further 
  - [] https://spring.io/guides/gs/rest-hateoas/
  - [] https://spring.io/guides/tutorials/bookmarks/
- [] Sort out handling of JSON when creating and updating snippets
- [] Create a documentation service which holds API documentation + error
  descriptions as stated in RFC-7807 *type* member. 
  https://tools.ietf.org/html/rfc7807
  https://spring.io/guides/gs/testing-restdocs/
- [] Extend the error handling controller for snippets.
- [] Add support for public/private snippets
  - [] introduce the variable in the snippet object
  - [] Add a list endpoint of public snippets
- [] Introduce logging
- [] Different database configurations depending on test or production.
  - [] https://spring.io/guides/gs/accessing-data-mysql/
  - [] h2 database for tests
  - [] Don't mind production database yet
- [] Introduce tests
- [] Github Action for running checkstyle, unit tests and integration tests
  - [] Add integration test config in Gradle for XXXIT
  - [] Add Github Action for checkstyle
  - [] Add Github Action for unit tests
  - [] Add Github Action for integration tests
  - [] Add JaCoCo test coverage to Gradle
  - [] Add Github Action for SonarCloud

# Coding guidelines
- Authorization & Roles: Most users should be able to access everything and will have the role *user*.
If it is something that only administrators need to access then it should be
required the role *admin*.

# Links
- [MVC Annotations for Spring](https://www.baeldung.com/spring-mvc-annotations)
- [Markdown Cheatsheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)