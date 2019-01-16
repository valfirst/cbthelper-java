package com.crossbrowsertesting;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author brianna/patrick
 */
/**
 * Builder for generating selenium capabilities
 */
public class CapsBuilder {
    String gUsername;
    String gAuthkey;
    String api = "https://crossbrowsertesting.com/api/v3/selenium/";
    String hub = "http://hub.crossbrowsertesting.com:80/wd/hub";

    public DesiredCapabilities caps = new DesiredCapabilities();
    JSONArray capsData;
    String platform=null, browser=null, width=null, height=null, name=null, version=null, resolution=null;
    boolean recordVideo=false, recordNetwork=false;
    
    public CapsBuilder(String username, String authkey) throws UnirestException { //constructor
        gUsername = username;
        gAuthkey = authkey;
        HttpResponse<JsonNode> response = Unirest.get("http://crossbrowsertesting.com/api/v3/selenium/browsers")
            .basicAuth(gUsername, gAuthkey)
            .asJson();
        this.capsData = response.getBody().getArray();

    }

    /**
     * Used to generate the capabilites using any options the user specifies
     * 
     * @return a desired capabilites object
     */
    public DesiredCapabilities build() { //probably going to change return type
        return this.choose();
    }
    /**
     * Sets the browser the user wants to use. The string will be compared against the ‘name’ and ‘api_name’ properties returned from the selenium api.
     * @param browser a string specifying the browser
     */
    public CapsBuilder withBrowser(String browser) {
        this.browser = browser;
        return this;
    }
    /**
     * Sets the name that will appear in the web app
     * @param name a string specifying the desired test name
     */
    public CapsBuilder withName(String name) {
        this.name = name;
        return this;
    }
    /**
     * Sets the platform (OS) the user wants to use. The string will be compared against the ‘name’ and ‘api_name’ properties returned from the selenium api
     * @param platform  a string specifying the platform (eg. Windows 7, Mac 10.13)
     */
    public CapsBuilder withPlatform(String platform) {
        this.platform = platform;
        return this;
    }
    /**
     * Sets the build name in the web app
     * @param build a string specifying the build
     */
    public CapsBuilder withBuild(String build) {
        this.version = build;
        return this;
    }
    /**
     * Sets whether or not video will be recorded
     * @param video a boolean specifying whether or not to record video
     */
    public CapsBuilder withRecordVideo(boolean video) {
        this.recordVideo = video;
        return this;
    }
    /**
     * Sets whether or not the network traffic will be recorded
     * @param network a boolean specifying whether or not to record network traffic
     */
    public CapsBuilder withRecordNetwork(boolean network) {
        this.recordNetwork = network;
        return this;
    }
    /**
     * Sets the screen size for the test
     * @param width
     * @param height 
     */
    public CapsBuilder withResolution(String width, String height) {
        this.resolution = width + "x" + height;
        return this;
    }

    public JSONObject bestOption(JSONArray options, String target){
        target = target.toLowerCase();
        //https://stackoverflow.com/questions/33215539/foreach-with-jsonarray-and-jsonobject
        //Apparently, org.json.simple.JSONArray implements a raw Iterator. This means that each element is considered to be an Object. You can try to cast:
        for (Object obj : options){
            if(obj instanceof JSONObject){
                JSONObject jsonobj = (JSONObject) obj;
                String name = jsonobj.getString("name").toLowerCase();
                String apiName = jsonobj.getString("api_name").toLowerCase();
                if(target.equals(name) || target.equals(apiName))
                    return jsonobj;
            }
        }
        return null;
    }

    public JSONObject bestBrowserNoPlatform(JSONArray options, String target) {
        target = target.toLowerCase();
        System.out.println(target);
        for(int i=0; i < options.length(); i++){
            JSONObject obj = options.getJSONObject(i);
            JSONArray browsers = obj.getJSONArray("browsers");
            for(int j =0; j < browsers.length(); j++){
                JSONObject browobj = browsers.getJSONObject(j);
                String name = browobj.getString("name").toLowerCase();
                String apiName = browobj.getString("api_name").toLowerCase();
                if (target.equals(name) || target.equals(apiName)){
                    return browobj;
                }
            }
        }
        return null;
    }
    
    public DesiredCapabilities choose() {
        JSONObject platformDetails  = null;
        if (this.platform != null){
            platformDetails = this.bestOption(this.capsData, this.platform);
            if (platformDetails != null){
                JSONObject platformCap = platformDetails.getJSONObject("caps");
                //for each caps but that capability in (mobiles has 3 caps where as desktop has 1)
                for(int i = 0; i < platformCap.names().length(); i++){
                    String title = platformCap.names().getString(i);
                    String add = platformCap.getString(title);
                    this.caps.setCapability(title, add);
                }
            }
        }
        JSONObject browserCap;
        if(this.browser != null){
            if (platformDetails != null){
                JSONArray platformBrowsers = platformDetails.getJSONArray("browsers");
                browserCap = this.bestOption(platformBrowsers,this.browser);
            }
            else {
                browserCap = this.bestBrowserNoPlatform(this.capsData, this.browser);
            }
            if (browserCap != null){
                browserCap = browserCap.getJSONObject("caps");
                //for each caps
                for(int i = 0; i < browserCap.names().length(); i++){
                    String title = browserCap.names().getString(i);
                    String add = browserCap.getString(title);
                    this.caps.setCapability(title, add);
                }
            }
        }

        if (this.width != null && this.height != null ){
            this.caps.setCapability("screenResolution", this.width + "x" + this.height);
        }

        if(this.name != null ){
            this.caps.setCapability("name", this.name);
        }

        if(this.version != null ){
            this.caps.setCapability("build", this.version);
        }

        if(this.recordVideo){
            this.caps.setCapability("record_video", this.version);
        }

        if(this.recordNetwork){
            this.caps.setCapability("record_network", this.version);
        }
        return this.caps;
    }
}









