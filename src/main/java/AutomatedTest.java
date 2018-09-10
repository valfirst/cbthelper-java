/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import org.apache.commons.codec.binary.Base64;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author CBT
 */
public class AutomatedTest {
    
    String testId;
    
    public AutomatedTest(String testId) {
        this.testId = testId;
    }
    
    public JsonNode setScore(String score) throws UnirestException { //won't return payload, but do return status for error checking?
        HttpResponse<JsonNode> response = Unirest.put(Builders.api + this.testId)
                .basicAuth(Builders.username, Builders.authkey)
                .field("action","set_score")
                .field("score", score)
                .asJson();
        return response.getBody();
    }
    
    public JsonNode setDescription(String description) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.put(Builders.api + this.testId)
                .basicAuth(Builders.username, Builders.authkey)
                .field("action","set_description")
                .field("description", description)
                .asJson();
        return response.getBody();
    }
    
    public JsonNode stop(boolean score) throws UnirestException {//overloaded method
        HttpResponse<JsonNode> response = Unirest.put(Builders.api + this.testId)
                .basicAuth(Builders.username, Builders.authkey)
                .field("action","set_score")
                .field("score", score)
                .asJson();
        HttpResponse<JsonNode> deletion_response = Unirest.delete(Builders.api + this.testId)
                .basicAuth(Builders.username, Builders.authkey)
                .asJson();
        return response.getBody();
    }
    
    public void stop() throws UnirestException {//overloaded method also add error checking
        HttpResponse<JsonNode> deletion_response = Unirest.delete(Builders.api + this.testId)
                .basicAuth(Builders.username, Builders.authkey)
                .asJson();
    }
    
    public Snapshot takeSnapshot() throws UnirestException {//overloaded method
        HttpResponse<JsonNode> response = Unirest.post(Builders.api + this.testId + "/snapshots")
                .basicAuth(Builders.username, Builders.authkey).
                asJson();
        String hash = (String) response.getBody().getObject().get("hash");
        Snapshot snap = new Snapshot(hash, this.testId);
        return snap;
    }
    
    public Snapshot takeSnapshot(String description) throws UnirestException {//overloaded method
        HttpResponse<JsonNode> response = Unirest.post(Builders.api + this.testId + "/snapshots")
                .basicAuth(Builders.username, Builders.authkey).
                asJson();
        String hash = (String) response.getBody().getObject().get("hash");
        Snapshot snap = new Snapshot(hash, this.testId);
        snap.setDescription(description);
        return snap;
    }
    
    public Snapshot[] getSnapshots() throws UnirestException {
        Snapshot[] snaps;
        HttpResponse<String> response = Unirest.get(Builders.api + this.testId + "/snapshots")
                .basicAuth(Builders.username, Builders.authkey)
                .asString();
        JSONArray results = new JSONArray(response.getBody());
        String hash;
        snaps = new Snapshot[results.length()];
        for(int i=0; i<results.length();i++) {
            hash = results.getJSONObject(i).getString("hash");
            snaps[i] = new Snapshot(hash, this.testId);
        }
        return snaps;
    }

    public Video startRecordingVideo() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post(Builders.api + this.testId + "/videos")
                .basicAuth(Builders.username, Builders.authkey).
                asJson();
        String hash = (String) response.getBody().getObject().get("hash");
        Video video = new Video(hash, this.testId);
        return video;
    }
    
    public Video[] getVideos() throws UnirestException {
        Video[] videos;
        HttpResponse<String> response = Unirest.get(Builders.api + this.testId + "/videos")
                .basicAuth(Builders.username, Builders.authkey)
                .asString();
        JSONArray results = new JSONArray(response.getBody());
        String hash;
        videos = new Video[results.length()];
        for(int i=0; i<results.length();i++) {
            hash = results.getJSONObject(i).getString("hash");
            videos[i] = new Video(hash, this.testId);
        }
        return videos;
    }
    //saveAllSnapshots
    //saveAllVideos
}