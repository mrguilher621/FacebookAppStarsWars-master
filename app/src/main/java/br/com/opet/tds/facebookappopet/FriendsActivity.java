package br.com.opet.tds.facebookappopet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendsActivity extends Activity {

    private String facebookID;
    ProfilePictureView profile;
    TextView textNome;
    TextView textJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        profile = (ProfilePictureView) findViewById(R.id.profile);
        textNome = (TextView) findViewById(R.id.textNome);


        facebookID = getIntent().getStringExtra("FB_ID");
        AccessToken token = AccessToken.getCurrentAccessToken();

        profile.setProfileId(token.getUserId());
        loadUserName();

        textJSON = (TextView) findViewById(R.id.textJSON);


        String url = "https://swapi.co/api/films/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                textJSON.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    public void loadUserName(){
       new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                JSONObject obj = response.getJSONObject();
                try{
                    String nome = obj.getString("name");
                    textNome.setText("Óla! " + nome);
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }).executeAsync();
    }
}
