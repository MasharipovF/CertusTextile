package masharipov.certustextile;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.Random;

/**
 * Created by developer on 01.04.2016.
 */
public class ItemFragment extends Fragment{
    ImageView tovar;
    Context This;

    //Bu sticker i tovari URI i

    Uri uriT;
    Uri uriS;

    Bitmap bitTovar;
    Bitmap bitSticker;

    //Bu voqtinchali peremenniyla Resurs bilan ishlashga

    FrameLayout frameSt;
    int voqtinchali_resurs;
    int voqtinchali_sticker=0;


    //Rabota s nakleykoy
    int stat_baland=200;
    int stat_eni=200;

    public float xi=0,yi=0;

    public int frameBalandligi;
    public int frameEni;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.d("lifee","onAttach");
        This=context;
        if(bitTovar!=null)
        bitTovar.recycle();
        if(bitSticker!=null)
            bitSticker.recycle();
        bitSticker=null;
        bitTovar=null;

        //   bitTovar = BitmapFactory.decodeResource(getResources(), voqtinchali_resurs);

    }

    @Override
    public void onCreate(Bundle context){
        super.onCreate(context);
        Log.d("lifee","onCreate");
        if(voqtinchali_sticker!=0) {
            bitSticker = BitmapFactory.decodeResource(getResources(), voqtinchali_sticker);
            bitSticker =Bitmap.createScaledBitmap(bitSticker,stat_eni,stat_baland,false);

        }
            bitTovar = BitmapFactory.decodeResource(getResources(), voqtinchali_resurs);

    }

    public ItemFragment(){

    }
    public  ItemFragment(int res){
        voqtinchali_resurs=res;
        This=getActivity();


    }
    public  ItemFragment(Uri pathFile){
        uriT=pathFile;
        This=getActivity();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View fragment_item=inflater.inflate(R.layout.fragment_itemfragmentr,container,false);

        tovar =(ImageView) fragment_item.findViewById(R.id.tovar);
        frameSt =(FrameLayout) fragment_item.findViewById(R.id.frameStick);
        tovar.setImageBitmap(bitTovar);
        fragment_item.post(new Runnable() {
            @Override
            public void run() {
                if(bitSticker!=null){
                frameEni=frameSt.getWidth();
                frameBalandligi=frameSt.getHeight();
                 frameSt.addView(new MyView(This));
                Log.d("lifee", "Frame size  "+frameEni+"x"+frameBalandligi);}
                else Log.d("lifee","Null bitmap sticker");
            }
        });
        Log.d("lifee","onCreateView");
       // bitTovar.recycle();
       // Picasso.with(This).load(voqtinchali_resurs).into(tovar);
        return fragment_item;
    }
    @Override
    public void onStart(){
        super.onStart();

    }
    //Bu funksiya boshqa bir tovar tanlanganda o`ziga yengi tovari PATH ni qabul qilib
    //      tovari korinishini o`zgartiradi

    public boolean changeTovar(Uri pathFile){
        uriT=pathFile;
        Picasso.with(This).load(uriT).into(tovar);
        return true;
    }

    public boolean changeTovar(int pathFile){
        voqtinchali_resurs=pathFile;
        Picasso.with(This).load(voqtinchali_resurs).into(tovar);
        return true;
    }

    public boolean changeSticker(Uri pathFile){
        uriS=pathFile;
        Picasso.with(This).load(uriS).into(tovar);
        // return true esli sobitiya proizowlo false esli vozniklo owibka
        return true;
    }

    public boolean changeSticker(int pathFile){
        voqtinchali_sticker=pathFile;
        frameEni=frameSt.getWidth();
        frameBalandligi=frameSt.getHeight();
        bitSticker=BitmapFactory.decodeResource(getResources(), voqtinchali_sticker);
        bitSticker =Bitmap.createScaledBitmap(bitSticker,stat_eni,stat_baland,true);
        frameSt.removeAllViews();
        frameSt.addView(new MyView(This));

        // return true esli sobitiya proizowlo false esli vozniklo owibka
        return true;
    }
    public void plusSize(float A){
        if(voqtinchali_sticker!=0){
        stat_baland*=(1+A);
        stat_eni*=(1+A);
        bitSticker.recycle();
        bitSticker=BitmapFactory.decodeResource(getResources(), voqtinchali_sticker);
        bitSticker =Bitmap.createScaledBitmap(bitSticker,stat_eni,stat_baland,true);
        frameSt.removeAllViews();
             frameSt.addView(new MyView(This));
        }

    }

    public void minusSize(float A){
        if(voqtinchali_sticker!=0){
            stat_baland*=(1-A);
            stat_eni*=(1-A);
            bitSticker.recycle();
            bitSticker=BitmapFactory.decodeResource(getResources(), voqtinchali_sticker);
            bitSticker =Bitmap.createScaledBitmap(bitSticker,stat_eni,stat_baland,true);
            frameSt.removeAllViews();
            frameSt.addView(new MyView(This));
        }

    }
    class MyView extends View {

        Paint p;
        // координаты для рисования квадрата

        int side_en = stat_eni;
        int side_ba = stat_baland;


        // переменные для перетаскивания
        boolean drag = false;
        float dragX = 0;
        float dragY = 0;


        public MyView(Context context) {
            super(context);
            if(xi==0) {
                xi = frameEni / 2 - stat_eni / 2;
                yi = frameBalandligi / 2 - stat_baland / 2 - 100;
            }
            p = new Paint(Paint.ANTI_ALIAS_FLAG);
            // matrix.postScale(2, 3);
            // matrix.postTranslate(200, 50);

        }

        int pp;
        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected void onDraw(Canvas canvas) {
            // рисуем квадрат
            //  canvas.drawRect(x, y, x + side, y + side, p);

            canvas.drawBitmap(bitSticker,xi,yi,p);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // координаты Touch-события
            float evX = event.getX();
            float evY = event.getY();
            switch (event.getAction()) {
                // касание началось
                case MotionEvent.ACTION_DOWN:
                    // если касание было начато в пределах квадрата
                    if (evX >= xi && evX <= xi + side_en && evY >= yi && evY <= yi+ side_ba) {
                        // включаем режим перетаскивания
                        drag = true;
                        // разница между левым верхним углом квадрата и точкой касания
                        dragX = evX - xi;
                        dragY = evY - yi;
                    }
                    break;
                // тащим
                case MotionEvent.ACTION_MOVE:
                    // если режим перетаскивания включен
                    if (drag&&evX - dragX<frameEni-stat_eni&&evX - dragX>0&&evY - dragY>0&&evY-dragY<frameBalandligi-stat_baland) {
                        // определеяем новые координаты для рисования
                        xi = evX - dragX;
                        yi = evY - dragY;
                        // перерисовываем экран
                        invalidate();
                    }
                    break;
                // касание завершено
                case MotionEvent.ACTION_UP:
                    // выключаем режим перетаскивания
                    drag = false;
                    break;
            }
            return true;
        }

    }
}
