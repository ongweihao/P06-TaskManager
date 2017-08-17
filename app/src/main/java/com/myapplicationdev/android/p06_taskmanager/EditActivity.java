package com.myapplicationdev.android.p06_taskmanager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {


    Button btnEdit, btnDelete, btnCancel;
    EditText etName, etDescription, etSeconds;
    int piReqCode = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etName = (EditText) findViewById(R.id.etName);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etSeconds = (EditText) findViewById(R.id.etTime);

        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        Intent i = getIntent();
        final Task data = (Task) i.getSerializableExtra("data");

        etName.setText(data.getName());
        etDescription.setText(data.getDescription());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int seconds = Integer.valueOf(etSeconds.getText().toString());
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, seconds);

                data.setName(etName.getText().toString());
                data.setDescription(etDescription.getText().toString());

                DBHelper dbh = new DBHelper(EditActivity.this);
                dbh.updateTask(data);
                dbh.close();

                //Create a new PendingIntent and add it .to the AlarmManager
                Intent iReminder = new Intent(EditActivity.this, TaskReminderReceiver.class);

                iReminder.putExtra("id", data.getId());
                iReminder.putExtra("name", data.getName());
                iReminder.putExtra("desc", data.getDescription());

                PendingIntent pendingIntent = PendingIntent.getBroadcast(EditActivity.this, piReqCode, iReminder, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                setResult(RESULT_OK);
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBHelper dbh = new DBHelper(EditActivity.this);
                dbh.deleteTask(data.getId());
                dbh.close();
                setResult(RESULT_OK);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}
