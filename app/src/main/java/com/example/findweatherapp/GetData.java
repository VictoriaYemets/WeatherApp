package com.example.findweatherapp;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GetData extends AsyncTask <URL, Void, String> {

    private static final String TAG = "GetData";

    protected String getResponseFromHttpGetUrl (URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String result = null;
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                result = scanner.next();
                return result;
            }else {
                return null;
            }
        } finally {
            Log.d(TAG, "getResponseFromHttpGetUrl: " + result);
            urlConnection.disconnect();
        }


    }

    public interface AsyncResponse{
        void proccessFinish(String output);
    }

    public AsyncResponse delegate;

    public GetData (AsyncResponse delegate) {
        this.delegate = delegate;
    }
    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute: called");
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(URL[] url) {

        Log.d(TAG, "doInBackground: called");
        String result = null;
        URL urlQuery = url[0];
        try {
            result = getResponseFromHttpGetUrl(urlQuery);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(o);
        Log.d(TAG, "onPostExecute: called");
        Log.d(TAG, "onPostExecute: " + result);
        delegate.proccessFinish(result);
    }

}
