package com.example.wedeliver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class registration extends AppCompatActivity {
    EditText name,email,address,password,cpassword,number;
    Button signup,select;
    ImageView imageView;
    FirebaseAuth firebaseAuth;
    Uri selectedImage;
    private static int RESULT_LOAD_IMAGE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        number=findViewById(R.id.mobile);
        imageView=findViewById(R.id.profile_pic);
        password=findViewById(R.id.password);
        cpassword=findViewById(R.id.cpassword);
        select=findViewById(R.id.selectImage);
        signup=findViewById(R.id.register);
        firebaseAuth=FirebaseAuth.getInstance();
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString()!=null && email.getText().toString()!=null && address.getText().toString()!=null && password.getText().toString()!=null && cpassword.getText().toString()!=null && selectedImage!=null)
                {

                    if((password.getText().toString()).equals(cpassword.getText().toString()))
                    {
                        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    ProgressDialog progressDialog
                                            = new ProgressDialog(view.getContext());
                                    progressDialog.setTitle("Uploading...");
                                    progressDialog.show();
                                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                                    String useremail=email.getText().toString();
                                    String newemail=useremail.replace(".","");
                                    DatabaseReference databaseReference=firebaseDatabase.getReference("users").child(newemail);
                                    databaseReference.child("name").setValue(name.getText().toString());
                                    databaseReference.child("email").setValue(email.getText().toString());
                                    databaseReference.child("phonenumber").setValue(number.getText().toString());
                                    databaseReference.child("address").setValue(address.getText().toString());


                                    FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
                                    StorageReference storageReference=firebaseStorage.getReference("users").child(newemail);
                                    storageReference.putFile(selectedImage)
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                                    Snackbar.make(findViewById(android.R.id.content), "Image Uploaded successfully", Snackbar.LENGTH_SHORT).show();
                                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            databaseReference.child("ImageLink").setValue(uri.toString());
                                                            progressDialog.dismiss();

                                                        }
                                                    });



                                                }})
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(@NonNull UploadTask.TaskSnapshot takeSnapshot) {
                                                    double progress
                                                            = (100.0
                                                            * takeSnapshot.getBytesTransferred()
                                                            / takeSnapshot.getTotalByteCount());
                                                    progressDialog.setMessage(
                                                            "Uploaded "
                                                                    + (int)progress + "%");
                                                }
                                            });
                                    firebaseAuth.signOut();
                                    Intent intent=new Intent(getApplicationContext(),login.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),""+task.getException(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                    else
                    {
                        Snackbar.make(view,"password and confirm password are not equal",Snackbar.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Snackbar.make(view,"fill all the details",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null) {
            selectedImage = data.getData();
            imageView.setImageURI(selectedImage);
        }
    }
}