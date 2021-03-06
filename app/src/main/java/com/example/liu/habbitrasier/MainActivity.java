package com.example.liu.habbitrasier;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private RadioGroup RgGroup;
    private ImageButton plus;
    final ArrayList<Habit> habitList = new ArrayList<>();
    DatabaseHelper db;
    ListView lst;
    private CustomListView mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RgGroup = (RadioGroup) findViewById(R.id.rg_group);
        plus = (ImageButton) findViewById(R.id.imageButton2);
        db = new DatabaseHelper(MainActivity.this);
        //TODO
        //initial fake data
        //db.addData("Study HCI","","","","",""," Read Critiques");
        populateListView();
        //click listener for ListView - view detail
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = habitList.get(i).getHabitName();
                Intent Det = new Intent(MainActivity.this, HabitDetail.class);
                Det.putExtra("name", name);
                startActivity(Det);
            }
        });


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Task = new Intent(MainActivity.this, Task.class);
                startActivity(Task);
            }
        });

        RgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.calender:
                        Intent Cal = new Intent(MainActivity.this, Calender.class);
                        startActivity(Cal);
                        break;
                    case R.id.achievement:
                        Intent Ach = new Intent(MainActivity.this, Achievement.class);
                        startActivity(Ach);
                        break;
                    case R.id.pet:
                        Intent Pet = new Intent(MainActivity.this, PetActivity.class);
                        startActivity(Pet);
                        break;
                    default:
                        break;
                }
            }
        });

        // Initialize notification.
        createNotificationChannel();    // Creating channel is required.

//        // Set a tapping handler.
//        // Create an explicit intent for an Activity in your app
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.home)
//                .setContentTitle("Habit Raiser")
//                .setContentText("It's time to do the task.")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                // Set the intent that will fire when the user taps the notification
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//
//        // Send a notification.
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        // notificationId is a unique int for each notification that you must define
//        notificationManager.notify(R.string.notif_id, builder.build());

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notif_name);
            String description = getString(R.string.notif_des);
            String CHANNEL_ID = getString(R.string.notif_id);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
//            channel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification2),
//                    new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                            .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
//                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT).build());
//            channel.setVibrationPattern(new long[] { 1000, 1000, 1000});
//            channel.setLightColor(Color.RED);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    //Populate listview
    private void populateListView() {
        // Uncomment these lines to drop a table, and create a new table. Clear Data is optional.
        // This function is called everytime the app is launched. Keep in mind.

        // db.dropTable();
        // db.createTable();
        // db.clearData();

        // If need to delete the data table, uncomment this line:

        habitList.clear();
        Cursor data = db.getData();
        if (data == null) {
            Toast.makeText(MainActivity.this, "NOTHING HERE", Toast.LENGTH_SHORT).show();
        }
        while (data.moveToNext()) {
            //Get the value from the database and add to Arraylist
            try{
                int ciHabitName = data.getColumnIndex("habitName");
                int ciDesp = data.getColumnIndex("description");
                int ciPriority = data.getColumnIndex("priority");
                String strHabitName = data.getString(ciHabitName);
                String strDesp = data.getString(ciDesp);
                String strPriority = data.getString(ciPriority);

                habitList.add(new Habit(strHabitName, strDesp, strPriority));
            }catch (Exception e){
                // Skip.
int a = 0;
            }
        }

        lst = (ListView) findViewById(R.id.lst);
        mAdapter = new CustomListView(this, habitList);
        lst.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }
}
