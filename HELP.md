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
* https://stackabuse.com/get-http-post-body-in-spring/
* 
MONGO
- https://spring.io/guides/gs/accessing-data-mongodb/
- https://www.youtube.com/watch?v=ssj0CGxv60k

JPA and H2
- https://spring.io/guides/gs/accessing-data-jpa/#initial
- https://github.com/spring-guides/gs-accessing-data-jpa/blob/main/complete/pom.xml
- http://localhost:8080/h2-console

KAFKA
- org.apache.kafka.clients.requestConversionProducer.KafkaProducer
- https://kafka.apache.org/30/javadoc/org/apache/kafka/clients/requestConversionProducer/KafkaProducer.html
- https://www.confluent.io/wp-content/uploads/confluent-kafka-definitive-guide-complete.pdf
- https://www.youtube.com/watch?v=jY02MB-sz8I

Kowl - A Web UI for Apache Kafka

- https://github.com/cloudhut/kowl
```terminal
RUN KAFKA, RUN KOWL: 
$ sudo docker-compose up 
$ sudo docker run --network=host -p 8080:8080 -e KAFKA_BROKERS=localhost:9092 --name kowl quay.io/cloudhut/kowl:master
$ http://localhost:8080/

RUN APP:
$ mvn clean package
$ java -jar tartget/*.jar

TEST: 
$ curl -X POST -H "Content-Type: application/json" -d '{"temperature": 37.2, "device": "IOT-WINE-FIELD-SENSOR-ABC123567"}' 'http://www.localhost:8585/temperature'

Response: 
{"id":5,"uuid":"1219cbe7-be37-4f13-b074-0893b210ce17","date":"2022-02-13T23:52:18.983756179+01:00","tempCelsius":37.2,"tempFahrenheit":null,"status":"PENDING"}

$ curl 'http://www.localhost:8585/temperature?uuid=1219cbe7-be37-4f13-b074-0893b210ce17' -v
Response:
{"id":5,"uuid":"1219cbe7-be37-4f13-b074-0893b210ce17","date":"2022-02-13T23:52:18.983756+01:00","tempCelsius":37.2,"tempFahrenheit":98.96000000000001,"status":"SUCCESS"}
```

SHUTDOWN: 
```terminal
$ sudo docker-compose down
$ sudo docker stop kowl && sudo docker rm kowl

```

Log: 
- kafka        | [2022-02-08 00:07:47,218] INFO [KafkaServer id=1001] started (kafka.server.KafkaServer)


JAVA
- https://turreta.com/2017/06/26/java-3-ways-to-implement-a-generic-interface/