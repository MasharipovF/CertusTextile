package masharipov.certustextile.tpriew;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import coverflow.photoview.PhotoViewAttacher;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import masharipov.certustextile.ItemFragment;
import masharipov.certustextile.R;

public class Tview extends AppCompatActivity {
    ImageView A1,A2,A3;
    private PhotoViewAttacher mAttacher1,mAttacher2,mAttacher3;
    String filepath="";
    TextView TagN,Sticke;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tview);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        A1=(ImageView)findViewById(R.id.a1);
        A2=(ImageView)findViewById(R.id.a2);
        A3=(ImageView)findViewById(R.id.a3);
        Sticke=(TextView)findViewById(R.id.idnakleyk);
        TagN=(TextView)findViewById(R.id.tagtovarni);
        TagN.setText(getIntent().getStringExtra("tag"));


        String temp;
        String stick="";

        if(!getIntent().getStringExtra("firstS").equals("1")){
            temp=Uri.parse(getPath(Uri.parse(getIntent().getStringExtra("firstS")))).getLastPathSegment();
            StringBuilder sb = new StringBuilder(temp);
            sb.delete(temp.length()-4,temp.length());
            temp=sb.toString();

            stick=temp;
            Log.d("sssss",stick);

            stick=stick+"-";
        }
        if(!getIntent().getStringExtra("secS").equals("1")){
            temp=Uri.parse(getPath(Uri.parse(getIntent().getStringExtra("secS")))).getLastPathSegment();
            StringBuilder sb = new StringBuilder(temp);
            sb.delete(temp.length()-4,temp.length());
            temp=sb.toString();

            stick=stick+temp;
            stick=stick+"-";
            Log.d("sssss",stick);
        }
        if(!getIntent().getStringExtra("thirS").equals("1")){
            temp=Uri.parse(getPath(Uri.parse(getIntent().getStringExtra("thirS")))).getLastPathSegment();

            StringBuilder sb = new StringBuilder(temp);
            sb.delete(temp.length()-4,temp.length());
            temp=sb.toString();

            stick=stick+temp;
            Log.d("sssss",stick);


        }
        if(stick.length()!=0)
        if (stick.charAt(stick.length()-1)=='-'){
            StringBuilder sb = new StringBuilder(stick);
            sb.deleteCharAt(stick.length()-1);
            stick=sb.toString();

            Log.d("sssss",stick);
        }
        Sticke.setText(stick);

        try {

            if(getIntent().getBooleanExtra("oldi",false))
            {
                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/Certus/cache/oldi.png");
                A1.setImageBitmap(bitmap);
            }
            else {
                Bitmap bitmap = BitmapFactory.decodeFile(getPath(Uri.parse(getIntent().getStringExtra("oldiUri"))));
                A1.setImageBitmap(bitmap);
            }

            if(getIntent().getBooleanExtra("yon",false))
            {
                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/Certus/cache/yon.png");
                A2.setImageBitmap(bitmap);
            }
            else {
                Bitmap bitmap = BitmapFactory.decodeFile(getPath(Uri.parse(getIntent().getStringExtra("yonUri"))));
                A2.setImageBitmap(bitmap);
            }


            if(getIntent().getBooleanExtra("orqa",false))
            {
                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/Certus/cache/orqa.png");
                A3.setImageBitmap(bitmap);
            }
            else {
                Bitmap bitmap = BitmapFactory.decodeFile(getPath(Uri.parse(getIntent().getStringExtra("orqaUri"))));

                A3.setImageBitmap(bitmap);
            }


        }
        catch (Exception o){

        }

        findViewById(R.id.galeria).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ssss",lastFileModified( Environment.getExternalStorageDirectory().toString() + "/Certus" + "/Saved/").getAbsolutePath());
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(lastFileModified( Environment.getExternalStorageDirectory().toString() + "/Certus" + "/Saved/")), "image/*");
                startActivity(intent);
            }
        });

        findViewById(R.id.shareeee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filepath.equals(""))
                takeScreenshot();
                shareImage(new File(filepath));
            }
        });

        findViewById(R.id.ichose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();
                Toast.makeText(getApplicationContext(),"Заказ сохранен!",Toast.LENGTH_SHORT).show();
                findViewById(R.id.ichose).setVisibility(View.GONE);
                findViewById(R.id.forgone).setVisibility(View.GONE);
            }
        });
        //   f1= (new Gson()).fromJson((JsonElement) getIntent().getSerializableExtra("oldi"),ItemFragment.class);
       // f2=(ItemFragment) getIntent().getSerializableExtra("yon");

        //f3=(ItemFragment) getIntent().getSerializableExtra("orqa");

        //File getBit = new File(getPath(Uri.parse(current.getURI())));

        mAttacher1 = new PhotoViewAttacher(A1);
        mAttacher2 = new PhotoViewAttacher(A2);
        mAttacher3 = new PhotoViewAttacher(A3);

    }
    @Override
    public void onStart(){
        super.onStart();

    }
    String mPath;
    private void shareImage(File imageFile) {
        Intent share = new Intent(Intent.ACTION_SEND);

        share.setType("image/jpeg");

        // Make sure you put example png image named myImage.png in your
        // directory
        String imagePath = imageFile.getPath();

        File imageFileToShare = new File(imagePath);

        Uri uri = Uri.fromFile(imageFileToShare);
        share.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(share, "Юбориш :"));
    }
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    private void takeScreenshot() {
        Date aippe=new Date();
        aippe.setTime(System.currentTimeMillis());
        SimpleDateFormat formatterku=new SimpleDateFormat("dd-MM-yyyy__HH-mm-ss" );

        try {

            mPath = Environment.getExternalStorageDirectory().toString() + "/Certus" + "/Saved/";
            File imageFilee= new File(mPath);
            if (!imageFilee.exists()){
                imageFilee.mkdirs();
                File Nn=new File(imageFilee,".nomedia" );
                try {
                    Nn.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            File imageFile=new File(imageFilee.getAbsolutePath()+"/"+formatterku.format(aippe) + ".jpg");
            View v10 = findViewById(R.id.kkk);
            v10.setVisibility(View.INVISIBLE);
            View content = findViewById(R.id.rea);

            View v1 = content;
            v1.setDrawingCacheEnabled(true);
            Bitmap bm = content.getDrawingCache();








            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bm.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            filepath=imageFile.getAbsolutePath();
            outputStream.flush();
            outputStream.close();


        } catch (Throwable e) {

            e.printStackTrace();
        }
        finally {
            findViewById(R.id.kkk).setVisibility(View.VISIBLE);
        }
    }

    public static File lastFileModified(String dir) {
        File fl = new File(dir);
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        long lastMod = Long.MIN_VALUE;
        File choice = null;
        for (File file : files) {
            if (file.lastModified() > lastMod) {
                choice = file;
                lastMod = file.lastModified();
            }
        }
        return choice;
    }
    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }


}
