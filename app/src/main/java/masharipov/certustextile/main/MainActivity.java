package masharipov.certustextile.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

import coverflow.CoverFlowView;
import masharipov.certustextile.R;
import masharipov.certustextile.SecondActivity;
import masharipov.certustextile.edit.EditActivity;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private ArrayList<Integer> mData;
    private ArrayList<String> mTexts;
    private TextSwitcher switcher;
    private Intent intent;
    int tempPos = -1;
    MyAdap adapter;
    CoverFlowView<MyAdap> mCoverFlowView;
    SharedPreferences sPref;
    SharedPreferences.Editor ed;
    String passkey = "passkey";
    String zapasParol = "159263";
    String passButtonState = "normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_moder);

        // to store passwords
        sPref = getSharedPreferences("password", MODE_PRIVATE);
        ed = sPref.edit();


        switcher = (TextSwitcher) findViewById(R.id.textswitcherr);


        mData = new ArrayList<>();
        mData.add(R.drawable.mayka2);
        mData.add(R.drawable.futblki3);
        mData.add(R.drawable.poloone);
        mTexts = new ArrayList<>();
        mTexts.add("Майки");
        mTexts.add("Футболки");
        mTexts.add("Поло");


        mCoverFlowView = (CoverFlowView<MyAdap>) findViewById(R.id.coverflow);
        adapter = new MyAdap(this, mData);
        mCoverFlowView.setAdapter(adapter);
        mCoverFlowView.setDrawingCacheEnabled(true);
        mCoverFlowView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        mCoverFlowView.setCoverFlowListener(new CoverFlowView.CoverFlowListener<MyAdap>() {
            @Override
            public void imageOnTop(CoverFlowView<MyAdap> coverFlowView, int position, float left, float top, float right, float bottom) {
                Log.d("POSITION", Integer.toString(tempPos) + " " + Integer.toString(position));
                if (position != tempPos) {
                    switcher.setText(mTexts.get(position));
                    tempPos = position;
                }

            }

            @Override
            public void topImageClicked(CoverFlowView<MyAdap> coverFlowView, int position) {
                // Toast.makeText(getApplicationContext(), Integer.toString(position) + " is on Top", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("POS", position);
                String togo = "Futbolka";
                switch (position) {
                    case 0:
                        togo = "Mayka";
                        break;
                    case 1:
                        togo = "Futbolka";
                        break;
                    case 2:
                        togo = "Polo";
                        break;
                    default:
                        togo = "Futbolka";
                }
                intent.putExtra("TABLENAME", togo);
                startActivity(intent);
                //     Toast.makeText(getApplicationContext(), Integer.toString(position), Toast.LENGTH_SHORT).show();

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
                //   mCoverFlowView.setSelection(++tempPos%3);
                mCoverFlowView.toMoveBack();
                break;
            case R.id.rightbtn:

                //   mCoverFlowView.setSelection(--tempPos%3);
                mCoverFlowView.toMoveNext();
                break;
            case R.id.slidebtn:
                intent = new Intent(MainActivity.this, SlideshowActivity.class);
                startActivity(intent);
                break;
            case R.id.infobtn:
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("Certus Textile");
                adb.setMessage("Программное обеспечение разработано 'Masharipov&Islomov' Ltd.");
                adb.setIcon(R.mipmap.ic_launcher);
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                adb.create();
                adb.show();
                break;
            case R.id.editbtn:

                // TODO vremmenno, uberu posle zaversheniya projecta
                intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);

               /* final Dialog dialog = new Dialog(this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.password_prompt);
                dialog.setTitle("Введите пароль:");
                dialog.setCancelable(true);

                Button posBtn = (Button) dialog.findViewById(R.id.dialogPos);
                Button negBtn = (Button) dialog.findViewById(R.id.dialogNeg);
                final EditText userInput = (EditText) dialog.findViewById(R.id.editTextDialogUserInput);
                final EditText newpass1 = (EditText) dialog.findViewById(R.id.newpass1);
                final EditText newpass2 = (EditText) dialog.findViewById(R.id.newpass2);
                final TextView changePasBtn = (TextView) dialog.findViewById(R.id.changepass);
                userInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                newpass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                newpass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                // esli net parolya
                if (sPref.getString(passkey, "0").equals("0")) {
                    newpass1.setVisibility(View.VISIBLE);
                    newpass2.setVisibility(View.VISIBLE);
                    userInput.setVisibility(View.GONE);
                    changePasBtn.setVisibility(View.GONE);
                }

                changePasBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newpass1.setVisibility(View.VISIBLE);
                        newpass2.setVisibility(View.VISIBLE);
                        changePasBtn.setVisibility(View.GONE);
                        userInput.getText().clear();
                        passButtonState = "newpass";
                        dialog.setTitle("Изменение пароля");
                        userInput.setHint("Текущий пароль");
                        userInput.setError(null);
                    }
                });

                posBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (newpass1.getVisibility() == View.VISIBLE && newpass2.getVisibility() == View.VISIBLE) { // esli v rejime izmeneiya parolya
                            if (sPref.getString(passkey, "0").equals("0")) { // esli parolya ewe net
                                if (newPassChecker(newpass1, newpass2)) { // esli noviy parol zapisan
                                    newpass1.setVisibility(View.GONE);
                                    newpass2.setVisibility(View.GONE);
                                    changePasBtn.setVisibility(View.VISIBLE);
                                    userInput.setVisibility(View.VISIBLE);
                                }
                            } else { // esli parol uje est
                                newpass1.setVisibility(View.VISIBLE);
                                newpass2.setVisibility(View.VISIBLE);
                                userInput.setVisibility(View.VISIBLE);
                                changePasBtn.setVisibility(View.GONE);

                                if (sPref.getString(passkey, "").equals(userInput.getText().toString()) || userInput.getText().toString().equals(zapasParol)) { // esli parol sovpadaye
                                    if (newPassChecker(newpass1, newpass2)) {
                                        newpass1.setVisibility(View.GONE);
                                        newpass2.setVisibility(View.GONE);
                                        userInput.setVisibility(View.VISIBLE);
                                        changePasBtn.setVisibility(View.VISIBLE);
                                        userInput.getText().clear();
                                    }
                                } else {
                                    userInput.setError("Неправильный пароль");
                                }
                            }
                        } else {
                            if (sPref.getString(passkey, "0").equals(userInput.getText().toString()) || userInput.getText().toString().equals(zapasParol)) {
                                intent = new Intent(MainActivity.this, EditActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                            } else {
                                userInput.setError("Неправильный пароль");
                            }
                        }
                    }
                });
                negBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (passButtonState) {
                            case "normal":
                                dialog.dismiss();
                                break;
                            case "newpass":
                                newpass1.setVisibility(View.GONE);
                                newpass2.setVisibility(View.GONE);
                                userInput.setVisibility(View.VISIBLE);
                                changePasBtn.setVisibility(View.VISIBLE);
                                userInput.getText().clear();
                                newpass1.getText().clear();
                                newpass2.getText().clear();
                                passButtonState = "normal";
                                dialog.setTitle("Введите пароль:");
                                userInput.setHint("");
                                userInput.setError(null);
                                break;
                        }
                    }
                });
                dialog.show();*/
            default:
                break;
        }

    }

    public boolean newPassChecker(EditText e1, EditText e2) {
        if (e1.getText().toString().equals(e2.getText().toString())) { // vvedennie paroli sovpadayut
            ed.putString(passkey, e1.getText().toString());
            ed.commit();
            return true;
        } else {
            e2.setError("Пароли не совпадают");
            return false;
        }
    }
}

