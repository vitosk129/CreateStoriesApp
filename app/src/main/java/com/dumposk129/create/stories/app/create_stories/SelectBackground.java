package com.dumposk129.create.stories.app.create_stories;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dumposk129.create.stories.app.R;
import com.dumposk129.create.stories.app.model.Frame;
import com.dumposk129.create.stories.app.sql.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by DumpOSK129.
 */
public class SelectBackground extends ActionBarActivity implements View.OnClickListener {
    private Button btnBg, btnGallery, btnNext;
    private static int RESULT_LOAD_IMG = 1;
    private String picturePath;
    private ImageView imgView;
    private Bitmap bitmap;
    private boolean hasBg = false;
    private String pathBg;
    private long frame_id, frame_order;
    private final String PATH = "StoryApp/StoryName/Frame";
    private int sId;

    Intent intent;
    DatabaseHelper db;

    File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + PATH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_bg_char);

        btnBg = (Button) findViewById(R.id.btnImage);
        btnGallery = (Button) findViewById(R.id.btnGallery);
        btnNext = (Button) findViewById(R.id.btnNext);
        imgView = (ImageView) findViewById(R.id.full_image_view);

        btnBg.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        // get intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            if (bundle.containsKey("sId")){
                sId = getIntent().getExtras().getInt("sId");
            }

            if (bundle.containsKey("bg")){
                byte[] byteArr = bundle.getByteArray("bg");
                bitmap = BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length);
                imgView.setImageBitmap(bitmap);
                hasBg = true;
            }

            if (bundle.containsKey("frame_order")){
                frame_order = (int)getIntent().getExtras().getLong("frame_order");
            }
        }

        /*Toast.makeText(getApplicationContext(), "sId: "+sId, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "frame_id: "+frame_id, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "frame_order: "+frame_order, Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onClick(View v) {
        if (v == btnBg) {
            intent = new Intent(SelectBackground.this, PhotoBackground.class);
            intent.putExtra("sId", sId);
            intent.putExtra("frame_id", frame_id);
            intent.putExtra("frame_order", frame_order);
            startActivity(intent);
        } else if (v == btnGallery) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra("sId", sId);
            intent.putExtra("frame_id", frame_id);
            intent.setType("image/*");
            startActivityForResult(intent, RESULT_LOAD_IMG);
        } else if (v == btnNext) {
            if (hasBg) {
                createDirectory(); //Create Directory.

                /* Save photo path */
                pathBg = PhotoHelper.writeImagePath(bitmap);

                //TODO : DON'T FORGET INCREASE FRAME_ORDER WHEN DONE(RENDER TO VIDEO EACH FRAME).
                frame_id = createFrameInSQLiteDB(); // Insert frame_id.

                // TODO: Update Path
                PhotoHelper.updatePath(getApplicationContext(), (int) frame_id, pathBg); // Update Path in db.

                /* Go to Next Page */
                intent = new Intent(SelectBackground.this, SelectCharacter.class);
                intent.putExtra("sId", sId);
                intent.putExtra("frame_id", frame_id);
                intent.putExtra("frame_order", frame_order);
                startActivity(intent);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please select an action", Toast.LENGTH_LONG).show();
        }
    }

    /* Show Image from Gallery */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null) {
                Uri selectImage = data.getData();
                String[] filePathCol = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectImage, filePathCol, null, null, null);
                cursor.moveToFirst();

                int colIndex = cursor.getColumnIndex(filePathCol[0]);
                picturePath = cursor.getString(colIndex);
                cursor.close();
                imgView = (ImageView) findViewById(R.id.full_image_view);
                imgView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                bitmap = BitmapFactory.decodeFile(picturePath);
                hasBg = true;

                /* Convert to bitmap */
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapData = stream.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show();
        }
    }

    /* Create Path */
    private void createDirectory() {
        dir.mkdirs();
    }

    /* Create Frame */
    private long createFrameInSQLiteDB() {
        db = new DatabaseHelper(getApplicationContext());
        Frame frame = new Frame();
        if (pathBg != "") {
            frame_order++;
            frame.setFrameOrder((int)frame_order);
            frame.setStoryId(sId);
        }
        return db.createNewFrame(frame);
    }
}