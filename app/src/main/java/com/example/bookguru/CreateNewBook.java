package com.example.bookguru;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateNewBook extends AppCompatActivity {

    private static Button btnQuery;
    private static EditText book, auth, pub, date;
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "http://192.168.0.198/bookguru/InsertTrans.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    private static String bookname = "";
    public static String publisher = "";
    public static String author = "";
    public static String dateofpub = "";

    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_book);

        btnQuery = (Button) findViewById(R.id.btnQuery);
        book = (EditText) findViewById(R.id.book);
        auth = (EditText) findViewById(R.id.author);
        pub = (EditText) findViewById(R.id.publisher);
        date = (EditText) findViewById(R.id.date);

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookname = book.getText().toString();
                author = auth.getText().toString();
                publisher = pub.getText().toString();
                dateofpub = date.getText().toString();
                new uploadDatatoURL().execute();
            }
        });

    }
    private class uploadDatatoURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(CreateNewBook.this);

        public uploadDatatoURL() {}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setMessage(cMessage);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params){
            int nSuccess;
            try {
                ContentValues cv = new ContentValues();
                cPostSQL = " '" + bookname + "' , '" + author + "' , '" + publisher + "' , '" + dateofpub + "'  ";
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHost, "POST", cv);
                if (json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
                        online_dataset = json.getString(TAG_MESSAGE);
                        return online_dataset;
                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                } else {
                    return "HTTPSERVER_ERROR";
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            pDialog.dismiss();
            String isEmpty = "";
            AlertDialog.Builder alert = new AlertDialog.Builder(CreateNewBook.this);
            if (s !=null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) {
                }
                Toast.makeText(CreateNewBook.this, s, Toast.LENGTH_SHORT).show();
            }else{
                alert.setMessage("Query Interrupted ... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }

    }
}
