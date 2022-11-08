package com.example.barterx;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barterx.algorithms.BitmapConverterFromUrl;
import com.example.barterx.databinding.ActivityListingsBinding;
import com.example.barterx.ml.MobilenetV110224Quant;
import com.example.barterx.model.ListingDto;
import com.example.barterx.model.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Listings extends AppCompatActivity {

    private ActivityListingsBinding activityListingsBinding;
    private final int GALLERY_REQ_CODE = 1000;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private FirebaseUser user;
    private StorageReference storageReference;
    private List<String> listingImages = new ArrayList<>();
    private Bitmap imageToClassify;

    private int[] images = {
            R.drawable.a, R.drawable.b, R.drawable.c,
            R.drawable.d, R.drawable.e, R.drawable.f
    };
    private Uri[] Images = new Uri[3];
    ImageSwitcher imageSwitcher;
    Button next, prev, addNew, cont;
    int index = 0;

    String[] conditionItems = {
            "New", "Used - Like New", "Used - Partly Damaged", "Used - Damaged", "Used - Not Recoverable"
    };
    AutoCompleteTextView condition;
    ArrayAdapter<String> conditionAdapterItems;


    String[] categoryItems = {
            "Vehicles", "Property", "Classifieds", "Clothing", "Electronics", "Entertainment", "Family",
            "Garden and Outdoors", "Hobbies", "Home Goods", "Home Improvement Supplies", "Musical Instruments",
            "Office Supplies", "Sporting Goods", "Toys & Games", "Beauty Products", "Baby Care"
    };
    AutoCompleteTextView category;
    ArrayAdapter<String> categoryAdapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityListingsBinding = ActivityListingsBinding.inflate(getLayoutInflater());
        setContentView(activityListingsBinding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("imageDir");


        next = findViewById(R.id.buttonNext);
        prev = findViewById(R.id.buttonPrev);
        addNew = findViewById(R.id.buttonAddNewImage);
        imageSwitcher = findViewById(R.id.imageSwitcher);

        /**
         * Drop Down list stuff
         */

        cont = findViewById(R.id.buttonContinue);
        condition = findViewById(R.id.txtCondition);
        conditionAdapterItems = new ArrayAdapter<>(this, R.layout.catergory_list_items, conditionItems);
        condition.setAdapter(conditionAdapterItems);
        condition.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), "Item : " + item, Toast.LENGTH_SHORT).show();
            }
        });

        category = findViewById(R.id.txtCategory);
        categoryAdapterItems = new ArrayAdapter<>(this, R.layout.catergory_list_items, categoryItems);
        category.setAdapter(categoryAdapterItems);
        category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), "Item : " + item, Toast.LENGTH_SHORT).show();
            }
        });

        /***
         * ImageSwitcher Stuff
         */

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Images != null) index++;
                if (index == Images.length)
                    index = 0;
                imageSwitcher.setImageURI(Images[index]);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Images != null) index--;
                if (index < 0)
                    index = Images.length - 1;
                imageSwitcher.setImageURI(Images[index]);
            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {

                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setMaxWidth(250);
                imageView.setMaxHeight(250);
                return imageView;
            }
        });

        if (Images != null) imageSwitcher.setImageURI(Images[index]);

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });
    }

    private void pickImage() {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, GALLERY_REQ_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
                imageUri = data.getData();
                if (imageUri != null)
                    imageSwitcher.setImageURI(imageUri);
                Images[index] = imageUri;
            }
        }

    }


    private void upload() {
        activityListingsBinding.simpleProgressBar.setVisibility(View.VISIBLE);
        for (Uri imgUri : Images) {
            final String imageKey = UUID.randomUUID().toString();
            StorageReference fileRef = storageReference.child("listing/" + imageKey);
            fileRef.putFile(imgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String imgUrl = task.getResult().toString();
                                    listingImages.add(imgUrl);
                                    if(listingImages.size() == Images.length){
                                        uploadMetaData();
                                    }
                                }
                            });
                            activityListingsBinding.simpleProgressBar.setVisibility(View.INVISIBLE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            activityListingsBinding.simpleProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Image Upload Failed", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void uploadMetaData() {
        activityListingsBinding.simpleProgressBar.setVisibility(View.VISIBLE);
        DocumentReference documentReference = database.collection("users").document(user.getEmail());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Profile usr = document.toObject(Profile.class);
                        ListingDto dto = new ListingDto(
                                activityListingsBinding.txtProductName.getText().toString(),
                                activityListingsBinding.txtCategory.getText().toString(),
                                activityListingsBinding.txtCondition.getText().toString(),
                                activityListingsBinding.txtDescription.getText().toString()
                        );
                        dto.setLatitude(usr.getLatitude());
                        dto.setLongitude(usr.getLogitude());
                        dto.setMerchantId(user.getUid());
                        dto.setListingImages(listingImages);
                        dto.setProductId(UUID.randomUUID().toString());
                        database.collection("listing").add(dto)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Listing Completed", Toast.LENGTH_LONG).show();
                                            clear();
                                            activityListingsBinding.simpleProgressBar.setVisibility(View.INVISIBLE);
                                            startActivity(new Intent(Listings.this, Menu.class));
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    public  void  clear(){
        activityListingsBinding.txtCategory.setText("");
        activityListingsBinding.txtCondition.setText("");
        activityListingsBinding.txtDescription.setText("");
        activityListingsBinding.txtProductName.setText("");
        listingImages.clear();
        Images = null;
    }


}