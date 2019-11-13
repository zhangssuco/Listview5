package com.example.listview5;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    static final ArrayList<HashMap<String,String>> myList =
            new ArrayList<HashMap<String,String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        handlelistview();
        populateListdatamodel();
        initializesharedpreferences();
    }
    private void handlelistview()
    {

        final ListView sunycampuseslv = (ListView) findViewById(R.id.leaderboard);
        final SimpleAdapter adapter = new SimpleAdapter(
                this,
                myList,
                R.layout.persunycampus,
                new String[] {"ID","Name","Mascot"},
                new int[] {R.id.id,R.id.name, R.id.mascot}
        );

        sunycampuseslv.setAdapter(adapter);
        sunycampuseslv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = sunycampuseslv.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        });

    }
    private void populateListdatamodel() {
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

        for(int i=0; i<50; i++){
            temp = new HashMap<String,String>();
            temp.put("ID","Binghamton");
            temp.put("Name", "SUNY Bing");
            temp.put("Mascot", "something?");
            myList.add(temp);
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    String filename="campuses.txt";
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.loadfromtext) {

            Toast.makeText(getApplicationContext(),"Internal or Assset?"+
                            String.valueOf(pref.getBoolean("internalstorage", false)),
                    Toast.LENGTH_LONG).show();
            if (pref.getBoolean("internalstorage", false)==false) {
                Toast.makeText(getApplicationContext(),"load from asset readonly", Toast.LENGTH_LONG).show();
                loadfromtext(filename);
            }
            else {
                Toast.makeText(getApplicationContext(),"load from internal", Toast.LENGTH_LONG).show();
                readfromFile(this);
            }
            handlelistview();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete) {
            myList.remove(0);
            handlelistview();

            return true;
        }
        if (id == R.id.add) {

            HashMap<String,String> temp;
            temp = new HashMap<String,String>();
            temp.put("ID","tester");
            temp.put("Name", "tester");
            temp.put("Mascot", "tester");
            myList.add(temp);
            handlelistview();

            return true;
        }

        if (id == R.id.save) {
            writeToFile(getApplicationContext());
            return true;
        }

        if (id == R.id.showsharedpreferences) {

            showsharedpreferences();

            return true;
        }

        if (id == R.id.updatesharedpreferences) {
            flipsharedpreferences();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    File dir=null;

    private String readfromFile(Context context) {
        //File fileEvents = new File(context.getFilesDir()+"//mydir//tt.txt");
        StringBuilder text = new StringBuilder();

        try {
            dir = new File(context.getFilesDir(),"mydir");
            File f = new File(dir, filename);
            BufferedReader br = new BufferedReader(new FileReader(f));
            myList.clear();

            String line;
            while ((line = br.readLine()) != null) {
                String []items=line.split(",");
                HashMap<String,String> temp;
                temp = new HashMap<String,String>();
                temp.put("ID",items[0]);
                temp.put("Name", items[1]);
                temp.put("Mascot", items[2]);
                myList.add(temp);
            }
            br.close();

        } catch (IOException e) { }
        String result = text.toString();
        return result;
    }


    private void writeToFile(Context context) {
        try {
            dir = new File(context.getFilesDir(),"mydir");
            if(!dir.exists()){
                dir.mkdir();
            }
            File f = new File(dir, filename);
            Toast.makeText(context, filename,Toast.LENGTH_LONG).show();
            FileWriter writer = new FileWriter(f);

            for (int counter = 0; counter < myList.size(); counter++) {
                HashMap<String, String> temp=myList.get(counter);
                String data=temp.get("ID")+"," +temp.get("Name")+","+temp.get("Mascot")+"\n";
                writer.append(data);
            }
            writer.flush();
            writer.close();

            editor.putBoolean("internalstorage", true);
            editor.commit(); // commit changes

            Toast.makeText(getApplicationContext(),"Write to internal"+
                    String.valueOf(pref.getBoolean("internalstorage", false)),
                    Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    void showsharedpreferences()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        int choice=pref.getInt("choice", -1); // getting Integer
        Toast.makeText(getApplicationContext(),
                String.valueOf(choice)+filename,
                Toast.LENGTH_LONG).show();
    }

    void flipsharedpreferences()
    {
        SharedPreferences.Editor editor = pref.edit();
        if (pref.getInt("choice", -1)==1) {
            editor.putInt("choice", 0);
            filename=pref.getString("filename1", null);
        }
        else {
            editor.putInt("choice", 1);
            filename = pref.getString("filename2", null);
        }
        editor.commit(); // commit changes
    }

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    void initializesharedpreferences()
    {
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        editor.putString("filename1", "campuses.txt"); // Storing string
        editor.putString("filename2", "campuses2.txt"); // Storing integer
        editor.putInt("choice", 0);
        //editor.putBoolean("internalstorage", false);
        editor.commit(); // commit changes
    }

    void loadfromtext(String filename)
    {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(filename)));
            myList.clear();

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                    String []items=mLine.split(",");

                HashMap<String,String> temp;
                temp = new HashMap<String,String>();
                temp.put("ID",items[0]);
                temp.put("Name", items[1]);
                temp.put("Mascot", items[2]);
                myList.add(temp);

            }

        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        handlelistview();
    }
}

