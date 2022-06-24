package com.cy.myappauth.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cy.myappauth.R;
import com.cy.myappauth.userDataTest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MainMenuActivity extends AppCompatActivity {
    //db
    DatabaseReference reference;

    //uitest_btn,test_btn,
    ImageButton btn_plan;
    Button  btn_shop,btn_cook,  btn_basket,btnPU;
    TextView text;
    ImageButton profileImBtn,btn_fridge;
    private Button btn_web;

    //  private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().setTitle("Меню");
        profileImBtn=findViewById(R.id.profileBtn);
        btn_fridge=findViewById(R.id.imageButton4);

        btn_plan = findViewById(R.id.planBtn);
        btn_shop = findViewById(R.id.shopBtn);
        btn_cook = findViewById(R.id.cookBtn);
        btn_basket = findViewById(R.id.basketBtn);
        btn_web = findViewById(R.id.webBtn);
        btnPU = findViewById(R.id.buttonPU);

        btnPU.setOnClickListener(v->PopUpOpen());
//        test_btn=findViewById(R.id.button5);
//test_btn.setOnClickListener();


        text = findViewById(R.id.testText);
        //    image = findViewById(R.id.image);
        //  profileImBtn.setImageURI(userDataTest.photo);
        Picasso.get().load(userDataTest.photo).into(profileImBtn);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/fbapp-fceca.appspot.com/o/q1.jpg?alt=media&token=6b446758-344d-4b1e-90e5-5b9eba48213d").into(btn_fridge);

//        btn_plan.setOnClickListener(v -> GoCook());
        btn_web.setOnClickListener(v -> GoWeb());
        btn_plan.setOnClickListener(v -> GoPlan());
        btn_shop.setOnClickListener(v -> GoShop());
        btn_cook.setOnClickListener(v -> GoCook());
        btn_basket.setOnClickListener(v -> GoBasket());
        btn_fridge.setOnClickListener(v -> GoFridge());

//        test_btn.setOnClickListener(v->GoADD());
        profileImBtn.setOnClickListener(v->GoProfile());
        TestAct();
        text.setText(userDataTest.userName+"\n"+userDataTest.usref);
    }

    private void PopUpOpen() {
        PopupMenu popupMenu=new PopupMenu(MainMenuActivity.this,btnPU);
        //R.menu??
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MainMenuActivity.this,"clc "+item.getTitle(),Toast.LENGTH_LONG).show();
                return true;
            }
        });
        popupMenu.show();
    }

    private void GoProfile() {
        TestAct();
        Intent intent=new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }

    private void GoFridge() {
        if(!userDataTest.isSecondUser){
            TestAct();}
        Intent intent=new Intent(this,FridgeActivity.class);
        startActivity(intent);
    }

    private void TestAct() {
        String userTofind=userDataTest.userName;
//        text.setText("must be added to db:");
        try {
            reference = FirebaseDatabase.getInstance().getReference().child("Users");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    boolean isThereUser = false;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        //  for (DataSnapshot dss : ds.getChildren()) {
                        if (ds.hasChild("name")) {
                            if (ds.child("name").getValue().equals(userTofind)) {
                                text.append("\n db:" + ds.child("name").getValue());
                                isThereUser = true;
                                userDataTest.usref = ds.getKey();
                            }
                        }
//                 if (dss.child("name").getValue() == userTofind) {
//                     text.setText(" exist in db");
//                     isThereUser = true;
//                 } else {
//                     //   text.append("\n must be added to db:");
//
//                 }
                        // }
                        // }
                    }

                    if (!isThereUser) {
                        text.append("\n new  added to db:" + userTofind);
//             DatabaseReference dr = reference.push();
//             dr.setValue(null);
//             dr.push().setValue("kjj");


                        Map<String, String> userData = new HashMap<String, String>();
                        //  reference.push().setValue(null);
                        userData.put("name", userTofind);
                        //userData.put("Holding_Stock", holdingStock);reference.child(userTofind).child("name").push().setValue(userTofind);
                        // }
                        userData.put("email", userTofind);
                        userData.put("fridge", "1");
                        // userData.put("fridge/milk", "1");
                        // }
                        userDataTest.usref = reference.push().getKey();
                        reference.child(userDataTest.usref).setValue(userData);
                        reference.child(userDataTest.usref).child("profile").child("name").setValue(userTofind);
                        reference.child(userDataTest.usref).child("profile").child("profilePic").setValue(userDataTest.photo);
                        reference.child(userDataTest.usref).child("profile").child("permission").setValue(true);
                        reference.child(userDataTest.usref).child("profile").child("ref").setValue(userDataTest.usref);

                    } else {
                        text.append("\n already in db:" + userDataTest.usref);

                        reference.child(userDataTest.usref).child("profile").child("name").setValue(userTofind);
                        reference.child(userDataTest.usref).child("profile").child("profilePic").setValue(userDataTest.photo);
                        reference.child(userDataTest.usref).child("profile").child("permission").setValue(true);
                        reference.child(userDataTest.usref).child("profile").child("ref").setValue(userDataTest.usref);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    text.setText("cancelled");
                }
            });
        }
        catch (Exception e){
            Toast.makeText(this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
    }


    private void GoBasket() {
        if(!userDataTest.isSecondUser){
            TestAct();}
//        Intent intent=new Intent(this,testBasketActivity.class);
        //  Intent intent=new Intent(this,newBasketActivity.class);
//        Intent intent=new Intent(this,BasketActivity.class);
//        Intent intent=new Intent(this,TestActivity.class);
        Intent intent=new Intent(this,CookActivity.class);
        startActivity(intent);
    }
    private void GoCook() {
        if(!userDataTest.isSecondUser){
            TestAct();}
        Intent intent=new Intent(this,DishActivity.class);
        startActivity(intent);
    } private void GoWeb() {
//        if(!userDataTest.isSecondUser){
//            TestAct();}
        Intent intent=new Intent(this,WebActivity.class);
        startActivity(intent);
    }
    private void GoADD() {


        //    Intent intent=new Intent(this,AddPhoto.class);
//        Intent intent=new Intent(this,PhotoAddActivity.class);
        //    startActivity(intent);
    }

    private void GoShop() {
        if(!userDataTest.isSecondUser){
            TestAct();}
        Intent intent=new Intent(this,ShopActivity.class);
        startActivity(intent);
    }

    private void GoPlan() {
        if(!userDataTest.isSecondUser){
            TestAct();}
//        Intent intent=new Intent(this,GridShopActivity.class);
        Intent intent=new Intent(this,BasketActivity.class);
        startActivity(intent);
    }


}