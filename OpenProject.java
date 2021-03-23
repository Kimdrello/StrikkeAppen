package no.hiof.kimandre.strikkeappen;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OpenProject extends AppCompatActivity {
    private ProgressDialog mProgress;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_project);

        mProgress = new ProgressDialog(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setLogo(R.mipmap.ic_launcher_foreground);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        final StorageReference storage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("StrikkeAppen").child("saved_grids");

        Intent intent = getIntent();
        final String filepath = intent.getExtras().getString("ImagePath");

        final PhotoView IV = (PhotoView) findViewById(R.id.OpenProjectImageView);
        Bitmap bm = BitmapFactory.decodeFile(filepath);
        IV.setImageBitmap(bm);

        Button btnDeleteProject = (Button) findViewById(R.id.deleteProjectBtn);
        btnDeleteProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener areYouSureDialogBox = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int yesOrNo) {
                        switch (yesOrNo){
                            case DialogInterface.BUTTON_POSITIVE:
                                File file = new File(filepath);
                                file.delete();
                                String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                                File myDir = new File(root + "/StrikkeAppen/saved_grids");
                                if (!myDir.exists() || myDir.listFiles().length == 0) {
                                    Intent intent = new Intent(OpenProject.this, StartUp.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                                else{
                                    Intent intent = new Intent(OpenProject.this, SavedProjects.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(OpenProject.this);
                builder.setMessage("Er du sikker på at du vil slette strikkemønsteret?").setPositiveButton("Ja", areYouSureDialogBox)
                        .setNegativeButton("Nei", areYouSureDialogBox).show();
            }
        });

        Button btnOpenProject = (Button) findViewById(R.id.shareProjectBtn);
        btnOpenProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener areYouSureDialogBox = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int yesOrNo) {
                        switch (yesOrNo){
                            case DialogInterface.BUTTON_POSITIVE:
                                mProgress.show();

                                final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
                                String currentDateandTime = dateFormat.format(new Date());
                                String fileName = "sharedGrid_" + currentDateandTime + ".png";

                                Uri file = Uri.fromFile(new File(filepath));

                                StorageReference riversRef = storage.child("StrikkeAppen").child("saved_grids").child(fileName);

                                riversRef.putFile(file)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Uploads upload = new Uploads(taskSnapshot.getDownloadUrl().toString());

                                                String uploadID = myRef.push().getKey();
                                                myRef.child(uploadID).setValue(upload);

                                                mProgress.dismiss();
                                                Toast.makeText(OpenProject.this, "Strikkemønsteret er nå delt.", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                mProgress.dismiss();
                                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                                mProgress.setMessage("Deler strikkemønster: " + ((int) progress) + "%...");
                                            }
                                        });
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(OpenProject.this);
                builder.setMessage("Er du sikker på at du vil dele dette strikkemønsteret?").setPositiveButton("Ja", areYouSureDialogBox)
                        .setNegativeButton("Nei", areYouSureDialogBox).show();
            }
        });
    }
    //Kilder:
    //https://stackoverflow.com/questions/5486529/delete-file-from-internal-storage
    //https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
}
