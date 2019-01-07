package com.pbsi2.fakenewsbaby;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class GetNews extends AsyncTaskLoader<ArrayList<BadNews>> {
    private String TAG = "GetNews";
    private String url;

 /*   public GetNews(Context context) {
        super(context);
    }*/
    public GetNews(Context context, String murl) {
        super(context);
        url = murl;
    }

    @Override
    public ArrayList<BadNews> loadInBackground() {
        HttpHandler sh = new HttpHandler();
        // Making a request to url and getting response
        ArrayList<BadNews> myNews = new ArrayList<BadNews>();
        String jsonStr = sh.makeServiceCall(url);
        Log.e(TAG, "Response from url: " + jsonStr);
        try {
            if (jsonStr == null) {
                return null;
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }
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

                // Author is ginven in TAGS: node in JSON Object
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
                myNews.add(new BadNews(wtitle, section, link, author, type, date, false));
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