package com.example.android.examhelper;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button mSelectFiles;
    static final int REQUEST_CODE=1;
    ClipData mClipData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSelectFiles = (Button)findViewById(R.id.select_files_button);

        mSelectFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_GET_CONTENT);
                in.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                in.setType("text/*");
                try{
                    startActivityForResult(in,REQUEST_CODE);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(MainActivity.this, "Please install a file browser", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case REQUEST_CODE:
                if(data!=null)
                {
                    mClipData = data.getClipData();
                    if(mClipData!=null)
                    {
                        Intent in = new Intent(this,QuestionsDisplay.class);
                        in.putExtra("uri_list",mClipData);
                        startActivity(in);
                    }
                    else
                    {
                        Toast.makeText(this, "Please select at least 2 files", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(this, "Please select at least 2 files", Toast.LENGTH_SHORT).show();
                    break;
                }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent startSettingsActivity = new Intent(this, AboutActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
