package com.example.bookguru;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog; import android.app.ProgressDialog; import android.content.ContentValues; import android.content.Context; import android.content.DialogInterface; import android.content.Intent; import android.os.AsyncTask; import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView; import android.widget.ArrayAdapter; import android.widget.Button; import android.widget.EditText; import android.widget.ListView; import android.widget.TextView; import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList; import java.util.Arrays;
import android.os.Bundle;

public class ManageBooks extends AppCompatActivity {

    private static Button btnQuery;
    TextView textView, txtbook, txtDefault_author, txtDefault_publisher, txtDefault_date, txtDefault_ID;
    private static EditText editbook;
    private static JSONParser jParser = new JSONParser();
    private static String urlHostBook = "http://192.168.0.198/bookguru/selectBookTitle.php";
    private static String urlHostDelete = "http://192.168.0.198/bookguru/delete.php";
    private static String urlHostAuthor = "http://192.168.0.198/bookguru/selectAuthor.php";
    private static String urlHostPublisher = "http://192.168.0.198/bookguru/selectPublisher.php";
    private static String urlHostDate = "http://192.168.0.198/bookguru/SelectDate.php";
    private static String urlHostID = "http://192.168.0.198/bookguru/selectId.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    private static String cItemcode = "";
    //how to globalize string
    public static String wew = "";
    public static String gender = "";
    public static String civstats = "";

    private String bks, auth, pub, date, aydi;

    String cItemSelected, cItemSelected_author, cItemSelected_publisher, cItemSelected_ID, cItemSelected_date;
    ArrayAdapter<String> adapter_bnames;
    ArrayAdapter<String> adapter_author;
    ArrayAdapter<String> adapter_publisher;
    ArrayAdapter<String> adapter_date;
    ArrayAdapter<String> adapter_ID;
    ArrayList<String> list_bnames;
    ArrayList<String> list_author;
    ArrayList<String> list_publisher;
    ArrayList<String> list_date;
    ArrayList<String> list_ID;
    AdapterView.OnItemLongClickListener longItemListener_fnames;
    Context context = this;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_books);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        editbook = (EditText) findViewById(R.id.editbook);
        txtbook = (TextView) findViewById(R.id.txt_book);
        listView = (ListView) findViewById(R.id.listview);
        textView = (TextView) findViewById(R.id.textView4);
        txtDefault_author = (TextView) findViewById(R.id.txt_author);
        txtDefault_publisher = (TextView) findViewById(R.id.txt_publisher);
        txtDefault_date = (TextView) findViewById(R.id.txt_date);
        txtDefault_ID = (TextView) findViewById(R.id.txt_ID);

        txtbook.setVisibility(View.GONE);
        txtDefault_author.setVisibility(View.GONE);
        txtDefault_publisher.setVisibility(View.GONE);
        txtDefault_date.setVisibility(View.GONE);
        txtDefault_ID.setVisibility(View.GONE);

        Toast.makeText(ManageBooks.this, "Nothing Selected", Toast.LENGTH_SHORT).show();

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cItemcode = editbook.getText().toString();

                new uploadDataToURL().execute();
                new Author().execute();
                new Publisher().execute();
                new Date().execute();
                new id().execute();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cItemSelected = adapter_bnames.getItem(position);
                cItemSelected_author = adapter_author.getItem(position);
                cItemSelected_publisher = adapter_publisher.getItem(position);
                cItemSelected_date = adapter_date.getItem(position);
                cItemSelected_ID = adapter_ID.getItem(position);

                androidx.appcompat.app.AlertDialog.Builder alert_confirm =
                        new androidx.appcompat.app.AlertDialog.Builder(context);
                alert_confirm.setMessage("Edit the records of" + " " + cItemSelected);
                alert_confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        txtbook.setText(cItemSelected);
                        txtDefault_author.setText(cItemSelected_author);
                        txtDefault_publisher.setText(cItemSelected_publisher);
                        txtDefault_date.setText(cItemSelected_date);
                        txtDefault_ID.setText(cItemSelected_ID);

                        bks = txtbook.getText().toString().trim();
                        auth = txtDefault_author.getText().toString().trim();
                        pub = txtDefault_publisher.getText().toString().trim();
                        date = txtDefault_date.getText().toString().trim();
                        aydi = txtDefault_ID.getText().toString().trim();

                        Intent intent = new Intent(ManageBooks.this, EditBooks.class);
                        intent.putExtra(EditBooks.BOOK, bks);
                        intent.putExtra(EditBooks.AUTHOR, auth);
                        intent.putExtra(EditBooks.PUBLISHER, pub);
                        intent.putExtra(EditBooks.DATE, date);
                        intent.putExtra(EditBooks.ID, aydi);
                        startActivity(intent);
                    }
                });
                alert_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert_confirm.show();
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cItemSelected = adapter_bnames.getItem(position);
                cItemSelected_author = adapter_author.getItem(position);
                cItemSelected_publisher = adapter_publisher.getItem(position);
                cItemSelected_date = adapter_date.getItem(position);
                cItemSelected_ID = adapter_ID.getItem(position);

                androidx.appcompat.app.AlertDialog.Builder alert_confirm =
                        new androidx.appcompat.app.AlertDialog.Builder(context);
                alert_confirm.setMessage("Are you sure you want to delete" + " " + cItemSelected);
                alert_confirm.setPositiveButton(R.string.msg2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtDefault_ID.setText(cItemSelected_ID);

                        aydi = txtDefault_ID.getText().toString().trim();
                        new delete().execute();
                    }
                });
                alert_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert_confirm.show();
            }
        });
    }
    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBooks.this);

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
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);
                JSONObject json = jParser.makeHTTPRequest(urlHostBook, "POST", cv);
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBooks.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) {
                }
                //Toast.makeText(ManageRecords. this, s, Toast.LENGTH_SHORT).show();
                String wew = s;
                String str = wew;
                final String bnames[] = str.split("-");
                list_bnames = new ArrayList<String>(Arrays.asList(bnames));
                adapter_bnames = new ArrayAdapter<String>(ManageBooks.this, android.R.layout.simple_list_item_1, list_bnames);
                listView.setAdapter(adapter_bnames);
                textView.setText(listView.getAdapter().getCount() + "record(s) found.");
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class Author extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBooks.this);

        public Author() {
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
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHostAuthor, "POST", cv);
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String Gender) {

            super.onPostExecute(Gender);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBooks.this);
            if (Gender != null) {
                if (isEmpty.equals("") && !Gender.equals("HTTPSERVER_ERROR")) {
                }

                String gender = Gender;
                String str = gender;
                final String Genders[] = str.split("-");
                list_author = new ArrayList<String>(Arrays.asList(Genders));
                adapter_author = new ArrayAdapter<String>(ManageBooks.this, android.R.layout.simple_list_item_1, list_author);
                //listView.setAdapter(adapter_gender);


            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class Publisher extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBooks.this);

        public Publisher() {
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
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);
                JSONObject json = jParser.makeHTTPRequest(urlHostPublisher, "POST", cv);
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String CS) {
            super.onPostExecute(CS);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBooks.this);
            if (CS != null) {
                if (isEmpty.equals("") && !CS.equals("HTTPSERVER_ERROR")) {
                }
                String CivilStat = CS;

                String str = CivilStat;
                final String Civs[] = str.split("-");
                list_publisher = new ArrayList<String>(Arrays.asList(Civs));
                adapter_publisher = new ArrayAdapter<String>(ManageBooks.this, android.R.layout.simple_list_item_1, list_publisher);
                //listView.setAdapter(adapter_gender);

            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class Date extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBooks.this);

        public Date() {
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
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);
                JSONObject json = jParser.makeHTTPRequest(urlHostDate, "POST", cv);
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String CS) {
            super.onPostExecute(CS);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBooks.this);
            if (CS != null) {
                if (isEmpty.equals("") && !CS.equals("HTTPSERVER_ERROR")) {
                }
                String CivilStat = CS;

                String str = CivilStat;
                final String Civs[] = str.split("-");
                list_date = new ArrayList<String>(Arrays.asList(Civs));
                adapter_date = new ArrayAdapter<String>(ManageBooks.this, android.R.layout.simple_list_item_1, list_date);
                //listView.setAdapter(adapter_gender);

            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class id extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBooks.this);

        public id() {
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
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);
                JSONObject json = jParser.makeHTTPRequest(urlHostID, "POST", cv);
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
            } catch (JSONException e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String aydi) {
            super.onPostExecute(aydi);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBooks.this);
            if (aydi != null) {
                if (isEmpty.equals("") && !aydi.equals("HTTPSERVER_ERROR")) {
                }
                Toast.makeText(ManageBooks.this, "Data selected", Toast.LENGTH_SHORT).show();
                String AYDI = aydi;
                String str = AYDI;
                final String ayds[] = str.split("-");
                list_ID = new ArrayList<String>(Arrays.asList(ayds));
                adapter_ID = new ArrayAdapter<String>(ManageBooks.this,
                        android.R.layout.simple_list_item_1, list_ID);
                //listView.setAdapter(adapter_gender);
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class delete extends AsyncTask<String, String, String> {

        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBooks.this);

        public delete() {
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
                cPostSQL = cItemSelected_ID;
                cv.put("id", cPostSQL);
                JSONObject json = jParser.makeHTTPRequest(urlHostDelete, "POST", cv);
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String del) {
            super.onPostExecute(del);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBooks.this);
            if (aydi != null) {
                if (isEmpty.equals("") && !del.equals("HTTPSERVER_ERROR")) {
                }
                Toast.makeText(ManageBooks.this, "Data Deleted", Toast.LENGTH_SHORT).show();
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }


}
