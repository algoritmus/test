package com.testcomplete.bg;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;


public class GenerateHash {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static String generateHash(String ai,String ui){
		try
		{
		  Long clienttime = System.currentTimeMillis()/1000;; //Clienttime
		  String activationid =  ai; //ActivationId
		  String uuid = ui; //UniqueDeviceId
		  String key = uuid;
		  String value = uuid + activationid + clienttime; //concatenate K, A och T

		  // Get an hmac_sha1 key from the raw key bytes             
		  byte[] keyBytes = key.getBytes();                        
		  SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

		  // Get an hmac_sha1 Mac instance and initialize with the signing key             
		  Mac mac = Mac.getInstance("HmacSHA1");             
		  mac.init(signingKey);

		  // Compute the hmac on input data bytes             
		  byte[] rawHmac = mac.doFinal(value.getBytes());          
		    
		  // Convert raw bytes to Hex             
		  byte[] hexBytes = new Hex().encode(rawHmac);    
		        
		  //  Covert array of Hex bytes to a String             
		  return new String(hexBytes, "UTF-8");
		  
		}
		catch(Exception e)
		{
		  System.out.println("Fel uppstod vid hasning: " + e.getMessage());
		}
		return "";

	}

}
