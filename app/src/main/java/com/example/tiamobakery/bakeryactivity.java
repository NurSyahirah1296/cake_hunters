package com.example.tiamobakery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class bakeryactivity extends AppCompatActivity {
    TextView tvrname,tvrphone,tvraddress,tvrloc;
    ImageView imgBakery;
    ListView lvcake;
    Dialog myDialogWindow;
    ArrayList<HashMap<String, String>> cakelist;
    String userid,bakeryid,userphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bakeryactivity);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bakeryid = bundle.getString("bakeryid");
        String bname = bundle.getString("name");
        String bphone = bundle.getString("phone");
        String baddress = bundle.getString("address");
        String blocation = bundle.getString("location");
        userid = bundle.getString("userid");
        userphone = bundle.getString("userphone");
        initView();
        tvrname.setText(bname);
        tvraddress.setText(baddress);
        tvrphone.setText(bphone);
        tvrloc.setText(blocation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Picasso.with(this).load("https://tiamobakery.000webhostapp.com/cakehunters/images/"+bakeryid+".jpg")
                .fit().into(imgBakery);

        lvcake.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showCakeDetail(position);
            }
        });
        loadCakes(bakeryid);

    }

    private void showCakeDetail(int p) {
        myDialogWindow = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        myDialogWindow.setContentView(R.layout.dialogwindow);
        myDialogWindow.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView tvfname,tvfprice,tvfquan;
        final ImageView imgcake = myDialogWindow.findViewById(R.id.ivfood);
        final Spinner spquan = myDialogWindow.findViewById(R.id.spinner2);
        Button btnorder = myDialogWindow.findViewById(R.id.orderbtn);
        final ImageButton btnfb = myDialogWindow.findViewById(R.id.FBbtn);
        tvfname= myDialogWindow.findViewById(R.id.textView12);
        tvfprice = myDialogWindow.findViewById(R.id.textView13);
        tvfquan = myDialogWindow.findViewById(R.id.textView14);
        tvfname.setText(cakelist.get(p).get("cakename"));
        tvfprice.setText(cakelist.get(p).get("cakeprice"));
        tvfquan.setText(cakelist.get(p).get("cakequantity"));
        final String cakeid =(cakelist.get(p).get("cakeid"));
        final String cakename = cakelist.get(p).get("cakename");
        final String cakeprice = cakelist.get(p).get("cakeprice");
        btnorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fquan = spquan.getSelectedItem().toString();
                dialogOrder(cakeid,cakename,fquan,cakeprice);
            }
        });

        btnfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap image = ((BitmapDrawable)imgcake.getDrawable()).getBitmap();
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();

                ShareDialog shareDialog = new ShareDialog(bakeryactivity.this);
                shareDialog.show(content);
            }
        });
        int quan = Integer.parseInt(cakelist.get(p).get("cakequantity"));
        List<String> list = new ArrayList<String>();
        for (int i = 1; i<=quan;i++){
            list.add(""+i);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spquan.setAdapter(dataAdapter);

        Picasso.with(this).load("https://tiamobakery.000webhostapp.com/cakehunters/images/"+cakeid+".jpg")
                .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                .fit().into(imgcake);
        myDialogWindow.show();
    }

    private void dialogOrder(final String cakeid, final String cakename, final String fquan, final String cakeprice) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Order "+cakename+ " with quantity "+fquan);

        alertDialogBuilder
                .setMessage("Are you sure")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        insertCart(cakeid,cakename,fquan,cakeprice);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void insertCart(final String cakeid, final String cakename, final String fquan, final String cakeprice) {
        class InsertCart extends AsyncTask<Void,Void,String>{

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("cakeid",cakeid);
                hashMap.put("bakeryid",bakeryid);
                hashMap.put("cakename",cakename);
                hashMap.put("quantity",fquan);
                hashMap.put("cakeprice",cakeprice);
                hashMap.put("userid",userphone);
                requesthandler rh = new requesthandler();
                String s = rh.sendPostRequest("https://tiamobakery.000webhostapp.com/cakehunters/insert_cart.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s.equalsIgnoreCase("success")){
                    Toast.makeText(bakeryactivity.this, "Success", Toast.LENGTH_SHORT).show();
                    myDialogWindow.dismiss();
                    loadCakes(bakeryid);
                }else{
                    Toast.makeText(bakeryactivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

        }
        InsertCart insertCart = new InsertCart();
        insertCart.execute();
    }

    private void loadCakes(final String bakeryid) {
        class LoadCake extends AsyncTask<Void,Void,String>{

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("bakeryid",bakeryid);
                requesthandler requestHandler = new requesthandler();
                String s = requestHandler.sendPostRequest("https://tiamobakery.000webhostapp.com/cakehunters/load_foods.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                cakelist.clear();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray cakearray = jsonObject.getJSONArray("cake");
                    for (int i = 0; i < cakearray.length(); i++) {
                        JSONObject c = cakearray.getJSONObject(i);
                        String jsid = c.getString("cakeid");
                        String jsfname = c.getString("cakename");
                        String jsfprice = c.getString("cakeprice");
                        String jsquan = c.getString("quantity");
                        HashMap<String,String> cakelisthash = new HashMap<>();
                        cakelisthash.put("cakeid",jsid);
                        cakelisthash.put("cakename",jsfname);
                        cakelisthash.put("cakeprice",jsfprice);
                        cakelisthash.put("cakequantity",jsquan);
                        cakelist.add(cakelisthash);
                    }
                }catch(JSONException e){}
                ListAdapter adapter = new customadaptercake(
                        bakeryactivity.this, cakelist,
                        R.layout.cakelistbakery, new String[]
                        {"cakename","cakeprice","cakequantity"}, new int[]
                        {R.id.tv1,R.id.tv2,R.id.tv3});
                lvcake.setAdapter(adapter);

            }
        }
        LoadCake loadCake = new LoadCake();
        loadCake.execute();
    }

    private void initView() {
        imgBakery = findViewById(R.id.imageView3);
        tvrname = findViewById(R.id.textView6);
        tvrphone = findViewById(R.id.textView7);
        tvraddress = findViewById(R.id.textView8);
        tvrloc = findViewById(R.id.textView9);
        lvcake = findViewById(R.id.listcake);
        cakelist = new ArrayList<>();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(bakeryactivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userid",userid);
                bundle.putString("phone",userphone);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
