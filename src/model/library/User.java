package model.library;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.lang.StringBuilder;
import java.util.Scanner;
import java.io.FileNotFoundException; 



public class User extends LibraryModel {
	
//	public static void main(String[] args) {
//	
//		User user = new User("uname", "password");
//		User user2 = new User("u2", "pass");
//		
//		System.out.println(user.validateLogin("uname", "password"));
//		System.out.println(user2.validateLogin("u2", "password"));
//	}
	
	
	private final String username; 
	private final String password;
	private final static int saltLength = 64;
	
	public User(String username, String password) {
		this.username = username; 
		this.password = salt(encrypt(password));
		writeFile();
	}
	
	private void writeFile() {
		try {
			FileWriter fileWriter = new FileWriter(new File("data/login"), true);			
			fileWriter.write(username + " " + password + "\n");
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
		for (int i = 0; i < User.saltLength; i++) {
			char randChar = (char)('!' + random.nextInt(93));
			sb.append(String.valueOf(randChar));
		}		
		return sb.toString();
	}
	
	private String encrypt(String password) {
		String temp = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			byte[] mDigest = md.digest(password.getBytes());
			
			BigInteger bi = new BigInteger(1, mDigest);
			
			temp = bi.toString(16);
			
		} catch (NoSuchAlgorithmException e) {
			// This can never happen
			System.exit(1);
		}
		
		return temp;
	}
	
	
	// used for 
	public boolean validateLogin(String username, String password) {
		// checks if the user has the same user and pass
		String temp = usernameExist(username);
		temp = temp.substring(0, temp.length() - User.saltLength);
		return temp != null && temp.equals(encrypt(password));
	}
	
	private String usernameExist(String username) {
		// returns the encrypted and salted pass associated with user
		File file = new File("data/login");
		try {
			Scanner scan = new Scanner(file);
			while (scan.hasNext()) {		
				String[] lines = scan.nextLine().split(" ");
				// assumes that there is none of the same username
				if (lines[0].equals(username)) {
					return lines[1];
				}
			}
			scan.close();
		} catch (FileNotFoundException e) {
			// this can never happen
			System.exit(1);
		} 
		return null;
		
	}
	
	
	
	
	
	
	
}