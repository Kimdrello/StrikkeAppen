package no.hiof.kimandre.strikkeappen;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class SavedProjects extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_projects);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setLogo(R.mipmap.ic_launcher_foreground);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int dimensions = size.x;

        String filesPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/StrikkeAppen/saved_grids";

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        final ImageAdapter IA = new ImageAdapter(this, dimensions/2, filesPath);
        gridview.setAdapter(IA);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(SavedProjects.this, OpenProject.class);
                intent.putExtra("ImagePath", IA.getItem(position));
                startActivity(intent);
            }
        });
    }
    //Kilder:
    //https://stackoverflow.com/questions/8515830/how-to-add-images-on-gridview
    //https://developer.android.com/guide/topics/ui/layout/gridview.html
    //https://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels

}
