package com.crossbrowsertesting;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author brianna
 */
public class Builders {
    public static String username;
    public static String authkey;
    public static String hub;
    public static String api;
    
    public static void login(String login_username, String login_authkey) {
        username = login_username;
        authkey = login_authkey;
        hub = "http://hub.crossbrowsertesting.com:80/wd/hub";
        api = "https://crossbrowsertesting.com/api/v3/selenium/";
    }
}
