## User Identity

APIs available:
* `POST /login/generateOTP` - Request body contains a JSON with `name`, `email`, and `phoneNumber`, generates and sends an OTP, valid for two minutes. Has an access rate limiter set as middleware.
* `POST /login/sendOTP` - Request body contains a JSON with `phoneNumber` and `otp` and sends the JWT token generated back in response, valid for an hour.
* `GET /home` - Requires an `Authorization:Bearer <JWT_token>` header, and the user's details are sent back in the response.
* `GET /logout` - Renders the token sent in the header invalid for future use.

### Starting the service

* Create an `application-secrets.properties` in `src/main/resources` with the following:
```bash
spring.jpa.hibernate.ddl-auto=<'create' on the first run, then 'update'>
spring.datasource.url=jdbc:mysql://<DB_HOST>/<DB_NAME>
spring.datasource.username=<DB_USER>
spring.datasource.password=<DB_PASS>
```
* Run `./gradlew bootRun` to start the service on port `8080`.
