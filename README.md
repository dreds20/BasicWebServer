# BasicWebServer
An experiment to create a basic webserver as a personal project.

## Starting the web server
Docker compose has been used to configure a postgresql database and the Web Server on separate containers. With Docker
installed simply run the command `docker compose up` from the root directory of the project.  Opening a browser to `http://localhost`
will load the index.html page.

If you wish to add more pages to the web server, new records can be added to the postgresql database, e.g.
```sql
INSERT INTO pages(page, verbs, content_type, path)
VALUES ('/', 'GET', 'text/html', 'pages/index.html');
```
Where the fields represent the following:
- page: This will be the location after the host in the request that is being looked up, e.g. `/info`.
- verbs: A comma-delimited list of the verbs the page will accept, note the only behaviour this triggers currently is
    to return a 405 method not allowed response.
- content_type: Provides the header value Content-Type for the server response.
- path: The path to the page resource that will be loaded for the body of the response. By default an absolute path will be constructed
    from the root directory of the project to point at this resource, but the configuration value `pages.basePath` that can be updated in
    the properties file `/src/main/resources/config.properties`, or populating the environment variable `PAGES_BASEPATH`, will combine the
    two and create a URI to perform the resource download.

On top of a new entry to the database, a new page corresponding to the path should be created, this will be loaded as the body.






