package com.kepler.news.newsly.helper;

import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by vishaljasrotia on 29/05/17.
 */

public class Common {


    public static int ALWAYS                             = 0;
    public static int NEVER                              = 1;
    public static int ONWIFI                             = 2;

    public static String LOADIMAGE                         = "loadimage";


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


    //public static final LinkedHashMap<String, String> myMap = createMap();


    public static LinkedHashMap<String, String> createUSAMap()
    {
        LinkedHashMap<String,String> myMap = new LinkedHashMap<String,String>();

        //display name , sourceName
        myMap.put("USA Today", "USA Today");
        myMap.put("The New York Times", "The New York Times");
        myMap.put("The Wall Street Journal", "The Wall Street Journal");
        myMap.put("Los Angeles Times", "Los Angeles Times");
        myMap.put("Bloomberg Business", "Bloomberg Business");
        myMap.put("FOX Sports" , "FOX Sports");
        return myMap;
    }


    public static LinkedHashMap<String, String> createUKMap()
    {
        LinkedHashMap<String,String> myMap = new LinkedHashMap<String,String>();

        //display name , sourceName
        myMap.put("The Guardian", "The Guardian");
        myMap.put("BBC News","BBC News");
        myMap.put("CNN International", "CNN International");
        myMap.put("Cricinfo" ,"Cricinfo");
        myMap.put("Reuters" ,"Reuters");
        myMap.put("The Independent", "The Independent");
        myMap.put("Daily Mail","Daily Mail");


        return myMap;
    }


    public static LinkedHashMap<String, String> createINDIAMap()
    {
        LinkedHashMap<String,String> myMap = new LinkedHashMap<String,String>();
        //display name , sourceName
        myMap.put("The Hindu","The Hindu");
        myMap.put("Amar Ujala","Amar Ujala");
        myMap.put("Deccan Herald", "Deccan Herald");//     |
        myMap.put("Hindustan Times","Hindustan Times");//    |
        myMap.put("The Times of India", "The Times of India");// |
        myMap.put("Zee News", "Zee News");//

        return myMap;
    }

    public static LinkedHashMap<String, String> createAUSMap()
    {
        LinkedHashMap<String,String> myMap = new LinkedHashMap<String,String>();
        //display name , sourceName
        myMap.put("The Daily Telegraph", "The Daily Telegraph");
        myMap.put("The Sydney Morning Herald","The Sydney Morning Herald");

        return myMap;
    }


    public static LinkedHashMap<String, String> createChoosenMap() {


        LinkedHashMap<String, String> myMap = new LinkedHashMap<String, String>();


        LinkedHashMap<String, String> usaMap = createUSAMap();
        LinkedHashMap<String, String> ukMap = createUKMap();
        LinkedHashMap<String, String> indiaMap = createINDIAMap();
        LinkedHashMap<String, String> ausMap = createAUSMap();

        ArrayList<LinkedHashMap<String, String>> mapArrayList = new ArrayList<>();
        mapArrayList.add(usaMap);
        mapArrayList.add(ukMap);
        mapArrayList.add(indiaMap);
        mapArrayList.add(ausMap);

        for (LinkedHashMap<String, String> map : mapArrayList) {
            for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                myMap.put(mapEntry.getKey(), mapEntry.getValue());

            }
        }

        return  myMap;

    }

        public static LinkedHashMap<String, Integer> createStartMap()
        {


        LinkedHashMap<String,Integer> myMap = new LinkedHashMap<String,Integer>();


        LinkedHashMap<String, String> usaMap = createUSAMap();
        LinkedHashMap<String, String> ukMap = createUKMap();
        LinkedHashMap<String, String> indiaMap = createINDIAMap();
            LinkedHashMap<String, String> ausMap = createAUSMap();

            ArrayList<LinkedHashMap<String, String>> mapArrayList = new ArrayList<>();
            mapArrayList.add(usaMap);
            mapArrayList.add(ukMap);
            mapArrayList.add(indiaMap);
            mapArrayList.add(ausMap);

        for (LinkedHashMap<String, String> map : mapArrayList)
        {
            for (Map.Entry<String, String> mapEntry : map.entrySet())
            {
                myMap.put(mapEntry.getKey(), 0);

            }
        }


        return myMap;
    }







//    { "USA Today", "The New York Times","The Guardian","The Wall Street Journal","Washington Post","Los Angeles Times",
//
//            "The Daily Telegraph"
//    };







}
