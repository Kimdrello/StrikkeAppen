package no.hiof.kimandre.strikkeappen;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveProjectFromDatabase extends AppCompatActivity {
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_project_from_database);

        mProgress = new ProgressDialog(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setLogo(R.mipmap.ic_launcher_foreground);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        final String Uri = intent.getExtras().getString("Download URL");

        final PhotoView IV = (PhotoView) findViewById(R.id.saveProjectFromDatabaseImageView);
        Glide.with(SaveProjectFromDatabase.this).load(Uri).into(IV);

        final StorageReference storageReferenceFromURL = FirebaseStorage.getInstance().getReferenceFromUrl(Uri);

        Button btnFireBase = (Button) findViewById(R.id.saveProjectFromDatabaseBtn);
        btnFireBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener areYouSureDialogBox = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int yesOrNo) {
                        switch (yesOrNo){
                            case DialogInterface.BUTTON_POSITIVE:
                                try{
                                    mProgress.show();

                                    String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                                    File myDir = new File(root + "/StrikkeAppen/saved_grids");
                                    if (!myDir.exists()) {
                                        myDir.mkdirs();
                                    }
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
                                    String currentDateandTime = dateFormat.format(new Date());
                                    String fileName = "sharedGrid_" + currentDateandTime + ".png";
                                    final File localFile = new File(myDir, fileName);
                                    if (localFile.exists ()) localFile.delete ();

                                    storageReferenceFromURL.getFile(localFile)
                                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                    Toast.makeText(SaveProjectFromDatabase.this, "Nedlasting vellykket!", Toast.LENGTH_SHORT).show();
                                                    mProgress.dismiss();
                                                    Intent intent = new Intent(SaveProjectFromDatabase.this, StartUp.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            mProgress.dismiss();
                                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                    }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                            mProgress.setMessage("Laster ned rutenett: " + ((int) progress) + "%...");
                                        }
                                    });
                                }
                                catch (Exception e){
                                    Toast.makeText(SaveProjectFromDatabase.this, "OBS! noe gikk galt: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(SaveProjectFromDatabase.this);
                builder.setMessage("Er du sikker på at du vil lagre dette strikkemønsteret?").setPositiveButton("Ja", areYouSureDialogBox)
                        .setNegativeButton("Nei", areYouSureDialogBox).show();
            }
        });
    }
    //Kilder:
    //https://stackoverflow.com/questions/39905719/how-to-download-a-file-from-firebase-storage-to-the-external-storage-of-android
}