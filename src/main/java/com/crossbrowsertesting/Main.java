package com.crossbrowsertesting;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mashape.unirest.http.exceptions.UnirestException;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;

/**
 *
 * @author CBT
 */

public class Main {
    static String username = ""; // Your username
    static String authkey = "12345";  // Your authkey
    String testScore = "unset";

    public static void main(String[] args) throws Exception{


    	Builders builder = new Builders();
    	builder.login("your-username", "your-authkey");

      //Build the caps for our driver
      CapsBuilder capsBuilder = new CapsBuilder(builder.username, builder.authkey);
      capsBuilder.withPlatform("Windows 8").withBuild("1.0").withBrowser("ff63").build();
      System.out.println(capsBuilder.caps);

    	RemoteWebDriver driver = new RemoteWebDriver(new URL("http://" + builder.username + ":" + builder.authkey + "@hub.crossbrowsertesting.com:80/wd/hub"),capsBuilder.caps);
      
      //initialize an AutomatedTest object with our selnium session id
      AutomatedTest myTest = new AutomatedTest(driver.getSessionId().toString());

      //start up a video
      Video video = myTest.startRecordingVideo();


      driver.get("http://google.com");

      //take a snapshot of where we currently are
      Snapshot googleSnap = myTest.takeSnapshot();
      googleSnap.setDescription("google.com");
      googleSnap.saveLocally("test/google.png");

      driver.get("http://crossbrowsertesting.com");

      //take a snapshot and set description all at once
      myTest.takeSnapshot("cbt.com");

      //stop our video and set a descripiton
      video.setDescription("google and cbt video");
      video.stopRecording();

      //set a score for our test and end it
      myTest.setScore("pass");
      myTest.stop();

      //save our video
      video.saveLocally("test/myvideo.mp4");

    }
}