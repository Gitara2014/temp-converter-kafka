# Read Me First

User calls POST convert-service/convert



{"celsius_temperature": 30.4}
 
convert-service generates UUID, inserts it, alongside timestamp as a new entry in some persistence, with PENDING status, and returns
the uuid to the API user

convert-service publishes event to requested_conversions topic, with uuid ,and celsius_temperature inside the message

. convertor takes the message, converts the celsius_temperature to fahrenheit, and publishes result to conversions_successfuL
topic, with the uuid andthe fahrenheit_temperature in the message, or (in case of failure) sends the message to

conversions_failed withthe uuid andthe reason message .

. convert-service takes the message from either of two topics, resolves entry in mongo by uuid and sets the result there, setting status
to SUCCESS or ERROR

. convert-service provides /conversions?uuid=xxxx-yyyy where user can check when the conversion is done


### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)

https://spring.io/guides/gs/accessing-data-mongodb/

JPA and H2

https://spring.io/guides/gs/accessing-data-jpa/#initial
https://github.com/spring-guides/gs-accessing-data-jpa/blob/main/complete/pom.xml
http://localhost:8080/h2-console

KAFKA

https://www.youtube.com/watch?v=jY02MB-sz8I