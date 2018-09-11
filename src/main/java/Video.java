
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

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
        //implement async like in repo
    }
}
