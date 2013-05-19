package org.nextweb;

import com.opera.core.systems.OperaDriver;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Snaps {
    private static WebDriver getDriver(String name){
        if(name.equalsIgnoreCase("firefox")){
            return new FirefoxDriver();
        }else if(name.equalsIgnoreCase("safari")){
            return new SafariDriver();
        }else if(name.equalsIgnoreCase("opera")){
             return new OperaDriver();
        }else if(name.equalsIgnoreCase("chrome")){
            return new ChromeDriver();
        }else if(name.equalsIgnoreCase("msie")){
            return new InternetExplorerDriver();
        }else{
            return null;
        }
    }


    public static byte[] getScreenshot(WebDriver driver, String url, int delay) throws Exception {
        System.out.println("...fetching " + url);
        driver.get(url);
        Thread.sleep(delay);
        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        return FileUtils.readFileToByteArray(srcFile);

    }

    public static void runTestsForBrowser(String browserName, int delay, JSONArray entries ){
        List passing = new ArrayList();
        List failing = new ArrayList();
        WebDriver driver = getDriver( browserName );
        try{
            System.out.println("-----------------------------------"
                    + "\nRunning tests for: " + browserName );


            if(driver == null){
                System.out.println("... no driver found, ending test for this browser");
            }else{
                for(int i = 0;i < entries.length(); i++){
                    JSONObject o = (JSONObject) entries.get(i);
                    String test = "... test undefined...";
                    try{
                        test = o.get("test").toString();
                        String expected = test.replace(".html", "-expected.html");
                        if(o.has("expected")){
                            expected = o.get("expected").toString();
                        }
                        byte[] a = getScreenshot(driver, test , delay);
                        byte[] b = getScreenshot(driver, expected, delay);
                        boolean areEqual = ArrayUtils.isEquals(a, b);
                        if(areEqual){
                            passing.add(test);
                        }else{
                            failing.add(test);
                        }

                    } catch( Exception e ){
                        failing.add(test + "..." + e.getMessage());
                    }
                }

            }
        }catch(Exception e){
            System.out.println(".... Encountered problem, ending tests for this browser." + e.getMessage());
        }
        System.out.println(passing.size() + " of " +
                (failing.size() + passing.size())
                + " passed."
        );
        if(!failing.isEmpty()){
            System.out.println("Failing tests: " + failing);
        }
        driver.close();
    }

    public static void main(String[] args) throws Exception{
        if(args.length == 0){
            System.out.println("Provide a properties file ...");
        }else{
            List browsers = new ArrayList();
            int delay = 1000;
            if(System.getenv("delay") != null){
                delay = Integer.parseInt(System.getenv("delay"));
            }

            String source = FileUtils.readFileToString(new File(args[0]));
            JSONObject config = new JSONObject(source);
            JSONArray jsonBrowsers = (JSONArray) config.get("browsers");
            JSONArray entries =  (JSONArray) config.get("tests");
            for(int i = 0;i < jsonBrowsers.length(); i++){
                runTestsForBrowser(jsonBrowsers.get(i).toString(), delay, entries);
            }

        }
        System.exit(0);
    }

}