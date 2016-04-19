package masharipov.certustextile.tpriew;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import coverflow.photoview.PhotoViewAttacher;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import masharipov.certustextile.ItemFragment;
import masharipov.certustextile.R;

public class Tview extends AppCompatActivity {
    ImageView A1,A2,A3;
    private PhotoViewAttacher mAttacher1,mAttacher2,mAttacher3;
    ItemFragment f1,f2,f3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tview);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        A1=(ImageView)findViewById(R.id.a1);
        A2=(ImageView)findViewById(R.id.a2);
        A3=(ImageView)findViewById(R.id.a3);

     //   f1= (new Gson()).fromJson((JsonElement) getIntent().getSerializableExtra("oldi"),ItemFragment.class);
       // f2=(ItemFragment) getIntent().getSerializableExtra("yon");

        //f3=(ItemFragment) getIntent().getSerializableExtra("orqa");

        //File getBit = new File(getPath(Uri.parse(current.getURI())));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.siniy_chorniy);
        A1.setImageBitmap(bitmap);
        Bitmap bitmapp = BitmapFactory.decodeResource(getResources(),R.drawable.siniy_chorniy_side);
        A2.setImageBitmap(bitmapp);
        Bitmap bitmappp = BitmapFactory.decodeResource(getResources(),R.drawable.siniy_chorniy_back);
        A3.setImageBitmap(bitmappp);

        mAttacher1 = new PhotoViewAttacher(A1);
        mAttacher2 = new PhotoViewAttacher(A2);
        mAttacher3 = new PhotoViewAttacher(A3);

    }
    @Override
    public void onStart(){
        super.onStart();

    }
}
