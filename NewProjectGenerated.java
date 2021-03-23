package no.hiof.kimandre.strikkeappen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.github.chrisbanes.photoview.PhotoView;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewProjectGenerated extends AppCompatActivity {
    private boolean useCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project_generated);
        Intent intent = getIntent();
        Bitmap bm = (Bitmap) intent.getParcelableExtra("ImageToBeGenerated");
        useCamera = intent.getBooleanExtra("CameraUsed?", false);

        final PhotoView IV = (PhotoView) findViewById(R.id.newProjectImageView);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setLogo(R.mipmap.ic_launcher_foreground);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        int newX = bm.getWidth();
        int newY = bm.getHeight();

        int[][] pixels = new int[newX][newY];
        for(int y = 0; y < newY; y++){
            for(int x = 0; x < newX; x++){
                pixels[x][y] = bm.getPixel(x, y);
            }
        }
        final PixelGridView pixelGrid = new PixelGridView(this);
        int gridSize = 0;

        if(newX > newY){
            gridSize = 20*newX;
            pixelGrid.setDimensions(gridSize/newX);
        }
        else{
            gridSize = 20*newY;
            pixelGrid.setDimensions(gridSize/newY);
        }

        pixelGrid.setNumColumns(newY);
        pixelGrid.setNumRows(newX);
        pixelGrid.setPixels(pixels);
        final Bitmap generatedGrid = getBitmapFromView(pixelGrid, gridSize + (gridSize/newX), gridSize + (gridSize/newY));
        IV.setImageBitmap(generatedGrid);

        Button btnSaveGrid = (Button) findViewById(R.id.newPictureBtn);
        btnSaveGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewProjectGenerated.this, CameraView.class);
                intent.putExtra("UseCamera?", useCamera);
                startActivity(intent);
            }
        });

        Button btnTakeNewPicture = (Button) findViewById(R.id.saveGridBtn);
        btnTakeNewPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewProjectGenerated.this, StartUp.class);
                String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                File myDir = new File(root + "/StrikkeAppen/saved_grids");
                if (!myDir.exists()) {
                    myDir.mkdirs();
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
                String currentDateandTime = dateFormat.format(new Date());
                String fileName = "generatedGrid_" + currentDateandTime + ".png";
                File file = new File(myDir, fileName);
                if (file.exists ()) file.delete ();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    generatedGrid.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    Toast.makeText(getApplicationContext(), "Lagring Vellykket!", Toast.LENGTH_SHORT).show();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Lagring mislykket: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //Kilder:
    //https://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
    //https://www.youtube.com/watch?v=mPOhnTnLcSY
    //https://github.com/chrisbanes/PhotoView

    public static Bitmap getBitmapFromView(View view, int width, int height) {
        Bitmap returnedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }
    //Kilde:
    //https://stackoverflow.com/questions/5536066/convert-view-to-bitmap-on-android
    //https://stackoverflow.com/questions/15662258/how-to-save-a-bitmap-on-internal-storage
    //https://stackoverflow.com/questions/5369682/get-current-time-and-date-on-android
}
