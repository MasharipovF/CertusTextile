package masharipov.certustextile.edit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import masharipov.certustextile.CertusDatabase;
import masharipov.certustextile.R;
import masharipov.certustextile.stickeradd.NakleykaActivity;

public class EditActivity extends AppCompatActivity {

    private RecyclerAdapter adapter;
    private RecyclerView clothes_list;
    private Spinner typeSpinner;
    private RadioGroup genderGroup, collarGroup;
    private EditText tagEdit;
    private List<List<RecyclerData>> forDatabase;
    private String collar, gender;
    private Context contextforDialog = this;
    private final int SAVE_BUTTON = 1, BACK_BUTTON = 2;
    private String[] categoriesRus = {"Футболки", "Майки", "Поло"};
    private String[] categories = {"Futbolka", "Mayka", "Polo"};
    private String categoryForBaza;

    private ImageView collar1, collar2, collar3, collar4;
    private RadioButton collarBtn1, collarBtn2, collarBtn3, collarBtn4;
    Integer[] futbolkaCollar = {R.drawable.kruglivorot, R.drawable.vvorot, R.drawable.vorotpugi, R.drawable.shirokiyvorot};
    Integer[] maykaCollar = {R.drawable.mayka_krugliy, R.drawable.mayka_lodachka, R.drawable.mayka_vobrazniy};
    Integer[] poloCollar = {R.drawable.poloyoqa, R.drawable.poloyoqa3, R.drawable.polo_stoykayoqa};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.modern_edit);


        // dlya otobrajeniya VOROTnikov
        collar1 = (ImageView) findViewById(R.id.collarImg1);
        collar2 = (ImageView) findViewById(R.id.collarImg2);
        collar3 = (ImageView) findViewById(R.id.collarImg3);
        collar4 = (ImageView) findViewById(R.id.collarImg4);
        collarBtn1 = (RadioButton) findViewById(R.id.collarRadio1);
        collarBtn2 = (RadioButton) findViewById(R.id.collarRadio2);
        collarBtn3 = (RadioButton) findViewById(R.id.collarRadio3);
        collarBtn4 = (RadioButton) findViewById(R.id.collarRadio4);


        collarGroup = (RadioGroup) findViewById(R.id.collarGroup);
        genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        tagEdit = (EditText) findViewById(R.id.itemTag);
        clothes_list = (RecyclerView) findViewById(R.id.recycler_add);
        clothes_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        forDatabase = new ArrayList<>();
        for (int i = 0; i < collarGroup.getChildCount(); i++) {
            if (collarGroup.getChildAt(i) instanceof ImageView) continue;
            forDatabase.add(null);
        }
        typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        List<String> spinItems = Arrays.asList(categoriesRus);
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinItems);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(spinAdapter);

        // znacheniya po umolchaniyu
        collar = "collar1";
        collarGroup.check(R.id.collarRadio1);
        gender = "male";
        genderGroup.check(R.id.male);
        adapter = new RecyclerAdapter(this, newData(collar, gender), (RelativeLayout) findViewById(R.id.layoutForSnackbar));
        adapter.collarTag = 0;
        forDatabase.set(adapter.getCollarTag(), adapter.getDatabase());
        clothes_list.setAdapter(adapter);
        collarGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.collarRadio1:
                        setData(adapter.getCollarTag(), 0);
                        break;
                    case R.id.collarRadio2:
                        setData(adapter.getCollarTag(), 1);
                        break;
                    case R.id.collarRadio3:
                        setData(adapter.getCollarTag(), 2);
                        break;
                    case R.id.collarRadio4:
                        setData(adapter.getCollarTag(), 3);
                        break;
                }
            }
        });


        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (typeSpinner.getSelectedItem().toString()) {
                    case "Футболки":
                        categoryForBaza = categories[0];
                        setCollarImages(futbolkaCollar);
                        collar4.setVisibility(View.VISIBLE);
                        collarBtn4.setVisibility(View.VISIBLE);
                        break;
                    case "Майки":
                        categoryForBaza = categories[1];
                        setCollarImages(maykaCollar);
                        collar4.setVisibility(View.GONE);
                        collarBtn4.setVisibility(View.GONE);
                        break;
                    case "Поло":
                        categoryForBaza = categories[2];
                        setCollarImages(poloCollar);
                        collar4.setVisibility(View.GONE);
                        collarBtn4.setVisibility(View.GONE);
                        break;
                    default:
                        categoryForBaza = categories[0];
                        setCollarImages(futbolkaCollar);
                        collar4.setVisibility(View.VISIBLE);
                        collarBtn4.setVisibility(View.VISIBLE);
                        break;
                }
                collarGroup.check(R.id.collarRadio1);
                genderGroup.check(R.id.male);
                gender = "male";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male:
                        gender = "male";
                        break;
                    case R.id.female:
                        gender = "female";
                        break;
                    case R.id.boy:
                        gender = "boy";
                        break;
                    case R.id.girl:
                        gender = "girl";
                        break;
                }
            }
        });

        // keeping it for some reasons)))
      /*  tagEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                List<RecyclerData> list = forDatabase.get(adapter.getCollarTag());
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setTag(tagEdit.getText().toString());
                }
            }
        });
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = typeSpinner.getSelectedItem().toString();
                typePos = typeSpinner.getSelectedItemPosition();
                List<RecyclerData> list = forDatabase.get(adapter.getCollarTag());
                RecyclerData recyclerData = list.get(list.size() - 1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/

        findViewById(R.id.savebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isDatabaseEmpty())
                    anothersaveDialog(contextforDialog, SAVE_BUTTON);
                else
                    Toast.makeText(getApplicationContext(), "База пуста, сначала добавьте товары", Toast.LENGTH_SHORT).show();
            }
        });

        // TODO pered tem kak zaxodit po etim knopkam, soxranit li bazu
        findViewById(R.id.stickerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, NakleykaActivity.class);
                intent.putExtra("TYPE", "STICKER");
                startActivity(intent);
            }
        });

        findViewById(R.id.goodsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, NakleykaActivity.class);
                intent.putExtra("TYPE", "GOODS");
                startActivity(intent);
            }
        });


        findViewById(R.id.slidebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, NakleykaActivity.class);
                intent.putExtra("TYPE", "SLIDESHOW");
                startActivity(intent);
            }
        });

    }

    private void setData(int receivedPos, int sentPos) {

        forDatabase.set(receivedPos, adapter.getDatabase());
        try {
            if (forDatabase.get(sentPos) == null) {
                collar = "collar" + Integer.toString(sentPos + 1);
                adapter.setDatabase(newData(collar, gender));
                adapter.collarTag = sentPos;
                forDatabase.set(adapter.getCollarTag(), adapter.getDatabase());

            } else {
                adapter.setDatabase(forDatabase.get(sentPos));
                adapter.collarTag = sentPos;
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private List<RecyclerData> newData(String collar, String gender) {
        List<RecyclerData> mdata = new ArrayList<>();
        RecyclerData rdata = new RecyclerData();
        rdata.setCollar(collar);
        rdata.setSize("XS");
        rdata.setSizePos(0);
        mdata.add(rdata);
        return mdata;
    }

    private void saveDialog(final Context context, int flag) {
        if (isDatabaseEmpty()) {
            Toast.makeText(context, "База пуста, сначала добавьте товары", Toast.LENGTH_SHORT).show();
        } else {
            final AlertDialog.Builder adb = new AlertDialog.Builder(context);
            adb.setTitle("Сохранить изменения?");
            adb.setMessage("Сохранить внесенные изменения в базу данных?");
            adb.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    forDatabase.clear();
                    finish();
                }

            });


            adb.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<RecyclerData> list = new ArrayList<>();
                    String uniqueID;

                    for (int i = 0; i < forDatabase.size(); i++) {
                        List<RecyclerData> tmpList = forDatabase.get(i);
                        if (tmpList == null || tmpList.size() == 0) continue;
                        tmpList.remove(tmpList.size() - 1);
                        if (tmpList.size() != 0) {
                            for (int j = 0; j < tmpList.size(); j++) {
                                uniqueID = Long.toString(System.currentTimeMillis());
                                RecyclerData mItem = tmpList.get(j);
                                mItem.setGender(gender);
                                mItem.setID(uniqueID);
                                list.add(mItem);
                            }
                        }
                        Log.v("TOVAR", "Extracted from collar " + Integer.toString(i) + ",data size " + Integer.toString(tmpList.size()));
                    }
                    CertusDatabase certusDatabase = new CertusDatabase(getApplicationContext(), list);
                    certusDatabase.saveGoodsToDB(categoryForBaza, false);

                    // ochistka dannix
                    forDatabase.clear();
                    adapter.clearDatabase();
                    int j = 0;
                    for (int i = 0; i < collarGroup.getChildCount(); i++) {
                        if (collarGroup.getChildAt(i) instanceof ImageView) continue;
                        forDatabase.add(null);
                    }
                    adapter.collarTag = 0;
                    collar = "collar1";
                    gender = "male";
                    collarGroup.check(R.id.collarRadio1);
                    genderGroup.check(R.id.male);
                    adapter.setDatabase(newData(collar, gender));
                    forDatabase.set(adapter.getCollarTag(), adapter.getDatabase());
                    adapter.notifyDataSetChanged();
                    tagEdit.getText().clear();
                }
            });
            adb.setNeutralButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            adb.setIcon(android.R.drawable.ic_dialog_info);
            adb.create();
            adb.show();
        }

    }

    private boolean isDatabaseEmpty() {
        try {
            List<List<RecyclerData>> tmpData = forDatabase;
            List<RecyclerData> recyclerDataList;
            int emptyCounter = 0;
            for (int i = 0; i < tmpData.size(); i++) {
                recyclerDataList = tmpData.get(i);
                if (recyclerDataList == null || recyclerDataList.size() == 1) {
                    emptyCounter++;
                }
            }
            return emptyCounter == tmpData.size();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return true;
        }
    }


    public void setCollarImages(Integer[] images) {
        collar1.setImageResource(images[0]);
        collar2.setImageResource(images[1]);
        collar3.setImageResource(images[2]);
        if (images.length == 4)
            collar4.setImageResource(images[3]);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        if (isDatabaseEmpty()) {
            finish();
            super.onBackPressed();
        } else {
            anothersaveDialog(this, BACK_BUTTON);
           /* AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Сохранить изменения?");
            adb.setMessage("Сохранить внесенные изменения в базу данных?");
            adb.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    forDatabase.clear();
                    finish();
                }

            });

            adb.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<RecyclerData> list = new ArrayList<>();
                    for (int i = 0; i < forDatabase.size(); i++) {
                        List<RecyclerData> tmpList = forDatabase.get(i);
                        if (tmpList == null || tmpList.size() == 0) continue;
                        tmpList.remove(tmpList.size() - 1);
                        if (tmpList.size() != 0) {
                            list.addAll(tmpList);
                        }
                    }
                    Log.v("TOVAR", "LIST SIZE " + Integer.toString(list.size()));
                    CertusDatabase certusDatabase = new CertusDatabase(getApplicationContext(), list);
                    certusDatabase.saveGoodsToDB(categoryForBaza, false);
                    finish();
                }
            });
            adb.setNeutralButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            adb.create();
            adb.show();*/
        }
    }

    public void anothersaveDialog(Context context, int flag) {
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.save_dialog);
        dialog.setCancelable(true);

        Button okBtn = (Button) dialog.findViewById(R.id.saveDialogOKBtn);
        Button noBtn = (Button) dialog.findViewById(R.id.saveDialogNOBtn);
        Button cancelBtn = (Button) dialog.findViewById(R.id.saveDialogCancelBtn);
        TextView typeTxt = (TextView) dialog.findViewById(R.id.saveDialogType);
        TextView genderTxt = (TextView) dialog.findViewById(R.id.saveDialogGender);

        typeTxt.setText(typeSpinner.getSelectedItem().toString());

        String curGender;
        switch (gender) {
            case "male":
                curGender = "Мужской";
                break;
            case "female":
                curGender = "Женский";
                break;
            case "boy":
                curGender = "Детский (м)";
                break;
            case "girl":
                curGender = "Детский (ж)";
                break;
            default:
                curGender = "Мужской";
                break;
        }
        genderTxt.setText(curGender);

        switch (flag) {
            case SAVE_BUTTON:
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<RecyclerData> list = new ArrayList<>();
                        String uniqueID = Long.toString(System.currentTimeMillis());

                        for (int i = 0; i < forDatabase.size(); i++) {
                            List<RecyclerData> tmpList = forDatabase.get(i);
                            if (tmpList == null || tmpList.size() == 0) continue;
                            tmpList.remove(tmpList.size() - 1);
                            if (tmpList.size() != 0) {
                                for (int j = 0; j < tmpList.size(); j++) {
                                    RecyclerData mItem = tmpList.get(j);
                                    mItem.setGender(gender);
                                    mItem.setID(uniqueID);
                                    list.add(mItem);
                                }
                            }
                            Log.v("TOVAR", "Extracted from collar " + Integer.toString(i) + ",data size " + Integer.toString(tmpList.size()));
                        }
                        CertusDatabase certusDatabase = new CertusDatabase(getApplicationContext(), list);
                        certusDatabase.saveGoodsToDB(categoryForBaza, false);

                        // ochistka dannix
                        clearDataBase();
                        dialog.dismiss();
                    }
                });

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearDataBase();
                        dialog.dismiss();
                    }
                });
                break;
            case BACK_BUTTON:
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<RecyclerData> list = new ArrayList<>();
                        String uniqueID = Long.toString(System.currentTimeMillis());
                        for (int i = 0; i < forDatabase.size(); i++) {
                            List<RecyclerData> tmpList = forDatabase.get(i);
                            if (tmpList == null || tmpList.size() == 0) continue;
                            tmpList.remove(tmpList.size() - 1);
                            if (tmpList.size() != 0) {
                                for (int j = 0; j < tmpList.size(); j++) {
                                    RecyclerData mItem = tmpList.get(j);
                                    mItem.setGender(gender);
                                    mItem.setID(uniqueID);
                                    list.add(mItem);
                                }
                            }
                            Log.v("TOVAR", "Extracted from collar " + Integer.toString(i) + ",data size " + Integer.toString(tmpList.size()));
                        }
                        Log.v("TOVAR", "LIST SIZE " + Integer.toString(list.size()));
                        CertusDatabase certusDatabase = new CertusDatabase(getApplicationContext(), list);
                        certusDatabase.saveGoodsToDB(categoryForBaza, false);
                        finish();
                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        forDatabase.clear();
                        finish();
                    }
                });
                break;
        }
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void clearDataBase() {
        forDatabase.clear();
        adapter.clearDatabase();
        int j = 0;
        for (int i = 0; i < collarGroup.getChildCount(); i++) {
            if (collarGroup.getChildAt(i) instanceof ImageView) continue;
            forDatabase.add(null);
        }
        adapter.collarTag = 0;
        collar = "collar1";
        gender = "male";
        collarGroup.check(R.id.collarRadio1);
        genderGroup.check(R.id.male);
        adapter.setDatabase(newData(collar, gender));
        forDatabase.set(adapter.getCollarTag(), adapter.getDatabase());
        adapter.notifyDataSetChanged();
        tagEdit.getText().clear();
    }

}