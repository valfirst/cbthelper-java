/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
/**
 *
 * @author CBT
 */
public class Snapshot {
    String hash;
    String testId;
    JsonNode info;
    
    public Snapshot(String hash, String testId) throws UnirestException {
        this.hash = hash;
        this.testId = testId;
        this.getInfo();
    }
    
    private void getInfo() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("http://crossbrowsertesting.com/api/v3/selenium/{seleniumTestId}/snapshots/{snapshotHash}")
                .basicAuth(Builders.username, Builders.authkey)
                .routeParam("seleniumTestId", this.testId)
                .routeParam("snapshotHash", this.hash)
                .asJson();
        this.info = response.getBody();
    }
    
    public JsonNode setDescription(String description) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.put("http://crossbrowsertesting.com/api/v3/selenium/{seleniumTestId}/snapshots/{snapshotHash}")
                .basicAuth(Builders.username, Builders.authkey)
                .routeParam("seleniumTestId", this.testId)
                .routeParam("snapshotHash", this.hash)
                .field("description", description)
                .asJson();
        return response.getBody();
    }
    
    public void saveLocally(String location) { //error checking on filepath is important
        String snap_url = this.info.getObject().getString("image");
        //turn into URL
        //SnapThread download = new SnapThread(snap_url, location);
    } 
}

class SnapThread extends Thread {
    private URL snap_url;
    private File snap_file;
    private Thread t;
    SnapThread(URL snap_url, File snap_file) {
        
    }
    @Override
    public void run() {
        try {
            FileUtils.copyURLToFile(this.snap_url, this.snap_file);
        } catch (IOException ex) {
            Logger.getLogger(SnapThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void start() {
        System.out.println("Downloading snapshot from " + snap_url.toString());
        t = new Thread(this);
        t.start();
    }
}