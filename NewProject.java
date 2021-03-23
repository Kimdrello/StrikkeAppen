package no.hiof.kimandre.strikkeappen;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class NewProject extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setLogo(R.mipmap.ic_launcher_foreground);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        Button btnCamera = (Button) findViewById(R.id.camerabtn);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewProject.this, CameraView.class);
                intent.putExtra("UseCamera?", true);
                startActivity(intent);
            }
        });

        Button btnImage = (Button) findViewById(R.id.imgfolderbtn);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewProject.this, CameraView.class);
                intent.putExtra("UseCamera?", false);
                startActivity(intent);
            }
        });

        Button btnFireBase = (Button) findViewById(R.id.databasebtn);
        btnFireBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewProject.this, SharedProjects.class);
                startActivity(intent);
            }
        });

    }
}
