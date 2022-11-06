package com.example.barterx;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.barterx.databinding.ActivityListingsBinding;

import java.net.URL;
import java.util.ArrayList;

public class Listings extends Drawer_Base {

    ActivityListingsBinding activityListingsBinding;

    int[] images = {
            R.drawable.a, R.drawable.b, R.drawable.c,
            R.drawable.d, R.drawable.e, R.drawable.f
    };
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
        allocateActivityTitle("LISTINGS");

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
                index++;
                if(index == images.length)
                    index = 0;
                imageSwitcher.setImageResource(images[index]);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index--;
                if(index < 0)
                    index = images.length - 1;
                imageSwitcher.setImageResource(images[index]);
            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "New image code will go here",
                        Toast.LENGTH_SHORT).show();
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

        imageSwitcher.setImageResource(images[index]);

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Continue", Toast.LENGTH_SHORT).show();
            }
        });
    }
}