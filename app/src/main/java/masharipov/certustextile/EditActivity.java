package masharipov.certustextile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private RecyclerAdapter adapter;
    private RecyclerView clothes_list;
    private Spinner typeSpinner;
    private RadioGroup genderGroup, collarGroup;
    private EditText tagEdit;
    private List<List<RecyclerData>> forDatabase;
    private String collar, gender;
    private Context contextforDialog = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modern_edit);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        collarGroup = (RadioGroup) findViewById(R.id.collarGroup);
        genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        tagEdit = (EditText) findViewById(R.id.itemTag);
        clothes_list = (RecyclerView) findViewById(R.id.recycler_add);
        clothes_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        forDatabase = new ArrayList<>(collarGroup.getChildCount());
        for (int i = 0; i < collarGroup.getChildCount(); i++)
            forDatabase.add(null);

        typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        List<String> spinItems = new ArrayList<>();
        spinItems.add("Futbolka");
        spinItems.add("Sviter");
        spinItems.add("Jemper");
        spinItems.add("Pidjak");
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinItems);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(spinAdapter);

        // znacheniya po umolchaniyu
        collar = "collar1";
        collarGroup.check(R.id.collar1);
        gender = "male";
        genderGroup.check(R.id.male);
        adapter = new RecyclerAdapter(this, newData(collar, gender));
        adapter.collarTag = 0;
        forDatabase.set(adapter.getCollarTag(), adapter.getDatabase());
        clothes_list.setAdapter(adapter);
        collarGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.collar1:
                        setData(adapter.getCollarTag(), 0);
                        tagEdit.setText(forDatabase.get(0).get(0).tag);
                        break;
                    case R.id.collar2:
                        setData(adapter.getCollarTag(), 1);
                        tagEdit.setText(forDatabase.get(1).get(0).tag);
                        break;
                    case R.id.collar3:
                        setData(adapter.getCollarTag(), 2);
                        tagEdit.setText(forDatabase.get(2).get(0).tag);
                        break;
                  /*  case R.id.collar4:
                        setData(adapter.getCollarTag(), 3);
                        tagEdit.setText(forDatabase.get(3).get(0).tag);
                        break;*/
                }
            }
        });

        /*
        final RadioButton collarRadio = (RadioButton) findViewById(R.id.collar4).setVisibility(View.GONE);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (typeSpinner.getSelectedItem().toString() == "") collarRadio.setVisibility(View.GONE);
                else collarRadio.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                List<RecyclerData> list = forDatabase.get(adapter.getCollarTag());
                RecyclerData recyclerData = list.get(list.size() - 1);
                switch (checkedId) {
                    case R.id.male:
                        recyclerData.setGender("male");
                        break;
                    case R.id.female:
                        recyclerData.setGender("female");
                        break;
                    case R.id.boy:
                        recyclerData.setGender("boy");
                        break;
                    case R.id.girl:
                        recyclerData.setGender("girl");
                        break;
                }
            }
        });

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


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDialog(contextforDialog);
            }
        });
    }

    private void setData(int receivedPos, int sentPos) {

        List<RecyclerData> list = forDatabase.get(receivedPos);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setTag(tagEdit.getText().toString());
        }
        forDatabase.set(receivedPos, adapter.getDatabase());
        try {
            if (forDatabase.get(sentPos) == null) {
                collar = "collar" + Integer.toString(sentPos + 1);
                genderGroup.check(R.id.male);
                gender = "male";
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
        rdata.setGender(gender);
        rdata.setSize("XS", 0);
        mdata.add(rdata);
        return mdata;
    }

    private void saveDialog(Context context) {
        if (isDatabaseEmpty()) {
            Toast.makeText(context, "База пуста, сначала добавьте товары", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            adb.setTitle("Сохранить изменения?");
            adb.setMessage("Сохранить внесенные изменения в базу данных?");
            adb.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    forDatabase.clear();
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            });

            adb.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CertusDatabase certusDatabase = new CertusDatabase(forDatabase, typeSpinner.getSelectedItem().toString(), getApplicationContext());
                    certusDatabase.saveToDB();
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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

    public String getPath(Uri uri) {
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter.onActivityResult(requestCode, resultCode, data);
    }

}