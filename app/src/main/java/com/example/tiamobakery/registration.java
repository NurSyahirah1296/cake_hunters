package com.example.tiamobakery;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class registration extends AppCompatActivity {
    Spinner sploc;
    EditText etEmail,etPass,etPhone,etName;
    Button Regbtn;
    ImageButton mapwindowbtn;
    TextView tvlogin,tvlocation;
    ImageView imgprofile;
    User user;
    Dialog myDialogMap;
    String hlatitude,hlongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        initView();
        Regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hlatitude.length()>1){
                    registerUserInput();
                }else{
                    Toast.makeText(registration.this, "Please select your home from map", Toast.LENGTH_SHORT).show();
                }

            }
        });
        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registration.this,login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTakePicture();
            }
        });
        mapwindowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMapWindow();
            }
        });
    }

    private void dialogTakePicture() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(this.getResources().getString(R.string.dialogtakepicture));

        alertDialogBuilder
                .setMessage(this.getResources().getString(R.string.dialogtakepicturea))
                .setCancelable(false)
                .setPositiveButton(this.getResources().getString(R.string.yesbutton),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, 1);
                        }
                    }
                })
                .setNegativeButton(this.getResources().getString(R.string.nobutton),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = ThumbnailUtils.extractThumbnail(imageBitmap,400,500);
            imgprofile.setImageBitmap(imageBitmap);
            imgprofile.buildDrawingCache();
            ContextWrapper cw = new ContextWrapper(this);
            File pictureFileDir = cw.getDir("basic", Context.MODE_PRIVATE);
            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdir();
            }
            Log.e("FILE NAME", "" + pictureFileDir.toString());
            if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
                return;
            }
            FileOutputStream outStream = null;
            String photoFile = "profile.jpg";
            File outFile = new File(pictureFileDir, photoFile);
            try {
                outStream = new FileOutputStream(outFile);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerUserInput() {
        String email,pass,phone,name,location;
        email = etEmail.getText().toString();
        pass = etPass.getText().toString();
        phone = etPhone.getText().toString();
        name = etName.getText().toString();
        location = sploc.getSelectedItem().toString();


        user = new User(name,phone,email,pass,location,hlatitude,hlongitude);

        registerUserDialog();
    }

    private void insertData() {
        class RegisterUser extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(registration.this,
                        "Registration","...",false,false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",user.email);
                hashMap.put("password",user.password);
                hashMap.put("phone",user.phone);
                hashMap.put("name",user.name);
                hashMap.put("location",user.location);
                requesthandler rh = new requesthandler();
                String s = rh.sendPostRequest
                        ("https://tiamobakery.000webhostapp.com/cakehunters/insert_registration.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")){
                    Toast.makeText(registration.this, "Registration Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(registration.this,login.class);
                    registration.this.finish();
                    startActivity(intent);

                }else if (s.equalsIgnoreCase("nodata")){
                    Toast.makeText(registration.this, "Please fill in data first", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(registration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }

            }
        }

        RegisterUser registerUser = new RegisterUser();
        registerUser.execute();
    }

    private void registerUserDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(this.getResources().getString(R.string.registerfor)+" "+user.name+"?");

        alertDialogBuilder
                .setMessage(this.getResources().getString(R.string.registerdialognew))
                .setCancelable(false)
                .setPositiveButton(this.getResources().getString(R.string.yesbutton),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        new Encode_image().execute(getDir(),user.phone+".jpg");
                        Toast.makeText(registration.this, getResources().getString(R.string.registrationprocess), Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton(this.getResources().getString(R.string.nobutton),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public String getDir(){
        ContextWrapper cw = new ContextWrapper(this);
        File pictureFileDir = cw.getDir("basic", Context.MODE_PRIVATE);
        if (!pictureFileDir.exists()) {
            pictureFileDir.mkdir();
        }
        Log.d("GETDIR",pictureFileDir.getAbsolutePath());
        return pictureFileDir.getAbsolutePath()+"/profile.jpg";
    }

    private void initView() {
        sploc = findViewById(R.id.spinLoc);
        etEmail = findViewById(R.id.txtuserEmail);
        etPass = findViewById(R.id.txtuserpassword);
        etPhone =findViewById(R.id.userphone);
        etName = findViewById(R.id.txtUsername);
        Regbtn = findViewById(R.id.regbtn);
        tvlogin = findViewById(R.id.alreadyreg);
        imgprofile = findViewById(R.id.userpic);
        tvlocation = findViewById(R.id.textLocation);
        mapwindowbtn = findViewById(R.id.btnMap);
    }

    public class Encode_image extends AsyncTask<String,String,Void> {
        private String encoded_string, image_name;
        Bitmap bitmap;

        @Override
        protected Void doInBackground(String... args) {
            String filname = args[0];
            image_name = args[1];
            bitmap = BitmapFactory.decodeFile(filname);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            byte[] array = stream.toByteArray();
            encoded_string = Base64.encodeToString(array, 0);
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            makeRequest(encoded_string, image_name);
        }

        private void makeRequest(final String encoded_string, final String image_name) {
            class UploadAll extends AsyncTask<Void, Void, String> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("encoded_string", encoded_string);
                    hashMap.put("image_name", image_name);
                    hashMap.put("email",user.email);
                    hashMap.put("password",user.password);
                    hashMap.put("phone",user.phone);
                    hashMap.put("name",user.name);
                    hashMap.put("location",user.location);
                    hashMap.put("latitude",user.hlatitude);
                    hashMap.put("longitude",user.hlongitude);
                    requesthandler rh = new requesthandler();
                    String s = rh.sendPostRequest("https://tiamobakery.000webhostapp.com/cakehunters/insert_registration.php", hashMap);
                    return s;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (s.equalsIgnoreCase("Success")) {
                        Toast.makeText(registration.this, "Registration Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(registration.this,login.class);
                        registration.this.finish();
                        startActivity(intent);
                    }else{
                        Toast.makeText(registration.this, "Failed Registration", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            UploadAll uploadall = new UploadAll();
            uploadall.execute();
        }
    }

    private void loadMapWindow() {
        myDialogMap = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        myDialogMap.setContentView(R.layout.mapwindow);
        myDialogMap.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnsavemap = myDialogMap.findViewById(R.id.savelocationbtn);
        MapView mMapView = myDialogMap.findViewById(R.id.mapsView);
        MapsInitializer.initialize(this);
        mMapView.onCreate(myDialogMap.onSaveInstanceState());
        mMapView.onResume();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                LatLng allpos;
                LatLng posisiabsen = new LatLng(6.413395, 100.426868);
                googleMap.addMarker(new MarkerOptions().position(posisiabsen).title("HOME").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
                if (ActivityCompat.checkSelfPermission(registration.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(registration.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        googleMap.clear();
                        hlatitude = String.valueOf(latLng.latitude);
                        hlongitude = String.valueOf(latLng.longitude);
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("New Home").icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).showInfoWindow();
                    }
                });
            }
        });
        btnsavemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hlatitude.length()>5){
                    myDialogMap.dismiss();
                    tvlocation.setText("https://www.google.com/maps/@"+hlatitude+","+hlongitude+",15z");

                }else{
                    Toast.makeText(registration.this, "Please select home location", Toast.LENGTH_SHORT).show();
                }

            }
        });
        myDialogMap.show();
    }
}
