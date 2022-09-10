package com.cu.gastosmerchant1;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cu.gastosmerchant1.databinding.ActivityAdContentBinding;
import com.cu.gastosmerchant1.settings.postAds.post_audience;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;

public class Ad_content extends AppCompatActivity {

    ActivityAdContentBinding binding;
    int content;
    String type;
    Uri selectedFileUri;
    public static final int REQUEST_CODE_STORAGE_PERMISSION = 786;
    //regarding shared preference
    private final String MyPrefs = "MY_PREFERENCE";
    private SharedPreferences sharedPreferences;
    private final String NAME = "MY_NAME";
    public static final int PDF_SELECTION_CODE = 99;
    ActivityResultLauncher<Intent> startActivityForFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inflate the layout as per google doc instructions
        binding= ActivityAdContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(MyPrefs, MODE_PRIVATE);
        type=getIntent().getStringExtra("Type_of_service");
        String shopName = sharedPreferences.getString(NAME, "Cafe Bistro");


        Resources res = getResources();
//        String text = String.format(res.getString(R.string.content_1),shopName);

        String content_1 = getString(R.string.content_1);
        String content_1_2 = getString(R.string.content_1_2);
        binding.txt1.setText(MessageFormat.format("{0} {1}{2}", content_1, shopName, content_1_2));

        String content_2 = getString(R.string.content_1);
        binding.txt2.setText(MessageFormat.format("{0} {1}{2}", content_2, shopName, content_1_2));

        String content_3 = getString(R.string.content_1);
        binding.txt3.setText(MessageFormat.format("{0} {1}{2}", content_3, shopName, content_1_2));

        binding.imageView.setOnClickListener(v->{
            finish();
        });
        if(type.equals("WhatsApp"))
            binding.uploadTxt.setVisibility(View.VISIBLE);
        binding.uploadTxt.setOnClickListener(v-> {
            //Ask for permission
            if (ContextCompat.checkSelfPermission(Ad_content.this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Ad_content.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
            }
            else{
                selectImage();
            }
        });
        startActivityForFile = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        assert result.getData() != null;
                        selectedFileUri = result.getData().getData();
                        Cursor cursor = Ad_content.this.getContentResolver()
                                .query(selectedFileUri, null, null, null, null);
                        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                        cursor.moveToFirst();

                        String name = cursor.getString(nameIndex);
                        String size = Long.toString(cursor.getLong(sizeIndex));
                        //String str_txt=name+" ("+size+" bytes)";
                        binding.uploadTxt.setText(name);
                        Log.e("File uploaded",name);
                    }
                }
        );
        binding.select.setOnClickListener(v->{
            if(binding.cons1.getVisibility()== View.VISIBLE){
                binding.cons1.setVisibility(View.GONE);
                binding.cons2.setVisibility(View.GONE);
                binding.cons3.setVisibility(View.GONE);
            }
            else{
                binding.inputNote.setVisibility(View.GONE);
                binding.cons1.setVisibility(View.VISIBLE);
                binding.cons2.setVisibility(View.VISIBLE);
                binding.cons3.setVisibility(View.VISIBLE);
            }
        });

        binding.cons1.setOnClickListener(v->{
            content=1;
            binding.img1.setVisibility(View.VISIBLE);
            binding.img2.setVisibility(View.GONE);
            binding.img3.setVisibility(View.GONE);
            binding.cons1.setBackgroundResource(R.drawable.bg_content_stroke);
            binding.cons2.setBackgroundResource(R.drawable.bg_content_stroke_inactive);
            binding.cons3.setBackgroundResource(R.drawable.bg_content_stroke_inactive);
        });
        binding.cons2.setOnClickListener(v->{
            content=2;
            binding.img1.setVisibility(View.GONE);
            binding.img2.setVisibility(View.VISIBLE);
            binding.img3.setVisibility(View.GONE);
            binding.cons1.setBackgroundResource(R.drawable.bg_content_stroke_inactive);
            binding.cons2.setBackgroundResource(R.drawable.bg_content_stroke);
            binding.cons3.setBackgroundResource(R.drawable.bg_content_stroke_inactive);
        });
        binding.cons3.setOnClickListener(v->{
            content=3;
            binding.img1.setVisibility(View.GONE);
            binding.img2.setVisibility(View.GONE);
            binding.img3.setVisibility(View.VISIBLE);
            binding.cons1.setBackgroundResource(R.drawable.bg_content_stroke_inactive);
            binding.cons2.setBackgroundResource(R.drawable.bg_content_stroke_inactive);
            binding.cons3.setBackgroundResource(R.drawable.bg_content_stroke);
        });

        binding.inputNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()<=160) {
                    binding.textView8.setText(charSequence.toString().length() + "/160");
                }else {
                    Toast.makeText(Ad_content.this, "Limit exceeded", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.create.setOnClickListener(v->{
            if(binding.inputNote.getVisibility()==View.VISIBLE)
                binding.inputNote.setVisibility(View.GONE);
            else {
                content=0;

                binding.img1.setVisibility(View.GONE);
                binding.img2.setVisibility(View.GONE);
                binding.img3.setVisibility(View.GONE);
                binding.cons1.setBackgroundResource(R.drawable.bg_content_stroke_inactive);
                binding.cons2.setBackgroundResource(R.drawable.bg_content_stroke_inactive);
                binding.cons3.setBackgroundResource(R.drawable.bg_content_stroke_inactive);

                binding.inputNote.setVisibility(View.VISIBLE);
                binding.cons1.setVisibility(View.GONE);
                binding.cons2.setVisibility(View.GONE);
                binding.cons3.setVisibility(View.GONE);
            }
        });
        binding.next.setOnClickListener(v->{
            if(selectedFileUri!=null || type.equals("SMS")) {
                Intent intent = new Intent(Ad_content.this, post_audience.class);
                if (!binding.inputNote.getText().toString().equals("")) {
                    intent.putExtra("sending_type", type);
                    intent.putExtra("sending_file_uri", selectedFileUri+"");
                    intent.putExtra("sending_content", binding.inputNote.getText().toString().trim());
                    startActivity(intent);
                } else if (content != 0 && binding.inputNote.getText().toString().equals("")) {
                    intent.putExtra("sending_type", type);
                    intent.putExtra("sending_file_uri", selectedFileUri+"");
                    intent.putExtra("sending_content", content + "");
                    startActivity(intent);
                } else {
                    if(type.equals("SMS")) {
                        Snackbar.make(binding.relativeLayout3, "Select any content for SMS", Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.parseColor("#ea4a1f"))
                                .setTextColor(Color.parseColor("#000000"))
                                .setBackgroundTint(Color.parseColor("#D9F5F8"))
                                .show();
                    }
                    else{
                        Snackbar.make(binding.relativeLayout3, "Select any content for Whatsapp", Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.parseColor("#ea4a1f"))
                                .setTextColor(Color.parseColor("#000000"))
                                .setBackgroundTint(Color.parseColor("#D9F5F8"))
                                .show();
                    }
                }
            }
            else{
                Snackbar.make(binding.relativeLayout3, "Upload any file for Whatsapp", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.parseColor("#ea4a1f"))
                        .setTextColor(Color.parseColor("#000000"))
                        .setBackgroundTint(Color.parseColor("#D9F5F8"))
                        .show();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length>0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            }
            else{
                Toast.makeText(Ad_content.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void selectImage() {
        // If you have access to the external storage, do whatever you need
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForFile.launch(chooseFile);
    }
}