package com.praveen.Demo_Products.util;

import java.util.Random;
import java.util.UUID;

public class CustomIdGenerator {
	
	private static final  String  CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	public static String generateId() {
		
		Random random = new Random();
		
		StringBuilder builder = new StringBuilder();
		
		for(int i=0; i<6; i++) {
			
			int index = random.nextInt(CHARACTERS.length());
			
		    builder.append(CHARACTERS.charAt(index)).toString();
			
		}
		
		return builder.toString();
	}
	
	public static String generateIdUsingUUID() {
		
		UUID uuid = UUID.randomUUID();
		
		return uuid.toString().substring(0, 6);
		
	}
	
}
