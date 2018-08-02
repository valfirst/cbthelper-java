/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import com.google.gson.Gson;
import org.json.JSONObject;
/**
 *
 * @author brianna
 */
public class Main {
    public static void main(String[] args) throws UnirestException {
        
        HttpResponse<JsonNode> response = Unirest.get(Builders.api + "13085041" + "/snapshots")
                .basicAuth("test", "test")
                .asJson();
        JSONObject results = response.getBody().getObject();
        System.out.println(results);
        System.out.println(results.length());
    }
}
