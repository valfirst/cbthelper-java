
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
    
    public void setDescription() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.put("http://crossbrowsertesting.com/api/v3/selenium/{seleniumTestId}/videos/{videoHash}")
                .basicAuth(Builders.username, Builders.authkey)
                .routeParam("seleniumTestId", this.testId)
                .routeParam("videoHash", this.hash)
                .asJson();
    }
    
    public void saveLocally(String location) {
        String video_url = this.info.getObject().getString("image");
        //turn into URL
        //SnapThread download = new VideoThread(snap_url, location);
    }
}
    
class VideoThread extends Thread {
    private URL video_url;
    private File video_file;
    private Thread t;
    VideoThread(URL snap_url, File snap_file) {
        
    }
    @Override
    public void run() {
        try {
            FileUtils.copyURLToFile(this.video_url, this.video_file);
        } catch (IOException ex) {
            Logger.getLogger(SnapThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void start() {
        System.out.println("Downloading snapshot from " + video_url.toString());
        t = new Thread(this);
        t.start();
    }
}
