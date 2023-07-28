package com.example.todo;

import android.util.Log;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Priority;
import com.amplifyframework.datastore.generated.model.Todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating a new Todo item
        Date date = new Date();
        int offsetMillis = TimeZone.getDefault().getOffset(date.getTime());
        int offsetSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(offsetMillis);
        Temporal.DateTime temporalDateTime = new Temporal.DateTime(date, offsetSeconds);
        Todo item = Todo.builder()
                .name("Finish quarterly taxes")
                .priority(Priority.HIGH)
                .completedAt(temporalDateTime)
                .build();

        // Observing changes in the Todo class
        Amplify.DataStore.observe(Todo.class,
                cancelable -> Log.i("Tutorial", "Observation began."),
                todoChange -> {
                    Todo todo = todoChange.item();
                    Log.i("Tutorial", "Todo: " + todo);
                },
                failure -> Log.e("Tutorial", "Observation failed", failure),
                () -> Log.i("Tutorial", "Observation complete")
        );
    }
}
