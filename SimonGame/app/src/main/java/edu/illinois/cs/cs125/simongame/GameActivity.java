package edu.illinois.cs.cs125.simongame;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.LinkedList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    LinkedList<Integer> generated_button_order;
    LinkedList<Integer> user_button_order;
    int user_score = 0;
    Random button_generator;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        generated_button_order = new LinkedList<Integer>();
        button_generator = new Random();
        user_button_order = new LinkedList<Integer>();
        TextView t = findViewById(R.id.HighScore);
        t.setText(Integer.valueOf(get_high_score()).toString());
    }
    public int get_high_score() {
        return this.getPreferences(Context.MODE_PRIVATE).getInt("high_score", 0);
    }
    public void start_restart_button_clicked(final View v) {
        //if user pressed as restart, clear/save state. Begin a new game.
        judge();
    }
    public void read_button_sequence() {
        //this is where put code to show user order of buttons
        for (Integer i : generated_button_order) {
            Toast.makeText(getApplicationContext(), i.toString(),
                    Toast.LENGTH_SHORT).show();
        }
        mp = MediaPlayer.create(this, R.raw.beep_short);
        mp.start();
    }
    public void simon_button_clicked(final View v) {
        //this should be called when one of the four buttons are clicked.
        //this is where sound/animation can be put
        Button b = (Button) v;
        user_button_order.add(Integer.valueOf(b.getText().toString()));
        if (user_button_order.size() == generated_button_order.size()) {
            judge();
        }
    }
    public void judge() {
        if (generated_button_order.size() == 0) {
            increment_button_list_size();
            return;
        }
        if (user_button_order.size() != generated_button_order.size()) {
            //user hit restart
            Toast.makeText(getApplicationContext(),
                    "Thanks for playing", Toast.LENGTH_SHORT).show();
            final_score();
            return;
        }
        for (int i = 0; i < generated_button_order.size(); i++) {
            if (!generated_button_order.get(i).equals(user_button_order.remove())) {
                //User messed up. Record score. Stop game (or give them another try).
                Toast.makeText(getApplicationContext(),
                        "WRONG! Pay attention.", Toast.LENGTH_SHORT).show();
                mp = MediaPlayer.create(this, R.raw.crash);
                mp.start();
                final_score();
                return;
            }
        }
        //User got them all correct, continue game. Maybe play an animation?
        Toast.makeText(getApplicationContext(), "Nice job!", Toast.LENGTH_SHORT).show();
        increase_score();
        increment_button_list_size();
    }
    public void increase_score() {
        user_score++;
        TextView t = findViewById(R.id.Score);
        t.setText(Integer.valueOf(user_score).toString());
    }
    public void final_score() {
        Toast.makeText(getApplicationContext(),
                "Thanks for playing. Your final score: "
                        + Integer.valueOf(user_score).toString(), Toast.LENGTH_LONG).show();
        //save high score to a preference file
        if (user_score > get_high_score()) {
            Toast.makeText(getApplicationContext(),
                    "You beat your old high score: "
                            + Integer.valueOf(get_high_score()).toString(), Toast.LENGTH_LONG).show();
            this.getPreferences(Context.MODE_PRIVATE).edit()
                    .putInt("high_score", user_score).apply();
        }
        user_score = 0;
        generated_button_order.clear();
    }
    public void increment_button_list_size() {
        generated_button_order.add(button_generator.nextInt(4));
        read_button_sequence();
    }
}

