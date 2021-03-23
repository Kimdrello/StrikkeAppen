package no.hiof.kimandre.strikkeappen;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import pub.devrel.easypermissions.EasyPermissions;


public class CameraView extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_LOAD_IMAGE = 2;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private boolean useCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setLogo(R.mipmap.ic_launcher_foreground);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        useCamera = getIntent().getBooleanExtra("UseCamera?", false);

        if(!EasyPermissions.hasPermissions(this, galleryPermissions)) {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    101, galleryPermissions);
        }
        try{
            if(useCamera){
                takePictureIntent();
            }
            else{
                pickImageIntent();
            }
        }
        catch (Exception e){
            Toast.makeText(CameraView.this, "OBS! noe gikk galt: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        //Kilde:
        //https://stackoverflow.com/questions/42571558/bitmapfactory-unable-to-decode-stream-java-io-filenotfoundexception
    }

    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        //Kilder:
        //https://developer.android.com/training/camera/photobasics.html
    }

    public void pickImageIntent() {
        Intent getPictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(getPictureIntent, RESULT_LOAD_IMAGE);
    }
    //Kilder:
    //https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bm = null;

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor c = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumn[0]);
            String imagePath = c.getString(columnIndex);
            c.close();
            bm = BitmapFactory.decodeFile(imagePath);
        }
        else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && null != data){
            bm = (Bitmap)data.getExtras().get("data");
        }
        else{
            Toast.makeText(getApplicationContext(), "Avbrutt av bruker.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CameraView.this, StartUp.class);
            startActivity(intent);
        }


        ImageView IV = (ImageView) findViewById(R.id.imageView);
        IV.setImageBitmap(bm);

        final Bitmap bitmapToBePassed = bm;
        final String legalInput = "^[0-9]*$";

        Button btn = (Button) findViewById(R.id.generateBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView xValue = (TextView) findViewById(R.id.xValue);
                TextView yValue = (TextView) findViewById(R.id.yValue);
                int xVal;
                int yVal;
                if( xValue.getText().toString().matches(legalInput) && yValue.getText().toString().matches(legalInput)){
                    xVal = Integer.parseInt(xValue.getText().toString());
                    yVal = Integer.parseInt(yValue.getText().toString());
                    if(xVal <= 100 && yVal <= 100){
                        if(xVal > 0 && yVal > 0){
                            Intent intent = new Intent(CameraView.this, NewProjectGenerated.class);
                            intent.putExtra("ImageToBeGenerated", imageToBeGenerated(bitmapToBePassed, xVal, yVal) );
                            intent.putExtra("CameraUsed?", useCamera);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Ugyldige verdier tastet inn.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "For store verdier tastet inn. 100 er max.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Ugyldige verdier tastet inn.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Kilder:
        //https://stackoverflow.com/questions/17853360/check-entered-value-is-number-or-not
    }
    public Bitmap imageToBeGenerated(Bitmap bm, int xVal, int yVal){
        final Bitmap scaledBitmap = bm.createScaledBitmap(bm, xVal, yVal, false);
        return rotateImage(scaledBitmap, 270);
    }
    //Kilder:
    //https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    //Kilder:
    //https://acomputerengineer.wordpress.com/2016/06/07/flip-imagebitmap-horizontally-and-vertically-in-android/
    //https://stackoverflow.com/questions/14066038/why-does-an-image-captured-using-camera-intent-gets-rotated-on-some-devices-on-a
}
