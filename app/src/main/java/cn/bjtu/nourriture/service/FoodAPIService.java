package cn.bjtu.nourriture.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;

import cn.bjtu.nourriture.FoodApi.Users;
import cn.bjtu.nourriture.Preferences.PrefUtils;

public class FoodAPIService extends IntentService {
    private final static String TAG = "FoodApi";
    //public final static String SERVER = "http://192.168.1.5:8080/";
    public final static String SERVER = "http://192.168.43.189:8080/";
    //public final static String SERVER = "http://54.165.166.137:8080/";
    static InputStream is = null;
    static JSONObject json = null;
    static String outPut = "";

    public FoodAPIService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //  Users u = (Users) intent.getSerializableExtra("object");
        String target = intent.getStringExtra("target");
        String json = intent.getStringExtra("json");
        String ret = intent.getStringExtra("ret");
        if (ret == null)
            ret = target;
        boolean post = intent.getBooleanExtra("post", true);
        boolean delete = intent.getBooleanExtra("delete", false);
        boolean patch = intent.getBooleanExtra("patch", false);
        boolean connected = intent.getBooleanExtra("connected", false);

        byte[] b = intent.getByteArrayExtra("img");
        if (b != null) {
                uploadUserPhoto(b);
        }
        apiPost(SERVER, target, json, post, connected, ret, delete, patch);
    }

    public void uploadUserPhoto(byte[] data) {
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(SERVER + "pictures/");
            ByteArrayBody bab = new ByteArrayBody(data, "forest.jpg");
            // File file= new File("/mnt/sdcard/forest.png");
            // FileBody bin = new FileBody(file);
            MultipartEntity reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("pictures", bab);
            reqEntity.addPart("path", new StringBody("sfsdfsdf"));
            postRequest.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();

            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            System.out.println("Response: " + s);
        } catch (Exception e) {
            // handle exception here
            Log.e(e.getClass().getName(), e.getMessage());
        }
    }


    public void apiPost(String url, String target, String request, boolean post, boolean connected, String ret, boolean delete, boolean patch) {
        Boolean error = false;
        try {
            HttpResponse httpResponse;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            if (delete == true)
            {
                HttpDelete httpDelete = new HttpDelete(url + target);
                httpDelete.setHeader("Content-Type", "application/json");
                if (connected == true)
                    httpDelete.setHeader("Auth-Token",  PrefUtils.getFromPrefs(FoodAPIService.this, PrefUtils.PREFS_TOKEN_KEY, "Unknown"));
                Log.e("test", httpDelete.getRequestLine().toString() + "\n" + request.toString());
                httpResponse = httpClient.execute(httpDelete);
            }
            else if (patch == true) {
                HttpPatch httpPatch = new HttpPatch(url + target);
                httpPatch.setHeader("Content-Type", "application/json");
                if (connected == true)
                    httpPatch.setHeader("Auth-Token",  PrefUtils.getFromPrefs(FoodAPIService.this, PrefUtils.PREFS_TOKEN_KEY, "Unknown"));
                StringEntity se = new StringEntity(request);
                httpPatch.setEntity(se);
                Log.e("test", httpPatch.getRequestLine().toString() + "\n" + request.toString());
                httpResponse = httpClient.execute(httpPatch);
            }
            else if (post == true) {
                HttpPost httpPost = new HttpPost(url + target);
                httpPost.setHeader("Content-Type", "application/json");
                if (connected == true)
                    httpPost.setHeader("Auth-Token",  PrefUtils.getFromPrefs(FoodAPIService.this, PrefUtils.PREFS_TOKEN_KEY, "Unknown"));
                StringEntity se = new StringEntity(request);
                httpPost.setEntity(se);
                Log.e("test", httpPost.getRequestLine().toString() + "\n" + request.toString());
                 httpResponse = httpClient.execute(httpPost);
            }
            else
            {
                HttpGet httpGet = new HttpGet(url + target);
                httpGet.setHeader("Content-Type", "application/json");
                if (connected == true)
                    httpGet.setHeader("Auth-Token",  PrefUtils.getFromPrefs(FoodAPIService.this, PrefUtils.PREFS_TOKEN_KEY, "Unknown"));
                Log.e("test", httpGet.getRequestLine().toString() + "\n" + request.toString());
                httpResponse = httpClient.execute(httpGet);
            }

            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            if (httpResponse.getStatusLine().getStatusCode() != 200)
                error = true;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = in.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            outPut = sb.toString();
            Log.e("Response", outPut);
        } catch (Exception e) {
            Log.e("Buffer Error", " Error converting result " + e.toString());
        }

        if (error == true) {
            try {
                JSONObject jsonObj = new JSONObject(outPut);
                sendBroadcast(jsonObj.get("error").toString(), "error");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            sendBroadcast(outPut, ret);
    }

    private void sendBroadcast(String response, String target) {
        Intent intent = new Intent(target);
        intent.putExtra("response", response);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
