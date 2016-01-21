kafka producer and consumer example in scala and java

you can test with local server. Gradle 2.0, Java 1.8.0_65

### start zookeeper
if you have installed zookeeper, start it, or
run the command:
``` sh
bin/zookeeper-server-start.sh config/zookeeper.properties
```

### start kafka with default configuration
``` sh
> bin/kafka-server-start.sh config/server.properties
```

### create a topic
``` sh
> bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 10 --topic test_topic
```

### package this example
``` sh
gradle clean build
```

it will package compiled classes and its dependencies into a jar.

### cd into jar location
``` sh
  cd build/lib/
```

### run the consumer
This example contains two consumers written in Java and in scala.
You can run this for java:
``` sh
java -cp kafka_example-0.1.0-SNAPSHOT.jar com.colobu.kafka.ConsumerExample localhost:2181 group1 test_topic 10 0
```

or this for scala:
``` sh
java -cp kafka_example-0.1.0-SNAPSHOT.jar com.colobu.kafka.ScalaConsumerExample localhost:2181 group1 test_topic 10 0
```

### run the consumer
This example also contains two producers written in Java and in scala.
you can run this for java:
``` sh
java -cp kafka_example-0.1.0-SNAPSHOT.jar com.colobu.kafka.ProducerExample 10000 colobu localhost:9092
```
or this for scala
``` sh
java -cp kafka_example-0.1.0-SNAPSHOT.jar com.colobu.kafka.ScalaProducerExample 10000 colobu localhost:9092
```
