package com.pbsi2.fakenewsbaby;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GetNews extends AsyncTaskLoader<ArrayList<BadNews>> {
    private String TAG = "GetNews";
    private final String url = MainActivity.guardianUrl;
    private final Context pContext;
    ArrayList<BadNews> myNews = new ArrayList<BadNews>();

    public GetNews(Context context) {
        super(context);
        pContext = context;
    }

    @Override
    public ArrayList<BadNews> loadInBackground() {
        HttpHandler sh = new HttpHandler();
        // Making a request to url and getting response

        String jsonStr = sh.makeServiceCall(url);
        Log.e(TAG, "Pierre Response from url: " + jsonStr);
        try {
            if (jsonStr == null) {
                Log.e(TAG, "Couldn't get json from server.");

                return null;
            } else {
                JSONObject jsonObj = new JSONObject(jsonStr);
                Log.e(TAG, "\nResponse 1st obj: " + jsonObj.toString());
                JSONObject jsonResp = jsonObj.getJSONObject("response");
                Log.e(TAG, "\nResponse 2st obj: " + jsonResp.toString());
                // Getting JSON Array node
                JSONArray mresults = jsonResp.getJSONArray("results");
                Log.e(TAG, "\nResponse 3rd obj: " + mresults.toString());
                // looping through All Contacts*/
                for (int i = 0; i < mresults.length(); i++) {
                    JSONObject c = mresults.getJSONObject(i);
                    String wtitle = c.getString("webTitle");
                    String section = c.getString("sectionName");
                    String link = c.getString("webUrl");
                    String type = c.getString("type");
                    String date = c.getString("webPublicationDate");
                    String author = "N/A";
                    // Author is given in TAGS: node in JSON Object
                    JSONArray tags = c.getJSONArray("tags");
                    if (tags.length() > 0) {
                        JSONObject d = tags.getJSONObject(0);
                        JSONArray authorxx = d.names();
                        if (authorxx != null) {
                            if (authorxx.toString().contains("\"firstName\"")) {
                                author = d.optString("firstName", "N/A");
                            }
                            if (authorxx.toString().contains("\"lastName\"")) {
                                author += " " + d.optString("lastName", " ");
                            }
                        }
                    }
                    myNews.add(new BadNews(wtitle, section, link, author, type, date));
                }
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        return myNews;
    }
    @Override
    public void deliverResult(ArrayList<BadNews> data) {
        super.deliverResult(data);
    }

}