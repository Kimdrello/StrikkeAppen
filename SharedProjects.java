package no.hiof.kimandre.strikkeappen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


public class SharedProjects extends AppCompatActivity {
    private String[] galleryPermissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ProgressDialog mProgress;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<Uploads> uploads;
    private Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_projects);

        if(!EasyPermissions.hasPermissions(SharedProjects.this, galleryPermissions)) {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    101, galleryPermissions);
        }

        mProgress = new ProgressDialog(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setLogo(R.mipmap.ic_launcher_foreground);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int dimensions = size.x;

        final GridView gridview = (GridView) findViewById(R.id.gridviewSharedProjects);

        mProgress.setMessage("Vennligst vent...");
        mProgress.show();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("StrikkeAppen").child("saved_grids");

        uploads = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Uploads upload = postSnapshot.getValue(Uploads.class);
                    uploads.add(upload);
                }
                final SharedImagesAdapter SIA = new SharedImagesAdapter(c, dimensions / 2, uploads);
                gridview.setAdapter(SIA);
                mProgress.dismiss();

                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        Intent intent = new Intent(c, SaveProjectFromDatabase.class);
                        intent.putExtra("Download URL", SIA.getItem(position).getUrl());
                        startActivity(intent);

                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgress.dismiss();
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(c, StartUp.class);
                startActivity(intent);
            }
        });
    }
}
