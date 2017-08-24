package com.kepler.news.newsly.helper;

import com.kepler.news.newsly.databaseHelper.NewsSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by vishaljasrotia on 29/05/17.
 */

public class Common {



    //5*1000 means 5 seconds.
    public static int UPDATEINTERVAL = 10*60*1000;
    public static final String FIRSTLAUNCH =  "firstlaunch";
    public static int ALWAYS                             = 0;
    public static int NEVER                              = 1;
    public static int ONWIFI                             = 2;

    public static String LOADIMAGE                         = "loadimage";


    public static String ID                                 = "id" ;
    public static String LANGUAGE                           = "language" ;
    public static String COUNTRY                            = "country";
    public static String SOURCEURL                          = "sourceUrl" ;
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


    public static  LinkedHashMap<String, String> mNewsSources = new LinkedHashMap<>();

    public static void update(ArrayList<NewsSource> objects)
    {
//        LinkedHashMap<String, String> mNewsSources = new LinkedHashMap<>();
//        Log.v("NEWSSOURCE","update : " + objects.size());
//        for(int i=0;i < objects.size();i++)
//        {
//            mNewsSources.put((String)objects.get(i), (String)objects.get(i));
//
//        }




    }

    public  static  LinkedHashMap<String, String> getNewsSources()
    {
        return  mNewsSources;
    }

//    public static ArrayList<String> getSelectedSources(){
//
//        return new Gson().fromJson(preferences.getString("BECON_LIST", ""), listOfBecons);
//    }
    public static LinkedHashMap<String, String> createUSAMap()
    {
        LinkedHashMap<String,String> myMap = new LinkedHashMap<String,String>();

        //display name , sourceName
        myMap.put("USA Today", "USA Today");
        myMap.put("New York Times", "New York Times");
        myMap.put("Wall Street Journal", "Wall Street Journal");
        myMap.put("Los Angeles Times", "Los Angeles Times");
        myMap.put("Bloomberg", "Bloomberg");
        myMap.put("Al Jazeera English" , "Al Jazeera English");
        myMap.put("Associated Press" , "Associated Press");
        myMap.put("Breitbart News" , "Breitbart News");
        myMap.put("Business Insider" , "Business Insider");
        myMap.put("Buzzfeed" , "Buzzfeed");
        myMap.put("CNBC" , "CNBC");
        myMap.put("CNN" , "CNN");
        myMap.put("Engadget" , "Engadget");
        myMap.put("Entertainment Weekly" , "Entertainment Weekly");
        myMap.put("ESPN" , "ESPN");
        myMap.put("ESPN Cric Info" , "ESPN Cric Info");
        myMap.put("Fox Sports" , "Fox Sports");
        myMap.put("Google News" , "Google News");
        myMap.put("Hacker News" , "Hacker News");
        myMap.put("IGN" , "IGN");
        myMap.put("MTV News" , "MTV News");
        myMap.put("National Geographic" , "National Geographic");
        myMap.put("New Scientist" , "New Scientist");
        myMap.put("New York Magazine" , "New York Magazine");
        myMap.put("Newsweek" , "Newsweek");
        myMap.put("NFL News" , "NFL News");
        myMap.put("Recode" , "Recode");
        myMap.put("TechCrunch" , "TechCrunch");
        myMap.put("Huffington Post" , "Huffington Post");
        myMap.put("The Next Web" , "The Next Web");
        myMap.put("The Verge" , "The Verge");
        myMap.put("Washington Post" , "Washington Post");
        myMap.put("Denver Post" , "Denver Post");

        myMap.put("Time" , "Time");


        return myMap;
    }


    public static LinkedHashMap<String, String> createUKMap()
    {
        LinkedHashMap<String,String> myMap = new LinkedHashMap<String,String>();

        //display name , sourceName
        myMap.put("The Guardian", "The Guardian");
        myMap.put("BBC News","BBC News");
        myMap.put("BBC Sport" , "BBC Sport");
        myMap.put("CNN International", "CNN International");
        myMap.put("Cricinfo" ,"Cricinfo");
        myMap.put("Reuters" ,"Reuters");
        myMap.put("The Independent", "The Independent");
        myMap.put("Daily Mail","Daily Mail");
        myMap.put("Business Insider (UK)" , "Business Insider (UK)");
        myMap.put("Financial Times" , "Financial Times");
        myMap.put("FourFourTwo" , "FourFourTwo");
        myMap.put("Metro" , "Metro");
        myMap.put("Mirror" , "Mirror");
        myMap.put("TalkSport" , "TalkSport");
        myMap.put("The Economist" , "The Economist");
        myMap.put("The Guardian (UK)" , "The Guardian (UK)");

        return myMap;
    }


    public static LinkedHashMap<String, String> createINDIAMap()
    {
        LinkedHashMap<String,String> myMap = new LinkedHashMap<String,String>();
        //display name , sourceName
        myMap.put("The Hindu","The Hindu");
        myMap.put("Amar Ujala","Amar Ujala");
        myMap.put("Dainik Bhaskar","Dainik Bhaskar");
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
        myMap.put("Brisbane Times" , "Brisbane Times");
        myMap.put("The Australian" , "The Australian");
        myMap.put("ABC News (AU)" , "ABC News (AU)");
        myMap.put("The Guardian (AU)" , "The Guardian (AU)");

        return myMap;
    }

    public static LinkedHashMap<String, String> createGERMANMap()
    {
        LinkedHashMap<String,String> myMap = new LinkedHashMap<String,String>();
        //display name , sourceName
        myMap.put("Bild", "Bild");
        myMap.put("Focus","FOCUS Online");
        myMap.put("Spiegel Online", "Spiegel Online");
        myMap.put("Stern" , "stern.de");
        myMap.put("MorgenPost","morgenpost.de");
        myMap.put("The Local", "thelocal.de");
        myMap.put("Sddeutsche Zeitung","Sddeutsche Zeitung");
        myMap.put("Kicker" , "kicker online");
        myMap.put("Der Tagesspiegel" , "Der Tagesspiegel");
        myMap.put("Die Zeit" , "Die Zeit");
        myMap.put("Gruenderszene" , "Gruenderszene");
        myMap.put("Handelsblatt" , "Handelsblatt");
        myMap.put("T3n" , "T3n");
        myMap.put("Wired.de" , "Wired.de");
        return myMap;
    }


    public static LinkedHashMap<String, String> createFranceNMap()
    {
        LinkedHashMap<String,String> myMap = new LinkedHashMap<String,String>();
        //display name , sourceName
        myMap.put("Football Italia","Football Italia");
        myMap.put("Le Monde", "Le Monde.fr");
        myMap.put("Le Figaro" , "Le Figaro.fr");
        myMap.put("L'Equipe" , "L'Equipe.fr");
        myMap.put("Linternaute", "linternaute.com");

        return myMap;
    }


    public static LinkedHashMap<String, String> createItalianNMap()
    {
        LinkedHashMap<String,String> myMap = new LinkedHashMap<String,String>();
        //display name , sourceName
        myMap.put("La Repubblica", "La Repubblica.it");
        myMap.put("Corriere della Sera" , "Corriere della Sera");

        return myMap;
    }






    public static LinkedHashMap<String, String> createChoosenMap() {

        LinkedHashMap<String, String> myMap = new LinkedHashMap<String, String>();
//        LinkedHashMap<String, String> myMap = new LinkedHashMap<String, String>();
//
//
//        LinkedHashMap<String, String> usaMap = createUSAMap();
//        LinkedHashMap<String, String> ukMap = createUKMap();
//        LinkedHashMap<String, String> indiaMap = createINDIAMap();
//        LinkedHashMap<String, String> ausMap = createAUSMap();
//        LinkedHashMap<String, String> germanMap = createGERMANMap();
//        LinkedHashMap<String, String> franceMap = createFranceNMap();
//
//
//        ArrayList<LinkedHashMap<String, String>> mapArrayList = new ArrayList<>();
//        mapArrayList.add(usaMap);
//        mapArrayList.add(ukMap);
//        mapArrayList.add(indiaMap);
//        mapArrayList.add(ausMap);
//        mapArrayList.add(germanMap);
//        mapArrayList.add(franceMap);
//
//        for (LinkedHashMap<String, String> map : mapArrayList) {
//            for (Map.Entry<String, String> mapEntry : map.entrySet()) {
//                myMap.put(mapEntry.getKey(), mapEntry.getValue());
//
//            }
//        }

        return  myMap;

    }

    public static LinkedHashMap<String, Integer> createStartMap()
    {


           LinkedHashMap<String,Integer> myMap = new LinkedHashMap<String,Integer>();
//
//
//        LinkedHashMap<String, String> usaMap = createUSAMap();
//        LinkedHashMap<String, String> ukMap = createUKMap();
//        LinkedHashMap<String, String> indiaMap = createINDIAMap();
//        LinkedHashMap<String, String> ausMap = createAUSMap();
//        LinkedHashMap<String, String> germanMap = createGERMANMap();
//        LinkedHashMap<String, String> franceMap = createFranceNMap();
//
//        ArrayList<LinkedHashMap<String, String>> mapArrayList = new ArrayList<>();
//        mapArrayList.add(usaMap);
//        mapArrayList.add(ukMap);
//        mapArrayList.add(indiaMap);
//        mapArrayList.add(ausMap);
//        mapArrayList.add(germanMap);
//        mapArrayList.add(franceMap);
        LinkedHashMap<String, String> obj = getNewsSources();
        for (String  sourceName : obj.keySet())
        {
           myMap.put(sourceName, 0);
        }


        return myMap;
    }







//    { "USA Today", "The New York Times","The Guardian","The Wall Street Journal","Washington Post","Los Angeles Times",
//
//            "The Daily Telegraph"
//    };







}
