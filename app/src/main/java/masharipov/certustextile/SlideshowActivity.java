package masharipov.certustextile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class SlideshowActivity extends AppCompatActivity {
    CarouselView carouselView;

    int[] sampleImages = {R.drawable.maykaaaa, R.drawable.futblkaaa, R.drawable.kurkaaa};
//https://github.com/sayyam/carouselview

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);

        carouselView = (CarouselView) findViewById(R.id.fancyCoverFlowa);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

    }

}
