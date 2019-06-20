package com.muhammadrizqip.roadtrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class listWisata extends AppCompatActivity {

    List <model> modelList = new ArrayList<>();
    RecyclerView rec_listwisata;

    // layout mmanager for reclerview
    RecyclerView.LayoutManager layoutManager;
    // firestore instance
    FirebaseFirestore db;
    CustomAdapter adapter;

    FloatingActionButton addbtn;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_wisata);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Data Wisata");

        addbtn = findViewById(R.id.addbtn);
        // initialize firestore
        db = FirebaseFirestore.getInstance();
        // initialize views
        rec_listwisata = findViewById(R.id.rec_listwisata);

        // set recler view propertis
        rec_listwisata.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rec_listwisata.setLayoutManager(layoutManager);


        pd = new ProgressDialog(this);
        // show data in recyclerView
        tampilDataWisata();
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(listWisata.this, WisataActivity.class));
                finish();
            }
        });
    }

    private void tampilDataWisata() {
        pd.setTitle("Loading Data");
        pd.show();
        db.collection("Kategori").document("Museum").collection("List_museum")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        modelList.clear();
                        pd.dismiss();
                        for (DocumentSnapshot doc : task.getResult()){
                           model mod  = new model(doc.getString("id"),
                                   doc.getString("nama"),
                                   doc.getLong("biaya").intValue());
                           modelList.add(mod);
                        }
                        //adapter
                        adapter = new CustomAdapter(listWisata.this, modelList);
                        //set adapter to recycleview
                        rec_listwisata.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(listWisata.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void hapusList(int index){
        pd.setTitle("Deleting Data");
        pd.show();
        db.collection("Kategori").document("Museum").collection("List_museum")
                .document(modelList.get(index).getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(listWisata.this, "DATA DELETED", Toast.LENGTH_SHORT).show();
                        tampilDataWisata();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(listWisata.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void caridata(String query) {
        pd.setMessage("Searching wisata ....");
        pd.show();
        db.collection("Kategori").document("Museum")
                .collection("List_museum").whereEqualTo("search", query.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        modelList.clear();
                        pd.dismiss();
                        for (DocumentSnapshot doc : task.getResult()){
                            model mod  = new model(doc.getString("id"),
                                    doc.getString("nama"),
                                    doc.getLong("biaya").intValue());
                            modelList.add(mod);
                        }
                        //adapter
                        adapter = new CustomAdapter(listWisata.this, modelList);
                        //set adapter to recycleview
                        rec_listwisata.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(listWisata.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating main_menu.xml
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.cari);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // callled when we preess search buton
                caridata(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //called when we type even a single letter
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handel other menu items clicks here
        if (item.getItemId()== R.id.setting){
            Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
