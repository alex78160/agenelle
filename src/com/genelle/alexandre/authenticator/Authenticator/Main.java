package com.genelle.alexandre.authenticator.Authenticator;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;


// read ~/.google-authenticator
// display counter with dots (30 second refresh)
// jcuff@srv:~/auth/javaauth/jc$ java -classpath ./ Authenticator.Main /home/jcuff/.google_authenticator 


public class Main {
 Timer timer;
 public static void main(String[] args) {
   System.out.println("\nAuthenticator Started!"); 
   System.out.println(":----------------------------:--------:");
   System.out.println(":       Code Wait Time       :  Code  :");
   System.out.println(":----------------------------:--------:");
   Main main = new Main();
   //String inFile=";
   try {
	   /*
   RandomAccessFile raf= new RandomAccessFile(inFile,"r");
   FileReader fileReader = new FileReader(raf.getFD());
   BufferedReader bufReader  = new LineNumberReader(fileReader,65536);
   */
   //main.reminder(/*bufReader.readLine()*/"");
   while(true) {
	   String key=args[0];
	   if (key == null || key.length() == 0) {
	      throw new Exception("invalid key");
	   }
	   String newout = Main.computePin(/*""*/key,null);
	   System.out.println("out = "+newout);
	   Thread.sleep(1000);
   }
   
   
   }
   catch (Exception e){
    e.printStackTrace();
   }
 }
 public void reminder(String secret) {
   timer = new Timer();
   timer.scheduleAtFixedRate(new TimedPin(secret), 0, 1 * 1000);
 }
 int count=1;
 class TimedPin extends TimerTask {
    private String secret; 
    public TimedPin (String secret){
      this.secret=secret;
    }
    String previouscode="";
    public void run() {
        String newout = Main.computePin(secret,null);
        if(previouscode.equals(newout)){
           System.out.print("."); 
        } 
        else {
          if(count<=30){
            for (int i=count+1; i<=30;i++){
              System.out.print("+");
            } 
          }
          System.out.println(": "+ newout + " :");
          count=0;
        }
        previouscode = newout; 
        count++;
       }
   } 
 public static String computePin(String secret, Long counter) {
    if (secret == null || secret.length() == 0) {
      return "Null or empty secret";
    }
    try {
      final byte[] keyBytes = Base32String.decode(secret);
      Mac mac = Mac.getInstance("HMACSHA1");
      mac.init(new SecretKeySpec(keyBytes, ""));
      PasscodeGenerator pcg = new PasscodeGenerator(mac);
      return pcg.generateTimeoutCode();
    } catch (GeneralSecurityException e) {
      return "General security exception";
    } catch (Base32String.DecodingException e) {
    	e.printStackTrace();
      return "Decoding exception";
    }
  }
}
