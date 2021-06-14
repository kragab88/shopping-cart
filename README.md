# Shopping Cart Challenge

Shopping cart is a spring-boot rest APIs provide services:
* Authentication
* List available products
* Create a cart for logged in user
* Display cart details
* Add product to a cart
* Remove the product from the cart


## Built With
*	Java 8
*	Spring-boot 2.5
*	Swagger-ui
*	Spring-Security
*	Javax validation
*	Junit 5
*	Mockito
*	Flyway
*	Postgresql
*	Docker


##  Installation


You can run the application using docker-compose directly with the below command:

    docker-compose up -d 
Two containers should be created one for shopping-cart application and another for Postgresql database.
The swagger ui should be accessible through [http://localhost:8080].

## Run test cases

You can run the unit tests and integration test cases using the below maven command

    mvn test

 

## Development Approach
The project has two components one for the shopping cart logic and another security component

### Security Component
Find implementation in .\src\main\java\com\atlavik\shoppingcart\security
 
#### User database tables
![enter image description here](https://github.com/kragab88/shopping-cart/blob/main/img/security_erd.PNG)

**Predefinded users created**

predefined users added with hashed password in cart_user database table using flyway scripts.

|**Username**|**Password**  |
|--|--|
|user1  | password |
|user2  | password |



#### Token Controller
Basic Authentication service (auth) created to provide a JWT token to the authenticated user.
API consumer should provide username and password encoded to request header. the API authenticates that user and provide a valid JWT token to be used for future calls.

    curl --user user1:password http://localhost:8080/v1/api/auth

URL: http://localhost:8080/v1/api/auth
request header contains header 

> Authorization Basic dXNlcjE6cGFzc3dvcmQ=


### Shopping Cart Component

#### Cart database tables
![enter image description here](https://github.com/kragab88/shopping-cart/blob/main/img/security_erd.PNG)

predefined products created using flyway script

    insert into product (category,description,price) values ('ELECTRONICS','Apple phone 12',665.5);
    insert into product (category,description,price) values ('ELECTRONICS','Apple phone 11',333.5);
    insert into product (category,description,price) values ('LAPTOP','MAC AIR',44);

#### REST APIs

REST APIs created as below to manipulate the user cart and cart products.
The cart services secured using Oauth2 jwt authorization the request should contain an authorization header with a valid jwt token

    Authorization Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTYyMzA1MDAxOSwiaWF0IjoxNjIzMDMyMDE5fQ.-nGVqmJo-VgepjxOmnwM1YAGQe0fYk2Dg94wzPT0Qp8PPxSm6-qvnjo2aiVX3EuiXWhMXcHzhHq5Jnqu87PCQg

**Get user cart**

Inquire for cart using the username for the logged-in user.

*The user can only have one cart.*

    curl --location --request GET 'http://localhost:8080/v1/api/carts' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTYyMzA1MDAxOSwiaWF0IjoxNjIzMDMyMDE5fQ.-nGVqmJo-VgepjxOmnwM1YAGQe0fYk2Dg94wzPT0Qp8PPxSm6-qvnjo2aiVX3EuiXWhMXcHzhHq5Jnqu87PCQg'


**Create user cart**

Create a new cart for the logged-in user:

*The user can only have one cart.*

POST   [/v1/api/carts](http://localhost:8080/swagger-ui.html#/operations/cart-controller/addCartUsingPOST)

**Get products for a cart using cartId**

GET  [/v1/api/carts/{cartId}/product](http://localhost:8080/swagger-ui.html#/operations/cart-controller/getCartProductsUsingGET)

**Add product to a cart using cartId and productId**

PUT [/v1/api/carts/{cartId}/product/{productId}](http://localhost:8080/swagger-ui.html#/operations/cart-controller/addCartProductUsingPUT)

**Remove product from a cart using cartId and productId**

DELETE [/v1/api/carts/{cartId}/product/{productId}](http://localhost:8080/swagger-ui.html#/operations/cart-controller/removeCartProductUsingDELETE)

**Get all products**

*The service is available for anonymous user*

GET [/v1/api/products](http://localhost:8080/swagger-ui.html#/operations/product-controller/getAllProductsUsingGET)


