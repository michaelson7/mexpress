package com.example.mexpress;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TripDetails extends AppCompatActivity {

    Button tripBtn,dialogbtn,delete;
    EditText name,destination,discription,type,amount,time,expense;
    ImageView imageView2;
    String imagePath="555";
    TextView dateView;
    Switch risk;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    DB_Handler db;
    tripModel models;
    boolean isRisk = false;
    final Calendar myCalendar= Calendar.getInstance();
    Button submit;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details_creation);

        delete= findViewById(R.id.button6);
        db = new DB_Handler(this);
        imageView2 = findViewById(R.id.imageView6);
        tripBtn = findViewById(R.id.button2);
        name = findViewById(R.id.editTextTextPersonName);
        destination = findViewById(R.id.editTextTextPersonName2);
        dateView = findViewById(R.id.textView16);
        risk = findViewById(R.id.switch1);
        discription = findViewById(R.id.editTextTextMultiLine);
        time = findViewById(R.id.time);
        dialogbtn = findViewById(R.id.button3);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);
        expense =  dialog.findViewById(R.id.editTextTextPersonName3);
        time =  dialog.findViewById(R.id.editTextTextPersonName5);
        amount =  dialog.findViewById(R.id.editTextTextPersonName4);
        submit =  dialog.findViewById(R.id.button5);

        Intent i = getIntent();
        boolean hasData =  i.getExtras().getBoolean("hasData");
        if (hasData){
            models = (tripModel)i.getSerializableExtra("model");
            tripBtn.setText("Update");
            name.setText(models.getName());
            dateView.setText(models.getDate());
            destination.setText(models.getDestination());
            discription.setText(models.getDescription());
            try {
                expense.setText(models.getExpenseType());
                amount.setText(models.getExpenseAmount());
                time.setText(models.getExpenseTime());
                //comments.setText(models.getExpenseComments());
            } catch (Exception e) {
                e.printStackTrace();
            }

            Glide.with(this).load(models.getImageLink()).into(imageView2);
        }else{
            //display default image
            Glide.with(this).load("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMAAAAB8CAMAAAAICk87AAAAJ1BM" +
                    "VEX39/fW1tb4+Pjn5+fz8/Ph4eHu7u7r6+vk5OTc3NzT09PZ2dnQ0NCrk/CdAAAEpUlEQVR4nNVc65qrIAzUAOKl7/+8J4GeVdBSK" +
                    "wHC/Nndb1thQsgEiAzGDjB0ChisGbRSU58UgPpOP6a5y0FA88/e9PSb7o4B6IPdobtBcOaH+O9uKMCFxWlEevEj0Fc+j4PQRzjCqK" +
                    "MuHZ6i0iyfAQyz0h+6ia5lhM+Eb1309ATjq5OIngkfvT/81OUUl4C7gVJobrFnDnc+qwKZkwCULvWDVaXlFnHmcOsbcibzvcl7+hIl2yI" +
                    "YoEd/lK7096wEWSPpeurOJGvM3XmAnPym+Ux45P3hE7RV7WQNtMrO8RvK2i/SlX6OapJbYObAFQYz4sDzNl0MZHxa3cmcP3njB8" +
                    "JUU9ZIurglqOK+xdWeA8tztakyCGh+UyZoAPCEtXQjFLaLjTQKS2FZK91CYVnjkq5kG7qcrJF0VZBMkrUSTopTjFG6ki09XGB8eWjFBRTJGvNM" +
                    "cFpfcfXELGulpCsJzReuSWDqb2j6VhkolJWuZMssolNeHBNt5+tO633MXPO1NP+7B0PGmYg7rWi+eeZl7RGE7P09DOKSTnavZRQCnP7JLuY5gAtZo" +
                    "/m5I8rS2ESEDTQTqEO7uUGtByxBrGHc8uGDKxuZFgLtCIPexiPsob9CT+HI/vaF1n4t9KcK+o9D8PdBYac/RzgC40YjEA3AYQi04JoMsKNRE9kaZ0BEYPm" +
                    "LRxdBSRDenZzeA7AYs2yeyzztEFsNQKbVkzKuz6tBZ3FsjKOzx6TXJtKLsKt6smRxZ/LF7jI2mcifRBLQaPltXN/Ov0zemTQRg2GOYpJAAuG8JSdBMV" +
                    "62bZkdlbkDAscuYu6AMWn1vm9oFJaeCKwzHEeEOgzT2hMB9Bp7HBCD/1/WbgisyxC4DE4JZKQ6IjCjx4S5xHxM78QTwMQHTslcZwTCwD+uGxF" +
                    "YeyJwNQLdEFhVGIT8tO7IhVYD8YosoCSewLgN59wB5n7C6LhiGNLLITeywZDIJ0C53M5gHXERDIeMugMCI+VvenaLGJ9ZB5lFBwRIeoHqvZSlFUEo" +
                    "zF0QGI0+bCyeVaF1d8+ICYybtz0tNKMVZScE0PmNslbhKjn+Tx8EPkMiAbjYy+qIAAxWgzW3MfsvScGzijor5XxmP2293R/3QSmb1KhWz6pKqTJU" +
                    "SxiE5wVQAg5qMg/aa5SYpTuQfdDOUZ/+vHE0f7b9WB7ytGmeEj1o8w4q40F7i3otqi5nDCCu2LtudTxzOT57uX26teBCAa6HuqsVapWNFjloryRrBUv06lTHF30tq7isFS/RK5xbcElXso1yskZ6UyFYu9yiwCSreOtE6uaIjIdWfJOsgKz5CwXqvUfGfLVClQAdNckpN9CkutxZjaPVdvc/saRcTavLOarj3aK32aobMhfdORcKMCHraoVGi9W4F0/zl3avtsQdeRbEfWYroP9e1n4dhAbSlcLP3iBg0zLCT+tYmVcX3pa1ujscP+BmQkzSIeboJIQ7CPriGk3eyryPr/uZrW4huY9keGn9WuAtJBK8xonbXXxK8Ei6xJvfg8LRSdYqXSjAhNNc5b0LpwaCRF/QjWz3cbhagbYwhEpXCk6yyI/+/+wP3vJDF7HzGhRR7T9MICBR733qYAAAAABJRU5ErkJggg==").into(imageView2);
        }
        config();
    }

    private void config() {
        //date
        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            String myFormat="MM/dd/yy";
            SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
            dateView.setText(dateFormat.format(myCalendar.getTime()));
        };
        dateView.setOnClickListener(view -> new DatePickerDialog(TripDetails.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        //switch
        risk.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isRisk = isChecked;
        });

        dialogbtn.setOnClickListener(v -> {
           submit.setOnClickListener(v1 -> {
                dialog.hide();
           });
            dialog.show();
        });

        delete.setOnClickListener(v -> {
            db.tripDelete(models.getId());
            Toast.makeText(this, "Deleted", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
        });

        //image picker
        imageView2.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                takePhoto();
            }
        });
        tripBtn.setOnClickListener(v -> {

            Intent i = getIntent();
            boolean hasData =  i.getExtras().getBoolean("hasData");

            //validate data
            if (name.getText().toString().trim().equals("") ||destination.getText().toString().trim().equals("") ||dateView.getText().toString().trim().equals("")  ){
                Toast.makeText(this, "Please add required data", Toast.LENGTH_SHORT).show();
                return;
            }

            if (hasData){
                tripModel model = new tripModel(
                        name.getText().toString().trim(),
                        dateView.getText().toString().trim(),
                        name.getText().toString().trim(),
                        destination.getText().toString().trim(),
                        String.valueOf(isRisk),
                        discription.getText().toString().trim(),
                        imagePath,
                        expense.getText().toString().trim(),
                        amount.getText().toString().trim(),
                        time.getText().toString().trim(),
                        null,
                        models.getId()
                );
                db.updateTrip(model);
            }else{
                tripModel model = new tripModel(
                        name.getText().toString().trim(),
                        dateView.getText().toString().trim(),
                        name.getText().toString().trim(),
                        destination.getText().toString().trim(),
                        String.valueOf(isRisk),
                        discription.getText().toString().trim(),
                        imagePath,
                        expense.getText().toString().trim(),
                        amount.getText().toString().trim(),
                        time.getText().toString().trim(),
                        null,
                        null
                );
                //save trup details if valid
                db.addData(model);
            }

            // --
            Toast.makeText(this, "UPLOADED", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
        });
    }

    //take photo
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void takePhoto() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView2.setImageBitmap(photo);

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imagePath = saveImage(thumbnail);
            // list.add(link);
            Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
            //  List<String> listNew = new ArrayList<>();
            //  listNew.add(link);
        }
    }

    //save image to device directory
    public String saveImage(Bitmap myBitmap) {
        String IMAGE_DIRECTORY = "/YourDirectNames";
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        if (!wallpaperDirectory.exists()) {  // have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}