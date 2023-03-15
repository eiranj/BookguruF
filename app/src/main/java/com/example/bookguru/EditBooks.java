package com.example.bookguru;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class EditBooks extends AppCompatActivity {
    private static Button btnQuery;
    private static EditText book, names, auth, pub, date;
    private static TextView tv_civ;
    private static String cItemcode = "";
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "http://192.168.0.107/ancuin/UpdateQty.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";

    public static String String_isempty = "";
    public static final String BOOK = "BOOK";
    public static final String AUTHOR = "AUTHOR";
    public static final String PUBLISHER = "PUBLISHER";
    public static final String DATE = "DATE";
    public static final String ID = "ID";
    private String bks, authr, pubs, dates, aydi;

    private static String bookname = "";
    public static String publisher = "";
    public static String author = "";
    public static String dateofpub = "";



    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_books);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        book = (EditText) findViewById(R.id.book);
        auth = (EditText) findViewById(R.id.author);
        pub = (EditText) findViewById(R.id.publisher);
        date = (EditText) findViewById(R.id.date);

        Intent i = getIntent();
        bks = i.getStringExtra(BOOK);
        authr = i.getStringExtra(AUTHOR);
        pubs = i.getStringExtra(PUBLISHER);
        dates = i.getStringExtra(DATE);
        aydi = i.getStringExtra(ID);
        names.setText(bks);

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookname = book.getText().toString();
                author = auth.getText().toString();
                publisher = pub.getText().toString();
                dateofpub = date.getText().toString();
                new uploadDataToURL().execute();
            }
        });

    }
    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPost = "", cPostSQL = "", cMessage = "Querying data...";
        String gens, civil;
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(EditBooks.this);

        public uploadDataToURL() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setMessage(cMessage);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            int nSuccess;
            try {
                ContentValues cv = new ContentValues();

                cPostSQL = aydi;
                cv.put("id", cPostSQL);

                cPostSQL = " '" + bookname + "' ";
                cv.put("bookname", cPostSQL);

                cPostSQL = " '" + author + "' ";
                cv.put("author", cPostSQL);

                cPostSQL = " '" + publisher + "' ";
                cv.put("publisher", cPostSQL);

                cPostSQL = " '" + dateofpub + "' ";
                cv.put("dateofpub", cPostSQL);


                JSONObject json = jParser.makeHTTPRequest(urlHost, "POST", cv);
                if (json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
                        online_dataset = json.getString(TAG_MESSAGE);
                        return online_dataset;
                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                } else{
                    return "HTTPSERVER_ERROR";
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute (String s){
            super.onPostExecute(s);
            pDialog.dismiss();
            String isEmpty = "";
            AlertDialog.Builder alert = new AlertDialog.Builder(EditBooks.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) {
                }
                Toast.makeText(EditBooks.this, s, Toast.LENGTH_SHORT).show();
            } else {
                alert.setMessage("Query Interrupted ... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}