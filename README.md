kafka producer and consumer example in scala and java

This is tested locally with 
```s
  Gradle 2.0, 
  Java 1.8.0_65
  Scala 2.11.7
  Zookeeper 3.4.6
  Kafka 2.10-0.9.0.0
  Spark 1.6.0       : https://jaceklaskowski.gitbooks.io/mastering-apache-spark/content/spark-building-from-sources.html
```
### start zookeeper
if you have installed zookeeper, start it, or
run the command:
``` sh
bin/zkServer.sh start conf/zoo.cfg
```

### start kafka with default configuration
``` sh
> bin/kafka-server-start.sh config/server.properties
```

### create a topic
``` sh
> bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 10 --topic test_topic
```

### Download and package this example
``` sh
> git clone https://github.com/iamon3/as_spark_kafka_gradle.git
> cd as_spark_kafka_gradle
> gradle clean build
```

it will package compiled classes and its dependencies into a jar.

### cd into jar location
``` sh
> cd build/lib/
```

### run the consumer
This example contains two consumers written in Java and in scala.
You can run this for java:
``` sh
> java -cp kafka_example-0.1.0-SNAPSHOT.jar com.colobu.kafka.ConsumerExample localhost:2181 group1 test_topic 10 0
```

or this for scala:
``` sh
> java -cp kafka_example-0.1.0-SNAPSHOT.jar com.colobu.kafka.ScalaConsumerExample localhost:2181 group1 test_topic 10 0
```

Stop the consumer Ctrl+C


### Deploy and run the consumer in Spark
``` sh
> cp kafka_example-0.1.0-SNAPSHOT.jar ${SPARK_INSTALLATION_DIR}/bin
> cd ${SPARK_INSTALLATION_DIR}/bin
> sh spark-submit --class com.colobu.kafka.ScalaConsumerExample \
> --master local[8] \
> kafka_example-0.1.0-SNAPSH.jar localhost:2181 group1 test_topic 10 0 group1 test_topic 10 0
``` 

Push messages to Kafka
``` sh
> ${KAFKA_INSTALLTION_DIR}/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test_topic 
> Hi
> Hello
> What's Up.
```

These messages will be consumed and will be displayed on spark terminal.

### run the producer
This example also contains two producers written in Java and in scala.
you can run this for java:
``` sh
> java -cp kafka_example-0.1.0-SNAPSHOT.jar com.colobu.kafka.ProducerExample 10000 colobu localhost:9092
```
or this for scala
``` sh
> java -cp kafka_example-0.1.0-SNAPSHOT.jar com.colobu.kafka.ScalaProducerExample 10000 colobu localhost:9092
```
