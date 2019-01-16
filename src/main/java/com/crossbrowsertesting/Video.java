package com.crossbrowsertesting;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author CBT
 */
public class Video {
    String hash;
    String testId;
    JsonNode info;
    
    public Video(String hash, String testId) throws UnirestException {
        this.hash = hash;
        this.testId = testId;
        this.getInfo();
    }
    
    private void getInfo() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("http://crossbrowsertesting.com/api/v3/selenium/{seleniumTestId}/videos/{videoHash}")
                .basicAuth(Builders.username, Builders.authkey)
                .routeParam("seleniumTestId", this.testId)
                .routeParam("videoHash", this.hash)
                .asJson();
        this.info = response.getBody();
    }
    
    public void stopRecording() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.delete("http://crossbrowsertesting.com/api/v3/selenium/{seleniumTestId}/videos/{videoHash}")
                .basicAuth(Builders.username, Builders.authkey)
                .routeParam("seleniumTestId", this.testId)
                .routeParam("videoHash", this.hash)
                .asJson();
    }
    
    public void setDescription(String description) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.put("http://crossbrowsertesting.com/api/v3/selenium/{seleniumTestId}/videos/{videoHash}")
                .basicAuth(Builders.username, Builders.authkey)
                .routeParam("seleniumTestId", this.testId)
                .routeParam("videoHash", this.hash)
                .field("description", description)
                .asJson();
    }
    
    public void saveLocally(String location) throws Exception{
        String video_url = this.info.getObject().getString("video");
        //turn into URL
        URL url = new URL(video_url);
        File file = new File(location); 
        VideoThread download = new VideoThread(url, file);
        download.start();
    }
}
    
class VideoThread extends Thread {
    private URL video_url;
    private File video_file;
    private Thread t;
    VideoThread(URL video_urlParam, File video_fileParam) {
        this.video_url = video_urlParam;
        this.video_file = video_fileParam;
    }
    @Override
    public void run() {
        int iteration = 1;
        int timeout = 20;
        while(iteration < timeout){ // give 20 trys to find the video. 1 min total
            try {
                try{
                    Thread.sleep(3000); //if you attempt to save DIRECTLY after taking picture/video amazon doesn't see it yet.
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
                FileUtils.copyURLToFile(this.video_url, this.video_file);
                break;
            } catch (IOException ex) {
                iteration = iteration + 1;
                //Logger.getLogger(SnapThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void start() {
        System.out.println("Downloading video from " + video_url.toString());
        t = new Thread(this);
        t.run();
    }
}
