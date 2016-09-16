package com.example.alvaro.sqlconnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Iterator;
import java.util.ArrayList;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private Spinner spinner;
    private TextView textViewCambio,textViewName,textViewDate;
    private EditText editTextEuro;
    private Button buttonEuro, buttonDollar;
    private Double cambio;
    private int euros,posicion;
    private boolean euro = true;
    private String tag_json_obj,urlEUR,urlDOL,date;
    private JsonObjectRequest jsonObjReqEUR,jsonObjReqDOL;
    private SQLiteDatabase db;
    private SQLHelper SQLiteRates;
    private ProgressDialog progress;
    private Typeface face;
    private RelativeLayout rl;

    private ArrayList<String> monedasEUR;
    private ArrayList<Double> pricesEUR;
    private ArrayList<String> monedasDOL;
    private ArrayList<Double> pricesDOL;
    private int[] Euroflags={
        R.mipmap.ic_flags,
        R.mipmap.ic_australia,
        R.mipmap.ic_bulgari,
        R.mipmap.ic_brazil,
        R.mipmap.ic_canada,
        R.mipmap.ic_switzland,
        R.mipmap.ic_china,
        R.mipmap.ic_czech,
        R.mipmap.ic_denmark,
        R.mipmap.ic_uk,
        R.mipmap.ic_hongkong,
        R.mipmap.ic_croatia,
        R.mipmap.ic_hungary,
        R.mipmap.ic_indonesia,
        R.mipmap.ic_israel,
        R.mipmap.ic_india,
        R.mipmap.ic_japan,
        R.mipmap.ic_korea,
        R.mipmap.ic_mexico,
        R.mipmap.ic_malasia,
        R.mipmap.ic_norway,
        R.mipmap.ic_newzeland,
        R.mipmap.ic_philipines,
        R.mipmap.ic_poloni,
        R.mipmap.ic_romania,
        R.mipmap.ic_russia,
        R.mipmap.ic_sweden,
        R.mipmap.ic_singapur,
        R.mipmap.ic_thailand,
        R.mipmap.ic_turkey,
        R.mipmap.ic_usa,
        R.mipmap.ic_southafrica
    };
    private int[] Dollarflags={
            R.mipmap.ic_flags,
            R.mipmap.ic_australia,
            R.mipmap.ic_bulgari,
            R.mipmap.ic_brazil,
            R.mipmap.ic_canada,
            R.mipmap.ic_switzland,
            R.mipmap.ic_china,
            R.mipmap.ic_czech,
            R.mipmap.ic_denmark,
            R.mipmap.ic_uk,
            R.mipmap.ic_hongkong,
            R.mipmap.ic_croatia,
            R.mipmap.ic_hungary,
            R.mipmap.ic_indonesia,
            R.mipmap.ic_israel,
            R.mipmap.ic_india,
            R.mipmap.ic_japan,
            R.mipmap.ic_korea,
            R.mipmap.ic_mexico,
            R.mipmap.ic_malasia,
            R.mipmap.ic_norway,
            R.mipmap.ic_newzeland,
            R.mipmap.ic_philipines,
            R.mipmap.ic_poloni,
            R.mipmap.ic_romania,
            R.mipmap.ic_russia,
            R.mipmap.ic_sweden,
            R.mipmap.ic_singapur,
            R.mipmap.ic_thailand,
            R.mipmap.ic_turkey,
            R.mipmap.ic_southafrica,
            R.mipmap.ic_europe
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl=(RelativeLayout)findViewById(R.id.rl);

        face=Typeface.createFromAsset(getAssets(),"ds.ttf");

        textViewName = (TextView)findViewById(R.id.textViewName);
        textViewName.setTypeface(face);
        textViewDate = (TextView)findViewById(R.id.textViewDate);
        textViewDate.setTypeface(face);

        SQLiteRates = new SQLHelper(this, "DBRates", null, 1);
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Loading currencies...");
        progress.show();

        euros = 1;
        buttonEuro = (Button)findViewById(R.id.buttonEuro);
        buttonEuro.setTypeface(face);
        buttonEuro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEuro.setBackgroundResource(R.drawable.buttonshape);
                buttonDollar.setBackgroundResource(R.drawable.buttonshapeb);
                rl.setBackgroundResource(R.drawable.eurobg);
                spinner.setSelection(0);
                euros=1;
                editTextEuro.setText(String.valueOf(euros));
                actualizarCambio();
                euro = true;
            }
        });
        buttonDollar = (Button)findViewById(R.id.buttonDollar);
        buttonDollar.setTypeface(face);
        buttonDollar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEuro.setBackgroundResource(R.drawable.buttonshapeb);
                buttonDollar.setBackgroundResource(R.drawable.buttonshape);
                rl.setBackgroundResource(R.drawable.amebg);
                spinner.setSelection(0);
                euros=1;
                editTextEuro.setText(String.valueOf(euros));
                actualizarCambio();
                euro = false;
            }
        });

        euro = true;
        buttonEuro.setBackgroundResource(R.drawable.buttonshape);
        buttonDollar.setBackgroundResource(R.drawable.buttonshapeb);


        editTextEuro=(EditText)findViewById(R.id.editTextEuro);
        editTextEuro.setText(String.valueOf(euros));
        editTextEuro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actualizarCambio();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        textViewCambio=(TextView)findViewById(R.id.textViewCambio);
        spinner = (Spinner)findViewById(R.id.spinnerMonedas);

        // Etiqueta utilizada para cancelar la petición
        tag_json_obj = "json_obj_req";
        urlEUR = "http://api.fixer.io/latest";
        urlDOL = "http://api.fixer.io/latest?base=USD";

        jsonObjReqEUR = new JsonObjectRequest(Request.Method.GET, urlEUR, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String monedasJSON = response.toString();
                RatesRoot ratesRoot = new Gson().fromJson(monedasJSON, RatesRoot.class);
                monedasEUR = ratesRoot.getRates().getNames();
                monedasEUR.add(0,"Select currency");
                pricesEUR = ratesRoot.getRates().getPrices();
                pricesEUR.add(0,0.0);
                date = ratesRoot.getDate();
                System.out.println(date);
                textViewDate.setText("Last update: "+date);
                updateTableEur(monedasEUR,pricesEUR);
                AdaptadorPersonal adaptadorPersonal = new AdaptadorPersonal(getApplicationContext(), R.layout.itemmoneda, monedasEUR);
                spinner.setAdapter(adaptadorPersonal);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        posicion = position;
                        actualizarCambio();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                progress.dismiss();
            }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    db=SQLiteRates.getReadableDatabase();
                    Cursor cDate = db.rawQuery("Select * from date", null);
                    Cursor cursor=db.rawQuery("Select * from RatesEUR", null);
                    if(cursor.getCount()>0 && cDate.getCount()>0) {
                        cDate.moveToNext();
                        date=cDate.getString(0);
                        textViewDate.setText("Last update: "+date);
                        monedasEUR=new ArrayList<>();
                        pricesEUR=new ArrayList<>();
                        while (cursor.moveToNext()) {
                            monedasEUR.add(cursor.getString(0));
                            pricesEUR.add(cursor.getDouble(1));
                        }
                    }
                    if(monedasEUR==null){lockApp();return;}
                    AdaptadorPersonal adaptadorPersonal = new AdaptadorPersonal(getApplicationContext(), R.layout.itemmoneda, monedasEUR);
                    spinner.setAdapter(adaptadorPersonal);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            posicion = position;
                            actualizarCambio();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(),"Currencies have not been updated",Toast.LENGTH_LONG).show();
                }
            }
        );

        jsonObjReqDOL = new JsonObjectRequest(Request.Method.GET, urlDOL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String monedasJSON = response.toString();
                RatesRootDollar ratesRootDollar = new Gson().fromJson(monedasJSON, RatesRootDollar.class);
                monedasDOL = ratesRootDollar.getRates().getNames();
                monedasDOL.add(0,"Select currency");
                pricesDOL = ratesRootDollar.getRates().getPrices();
                pricesDOL.add(0,0.0);
                updateTableDol(monedasDOL,pricesDOL);
            }
            },      new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    db=SQLiteRates.getReadableDatabase();
                    Cursor cursor=db.rawQuery("Select * from RatesDOL", null);
                    if(cursor.getCount()>0) {
                        monedasDOL=new ArrayList<>();
                        pricesDOL=new ArrayList<>();
                        while (cursor.moveToNext()) {
                            monedasDOL.add(cursor.getString(0));
                            pricesDOL.add(cursor.getDouble(1));
                        }
                    }
                }
            }
        );
        editTextEuro.setTypeface(face);
        textViewCambio.setTypeface(face);
// Añadimos la petición a la cola de peticiones

        AppController.getInstance().addToRequestQueue(jsonObjReqEUR, tag_json_obj);
        AppController.getInstance().addToRequestQueue(jsonObjReqDOL, tag_json_obj);
    }


    public View crearItemPersonalizadoEUR(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = getLayoutInflater();
        View miFila = inflater.inflate(R.layout.itemmoneda, parent, false);
        TextView nombre = (TextView) miFila.findViewById(R.id.textViewMoneda);
        nombre.setText(monedasEUR.get(position));
        ImageView flag = (ImageView) miFila.findViewById(R.id.imageViewFlag);
        flag.setImageResource(Euroflags[position]);
        TextView precio = (TextView) miFila.findViewById(R.id.textViewPrecio);
        precio.setText(String.valueOf(pricesEUR.get(position)));
        nombre.setTypeface(face);
        precio.setTypeface(face);
        return miFila;
    }

    public View crearItemPersonalizadoDOL(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = getLayoutInflater();
        View miFila = inflater.inflate(R.layout.itemmoneda, parent, false);
        TextView nombre = (TextView) miFila.findViewById(R.id.textViewMoneda);
        nombre.setText(monedasDOL.get(position));
        ImageView flag = (ImageView) miFila.findViewById(R.id.imageViewFlag);
        flag.setImageResource(Dollarflags[position]);
        TextView precio = (TextView) miFila.findViewById(R.id.textViewPrecio);
        precio.setText(String.valueOf(pricesDOL.get(position)));
        nombre.setTypeface(face);
        precio.setTypeface(face);
        return miFila;
    }

    public class AdaptadorPersonal extends ArrayAdapter<String> {

        public AdaptadorPersonal(Context context, int resource, ArrayList<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return euro?crearItemPersonalizadoEUR(position, convertView, parent):crearItemPersonalizadoDOL(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return euro?crearItemPersonalizadoEUR(position, convertView, parent):crearItemPersonalizadoDOL(position, convertView, parent);
        }
    }

    public void actualizarCambio(){
        try {
            euros = editTextEuro.getText().toString().length() == 0 ? 0 : Integer.parseInt(editTextEuro.getText().toString());
            cambio = euro ? pricesEUR.get(posicion) : pricesDOL.get(posicion);
            textViewCambio.setText(String.valueOf(String.format("%.4f", euros * cambio)));
            editTextEuro.setSelectAllOnFocus(true);
        }catch(Exception e){
            editTextEuro.setText("0");
            actualizarCambio();
        }
    }

    private void updateTableEur(ArrayList<String> monedasEUR, ArrayList<Double> pricesEUR){
        Iterator itNames;
        Iterator itPrices;
        String name;
        Double price;
        db=SQLiteRates.getWritableDatabase();
        db.execSQL("DELETE FROM RatesEUR");
        itNames = monedasEUR.iterator();
        itPrices = pricesEUR.iterator();
        while(itNames.hasNext()){
            name = itNames.next().toString();
            price = Double.valueOf(itPrices.next().toString());
            db.execSQL("INSERT INTO RatesEUR (name, price) VALUES ('" + name + "', " + price + ")");
        }
        db.execSQL("DELETE FROM Date");
        db.execSQL("INSERT INTO Date (date) VALUES ('" + date + "')");
    }

    private void updateTableDol(ArrayList<String> monedasDOL, ArrayList<Double> pricesDOL){
        Iterator itNames;
        Iterator itPrices;
        String name;
        Double price;
        db=SQLiteRates.getWritableDatabase();
        db.execSQL("DELETE FROM RatesDOL");
        itNames = monedasDOL.iterator();
        itPrices = pricesDOL.iterator();
        while(itNames.hasNext()) {
            name = itNames.next().toString();
            price = Double.valueOf(itPrices.next().toString());
            db.execSQL("INSERT INTO RatesDOL (name, price) VALUES ('" + name + "', " + price + ")");
        }
    }

    private void lockApp(){
        Toast.makeText(getApplicationContext(),"You must connect to internet to load currencies",Toast.LENGTH_LONG).show();
        //imageViewEuro.setImageAlpha(100);
        spinner.setClickable(false);
        buttonEuro.setClickable(false);
        buttonDollar.setClickable(false);
        editTextEuro.setEnabled(false);
        progress.dismiss();
    }
}
