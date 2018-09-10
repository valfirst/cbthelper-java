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
    
    public Video(String hash, String testId) {
        this.hash = hash;
        this.testId = testId;
    }
    
    public void getInfo() { //will decide on return type later
        //make request to base/videos/hash
    }
    
    public void stopRecording() {
        //delete to base/videos/hash
    }
    
    public void setDescription() {
        //put to base/videos/hash
    }
    
    public void saveLocally(String location) { //error checking on filepath
        //implement async like in repo
    }
}
