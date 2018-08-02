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
 * @author brianna
 */
public class AutomatedTest {
    
    String testId;
    
    public AutomatedTest(String testId) { //may change to int
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
        HttpResponse<JsonNode> response = Unirest.get(Builders.api + this.testId + "/snapshots")
                .basicAuth(Builders.username, Builders.authkey)
                .asJson();
        JSONObject results = response.getBody().getObject();
        JSONArray results2 = results.getJSONArray("");
        return snaps;
    }
    //saveAllSnapshots
    //startRecordingVideo
    //getVideos
    //saveAllVideos
    
    
    
    
    private void makeRequest(String requestMethod, String payload, String baseUrl) {
	URL url;
	String auth = "";

        if (Builders.username != null && Builders.authkey != null) {
            auth = "Basic " + Base64.encodeBase64String((Builders.username + ":" + Builders.authkey).getBytes());
        }
        try {
            url = new URL(baseUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", auth);
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(payload);
            osw.flush();
            osw.close();
            conn.getResponseMessage();
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
	}
}
