package net.huraki.tss;

import net.huraki.persistence.MapDBPersistence;
import net.huraki.persistence.MapDBTssTopicStore;

public class TssHandler {
	
	private MapDBPersistence mapStorage;
	private MapDBTssTopicStore tssTopicStore;
	
	public TssHandler(){
		
	}
	
	public void init(){
		this.mapStorage = new MapDBPersistence();
		this.mapStorage.initStore();
		this.tssTopicStore = this.mapStorage.tssTopicStore();
		System.out.println("TSS handler started");
	}
	
	public void createBinding(String mqttTopic, String shortTopic){
		this.tssTopicStore.storeBinding(mqttTopic, shortTopic);
	}
	
	public String translateBeforeSend(String mqttTopic){
		if(this.tssTopicStore.hasShortTopic(mqttTopic)){
			return this.tssTopicStore.getShortTopic(mqttTopic);
		}
		return mqttTopic;
	}
	
	public String translateBeforeCallback(String shortTopic){
		if(this.tssTopicStore.hasMqttTopic(shortTopic)){
			return this.tssTopicStore.getMqttTopic(shortTopic);
		}
		return shortTopic;
	}
	
	public void removeBinding(String topic){
		this.tssTopicStore.removeBinding(topic);
	}
	
	public String representStore(){
		return this.tssTopicStore.toString();
	}
	
	public void stop(){
		this.mapStorage.close();
		System.out.println("TSS handler stopped");
	}
	

}