package masharipov.certustextile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        int number = intent.getIntExtra("POS", -1);
        Toast.makeText(getApplicationContext(), Integer.toString(number) + " passed from Main activity", Toast.LENGTH_SHORT).show();
    }
}
