/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author brianna
 */
/**
 * Builder for generating selenium capabilities
 */
public class CapsBuilder {
    String gUsername = "";
    String gAuthkey  = "";
    String api = "https://crossbrowsertesting.com/api/v3/selenium/";
    String hub = "http://hub.crossbrowsertesting.com:80/wd/hub";

    String platform, browser, width, height, name, version, resolution;
    boolean recordVideo, recordNetwork;
    
    public CapsBuilder() { //constructor
        //finish this
    }
    /**
     * Used to generate the capabilites using any options the user specifies
     * 
     * @return TBD
     */
    public String build() { //probably going to change return type
        return "";
    }
    /**
     * Sets the browser the user wants to use. The string will be compared against the ‘name’ and ‘api_name’ properties returned from the selenium api.
     * @param browser a string specifying the browser
     */
    public void withBrowser(String browser) {
        this.browser = browser;
    }
    /**
     * Sets the name that will appear in the web app
     * @param name a string specifying the desired test name
     */
    public void withName(String name) {
        this.name = name;
    }
    /**
     * Sets the platform (OS) the user wants to use. The string will be compared against the ‘name’ and ‘api_name’ properties returned from the selenium api
     * @param platform  a string specifying the platform (eg. Windows 7, Mac 10.13)
     */
    public void withPlatform(String platform) {
        this.platform = platform;
    }
    /**
     * Sets the build name in the web app
     * @param build a string specifying the build
     */
    public void withBuild(String build) {
        this.version = build;
    }
    /**
     * Sets whether or not video will be recorded
     * @param video a boolean specifying whether or not to record video
     */
    public void withRecordVideo(boolean video) {
        this.recordVideo = video;
    }
    /**
     * Sets whether or not the network traffic will be recorded
     * @param network a boolean specifying whether or not to record network traffic
     */
    public void withRecordNetwork(boolean network) {
        this.recordNetwork = network;
    }
    /**
     * Sets the screen size for the test
     * @param width
     * @param height 
     */
    public void withResolution(String width, String height) {
        this.resolution = width + "x" + height;
    }
    
    public String bestOption(String options, String target) {
        return "a";
    }
    
    public String choose() {
        return "b";
    }
}
