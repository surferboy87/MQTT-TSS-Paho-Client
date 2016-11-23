package net.huraki.test.tss;

import net.huraki.tss.TssHandler;

public class TssTester {

	public static void main(String[] args) {

		TssHandler handler = new TssHandler();
		
		handler.init();
		
		System.out.println(handler.representStore());
		
		handler.createBinding("topic1", "1");
		handler.createBinding("topic2", "2");
		handler.createBinding("topic3", "3");
		handler.createBinding("topic4", "4");
		handler.createBinding("topic5", "5");
		
		System.out.println(handler.representStore());
		
		handler.stop();
		
	}
}
