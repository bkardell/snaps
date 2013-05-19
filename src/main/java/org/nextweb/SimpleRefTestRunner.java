package org.nextweb;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class SimpleRefTestRunner {
    private static WebDriver driver = new FirefoxDriver();

    public static byte[] getScreenshot(String url, int delay) throws Exception {
        System.out.println("...fetching " + url);
        driver.get(url);
        Thread.sleep(delay);
        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        return FileUtils.readFileToByteArray(srcFile);

    }


    public static void main(String[] args) throws Exception{
        if(args.length == 0){
            System.out.println("Provide a properties file ...");
        }else{
            List passing = new ArrayList();
            List failing = new ArrayList();
            int delay = 1000;
            if(System.getenv("delay") != null){
                delay = Integer.parseInt(System.getenv("delay"));
            }

            String source = FileUtils.readFileToString(new File(args[0]));
            JSONArray entries = new JSONArray(source);
            for(int i = 0;i < entries.length(); i++){
                JSONObject o = (JSONObject) entries.get(i);
                String test = "... test undefined...";
                try{
                    test = o.get("test").toString();
                    String expected = test.replace(".html", "-expected.html");
                    if(o.has("expected")){
                        expected = o.get("expected").toString();
                    }
                    byte[] a = getScreenshot(test , delay);
                    byte[] b = getScreenshot(expected, delay);
                    boolean areEqual = ArrayUtils.isEquals(a,b);
                    if(areEqual){
                        passing.add(test);
                    }else{
                        failing.add(test);
                    }

                } catch( Exception e ){
                    failing.add(test + "..." + e.getMessage());
                }
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
        System.exit(0);
    }

}