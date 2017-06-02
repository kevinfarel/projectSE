package com.example.kevinfarel.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PrintNowMenu extends AppCompatActivity {
    Spinner spinner;
    String Email,EmailUser,EmailPrinter,jsonString,NamaUser;
    String Type,Price;
    JSONArray arr,count;
    JSONObject jObj;
    Double totalpaperprice;
    EditText page;
    CheckBox bwcolor,bothside;
    List<String> listtype = new ArrayList<String>();
    List<String> listprice = new ArrayList<String>();
    Context ctx;
    class Background extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            String emailuser = params[0];
            String emailprinter = params[1];
            String ordertext = params[2];
            String finalize = params[3];
            String data = "";
            int tmp;
            try {
                URL url = new URL("https://kevinfarel.000webhostapp.com/insertorder.php");
                String urlParams = "emailuser="+emailuser+"&emailprinter="+emailprinter+"&ordertext="+ordertext+"&finalize="+finalize;
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }
                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("")){
                s="Paper Added.";
            }
            Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
        }
    }
    private class AsyncCaller extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
        }

        @Override
        protected Void doInBackground(Void... params) {
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you don't want to pass argument and u can access the parent class' variable url over here
            URL url = null;
            try {
                url = new URL("https://kevinfarel.000webhostapp.com/loadpaper.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
                jsonString = IOUtils.toString(in, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                count= new JSONArray(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int a=0;a<count.length();a++) {
                try {
                    arr = new JSONArray(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jObj = arr.getJSONObject(a);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Type = jObj.getString("Paper_Type");
                    Price = jObj.getString("Paper_Price");
                    Email=  jObj.getString("Email_Printer");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(Email.equals(EmailPrinter)) {
                    listtype.add(Type);
                    listprice.add(Price);
                }
            }
                SpinnerLoad(listtype);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_now_menu);
        page = (EditText) findViewById(R.id.page);
        page.setText("5");
        page.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        TextView info = (TextView) findViewById(R.id.infoPrinter);
        bwcolor= (CheckBox) findViewById(R.id.bwcolor);
        bothside= (CheckBox) findViewById(R.id.bothside);
        final TextView listpesanan =(TextView) findViewById(R.id.listpesanan);
        final TextView tax =(TextView) findViewById(R.id.tax);
        final TextView total =(TextView) findViewById(R.id.Total);
        Intent i = getIntent();
        info.setText("Printer's Name    : "+i.getStringExtra("name")+"\nAddress    :"+i.getStringExtra("address"));
        NamaUser=i.getStringExtra("NamaUser");
        EmailPrinter=i.getStringExtra("EmailPrinter");
        EmailUser=i.getStringExtra("EmailUser");
        Toast.makeText(this, EmailUser, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, NamaUser, Toast.LENGTH_SHORT).show();
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView price = (TextView) findViewById(R.id.PriceText);
                int x=spinner.getSelectedItemPosition();
                price.setText("Price each page: "+listprice.get(x));
                totalpaperprice= Integer.parseInt(page.getText().toString())*Integer.parseInt(listprice.get(x).toString())*1.1;
                listpesanan.setText(page.getText().toString()+" Pages of "+spinner.getSelectedItem().toString()+"Paper type : Rp."+totalpaperprice);
                tax.setText("10% tax : Rp."+0.1*totalpaperprice);
                total.setText("Total to paid :"+totalpaperprice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if(EmailPrinter.equals("Not Chosen"))
        {
            Toast.makeText(this, "Please Choose Location to print first", Toast.LENGTH_SHORT).show();
        }else {
            new AsyncCaller().execute();
        }
    }
    protected void SpinnerLoad(List<String> list)
    {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
    public void PilihLokasiPrinter(View v)
    {
        Intent i = new Intent(this,UserChoosePrinterMap.class);
        i.putExtra("NamaUser",NamaUser);
        i.putExtra("EmailUser",EmailUser);
        startActivity(i);
        finish();
    }
    protected String OrderTextMaker() {
        String x= "Nama : "+NamaUser+"\n";
        String y= "Price paid by "+NamaUser+": Rp."+totalpaperprice+"\n";
        String z= "Order:\nAmount page to print :"+page.getText().toString()+"\nPaper type: "+spinner.getSelectedItem()+"\n";
        String a=x+y+z;
        if(bothside.isChecked())
        {
            a=a+"\nPrint on both side";
        }
        if(bwcolor.isChecked())
        {
            a=a+"\nBlack and white color";
        }
        return a;
    }
    protected void ordernow(View v)
    {
        Background b = new Background();
        b.execute(EmailUser,EmailPrinter,OrderTextMaker(),"waiting");
        Intent i = new Intent(this,MainMenuUser.class);
        i.putExtra("email",EmailUser);
        startActivity(i);
        PrintNowMenu.this.finish();
    }
}
