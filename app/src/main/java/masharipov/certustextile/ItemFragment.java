package masharipov.certustextile;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
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
    MyView A1;
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
      //      bitSticker =Bitmap.createScaledBitmap(bitSticker,stat_eni,stat_baland,false);

        }
            bitTovar = BitmapFactory.decodeResource(getResources(), voqtinchali_resurs);

    }
    eventZOOM peredacha;
    public ItemFragment(){

    }
    public  ItemFragment(int res,eventZOOM eve){
        voqtinchali_resurs=res;
        This=getActivity();
        peredacha=eve;

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
                 A1=new MyView(This);
                 frameSt.addView(A1);
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
     //   bitSticker =Bitmap.createScaledBitmap(bitSticker,stat_eni,stat_baland,true);
        frameSt.removeAllViews();
        A1=new MyView(This);
        frameSt.addView(A1);

        // return true esli sobitiya proizowlo false esli vozniklo owibka
        return true;
    }
    public interface eventZOOM{
        void EVZ(int t);
        void EVR(int t);
    }
    public void rotationPlus(){
        A1.rotationPlus();
    }
    public void rotationMinus(){
        A1.rotationMinus();
    }
    public void plusSize(){
         A1.scalePlus();


    }

    public void minusSize(){
       A1.scaleMinus();


    }
    float scaleHeight=1,scaleWidht=1;
    float rotatt=0;
    class MyView extends View  {

        Paint p;
        Matrix mmatrix ;
        // координаты для рисования квадрата

        float side_en = stat_eni;
        float side_ba = stat_baland;



        // переменные для перетаскивания
        boolean drag = false;
        float dragX = 0;
        float dragY = 0;
        long rotation=0;
        public float scalePlus(){
            if(side_en*scaleWidht<540&&side_ba*scaleHeight<540){
                scaleHeight+=0.05f;
                scaleWidht+=0.05f;

           /* side_en*=scaleWidht;
            side_ba*=scaleHeight;
            stat_eni=side_en;
            stat_baland=side_ba;*/
                invalidate();
                peredacha.EVZ((int) (scaleHeight/0.05f)-4);
                return scaleHeight;}
            else return 0f;        }
        public float scaleMinus(){
            if(side_en*scaleWidht>50&&side_ba*scaleHeight>50){
            scaleHeight-=0.05f;
            scaleWidht-=0.05f;
           /* side_en*=scaleWidht;
            side_ba*=scaleHeight;
            stat_eni=side_en;
            stat_baland=side_ba;*/
            invalidate();
                peredacha.EVZ((int) (scaleHeight/0.05f)-4);

                return scaleHeight;}
            else return 0f;
        }
        public void rotationPlus(){
            rotatt += 10;
            rotatt%=360;
            invalidate();
            peredacha.EVR((int)rotatt);

        }
        public void rotationMinus(){
            rotatt -= 10;
            rotatt%=360;
            invalidate();
            peredacha.EVR((int)rotatt);

        }
        public MyView(Context context) {
            super(context);
            if(xi==0) {
                xi = frameEni / 2  ;

                yi = frameBalandligi / 2 - side_ba;
            }
            p = new Paint(Paint.ANTI_ALIAS_FLAG);
            mmatrix= new Matrix();
            // matrix.postScale(2, 3);
            // matrix.postTranslate(200, 50);

        }



        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected void onDraw(Canvas canvas) {
            if (bitSticker == null) return;
            Matrix matrix =mmatrix;
            mmatrix.reset();
            matrix.postTranslate(-side_en/2f, -side_ba/2f);
            matrix.postRotate(rotatt);
            matrix.postTranslate(+side_en/2f, +side_ba/2f);
            matrix.postScale((0.0f + scaleWidht),(0.0f + scaleWidht));

            Log.d("Kordinate", Float.toString(scaleHeight)+"  "+Float.toString(scaleWidht));
            Log.d("Kordinate", ">>>"+Float.toString(-side_ba*scaleHeight)+"  "+Float.toString(-side_ba*scaleWidht));
            matrix.postTranslate(xi-side_en*scaleWidht/2f, yi-side_ba*scaleHeight/2f);

            canvas.drawBitmap(bitSticker, matrix, null);
            //canvas.drawCircle(xi,yi,5,p);
            // рисуем квадрат
            //  canvas.drawRect(x, y, x + side, y + side, p);
            /*matrix.reset();
            float vw = this.getWidth ();
            float vh = this.getHeight ();

            canvas.drawBitmap(bitSticker,xi,yi,p);*/
        }
        boolean secondNotPress=true;
        double rostayaniya=0;
        double perviyRostayaniya=0;
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // координаты Touch-события
            float evX = event.getX();
            float evY = event.getY();

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                // касание началось
                case MotionEvent.ACTION_DOWN:

                    // если касание было начато в пределах квадрата
                    if (evX >= xi - side_en*scaleWidht/2-100&& evX <= xi + side_en*scaleWidht/2 +100&& evY >= yi-  side_ba*scaleHeight/2-100 && evY <= yi+side_ba*scaleHeight/2+100) {
                        // включаем режим перетаскивания
                        drag = true;
                        // разница между левым верхним углом квадрата и точкой касания
                        dragX = evX - xi;
                        dragY = evY - yi;
                    }
                    break;
                // тащим
                case MotionEvent.ACTION_MOVE:
                   if (event.getPointerCount()>1){

                       rostayaniya=Math.sqrt(Math.pow(event.getX(0)-event.getX(1),2)+Math.pow(event.getY(0)-event.getY(1),2));
                       Log.d("SecondTouch",Double.toString(perviyRostayaniya)+" "+Double.toString(rostayaniya));
                       if(perviyRostayaniya==0){
                           perviyRostayaniya=rostayaniya;
                           Log.d("SecondTouch","PERVIY");
                       }
                       else{
                           if(perviyRostayaniya+100<rostayaniya){
                               perviyRostayaniya=rostayaniya;
                                plusSize();
                           }
                           else if(perviyRostayaniya-100>rostayaniya){
                               perviyRostayaniya=rostayaniya;
                               minusSize();
                           }
                       }


                   }
                    else
                   {
                       if (drag&&evX - dragX<frameEni-stat_eni*scaleWidht/2f&&evX - dragX>0&&evY - dragY>0&&evY-dragY<frameBalandligi-stat_baland*scaleHeight/2f) {
                           // определеяем новые координаты для рисования
                           xi = evX - dragX;
                           yi = evY - dragY;
                           // перерисовываем экран
                           invalidate();

                       }
                    // если режим перетаскивания включен
                    }


                    break;
                case MotionEvent.ACTION_POINTER_UP: // прерывания касаний
                    drag=false;
                    perviyRostayaniya=0;
                        break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.d("SecondTouch",Integer.toString(event.getActionIndex()));
                    //    Log.d("SecondTouch","DRAG OFFF");


                   // Log.d("SecondTouch",Float.toString(event.getX())+" "+Float.toString(event.getY()));
                   // Log.d("SecondTouch",Integer.toString(pointerIndex));
                    break;

                // касание завершено
                case MotionEvent.ACTION_UP:
                    // выключаем режим перетаскивания
                    secondNotPress=true;
                    drag = false;
                    break;
            }
            return true;
        }

    }
}
