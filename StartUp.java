package no.hiof.kimandre.strikkeappen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import pub.devrel.easypermissions.EasyPermissions;

public class StartUp extends AppCompatActivity {
    private String[] galleryPermissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setLogo(R.mipmap.ic_launcher_foreground);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        if(!EasyPermissions.hasPermissions(StartUp.this, galleryPermissions)) {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    101, galleryPermissions);
        }

        Button btnStartNewProject = (Button) findViewById(R.id.StartNewProject);
        btnStartNewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartUp.this, NewProject.class);
                startActivity(intent);
            }
        });

        Button btnOpenProject = (Button) findViewById(R.id.openProjectsBtn);
        btnOpenProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File myDir = new File(root + "/StrikkeAppen/saved_grids");
                    if (!myDir.exists() || myDir.listFiles().length == 0) {
                        Toast.makeText(getApplicationContext(), "Ingen strikkemønstere funnet.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent intent = new Intent(StartUp.this, SavedProjects.class);
                        startActivity(intent);
                    }
                }
                catch (Exception e){
                    Toast.makeText(StartUp.this, "OBS! noe gikk galt: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //Kilde for editing av toolbar:
    //https://developer.android.com/training/appbar/setting-up.html
    //https://stackoverflow.com/questions/5794506/android-clear-the-back-stack
}