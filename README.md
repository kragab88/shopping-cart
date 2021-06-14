# Shopping Cart Challenge
Shopping cart is a spring-boot rest APIs provide services:
* Authontication
* List available products
* Create a cart for logged in user
* Display cart details
* Add product to a cart
* Remove product from cart


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

You can run the application using docker compose directly with the below command:

    docker-compose up -d 
Two containsers should be created one for shopping-cart application and another for postgresql database.
The swagger ui should be accessble through [http://localhost:8080].

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
predefinded users added with hashed password in cart_user database table using flyway scripts.
|**Username**|**Password**  |
|--|--|
|user1  | password |
|user2  | password |



#### Token Controller
Basic Authontication service (auth) created to provide JWT token to the authonticated user.
API consumer should provide username and password encoded to request header. the API autonticate that user and provide a valid JWT token to be used for future calls.

    curl --user user1:password http://localhost:8080/v1/api/auth

URL: http://localhost:8080/v1/api/auth
request header contains header 

> Authorization Basic dXNlcjE6cGFzc3dvcmQ=


### Shopping Cart logic 
#### User database tables


