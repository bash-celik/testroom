package com.gengar.testroom;


import android.util.Log;
import android.widget.ListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Ova klasa pomaze pri fetchovanju
 * tacnije radi svi fetxhovanje i parsovanje
 * sve metode su staticke da bi mogle da se pozovu iz drugih klasa,
 * jer se ova nece instancirati
 * */
public class FetchUtils {

    private static final String LOG_TAG = "com.gengar.testroom";

    public static List<AlbumSearch> fetchAlbumData(String requestUrl){
        List<AlbumSearch> list;

        if(requestUrl == null || requestUrl.equals("")){
            Log.d(LOG_TAG,"url is null");
            return null;
        }

        String jsonResponse = null;

        URL url = createURL(requestUrl);

        try {
            jsonResponse = makeHttpRequest(url);

        }catch (IOException e){
            Log.e(LOG_TAG,"error making http requset",e);
        }
        list = extractAlbum(jsonResponse);
        return list;


    }


   private static URL createURL(String stringUrl){
       URL url = null;
       try {
           url = new URL(stringUrl);
       }catch (MalformedURLException e){
           Log.e(LOG_TAG,"url creation error",e);
       }
       return url;
   }

    private static String makeHttpRequest(URL inURL) throws IOException{
        String jsonResponse = "";

        if(inURL == null){
            return null;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) inURL.openConnection();
            urlConnection.setReadTimeout(1000000000);
            urlConnection.setConnectTimeout(1600000000);
            urlConnection.setRequestMethod("GET");

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG,inURL.toString());
                Log.e(LOG_TAG,"Error responese code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"problem retriving json resault",e);
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<AlbumSearch> extractAlbum(String albumJSON){

        //u ovo cuvamo kreirane objekte i vracamo ih nazad
        //Tip je list, a konstruktor ArrayList
        //smatra se best practise da tip uvek bude list zbog lakseg kastovanja
        List<AlbumSearch> albums = new ArrayList<>();

        try {


            Log.e(LOG_TAG,albumJSON);
            JSONObject jsonObject = new JSONObject(albumJSON);
            JSONArray album = jsonObject.getJSONArray("data");

            for(int i = 0; i < album.length();i++){
                JSONObject object = album.getJSONObject(i);
                int id = object.getInt("id");
                String title = object.getString("title");
                int duration = object.getInt("duration");
                JSONObject artist = object.getJSONObject("artist");
                String name = artist.getString("name");
                JSONObject albumO = object.getJSONObject("album");
                String albumName = albumO.getString("title");

                float lenght = numCovert(duration);
                AlbumSearch albumOBJ = new AlbumSearch(id,albumName,lenght,name,title);

                albums.add(albumOBJ);
            }


        }catch (JSONException e){
            //log.e isto ko sto smo ranije koristili samo ima 3. parametar, exception;
            Log.e(LOG_TAG,"Error while parsing JSON mamu mu",e);
        }

        return  albums;


    }

    private static float numCovert(int len){


        float a = len / 60;
         return a;
    }



}
