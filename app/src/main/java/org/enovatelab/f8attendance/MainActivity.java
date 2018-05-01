package org.enovatelab.f8attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    FirebaseFirestore AttendeesDB = FirebaseFirestore.getInstance();
    public static final int REQUEST_CODE = 100;

    private String TAG = "FireLog";

    TextView AttendeeCodeID;

    private static final String LOG = "FireLog";

    private RecyclerView AttendeeList;
    private FirebaseFirestore mFireStore;

    private List<DataModel> AttendeeAdapter;

    private DataAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        AttendeeCodeID = findViewById(R.id.AttendeeCodeID);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Scanner.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


        AttendeeList = findViewById(R.id.AttendeesList);
        mFireStore = FirebaseFirestore.getInstance();
        
        AttendeeAdapter = new ArrayList<>();
        dataAdapter = new DataAdapter(AttendeeAdapter);

        AttendeeList.setHasFixedSize(true);
        AttendeeList.setLayoutManager(new LinearLayoutManager(this));
        AttendeeList.setAdapter(dataAdapter);

        mFireStore.collection("Attendees").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Log.d(LOG, "Error" + e.getMessage());
                }

                for (DocumentChange doc : documentSnapshots.getDocumentChanges()){

                    if (doc.getType() == DocumentChange.Type.ADDED){

                        DataModel dataModel = doc.getDocument().toObject(DataModel.class);
                        AttendeeAdapter.add(dataModel);
                        dataAdapter.notifyDataSetChanged();
                        //Log.d(LOG, "Name" + product_name);
                    }
                }
            }
        });
        
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if (data != null){
                final Barcode barcode = data.getParcelableExtra("Barcode");
                AttendeeCodeID.post(new Runnable() {
                    @Override
                    public void run() {
                        AttendeeCodeID.setText(barcode.displayValue);
                        String AC = AttendeeCodeID.getText().toString().trim();

                        if (!TextUtils.isEmpty(AC)){
                            Map<String, Object> attendeeToAdd = new HashMap<>();
                            attendeeToAdd.put("AttendeeCode", AC);

                            AttendeesDB.collection("Attendees")
                                    .add(attendeeToAdd)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "Saved Successfully" + documentReference.getId());
                                        }
                                    });
                        }
                    }
                });
            }
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if (data != null){
                final Barcode barcode = data.getParcelableExtra("Barcode");

                AttendeeCodeID.post(new Runnable() {
                    @Override
                    public void run() {
                        AttendeeCodeID.setText(barcode.displayValue);

                        String AC = AttendeeCodeID.getText().toString().trim();

                        Map<String, Object> attendeeToAdd = new HashMap<>();
                        attendeeToAdd.put("AttendeeCode", AC);

                        AttendeesDB.collection("Attendees")
                                .add(attendeeToAdd)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "Saved Successfully" + documentReference.getId());
                                    }
                                });
                    }
                });
            }
        }
    }*/
}
