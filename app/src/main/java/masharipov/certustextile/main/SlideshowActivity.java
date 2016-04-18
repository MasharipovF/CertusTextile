package masharipov.certustextile.main;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import masharipov.certustextile.R;

public class SlideshowActivity extends AppCompatActivity {
    CarouselView carouselView;

    int[] sampleImages = {R.drawable.futblka, R.drawable.futblkaikki, R.drawable.futblkaorqa};
//https://github.com/sayyam/carouselview

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(0,50,0,10);
            /*wotta sampledanmas listdan olasiz uri ni
            uridan bitmapga o`tkazvotganda getPath metodini iwlatin

             File  getBit = new File(getPath(uriT));
            Bitmap bitTovar=BitmapFactory.decodeFile(getBit.getAbsolutePath());

            * */
            imageView.setImageResource(sampleImages[position]);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*
        Bitta listga urilani solish kere bazadan ob. sampleImages ni orniga owani iwlatiw kere

         */

        setContentView(R.layout.activity_slideshow);

        carouselView = (CarouselView) findViewById(R.id.fancyCoverFlowa);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
        carouselView.setPageTransformer(new FadePageTransformer());
        carouselView.setPageTransformInterval(800);

        carouselView.setAnimateOnBoundary(false);
        carouselView.setSlideInterval(4500);
    }
    private static class FadePageTransformer implements ViewPager.PageTransformer {
        public void transformPage(View view, float position) {
            view.setTranslationX(view.getWidth() * -position);

            if(position <= -1.0F || position >= 1.0F) {
                view.setAlpha(0.0F);
            } else if( position == 0.0F ) {
                view.setAlpha(1.0F);
            } else {
                // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                view.setAlpha(1.0F - Math.abs(position));
            }
        }
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

