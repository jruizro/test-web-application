# Lightweight HTTP Server
The **Lightweight HTTPServer** is an application server building without frameworks or J2EE Servlet that implements:
- Login & pages navigation with user-role access 
- REST WebService to READ, ADD, DELETE and UPDATE Users and roles

# Getting Started

1. **Lightweight HTTPServer** is a Java application developed with JDK 1.8
2. **Lightweight HTTPServer** is built using Gradle. Configuration file is `build.gradle` at root folder
3. Source code is in Git repository 
 To clone execute `git clone --branch master https://github.com/jruizro/test-web-application.git` into a local folder
4. Run `gradle jar` will build jar with all dependencies included
    the application distribution archive can be found `/build/libs` directory
6. Run the application from command line `java -jar /build/libs/test-web-application-1.0.jar`
    * The application has one optional argument indicating the listening port for the application, by default is **8080**
    EJ: `java -jar /build/libs/test-web-application-1.0.jar 2222`
  
7. After the application is running, the web application should be available in [http://localhost:8080/](http://localhost:8080/), if you started it without optional argument
    And REST WebService published at [http://localhost:8080/users](http://localhost:8080/users)

# The Lightweight HTTPServer

This application is a web page and web service server build with [HttpServer](https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html) API.
Any servlet container component is used.

## Initialization

The application has four roles: **ADMIN**, **PAGE_1**, **PAGE_2**, **PAGE_3**.
The Role Policy Access Configuration for pages is:
- page_1.html - access with PAGE_1
- page_2.html - access with PAGE_2
- page_3.html - access with PAGE_3
ADMIN role can access any web page.

Configuration of USERS - ROLES is defined in `users.properties` file at `/resources` folder.
 Here is actual configuration

// Usernames List
users.names.list=admin,user1,user2,user3,noadmin

admin.role=ADMIN
admin.pass=admin

page1user.role=PAGE_1
page1user.pass=12345

page2user.role=PAGE_2
page2user.pass=12345

page3user.role=PAGE_3
page3user.pass=12345

noadmin.role=PAGE_1,PAGE_2,PAGE_3
noadmin.pass=noadmin

## Configuration

The **Lightweight HTTPServer** manages contexts:
    * `/` context for the web page navigation
    * `/users` context for the REST user resources

Both contexts needs permisions to access:
    * Web Navigation has a `Login form`: user and password 
    * REST WebService with `HTTP Basic Authentication`: user and password

# REST API

**Get user**
----

Get a given user resource

* **URL**

  `/users/{username}`
  
  **Example**
  `/users/admin`

* **Method:**
  
  `GET`

* **Curl Call Example**

    curl -v -X GET -u admin:admin --header "Accept: application/json" http://localhost:8080/users/admin

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{"username":"admin","roles":"[{\"rol\":\"ADMIN\"}]"}`
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** 

  * **Code:** 404 Not found <br />
    **Content:**


**Create users**
----

Create a new user with username, password and separated comma list of roles
    
* **URI**

  `/users`

* **Method:**
  
  `POST`
   
* **Curl Call Example**

    curl -v -X POST -u admin:admin --header "Accept: application/json" --data "username=newone&password=12345&roles=PAGE_1,PAGE_2" http://localhost:8080/users/

* **Success Response:**
  
  * **Code:** 201 <br />
    **Content:** `{"username":"newone","roles":"[{\"rol\":\"PAGE_1\"},{\"rol\":\"PAGE_2\"}]"}`
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** `ERROR: El usuario ya existe` o `ERROR: Falta parametro obligatorio`

  * **Code:** 401 Unauthorized <br />
    **Content:** 

**Update users**
----

Update roles of a given user resource

* **URI**

  `/users/{username}`
  
  **Example**
  
  `/users/newone`

* **Method:**
  
  `PUT`
 
*  **Curl Call Example**

    curl -v -X PUT -u admin:admin --header "Accept: application/json" --data "roles=PAGE_3" http://localhost:8080/users/newone

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{"username":"newone","roles":"[{\"rol\":\"PAGE_3\"}]"}`
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** `ERROR: Falta parametro obligatorio`

  * **Code:** 401 Unauthorized <br />
    **Content:** 
  
  * **Code:** 404 Not found <br />
      **Content:** 404 Not Found  
    
**Delete users**
----

Delete a given user resource

* **URL**

  `/users/{username}`
  
  **Example**
  `/users/user2`

* **Method:**
  
  `DELETE`
 
*  **Curl Call Example**

    curl -v -X DELETE -u admin:admin --header "Accept: application/json" http://localhost:8080/users/newone

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{"username":"newone","roles":"[{\"rol\":\"PAGE_3\"}]"}`
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** 

  * **Code:** 403 Forbidden <br />
    **Content:** 

  * **Code:** 404 Not Found <br />
    **Content:**
      
    
**REST Response Content-Type in different formats**

REST Web Service is configured to response in multiple formats according `Accept` Header send by client:
 * `application/json` then response will be delivered in JSON format
 * `application/xml` or `application/xhtml+xm` then response will be delivered in XML format
 * `text/html` or `text/plain` then response will be delivered in Plain Text format
default format is Plain Text

