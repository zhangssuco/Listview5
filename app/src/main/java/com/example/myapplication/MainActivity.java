package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity{

    static final ArrayList<HashMap<String,String>> myList =
            new ArrayList<HashMap<String,String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final ListView sunycampuseslv = (ListView) findViewById(R.id.leaderboard);

        final SimpleAdapter adapter = new SimpleAdapter(
                this,
                myList,
                R.layout.sunycampus,
                new String[] {"ID","Name","Mascot"},
                new int[] {R.id.standingText,R.id.scoreText, R.id.userText}
        );
        populateList();
        sunycampuseslv.setAdapter(adapter);

        sunycampuseslv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String s = sunycampuseslv.getItemAtPosition(i).toString();

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                            }
        });


    }

    private void populateList() {
        HashMap<String,String> temp;

        temp = new HashMap<String,String>();
        temp.put("ID","SUCO");
        temp.put("Name", "SUNY Oneonta");
        temp.put("Mascot", "Red Dragon");
        myList.add(temp);

        temp = new HashMap<String,String>();
        temp.put("ID","Albany");
        temp.put("Name", "SUNY Albany");
        temp.put("Mascot", "Dane");
        myList.add(temp);

        for(int i=0; i<30; i++){
        temp = new HashMap<String,String>();
        temp.put("ID","Binghamton");
        temp.put("Name", "SUNY Bing");
        temp.put("Mascot", "something?");
        myList.add(temp);}

    }

}