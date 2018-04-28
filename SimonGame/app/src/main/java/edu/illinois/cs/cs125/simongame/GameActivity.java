package edu.illinois.cs.cs125.simongame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.LinkedList;

public class GameActivity extends AppCompatActivity {

    LinkedList<Integer> generated_button_order;
    LinkedList<Integer> user_button_order;
    int user_score = 0;
    Random button_generator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        generated_button_order = new LinkedList<Integer>();
        button_generator = new Random();
    }
    public void start_restart_button_clicked(final View v) {
      //if user pressed as restart, clear/save state. Begin a new game.
      judge();
      generate_button_list(1);
    }
    public void simon_button_clicked(final View v) {
      //this should be called when one of the four buttons are clicked.
      user_button_order.add(v.getID());
      if (user_button_order.size() == generated_button_order.size()) {
        judge();
      }
    }
    public void judge() {
      for (int i = 0; i < generated_button_order.size(); i++) {
        if (generated_button_order.get(i) != user_button_order.remove()) {
          //User messed up. Record score. Stop game (or give them another try).
          user_score = generated_button_order.size() - 1;
        }
      }
      //User got them all correct, continue game. Maybe play an animation?
      generate_button_list(generated_button_order.size() + 1);
    }
    public void generate_button_list(final int n) {
      //if we are generating a button list, clear what is currently there
      generated_button_order.clear();
      //add n buttons to the list
      for (int i = 0; i < n; i++) {
        generated_button_order.add(new Integer(button_generator.nextInt(4)));
      }
    }
}
