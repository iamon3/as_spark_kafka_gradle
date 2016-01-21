package com.colobu.kafka;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumerExample {
	private final ConsumerConnector consumer;
	private final String topic;
	private ExecutorService executor;
	private long delay;

	/**
	 * 
	 * @param zookeeper zookeeper
	 * @param groupId
	 * @param topic topic
	 */
	public ConsumerExample(String zookeeper, String groupId, String topic, long delay) {
		consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig(zookeeper, groupId));
		this.topic = topic;
		this.delay = delay;
	}

	public void shutdown() {
		if (consumer != null)
			consumer.shutdown();
		if (executor != null)
			executor.shutdown();
	}

	/**
	 * consumer
	 * 
	 * @param numThreads 
	 */
	public void run(int numThreads) {
		
		//topic, thread kafkaStream
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(numThreads));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

		//
		executor = Executors.newFixedThreadPool(numThreads);
		int threadNumber = 0;
		for (final KafkaStream<byte[], byte[]> stream : streams) {
			executor.submit(new ConsumerTest(consumer, stream, threadNumber, delay));
			threadNumber++;
		}
	}

	/**
	 * consumer.
	 * 
	 * @param zookeeper zookeeper
	 * @param groupId
	 * @return
	 */
	private static ConsumerConfig createConsumerConfig(String zookeeper, String groupId) {
		Properties props = new Properties();
		props.put("zookeeper.connect", zookeeper);
		props.put("auto.offset.reset", "largest");
		props.put("group.id", groupId);
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		//props.put("auto.commit.enable", "false");
		
		return new ConsumerConfig(props);
	}

	public static void main(String[] args) throws InterruptedException {
		String zooKeeper = args[0];
		String groupId = args[1];
		String topic = args[2];
		int threads = Integer.parseInt(args[3]);
		long delay = Long.parseLong(args[4]);
		ConsumerExample example = new ConsumerExample(zooKeeper, groupId, topic,delay);
		example.run(threads);

		Thread.sleep(24*60*60*1000);
		
		example.shutdown();
	}
}
