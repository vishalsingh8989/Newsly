package com.kepler.news.newsly.helper;

import android.util.ArrayMap;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by vishaljasrotia on 29/05/17.
 */

public class Common {


    public static String SOURCEURL                          ="sourceUrl" ;
    public static String PUBLISHEDAT                        = "publishedat";
    public static String PREFERENCES                        = "newslyPreferences";

    public static String  DESCRIPTION                       = "description";
    public static String  TITLE                             = "title";
    public static String  AUTHOR                            = "author";
    public static String  SOURCENAME                        = "sourceName";
    public static String  IMAGEURL                          = "urltoimage";
    public static String  ADDTIME                           = "addtime";
    public static String  CATEGORY                          = "category";
    public static String  URL                               = "url";




    public static String chipScienceAndNatureSelected      = "chipScienceAndNatureSelected";

    public static String chipPolitics                      = "chipPolitics";


    public static String MUSIC                      = "music";




    public static String english                       = "english";
    public static String german                        = "german";
    public static String french                        = "french";
    public static String italian                       = "italian";



    public static String usa                         = "usa";
    public static String uk                          = "uk";
    public static String india                       = "india";
    public static String australia                   = "australia";
    public static String canada                      = "canada";
    public static String france                      = "france";
    public static String italy                       = "italy";
    public static String germany                     = "germany";


    public static final LinkedHashMap<String, String> myMap = createMap();
    public static LinkedHashMap<String, String> createMap()
    {
        LinkedHashMap<String,String> myMap = new LinkedHashMap<String,String>();
        myMap.put("USA Today", "USA Today");
        myMap.put("The New York Times", "The New York Times");
        myMap.put("The Guardian", "The Guardian");
        myMap.put("The Wall Street Journal", "The Wall Street Journal");
        myMap.put("Los Angeles Times", "Los Angeles Times");
        myMap.put("The Daily Telegraph", "The Daily Telegraph");
        return myMap;
    }


    public static LinkedHashMap<String, Integer> createStartMap()
    {
        LinkedHashMap<String,Integer> myMap = new LinkedHashMap<String,Integer>();
        myMap.put("USA Today", 0);
        myMap.put("The New York Times", 0);
        myMap.put("The Guardian", 0);
        myMap.put("The Wall Street Journal", 0);
        myMap.put("Los Angeles Times", 0);
        myMap.put("The Daily Telegraph", 0);
        return myMap;
    }
//    { "USA Today", "The New York Times","The Guardian","The Wall Street Journal","Washington Post","Los Angeles Times",
//
//            "The Daily Telegraph"
//    };







}
