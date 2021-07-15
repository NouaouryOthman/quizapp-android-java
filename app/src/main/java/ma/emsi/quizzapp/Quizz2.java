package ma.emsi.quizzapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Quizz2 extends AppCompatActivity {

    private RadioGroup rg;
    private RadioButton rb;
    private Button bNext;
    private int score;
    private TextView quiz;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz2);
        rg = (RadioGroup) findViewById(R.id.rg);
        bNext = (Button) findViewById(R.id.bNext);
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        ref = FirebaseDatabase.getInstance().getReference().child("quiz");
        quiz =(TextView) findViewById(R.id.quiz);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String q =snapshot.child("q2").getValue().toString();
                quiz.setText(q);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rg.getCheckedRadioButtonId() == -1)
                    Toast.makeText(getApplicationContext(), "Please choose an answer !", Toast.LENGTH_SHORT).show();
                else {
                    rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
                    if (rb.getText().toString().equalsIgnoreCase("yes"))
                        score += 1;
                    Intent intent = new Intent(Quizz2.this, Quizz3.class);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slidleft, R.anim.slidright);
                    finish();
                }
            }
        });
    }
}