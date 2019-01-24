package com.example.android.examhelper;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.examhelper.Utils.AlgorithmUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class QuestionsDisplay extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private ClipData mClipData;
    private List<Uri> mUriList;
    private List<String> mQuestionsList;
    private ListView mQuestionListView;
    private HashMap<String, Integer> mQuestionsCount;
    private QuestionsAdapter adapter;
    private ProgressBar mQuestionsProgressBar;
    private TextView mProgressBarLabel;
    private Button mSaveButton;
    private static final int WRITE_REQUEST_CODE = 1;
    private EditText mFileEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_display);

        mClipData = (ClipData) getIntent().getParcelableExtra("uri_list");
        mUriList = getUriList(mClipData);
        mQuestionListView = (ListView) findViewById(R.id.question_list_view);
        mQuestionsProgressBar = (ProgressBar) findViewById(R.id.questions_progress_bar);
        mProgressBarLabel = (TextView) findViewById(R.id.progress_bar_label);
        mSaveButton = (Button) findViewById(R.id.save_button);
        mFileEditText = (EditText) findViewById(R.id.file_name_edit_text);

        try {
            mQuestionsList = getQuestionsList(mUriList, getString(R.string.delimiter_default_value));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(QuestionsDisplay.this);
        new QuestionsAsyncTask().execute(mQuestionsList);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);


        mQuestionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap.Entry<String, Integer> h = adapter.getItem(position);
                String query = h.getKey();
                String escapedQuery = null;
                try {
                    escapedQuery = URLEncoder.encode(query, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Uri uri = Uri.parse("http://www.google.com/#q=" + escapedQuery);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, WRITE_REQUEST_CODE);


            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String filename = mFileEditText.getText().toString() + ".txt";
                    String fileContents;
                    StringBuilder fileContentsBuilder = new StringBuilder();
                    FileOutputStream outputStream;

                    Iterator it = mQuestionsCount.entrySet().iterator();
                    while (it.hasNext()) {
                        HashMap.Entry pair = (HashMap.Entry) it.next();
                        fileContents = pair.getKey().toString() + " " + pair.getValue().toString();
                        fileContentsBuilder.append(fileContents + "\n\n\n");
                        System.out.println(pair.getKey() + " = " + pair.getValue());
                        it.remove();
                    }
                    fileContents = fileContentsBuilder.toString();
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(QuestionsDisplay.this, "Can't create new file :(", Toast.LENGTH_SHORT).show();
                        }
                    }

                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(fileContents.getBytes());
                        Toast.makeText(QuestionsDisplay.this, "saved", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(QuestionsDisplay.this, "File not found!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(QuestionsDisplay.this, "Error saving!", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }

    public class QuestionsAsyncTask extends AsyncTask<List<String>, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mQuestionsProgressBar.setVisibility(View.VISIBLE);
            mProgressBarLabel.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(List<String>... lists) {
            mQuestionsCount = AlgorithmUtils.counter(lists[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new QuestionsAdapter(mQuestionsCount);
            mQuestionListView.setAdapter(adapter);
            mQuestionsProgressBar.setVisibility(View.GONE);
            mProgressBarLabel.setVisibility(View.GONE);
            mSaveButton.setVisibility(View.VISIBLE);
            mFileEditText.setVisibility(View.VISIBLE);
        }
    }

    private List<Uri> getUriList(ClipData data) {
        List<Uri> uriList = new ArrayList<>();
        for (int i = 0; i < data.getItemCount(); i++) {
            uriList.add(data.getItemAt(i).getUri());
        }
        return uriList;
    }

    private List<String> getQuestionsList(List<Uri> uriList, String delimiter) throws IOException {
        InputStream is = null;
        BufferedReader reader;
        String line;
        List<String> questionsList = new ArrayList<String>();
        StringBuilder info = new StringBuilder();
        for (Uri uri : uriList) {
            is = getContentResolver().openInputStream(uri);
            reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                info.append(line);
            }
        }
        if (delimiter.equals("alpha")) {
            questionsList.addAll(Arrays.asList(info.toString().split("[a-e][)]")));
        } else if (delimiter.equals("num")) {
            questionsList.addAll(Arrays.asList(info.toString().split("[0-9]{1,2}[)]")));

        }
        for (int i = 0; i < questionsList.size(); i++) {
            if (questionsList.get(i).matches("\\s+") || questionsList.get(i).matches("^\\s*[A-z.-\\]]+\\s*$") || questionsList.get(i).matches("\\n+")) {
                questionsList.remove(i);
                i--;
            }
        }
        return questionsList;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.list_key))) {
            try {
                mQuestionsList = getQuestionsList(mUriList, sharedPreferences.getString(getString(R.string.list_key), getString(R.string.delimiter_default_value)));
                String k = sharedPreferences.getString(getString(R.string.list_key), getString(R.string.delimiter_default_value));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        new QuestionsAsyncTask().execute(mQuestionsList);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
