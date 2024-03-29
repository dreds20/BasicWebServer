#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
     	GRANT ALL PRIVILEGES ON DATABASE server TO server;
     	GRANT ALL ON SCHEMA public TO server;
     	CREATE TABLE pages(id smallserial primary key not null, page varchar unique, verbs varchar, content_type varchar, path varchar);
     	INSERT INTO pages(page, verbs, content_type, path)
     	VALUES ('/', 'GET', 'text/html', 'pages/index.html');
EOSQL