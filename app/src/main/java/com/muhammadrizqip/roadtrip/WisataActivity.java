package com.muhammadrizqip.roadtrip;

import android.app.ProgressDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WisataActivity extends AppCompatActivity {
    private GoogleMap mMap;
    EditText wisataet, biayaet, fasilitaset, latitudeet, longtitudeet;
    Button btsave, btshow;
    ProgressDialog pb;
    String pwisata, pbiaya,pid;

    FirebaseFirestore db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata);

        final ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("Tambah Data Wisata");

        wisataet = findViewById(R.id.wisataet);
        fasilitaset= findViewById(R.id.fasilitaset);
        longtitudeet = findViewById(R.id.longtitudeet);
        latitudeet = findViewById(R.id.latitudeet);
        biayaet = findViewById(R.id.biayaet);
        btsave = findViewById(R.id.btsave);
        btshow = findViewById(R.id.btshow);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.mapView);
//        mapFragment.getMapAsync(this);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            // update data
            actionBar.setTitle("Update Data Wisata");
            btsave.setText("UPDATE");
            //get data
            pid= bundle.getString("pid");
            pwisata = bundle.getString("pwisata");
            pbiaya = bundle.getString("pbiaya");
            wisataet.setText(pwisata);
            biayaet.setText(pbiaya);


        }else {
            //new data
            actionBar.setTitle("Tambah Data Wisata");
            btsave.setText("SAVE");
        }

        pb = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();

        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = getIntent().getExtras();
                if (bundle != null){
                    //updating
                    String id = pid;
                    String wisata = wisataet.getText().toString().trim();
                    int biaya = Integer.parseInt(biayaet.getText().toString().trim());
                    updatedata(id, biaya, wisata);
                }
                else {
                    //add new data
                    String wisata = wisataet.getText().toString().trim();
                    int biaya = Integer.parseInt(biayaet.getText().toString().trim());
                    String fasilitas = fasilitaset.getText().toString().trim();
                    simpan(wisata,biaya,fasilitas);
                }
            }
        });

        btshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WisataActivity.this, listWisata.class));
                finish();
            }
        });
    }

    private void updatedata(String id,int biaya, String wisata) {
        pb.setMessage("Updating data");
        pb.show();
        db.collection("Kategori").document("Museum").collection("List_museum").document(id)
                .update("biaya", biaya, "nama", wisata, "search", wisata.toLowerCase())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pb.dismiss();
                        Toast.makeText(WisataActivity.this, "data berhasil di update", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pb.dismiss();
                        Toast.makeText(WisataActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void simpan(final String wisata, final int biaya, final String fasilitas) {
        final int hrg;
        //final double[] bobot = new double[1];
        pb.setMessage("Sedang menyimpan data");
        pb.show();
        final String id = UUID.randomUUID().toString();
        final String id2 = UUID.randomUUID().toString();
        final String id3 = UUID.randomUUID().toString();
        Map <String, Object> obj = new HashMap<>();
        obj.put("id", id);
        obj.put("biaya", biaya);
        obj.put("nama", wisata);
        obj.put("fasilitas", fasilitas);
        obj.put("search", wisata.toLowerCase());
        db.collection("Kategori").document("Museum").collection("List_museum").document(id).set(obj)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        double bobot_biaya = 0;
                        if (biaya <= 0)
                            bobot_biaya = 0.2;
                        else if (biaya > 0 && biaya <= 10000)
                            bobot_biaya = 0.4;
                        else if (biaya > 10000 && biaya <= 20000)
                            bobot_biaya = 0.6;
                        else if (biaya > 20000 && biaya <= 40000)
                            bobot_biaya = 0.8;
                        else if (biaya > 40000)
                            bobot_biaya = 1;
                        final Map <String, Object> obj2 = new HashMap<>();
                        obj2.put("hrg", biaya);
                        obj2.put("bobot", bobot_biaya);
                        db.collection("Kategori/Museum/List_museum/"+id+"/biaya").document(id2).set(obj2).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                        double bobot_fas = 0;
                        if (fasilitas.equals("toilet umum"))
                            bobot_fas = 0.2;
                        else if (fasilitas.equals("toilet umum, parkiran"))
                            bobot_fas = 0.4;
                        else if (fasilitas.equals("parkiran, pemandu"))
                            bobot_fas = 0.6;
                        else if (fasilitas.equals("toilet umum, pemandu"))
                            bobot_fas = 0.8;
                        else if  (fasilitas.equals("toilet umum, parkiran, pemandu"))
                            bobot_fas = 1;

                        final Map <String, Object> obj3 = new HashMap<>();
                        obj3.put("hrg", fasilitas);
                        obj3.put("bobot", bobot_fas);
                        db.collection("Kategori/Museum/List_museum/"+id+"/fasilitas").document(id3).set(obj3).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                pb.dismiss();
                                Toast.makeText(WisataActivity.this, "Data Ditambahkan", Toast.LENGTH_SHORT).show();
                                wisataet.setText("");
                                biayaet.setText("");
                                wisataet.requestFocus();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pb.dismiss();
                        Toast.makeText(WisataActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
////        // Add a marker in Sydney and move the camera
////        mMap = googleMap;
////        LatLng sydney = new LatLng(-7.80133999, 110.36478996);
////        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in jogja"));
////        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
////        float zoomLevel = 18.0f; //This goes up to 21
////        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
//    }
}
