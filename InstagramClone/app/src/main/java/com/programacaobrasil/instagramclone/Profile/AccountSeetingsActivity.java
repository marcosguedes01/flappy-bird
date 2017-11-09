package com.programacaobrasil.instagramclone.Profile;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.programacaobrasil.instagramclone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcos.guedes on 20/10/2017.
 */

public class AccountSeetingsActivity extends AppCompatActivity {
    private static final String TAG = "AccountSeetingsActivity";
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accountsettings);
        mContext = AccountSeetingsActivity.this;

        Log.d(TAG, "onCreate: started");

        setupSettingsList();
        setupBackArrow();
    }

    private void setupSettingsList()
    {
        Log.d(TAG, "setupSettingsList: initializing 'Account Settings' list.");

        ListView listView = (ListView)findViewById(R.id.lvAccountSettings);

        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.edit_profile));
        options.add(getString(R.string.sign_out));

        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);
    }

    private void setupBackArrow()
    {
        //Setup the backarrow for navigation back to "ProfileActivity"
        ImageView backArrow = (ImageView)findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to 'ProfileActivity'");
                finish();
            }
        });
    }
}
