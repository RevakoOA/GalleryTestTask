package com.andersen.just_me.reditgallery;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.SupportActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by just_me on 22.03.17.
 */

public class MainActivity extends SupportActivity {
    private static final String TAG = "MainActivity";
    private static final String VOLLEY_REQUEST_TAG = "VolleyTag";

    @Bind(R.id.imagesRV)
    RecyclerView imagesRV;
    @Bind(R.id.buttonsLL)
    LinearLayout buttonsLL;
    @Bind(R.id.newB)
    Button newB;
    @Bind(R.id.topB)
    Button topB;

    public static RequestQueue mRequestQueue = null;

    private static View.OnTouchListener enableChildren = (view, motionEvent) -> false;
    private static View.OnTouchListener disableChildren = (view, motionEvent) -> true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        imagesRV.setLayoutManager(llm);

        mRequestQueue = Volley.newRequestQueue(getBaseContext());
        loadImages(newB);
    }

    @OnClick({R.id.topB, R.id.newB})
    public void loadImages(Button button) {
        String url = null;
        switch (button.getId()) {
            case R.id.topB:
                url = "https://www.reddit.com/r/EarthPorn/top/.json?limit=100";
                break;
            case R.id.newB:
                url = "https://www.reddit.com/r/EarthPorn/new/.json?limit=100";
                break;
        }
        Log.d(TAG, "Url set to " + url);
        disableButtons(true);
        makeRemote(Request.Method.GET, url, null, successListener, errorListener);
    }

    private void disableButtons(boolean disable) {
        topB.setClickable(!disable);
        newB.setClickable(!disable);
    }

    public static void makeRemote(int method, String url, JSONObject payload, Response.Listener<JSONObject> success, Response.ErrorListener failure) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (/*Request.Method*/method, url, payload, success, failure);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        jsonRequest.setTag(VOLLEY_REQUEST_TAG);
        jsonRequest.setShouldCache(false);
        mRequestQueue.add(jsonRequest);
    }

    private Response.Listener<JSONObject> successListener = (response) -> {
        disableButtons(false);
        ArrayList<ImageData> imageDatas = parseImages(response);
        imagesRV.setAdapter(new GalleryAdapter(MainActivity.this, imageDatas));
    };
    private Response.ErrorListener errorListener = (error) -> {
        disableButtons(false);
        Log.e(TAG, "Error message is " + error.getMessage() + "; error cause is " + error.getCause()
                + "; Response is " + ((error.networkResponse == null) ? "null" : new String(error.networkResponse.data)));
    };

    public ArrayList<ImageData> parseImages(JSONObject response) {
        ArrayList<ImageData> datas = new ArrayList<>();
        try {
            JSONArray imagesData = response.getJSONObject("data").getJSONArray("children");
            JSONObject imageData;
            JSONObject urlsOfImage;
            JSONArray resolutions;
            ArrayList<String> urls = new ArrayList<>();
            ArrayList<Point> sizes = new ArrayList<>();
            JSONObject resolution;
            for (int i = 0; i < imagesData.length(); i++) {
                imageData = imagesData.getJSONObject(i).getJSONObject("data");
                if (!imageData.getString("post_hint").equals("image")) {
                    continue;
                }
                urlsOfImage = imageData.getJSONObject("preview").getJSONArray("images").getJSONObject(0);
                resolutions = urlsOfImage.getJSONArray("resolutions");
                for (int j = 0; j < resolutions.length(); j++) {
                    resolution = resolutions.getJSONObject(j);
                    urls.add(resolution.getString("url"));
                    sizes.add(new Point(resolution.getInt("width"), resolution.getInt("height")));
                }
                // get biggest src
                resolution = urlsOfImage.getJSONObject("source");
                urls.add(resolution.getString("url"));
                sizes.add(new Point(resolution.getInt("width"), resolution.getInt("height")));
                datas.add(new ImageData(imageData.getString("title"), imageData.getString("author"), imageData.getString("thumbnail"), urls, sizes));
                urls = new ArrayList<>();
                sizes = new ArrayList<>();
            }
            Log.d(TAG, "datas length is " + datas.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datas;
    }

}
