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
import android.widget.AdapterView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

import coverflow.CoverFlowView;
import coverflow.FancyCoverFlow;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private ArrayList<Integer> mData;
    private ArrayList<String> mTexts;
    private FancyCoverFlow coverFlow;
    private TextSwitcher switcher;
    private int onTopIndex = -1;
    private Intent intent;
    MyAdap adapter;
    CoverFlowView<MyAdap> mCoverFlowView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

       setContentView(R.layout.act_moder);




        mData = new ArrayList<>();
        mData.add(R.drawable.maykakac);
        mData.add(R.drawable.futbolakakach);
        mData.add(R.drawable.kurkakac);

        mTexts = new ArrayList<>();
        mTexts.add("Майки");
        mTexts.add("Футболки");
        mTexts.add("Куртки");


        switcher = (TextSwitcher) findViewById(R.id.textswitcher);

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

        mCoverFlowView = (CoverFlowView<MyAdap>) findViewById(R.id.coverflow);
       adapter = new MyAdap(this,mData);
        mCoverFlowView.setAdapter(adapter);
        mCoverFlowView.setDrawingCacheEnabled(true);
        mCoverFlowView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        mCoverFlowView.setCoverFlowListener(new CoverFlowView.CoverFlowListener<MyAdap>() {
            @Override
            public void imageOnTop(CoverFlowView<MyAdap> coverFlowView, int position, float left, float top, float right, float bottom) {
              //  switcher.setText(mTexts.get(position));
             //   onTopIndex = position;
                Toast.makeText(getApplicationContext(),mTexts.get(position),Toast.LENGTH_SHORT).show();

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

        /*


        coverFlow = (FancyCoverFlow) findViewById(R.id.coverflow);

        coverFlow.setMaxRotation(45);
        coverFlow.setUnselectedAlpha(0.3f);
        coverFlow.setUnselectedSaturation(0.0f);
        coverFlow.setUnselectedScale(0.9f);

        mData = new ArrayList<>();
        mData.add(R.drawable.maykakac);
        mData.add(R.drawable.futbolakakach);
        mData.add(R.drawable.kurkakac);
        mTexts = new ArrayList<>();
        mTexts.add("Майки");
        mTexts.add("Футболки");
        mTexts.add("Куртки");


        adapter = new CoverFlowAdapter(this, mData);
        coverFlow.setAdapter(adapter);
        coverFlow.setSelection(mData.size() / 2);
        onTopIndex = coverFlow.getSelectedItemPosition();


        coverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switcher.setText(mTexts.get(position));
                onTopIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isOnTop(position)) {
                    Toast.makeText(getApplicationContext(), Integer.toString(position) + " is on Top", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra("POS", position);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), Integer.toString(position) + " is going on Top", Toast.LENGTH_SHORT).show();
                    onTopIndex = position;
                }
            }
        });

        findViewById(R.id.leftbtn).setOnClickListener(this);
        findViewById(R.id.rightbtn).setOnClickListener(this);
        findViewById(R.id.slidebtn).setOnClickListener(this);
        findViewById(R.id.infobtn).setOnClickListener(this);
        findViewById(R.id.editbtn).setOnClickListener(this);*/
    }


    public void initSwitcher() {

    }

    public boolean isOnTop(int current_pos) {
        return (current_pos == onTopIndex);
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

