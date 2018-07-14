package com.nayemuzzaman.academicresearch;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private ShareDialog shareDialog;
    private String name, surname, imageUrl;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Bundle inBundle = getIntent().getExtras();
        if (inBundle != null) {
            name = inBundle.getString("name");
            surname = inBundle.getString("surname");
            imageUrl = inBundle.getString("imageUrl");
        }

        TextView nameView = (TextView)findViewById(R.id.nameAndSurname);
        nameView.setText("" + name + " " + surname);
    }


    public void onClick(View view) {
        switch (view.getId()){
            case R.id.share:
                share();
                break;

            case R.id.getPosts:
                getPosts();
                break;

            case R.id.logout:
                logout();
                break;
        }
    }

    private void share(){
        shareDialog = new ShareDialog(this);
        List<String> taggedUserIds= new ArrayList<String>();
        taggedUserIds.add("{USER_ID}");
        taggedUserIds.add("{USER_ID}");
        taggedUserIds.add("{USER_ID}");

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                .setContentTitle("This is a content title")
                .setContentDescription("This is a description")
                .setShareHashtag(new ShareHashtag.Builder().setHashtag("#helpToResearch").build())
                .setPeopleIds(taggedUserIds)
                .setPlaceId("{PLACE_ID}")
                .build();

        shareDialog.show(content);
    }

    private void getPosts(){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(), "/me/posts", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject object = response.getJSONObject();
                        for (int i = 0; i < object.optJSONArray("data").length(); i++){
                            Log.d(TAG, " Real Data"+object.optJSONArray("data").optJSONObject(i).optString("id"));

                        }
                    }}).executeAsync();

    }
    private void logout(){
        LoginManager.getInstance().logOut();
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
        finish();
    }
}

