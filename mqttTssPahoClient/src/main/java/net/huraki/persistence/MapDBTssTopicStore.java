package net.huraki.persistence;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import org.mapdb.DB;
import org.mapdb.Serializer;

/**
 * @author Raphael Huber
 *
 */
public class MapDBTssTopicStore {

	private DB db;
	private ConcurrentMap<String, String> mqttToShortMap;
	private ConcurrentMap<String, String> shortToMqttMap;

	public MapDBTssTopicStore(DB db) {
		this.db = db;
	}

	public void initStore() {
		this.mqttToShortMap = this.db.hashMap("mqttToShortMap", Serializer.STRING, Serializer.STRING).createOrOpen();
        this.shortToMqttMap = this.db.hashMap("shortToMqtt", Serializer.STRING, Serializer.STRING).createOrOpen();
	}
	
	public void storeBinding(String mqttTopic, String shortTopic){
		this.mqttToShortMap.put(mqttTopic, shortTopic);
		this.shortToMqttMap.put(shortTopic, mqttTopic);
	}
	
	public String getShortTopic(String mqttTopic) {
		return this.mqttToShortMap.get(mqttTopic);
	}
	
	public boolean hasShortTopic(String mqttTopic){
		return this.mqttToShortMap.containsKey(mqttTopic);
	}

	public String getMqttTopic(String shortTopic) {
		return this.shortToMqttMap.get(shortTopic);
	}
	
	public boolean hasMqttTopic(String shortTopic){
		return this.shortToMqttMap.containsKey(shortTopic);
	}
	
	public String removeBinding(String topic){
		if(this.mqttToShortMap.containsKey(topic)){
			return this.mqttToShortMap.remove(topic);
		}
		if(this.shortToMqttMap.containsKey(topic)){
			return this.shortToMqttMap.remove(topic);
		}
		return topic + " binding not found";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "";
		for (Entry<String, String> e : this.mqttToShortMap.entrySet()) {
			str += "\t{ mqttTopic=" + e.getKey() + " ; " + "shortTopic=" + e.getValue() + " }\n";
		}
		return "MapDBTssTopicStore: "
		+ "\nContains " + this.mqttToShortMap.size() + " bindings\n"
		+ "[\n" + str + "\n]";
	}

	
	
	
}
