package masharipov.certustextile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

import coverflow.CoverFlowView;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private ArrayList<Integer> mData;
    private ArrayList<String> mTexts;
    private TextSwitcher switcher;
   private Intent intent;
    MyAdap adapter;
    CoverFlowView<MyAdap> mCoverFlowView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

       setContentView(R.layout.act_moder);
        switcher = (TextSwitcher) findViewById(R.id.textswitcher);




        mData = new ArrayList<>();
        mData.add(R.drawable.maykakac);
        mData.add(R.drawable.futbolakakach);
        mData.add(R.drawable.kurkakac);

        mTexts = new ArrayList<>();
        mTexts.add("Майки");
        mTexts.add("Футболки");
        mTexts.add("Куртки");





        mCoverFlowView = (CoverFlowView<MyAdap>) findViewById(R.id.coverflow);
       adapter = new MyAdap(this,mData);
        mCoverFlowView.setAdapter(adapter);
        mCoverFlowView.setDrawingCacheEnabled(true);
        mCoverFlowView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        mCoverFlowView.setCoverFlowListener(new CoverFlowView.CoverFlowListener<MyAdap>() {
            @Override
            public void imageOnTop(CoverFlowView<MyAdap> coverFlowView, int position, float left, float top, float right, float bottom) {
                Toast.makeText(getApplicationContext(), Integer.toString(position) + " is "+mTexts.get(position), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void topImageClicked(CoverFlowView<MyAdap> coverFlowView, int position) {
                Toast.makeText(getApplicationContext(), Integer.toString(position) + " is on Top", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("POS", position);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void invalidationCompleted() {
                //Toast.makeText(getApplicationContext(),"!!",Toast.LENGTH_SHORT).show();

            }
        });
        findViewById(R.id.leftbtn).setOnClickListener(this);
        findViewById(R.id.rightbtn).setOnClickListener(this);
        findViewById(R.id.slidebtn).setOnClickListener(this);
        findViewById(R.id.infobtn).setOnClickListener(this);
        findViewById(R.id.editbtn).setOnClickListener(this);
        initSwitcher();

    }


    public void initSwitcher() {
        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                TextView textView = (TextView) inflater.inflate(R.layout.coverflow_item_txt, null);
                return textView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
        switcher.setInAnimation(in);
        switcher.setOutAnimation(out);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftbtn:
                   // coverFlow.moveBackward();
                   // onTopIndex--;
                   // switcher.setText(mTexts.get(onTopIndex));
               /* if(mCoverFlowView.getTopImageIndex()>0)
                mCoverFlowView.setSelection(mCoverFlowView.getTopImageIndex()-1);
                else mCoverFlowView.setSelection(mData.size()-1);*/
                mCoverFlowView.toMoveBack();

                break;
            case R.id.rightbtn:
                mCoverFlowView.toMoveNext();
                break;
            case R.id.slidebtn:
                intent = new Intent(MainActivity.this, SlideshowActivity.class);
                startActivity(intent);
                break;
            case R.id.infobtn:
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("Info");
                adb.setMessage("Certus Textile");
                adb.setIcon(android.R.drawable.ic_dialog_info);
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                adb.create();
                adb.show();
                break;
            case R.id.editbtn:
                intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            default:
                break;
        }
    }
}

