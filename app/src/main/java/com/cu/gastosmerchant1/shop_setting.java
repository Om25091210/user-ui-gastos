package com.cu.gastosmerchant1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.number.Scale;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.gastosmerchant1.details.Account_Information;
import com.cu.gastosmerchant1.details.Discount;
import com.cu.gastosmerchant1.details.Shop_Information;
import com.cu.gastosmerchant1.registration.BasicDetails;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class shop_setting extends AppCompatActivity {

    private Switch delivery;
    private Switch pickup;

    private ImageView prev;

    private CardView edit1;
    private CardView delete1;
    private CardView edit2;
    private CardView delete2;
    private CardView edit3;
    private CardView delete3;
    private CardView edit4;
    private CardView delete4;

    //todo check this image/button
    private ImageView mainPhotoEdit;

    private ImageView photoEdit1;
    private ImageView photoDelete1;
    private ImageView photo1;


    private ImageView photoEdit2;
    private ImageView photoDelete2;
    private ImageView photo2;


    private ImageView photoEdit3;
    private ImageView photoDelete3;
    private ImageView photo3;


    private ImageView photoEdit4;
    private ImageView photoDelete4;
    private ImageView photo4;


    private LinearLayout discountLl2;
    private LinearLayout discountLl3;
    private TextView percent1;
    private TextView percent2;
    private TextView percent3;
    private TextView minAmount1;
    private TextView minAmount2;
    private TextView minAmount3;
    private TextView state;
    private TextView area;

    boolean isMain = false;
    boolean isPhoto1 = false;
    boolean isPhoto2 = false;
    boolean isPhoto3 = false;
    boolean isPhoto4 = false;


    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST = 1888;

    private static final int PERMISSION_REQUEST_CODE1 = 22;
    private static final int CAMERA_REQUEST1 = 1881;

    private static final int PERMISSION_REQUEST_CODE2 = 2;
    private static final int CAMERA_REQUEST2 = 1882;

    private static final int PERMISSION_REQUEST_CODE3 = 3;
    private static final int CAMERA_REQUEST3 = 1883;

    private static final int PERMISSION_REQUEST_CODE4 = 4;
    private static final int CAMERA_REQUEST4 = 1884;

    public ActivityResultLauncher<Intent> resultLauncher;
    public ActivityResultLauncher<Intent> resultLauncher1;
    public ActivityResultLauncher<Intent> resultLauncher2;
    public ActivityResultLauncher<Intent> resultLauncher3;
    public ActivityResultLauncher<Intent> resultLauncher4;

    private Uri imageUri;
    private String shopImageUri;

    private Uri imageUri1;
    private String shopImageUri1;

    private Uri imageUri2;
    private String shopImageUri2;

    private Uri imageUri3;
    private String shopImageUri3;

    private Uri imageUri4;
    private String shopImageUri4;

    FirebaseAuth firebaseAuth;

    private EditText shopName;
    private TextView shopAddress;
    private String initialName;
    private Account_Information account_information;

    //todo till here

    private TextView selectCity;
    private TextView selectCategory;
    private TextView selectLocation;

    private ImageView mShopImage;

    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<Discount> discountArrayList = new ArrayList<>();


    Shop_Information shop_information;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_setting);
        firebaseAuth = FirebaseAuth.getInstance();

        delivery = findViewById(R.id.switch2);
        pickup = findViewById(R.id.switch3);

        edit1 = findViewById(R.id.card_edit_image2);
        delete1 = findViewById(R.id.card_delete_image2);
        edit2 = findViewById(R.id.card_edit_image3);
        delete2 = findViewById(R.id.card_delete_image3);
        edit3 = findViewById(R.id.card_edit_image4);
        delete3 = findViewById(R.id.card_delete_image4);
        edit4 = findViewById(R.id.card_edit_image5);
        delete4 = findViewById(R.id.card_delete_image5);


        discountLl2 = findViewById(R.id.linearLayout2);
        discountLl3 = findViewById(R.id.linearLayout3);
        percent1 = findViewById(R.id.percent1);
        percent2 = findViewById(R.id.percent2);
        percent3 = findViewById(R.id.percent3);
        minAmount1 = findViewById(R.id.minAmount1);
        minAmount2 = findViewById(R.id.minAmount2);
        minAmount3 = findViewById(R.id.minAmount3);
        area = findViewById(R.id.areaTv);
        state = findViewById(R.id.stateTv);
        mainPhotoEdit = findViewById(R.id.mainPhotoEdit);
        photoEdit1 = findViewById(R.id.photoEdit1);
        photoEdit2 = findViewById(R.id.photoEdit2);
        photoEdit3 = findViewById(R.id.photoEdit3);
        photoEdit4 = findViewById(R.id.photoEdit4);

        photoDelete1 = findViewById(R.id.photoDelete1);
        photoDelete2 = findViewById(R.id.photoDelete2);
        photoDelete3 = findViewById(R.id.photoDelete3);
        photoDelete4 = findViewById(R.id.photoDelete4);

        photo1 = findViewById(R.id.shop_image2);
        photo2 = findViewById(R.id.shop_image3);
        photo3 = findViewById(R.id.shop_image4);
        photo4 = findViewById(R.id.shop_image5);

        shopName = findViewById(R.id.shop_name);
        shopAddress = findViewById(R.id.shop_address);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                account_information = snapshot.getValue(Account_Information.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        selectLocation = findViewById(R.id.search2);
//        discount = findViewById(R.id.discount);


        mShopImage = findViewById(R.id.shop_image);


        fillCategories(categories);
        fillLocations(locations);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        selectCategory = findViewById(R.id.search1);
        selectCity = findViewById(R.id.search);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(shop_setting.this, R.layout.support_simple_spinner_dropdown_item, categories) {
            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };

        categoryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        categoryDropDown.setAdapter(categoryAdapter);
//        categoryDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selectCategory.setText(adapterView.getItemAtPosition(i).toString());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                //do nothing
//            }
//        });

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, locations) {
            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };

        //todo new ui code Here


        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        imageUri = result.getData().getData();
                        mShopImage.setImageURI(imageUri);
                        mShopImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        mShopImage.setScaleX(1);
                        mShopImage.setScaleY(1);
                        shopImageUri = imageUri.toString();
                        isMain = true;
                    }
                }
            }
        });

        photoDelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!shop_information.getShopImageUri1().equals("")) {
                    databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Shop_Information").child("shopImageUri1").setValue("");
                    edit1.setVisibility(View.GONE);
                    delete1.setVisibility(View.GONE);

                    StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(shop_information.getShopImageUri1());
                    ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(shop_setting.this, "Image successfully deleted", Toast.LENGTH_SHORT).show();
                            photo1.setImageResource(R.drawable.smallplus);
                            photo1.setScaleType(ImageView.ScaleType.CENTER);
                        }
                    });

                } else {
                    edit1.setVisibility(View.GONE);
                    delete1.setVisibility(View.GONE);
                    photo1.setImageResource(R.drawable.smallplus);
                    photo1.setScaleType(ImageView.ScaleType.CENTER);
                }
            }
        });

        photoDelete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!shop_information.getShopImageUri2().equals("")) {
                    databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Shop_Information").child("shopImageUri2").setValue("");
                    edit2.setVisibility(View.GONE);
                    delete2.setVisibility(View.GONE);

                    StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(shop_information.getShopImageUri2());
                    ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(shop_setting.this, "Image successfully deleted", Toast.LENGTH_SHORT).show();
                            photo2.setImageResource(R.drawable.smallplus);
                            photo2.setScaleType(ImageView.ScaleType.CENTER);
                        }
                    });

                } else {
                    edit2.setVisibility(View.GONE);
                    delete2.setVisibility(View.GONE);
                    photo2.setImageResource(R.drawable.smallplus);
                    photo2.setScaleType(ImageView.ScaleType.CENTER);
                }
            }
        });

        photoDelete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!shop_information.getShopImageUri3().equals("")) {
                    databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Shop_Information").child("shopImageUri3").setValue("");
                    edit3.setVisibility(View.GONE);
                    delete3.setVisibility(View.GONE);

                    StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(shop_information.getShopImageUri3());
                    ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(shop_setting.this, "Image successfully deleted", Toast.LENGTH_SHORT).show();
                            photo3.setImageResource(R.drawable.smallplus);
                            photo3.setScaleType(ImageView.ScaleType.CENTER);
                        }
                    });

                } else {
//                    Toast.makeText(shop_setting.this, "No Image Present", Toast.LENGTH_SHORT).show();
                    edit3.setVisibility(View.GONE);
                    delete3.setVisibility(View.GONE);
                    photo3.setImageResource(R.drawable.smallplus);
                    photo3.setScaleType(ImageView.ScaleType.CENTER);
                }
            }
        });

        photoDelete4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!shop_information.getShopImageUri4().equals("")) {
                    databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Shop_Information").child("shopImageUri4").setValue("");
                    edit4.setVisibility(View.GONE);
                    delete4.setVisibility(View.GONE);

                    StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(shop_information.getShopImageUri4());
                    ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(shop_setting.this, "Image successfully deleted", Toast.LENGTH_SHORT).show();
                            photo4.setImageResource(R.drawable.smallplus);
                            photo4.setScaleType(ImageView.ScaleType.CENTER);
                        }
                    });

                } else {
                    edit4.setVisibility(View.GONE);
                    delete4.setVisibility(View.GONE);
                    photo4.setImageResource(R.drawable.smallplus);
                    photo4.setScaleType(ImageView.ScaleType.CENTER);
                }
            }
        });

        mainPhotoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(shop_setting.this);
                dialog.setContentView(R.layout.dialog_picture_capture);
                Button camera = (Button) dialog.findViewById(R.id.camera);
                Button storage = (Button) dialog.findViewById(R.id.storage);
                storage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        resultLauncher.launch(intent);
                        dialog.dismiss();
                    }
                });

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cameraOn(CAMERA_REQUEST, PERMISSION_REQUEST_CODE);
                        dexterCalling(shop_setting.this, CAMERA_REQUEST);
                        dialog.dismiss();
                    }
                });

                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        resultLauncher1 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        imageUri1 = result.getData().getData();
                        photo1.setImageURI(imageUri1);
                        photo1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        photo1.setScaleX(1);
                        photo1.setScaleY(1);
                        edit1.setVisibility(View.VISIBLE);
                        delete1.setVisibility(View.VISIBLE);
                        shopImageUri1 = imageUri1.toString();
                        isPhoto1 = true;
                    }
                }
            }
        });

        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shop_information.getShopImageUri1().equals("")) {
                    buttonClick1();
                }
            }
        });

        resultLauncher2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        imageUri2 = result.getData().getData();
                        photo2.setImageURI(imageUri2);
                        photo2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        photo2.setScaleX(1);
                        photo2.setScaleY(1);
                        edit2.setVisibility(View.VISIBLE);
                        delete2.setVisibility(View.VISIBLE);
                        shopImageUri2 = imageUri2.toString();
                        isPhoto2 = true;
                    }
                }
            }
        });

        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shop_information.getShopImageUri2().equals("")) {
                    buttonClick2();
                }
            }
        });

        resultLauncher3 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        imageUri3 = result.getData().getData();
                        photo3.setImageURI(imageUri3);
                        photo3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        photo3.setScaleX(1);
                        photo3.setScaleY(1);
                        edit3.setVisibility(View.VISIBLE);
                        delete3.setVisibility(View.VISIBLE);
                        shopImageUri3 = imageUri3.toString();
                        isPhoto3 = true;
                    }
                }
            }
        });

        photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shop_information.getShopImageUri3().equals("")) {
                    buttonClick3();
                }
            }
        });

        resultLauncher4 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        imageUri4 = result.getData().getData();
                        photo4.setImageURI(imageUri4);
                        photo4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        photo4.setScaleX(1);
                        photo4.setScaleY(1);
                        edit4.setVisibility(View.VISIBLE);
                        delete4.setVisibility(View.VISIBLE);
                        shopImageUri4 = imageUri4.toString();
                        isPhoto4 = true;
                    }
                }
            }
        });

        photo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shop_information.getShopImageUri4().equals("")) {
                    buttonClick4();
                }
            }
        });

        photoEdit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick1();
            }
        });

        photoEdit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick2();
            }
        });

        photoEdit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick3();
            }
        });

        photoEdit4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick4();
            }
        });


        //todo till here

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Shop_Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                shop_information = snapshot.getValue(Shop_Information.class);
                selectCity.setText(shop_information.getShopDistrict());
                selectCategory.setText(shop_information.getCategory());
                selectLocation.setText(shop_information.getShopDistrict());
//                discount.setText(shop_information.getDiscounts().get(0).getDiscountPercentage()+"");
//                Log.d("shop_setting",shop_information.getDiscounts().get(0).getDiscountPercentage()+"");
                area.setText(shop_information.getShopArea());
                state.setText(shop_information.getShopState());
                shopName.setText(shop_information.getShopName());
                initialName = shop_information.getShopName();
                shopAddress.setText(shop_information.getShopAddress());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (shop_information.getShopImageUri1() != null && !shop_information.getShopImageUri1().equals("")) {
                            Picasso.get().load(Uri.parse(shop_information.getShopImageUri1())).into(photo1);
                            edit1.setVisibility(View.VISIBLE);
                            delete1.setVisibility(View.VISIBLE);
                            photo1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            photo1.setScaleX(1);
                            photo1.setScaleY(1);
                        }
                        if (shop_information.getShopImageUri2() != null && !shop_information.getShopImageUri2().equals("")) {
                            Picasso.get().load(Uri.parse(shop_information.getShopImageUri2())).into(photo2);
                            edit2.setVisibility(View.VISIBLE);
                            delete2.setVisibility(View.VISIBLE);
                            photo2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            photo2.setScaleX(1);
                            photo2.setScaleY(1);
                        }
                        if (shop_information.getShopImageUri3() != null && !shop_information.getShopImageUri3().equals("")) {
                            Picasso.get().load(Uri.parse(shop_information.getShopImageUri3())).into(photo3);
                            edit3.setVisibility(View.VISIBLE);
                            delete3.setVisibility(View.VISIBLE);
                            photo3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            photo3.setScaleX(1);
                            photo3.setScaleY(1);
                        }
                        if (shop_information.getShopImageUri4() != null && !shop_information.getShopImageUri4().equals("")) {
                            Picasso.get().load(Uri.parse(shop_information.getShopImageUri4())).into(photo4);
                            edit4.setVisibility(View.VISIBLE);
                            delete4.setVisibility(View.VISIBLE);
                            photo4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            photo4.setScaleX(1);
                            photo4.setScaleY(1);
                        }
                    }
                });

                discountArrayList = shop_information.getDiscounts();
                //todo set text
                if (discountArrayList.size() == 3) {
                    discountLl2.setVisibility(View.VISIBLE);
                    discountLl3.setVisibility(View.VISIBLE);

                    percent1.setText(String.valueOf(discountArrayList.get(0).getDiscountPercentage()));
                    percent2.setText(String.valueOf(discountArrayList.get(1).getDiscountPercentage()));
                    percent3.setText(String.valueOf(discountArrayList.get(2).getDiscountPercentage()));

                    minAmount1.setText(String.valueOf(discountArrayList.get(0).getMinBillAmount()));
                    minAmount2.setText(String.valueOf(discountArrayList.get(1).getMinBillAmount()));
                    minAmount3.setText(String.valueOf(discountArrayList.get(2).getMinBillAmount()));
                } else if (discountArrayList.size() == 2) {
                    discountLl2.setVisibility(View.VISIBLE);

                    percent1.setText(String.valueOf(discountArrayList.get(0).getDiscountPercentage()));
                    percent2.setText(String.valueOf(discountArrayList.get(1).getDiscountPercentage()));

                    minAmount1.setText(String.valueOf(discountArrayList.get(0).getMinBillAmount()));
                    minAmount2.setText(String.valueOf(discountArrayList.get(1).getMinBillAmount()));
                } else {
                    percent1.setText(String.valueOf(discountArrayList.get(0).getDiscountPercentage()));

                    minAmount1.setText(String.valueOf(discountArrayList.get(0).getMinBillAmount()));
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        locationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        locationDropDown.setAdapter(locationAdapter);
//        locationDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selectCity.setText(adapterView.getItemAtPosition(i).toString());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        prev = findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                finish();
            }
        });

        databaseReference.child("Merchant_data").child(firebaseAuth.getUid()).child("Shop_Information").child("delivery").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    delivery.setChecked(snapshot.getValue(Boolean.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Merchant_data").child(firebaseAuth.getUid()).child("Shop_Information").child("pickup").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    pickup.setChecked(snapshot.getValue(Boolean.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        databaseReference
                .child("Merchant_data").

                child(Objects.requireNonNull(firebaseAuth.getUid())).

                child("Shop_Information").

                child("shopImageUri").

                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String imageUri = snapshot.getValue(String.class);
                        try {
                            Picasso.get().load(Uri.parse(imageUri)).into(mShopImage);
                            mShopImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            mShopImage.setScaleX(1);
                            mShopImage.setScaleY(1);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Something went wrong to load image", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    void dexterCalling(Activity activity, int requestCode)//for granting RunTime permission
    {
        //    Log.d("chekingTemp", "dexterCalling: "+temp);
        Dexter.withActivity(this)

                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE)

                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                        ImagePicker.with(shop_setting.this)
                                .cameraOnly()
                                .saveDir(getExternalFilesDir(Environment.DIRECTORY_DCIM))
                                .start(requestCode);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {

            @Override
            public void onError(DexterError error) {

                Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
            }
        })

                .onSameThread().check();
    }

    //todo add this func.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            mShopImage.setImageURI(imageUri);
            mShopImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mShopImage.setScaleX(1);
            mShopImage.setScaleY(1);
            shopImageUri = imageUri.toString();
            isMain=true;
        } else if (requestCode == CAMERA_REQUEST1 && resultCode == Activity.RESULT_OK) {
            imageUri1 = data.getData();
            photo1.setImageURI(imageUri1);
            photo1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            photo1.setScaleX(1);
            photo1.setScaleY(1);
            edit1.setVisibility(View.VISIBLE);
            delete1.setVisibility(View.VISIBLE);
            shopImageUri1 = imageUri1.toString();
            isPhoto1=true;
        } else if (requestCode == CAMERA_REQUEST2 && resultCode == Activity.RESULT_OK) {
            imageUri2 = data.getData();
            photo2.setImageURI(imageUri2);
            photo2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            photo2.setScaleX(1);
            photo2.setScaleY(1);
            edit2.setVisibility(View.VISIBLE);
            delete2.setVisibility(View.VISIBLE);
            shopImageUri2 = imageUri2.toString();
            isPhoto2=true;
        } else if (requestCode == CAMERA_REQUEST3 && resultCode == Activity.RESULT_OK) {
            imageUri3 = data.getData();
            photo3.setImageURI(imageUri3);
            photo3.setScaleType(ImageView.ScaleType.CENTER_CROP);
            photo3.setScaleX(1);
            photo3.setScaleY(1);
            edit3.setVisibility(View.VISIBLE);
            delete3.setVisibility(View.VISIBLE);
            shopImageUri3 = imageUri3.toString();
            isPhoto3=true;
        } else if (requestCode == CAMERA_REQUEST4 && resultCode == Activity.RESULT_OK) {
            imageUri4 = data.getData();
            photo4.setImageURI(imageUri4);
            photo4.setScaleType(ImageView.ScaleType.CENTER_CROP);
            photo4.setScaleX(1);
            photo4.setScaleY(1);
            edit4.setVisibility(View.VISIBLE);
            delete4.setVisibility(View.VISIBLE);
            shopImageUri4 = imageUri4.toString();
            isPhoto4=true;
        }
    }

    private void cameraOn(int camera, int permission) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (ContextCompat.checkSelfPermission(shop_setting.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            ImagePicker.with(shop_setting.this)
                    .cameraOnly()
                    .saveDir(getExternalFilesDir(Environment.DIRECTORY_DCIM))
                    .start(camera);

        } else {
            ActivityCompat.requestPermissions(
                    shop_setting.this,
                    new String[]{Manifest.permission.CAMERA},
                    permission
            );
        }
    }

    void buttonClick1() {
        Dialog dialog = new Dialog(shop_setting.this);
        dialog.setContentView(R.layout.dialog_picture_capture);
        Button camera = (Button) dialog.findViewById(R.id.camera);
        Button storage = (Button) dialog.findViewById(R.id.storage);
        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                resultLauncher1.launch(intent);
                dialog.dismiss();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                cameraOn(CAMERA_REQUEST1, PERMISSION_REQUEST_CODE1);
                dexterCalling(shop_setting.this, CAMERA_REQUEST1);
                dialog.dismiss();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    void buttonClick2() {
        Dialog dialog = new Dialog(shop_setting.this);
        dialog.setContentView(R.layout.dialog_picture_capture);
        Button camera = (Button) dialog.findViewById(R.id.camera);
        Button storage = (Button) dialog.findViewById(R.id.storage);
        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                resultLauncher2.launch(intent);
                dialog.dismiss();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                cameraOn(CAMERA_REQUEST2, PERMISSION_REQUEST_CODE2);
                dexterCalling(shop_setting.this, CAMERA_REQUEST2);
                dialog.dismiss();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    void buttonClick3() {
        Dialog dialog = new Dialog(shop_setting.this);
        dialog.setContentView(R.layout.dialog_picture_capture);
        Button camera = (Button) dialog.findViewById(R.id.camera);
        Button storage = (Button) dialog.findViewById(R.id.storage);
        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                resultLauncher3.launch(intent);
                dialog.dismiss();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                cameraOn(CAMERA_REQUEST3, PERMISSION_REQUEST_CODE3);
                dexterCalling(shop_setting.this, CAMERA_REQUEST3);
                dialog.dismiss();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    void buttonClick4() {
        Dialog dialog = new Dialog(shop_setting.this);
        dialog.setContentView(R.layout.dialog_picture_capture);
        Button camera = (Button) dialog.findViewById(R.id.camera);
        Button storage = (Button) dialog.findViewById(R.id.storage);
        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                resultLauncher4.launch(intent);
                dialog.dismiss();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                cameraOn(CAMERA_REQUEST4, PERMISSION_REQUEST_CODE4);
                dexterCalling(shop_setting.this, CAMERA_REQUEST4);
                dialog.dismiss();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private ArrayList<String> fillCategories(ArrayList<String> arr) {
        arr.add("Select Category");
        arr.add("Food & Beverages");
        arr.add("Fashion");
        arr.add("Salon & spa");
        arr.add("Stores");
        arr.add("Medical");
        arr.add("others");


        return arr;

    }

    private ArrayList<String> fillLocations(ArrayList<String> arr) {
        arr.add("Select City");
        arr.add("Alwar");
        arr.add("Bhiwadi");
        arr.add("Chandigarh");
        arr.add("Delhi");
        arr.add("Ludhiana");
        arr.add("Mumbai");
        arr.add("Patna");
        arr.add("Kolkata");
        arr.add("Select Location");

        return arr;

    }

    void updateData() {
        Toast.makeText(shop_setting.this, "updated", Toast.LENGTH_SHORT).show();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(firebaseAuth.getUid() + "_" + account_information.getPhoneNumber());
        if (isMain) {
            storageReference.child("shop_image").putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storageReference.child("shop_image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Shop_Information").child("shopImageUri").setValue(uri.toString());
                            }
                        });
                    }

                }
            });
        }
        if (isPhoto1) {
            storageReference.child("shop_image1").putFile(imageUri1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storageReference.child("shop_image1").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Shop_Information").child("shopImageUri1").setValue(uri.toString());
                        }
                    });

                }
            });
        }
        if (isPhoto2) {
            storageReference.child("shop_image2").putFile(imageUri2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storageReference.child("shop_image2").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Shop_Information").child("shopImageUri2").setValue(uri.toString());
                        }
                    });
                }
            });
        }
        if (isPhoto3) {
            storageReference.child("shop_image3").putFile(imageUri3).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storageReference.child("shop_image3").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Shop_Information").child("shopImageUri3").setValue(uri.toString());
                        }
                    });

                }
            });
        }
        if (isPhoto4) {
            storageReference.child("shop_image4").putFile(imageUri4).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storageReference.child("shop_image4").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Shop_Information").child("shopImageUri4").setValue(uri.toString());
                        }
                    });

                }
            });
        }
        if (!initialName.equals(shopName.getText().toString().trim())) {
            databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Shop_Information").child("shopName").setValue(shopName.getText().toString().trim());
        }

        databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Shop_Information").child("delivery").setValue(delivery.isChecked());
        databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Shop_Information").child("pickup").setValue(pickup.isChecked());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateData();
    }
}
