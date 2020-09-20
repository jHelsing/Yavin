CREATE DATABASE keycloak;

-- Create a user for keycloak
CREATE USER keyUser WITH PASSWORD 'hp,YhV4:#+V"D!HX';
GRANT ALL PRIVELEGES ON DATABASE keycloak TO keyUser;