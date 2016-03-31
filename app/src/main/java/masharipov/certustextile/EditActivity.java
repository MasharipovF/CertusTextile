package masharipov.certustextile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private Spinner type_spinner;
    private RecyclerView clothes_list;
    private RecyclerAdapter adapter;
    private List<RecyclerData> forDB[] = new List[4], data;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        radioGroup = (RadioGroup) findViewById(R.id.yoqaGroup);
        clothes_list = (RecyclerView) findViewById(R.id.recycler_add);
        clothes_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        final RecyclerData rdata = new RecyclerData(null, null, null, null);
        data = new ArrayList<>();
        data.add(rdata);
        adapter = new RecyclerAdapter(getApplicationContext(), data);
        clothes_list.setAdapter(adapter);
        radioGroup.check(R.id.yoqa1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yoqa1:
                        data.clear();
                        data.add(rdata);
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.yoqa2:
                        data.clear();
                        data.add(rdata);
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.yoqa3:
                        data.clear();
                        data.add(rdata);
                        adapter.notifyDataSetChanged();
                        break;
                }

            }
        });


        type_spinner = (Spinner) findViewById(R.id.type_spinner);
        List<String> spin_items = new ArrayList<>();
        spin_items.add("Futbolka");
        spin_items.add("Sviter");
        spin_items.add("Jemper");
        spin_items.add("Pidjak");
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spin_items);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(spinAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter.onActivityResult(requestCode, resultCode, data);
    }
}
