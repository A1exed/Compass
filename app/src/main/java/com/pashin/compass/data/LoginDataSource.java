package com.pashin.compass.data;

import android.os.AsyncTask;

import com.pashin.compass.data.model.LoggedInUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private static final String SERVER_IP = "192.168.43.59";
    private boolean token;
    private boolean connectionError;

    public Result<LoggedInUser> login(String username, String password) throws InterruptedException, ExecutionException {
        Networking networking = new Networking();
        networking.execute(username, password);
        networking.get();
        if (connectionError) {
            token = false;
            return new Result.Error(new IOException("Connection error"));
        }
        if (token) {
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);
            return new Result.Success<>(fakeUser);
        } else {
            return new Result.Error(new IOException("Error logging in"));
        }
    }

    public void logout() {
        // ignore
    }

    public class Networking extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                isLogin((String) params[0], (String) params[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private void isLogin(String username, String password) throws IOException {
        String url = "http://" + SERVER_IP + ":8080/login?username=" + username + "&password=" + password;
        String response = getResponse(url);
        try {
            JSONObject json = new JSONObject(response);
            token = json.getBoolean("loginResult");
        } catch (JSONException e) {
            response = "error";
        }
        connectionError = response.equals("error");
    }

    private String getResponse(String requestUrl) throws IOException {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(15 * 1000);
            connection.connect();

            InputStream in;
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                in = connection.getErrorStream();
            } else {
                in = connection.getInputStream();
            }
            return convertStreamToString(in);
        } catch (SocketTimeoutException e) {
            return "error";
        }
    }

    private String convertStreamToString(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        stream.close();

        return sb.toString();
    }
}