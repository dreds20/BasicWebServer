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

## Further thoughts
This is a pretty simple implementation, it has no SSL support, it only follows the HTTP 1.1 standard, I've opted for a single file for the response
body, but could have made it return multiple resources, and the demonstration HTML is pretty nasty. I wanted to try and implement this with as core
Java code as possible, except for a few libraries for configuration, immutable objects, builders etc. There are much better implementations that could
be used out of the box, but I wanted a project to explore some fundamentals.

I used test containers for the database unit tests, which was fantastic and would strongly recommend, and have provided fairly comprehensive unit
tests and javadoc.

I came across a nice summary of HTTP requests/responses on this page https://www.jmarshall.com/easy/http/ which I found the link to on stack overflow,
which saved me from having to read through the RFC.

I've tried to implement this in such a way that we can modify the behaviour with new implementations, e.g. a new ConnectionManager could change
the way pages are looked up with the PageManager and the body retrieved via the ContentLoader to create a more simple or complex implementation, 
and the implementation of the ConnectionManager that is being used by default could also have new implementations of the PageManager and ContentLoader
provided to change how requests are looked up and content is served.

This was the first time I've used Immutables too, I've historically used Project Lombok to take away some of the boilerplate requirements of Java and implement
builders, and loggers, etc.


