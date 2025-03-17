package model.library;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.lang.StringBuilder;



public class User extends LibraryModel {
	private String username; 
	private String password;
	
	public User(String username, String password) {
		this.username = username; 
		this.password = password;
		writeFile();
	}
	
	private void writeFile() {
		try {
			FileWriter fileWriter = new FileWriter(new File("data/login"), true);
			String salted = salt(password);
			String encrypted = encrypt(salted);
			
			fileWriter.write(username + " " + encrypted + "\n");
			fileWriter.close();
		} catch (IOException e) {
			// This can never happen, the file is already created
			System.exit(1);
		} 
	}
	
	// 0 to 93, add the int value of 'a' 64 times
	private String salt(String password) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(password);
		for (int i = 0; i < 64; i++) {
			char randChar = (char)('a' + random.nextInt(93));
			sb.append(String.valueOf(randChar));
		}
		return sb.toString();
	}
	
	private String encrypt(String password) {
		String temp = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			BigInteger encrypted = new BigInteger(md.digest(password.getBytes()));
			
			temp = encrypted.toString(16);
			
		} catch (NoSuchAlgorithmException e) {
			// This can never happen
			System.exit(1);
		}
		
		return temp;
	}
	
}
