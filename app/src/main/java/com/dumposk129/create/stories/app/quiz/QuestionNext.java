package com.dumposk129.create.stories.app.quiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dumposk129.create.stories.app.R;
import com.dumposk129.create.stories.app.api.ApiConfig;
import com.dumposk129.create.stories.app.api.Quiz;
import com.dumposk129.create.stories.app.model.Choice;
import com.dumposk129.create.stories.app.navigation_drawer.NavigationDrawerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DumpOSK129.
 */
public class QuestionNext extends ActionBarActivity {

    private Toolbar mToolbar;
    private EditText txtQuestionNext_question, txtQuestionNext_answer1, txtQuestionNext_answer2, txtQuestionNext_answer3,
            txtQuestionNext_answer4;
    private Button btnQuestionNext_next;
    private RadioGroup rgQuestionNext;
    private RadioButton rbQuestionNext_answer1, rbQuestionNext_answer2, rbQuestionNext_answer3, rbQuestionNext_answer4;
    private String quizId;
    private int currentIndex;
    private int noOfQuestion;
    private int correctAnswer = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_form);

        //casting
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        txtQuestionNext_question = (EditText) findViewById(R.id.txtQuestionNext_question);
        txtQuestionNext_answer1 = (EditText) findViewById(R.id.txtQuestionNext_answer1);
        txtQuestionNext_answer2 = (EditText) findViewById(R.id.txtQuestionNext_answer2);
        txtQuestionNext_answer3 = (EditText) findViewById(R.id.txtQuestionNext_answer3);
        txtQuestionNext_answer4 = (EditText) findViewById(R.id.txtQuestionNext_answer4);
        rgQuestionNext = (RadioGroup) findViewById(R.id.rgQuestionNextForm);
        rbQuestionNext_answer1 = (RadioButton) findViewById(R.id.rbQuestionNext_answer1);
        rbQuestionNext_answer2 = (RadioButton) findViewById(R.id.rbQuestionNext_answer2);
        rbQuestionNext_answer3 = (RadioButton) findViewById(R.id.rbQuestionNext_answer3);
        rbQuestionNext_answer4 = (RadioButton) findViewById(R.id.rbQuestionNext_answer4);
        btnQuestionNext_next = (Button) findViewById(R.id.btnQuestionNext);

        // Navigation Drawer
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);


        // Get Data
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        noOfQuestion = bundle.getInt("NumOfQuestion");
        quizId = bundle.getString("QuizID");
        currentIndex = bundle.getInt("index") + 1;

        if (noOfQuestion == currentIndex) {
            btnQuestionNext_next.setText("Finished");
        }

        btnQuestionNext_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SET Correct Answer
                int selectedID = rgQuestionNext.getCheckedRadioButtonId();

                if(selectedID == rbQuestionNext_answer1.getId()){
                    correctAnswer = 1;
                }else if(selectedID == rbQuestionNext_answer2.getId()){
                    correctAnswer = 2;
                }else if(selectedID == rbQuestionNext_answer3.getId()){
                    correctAnswer = 3;
                }else if(selectedID == rbQuestionNext_answer4.getId()){
                    correctAnswer = 4;
                }

                // Call AsyncTask
                new SaveQuestionTask().execute();
                //Toast.makeText(getApplicationContext(),"Click",Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void onQuestionNextClickListener() {
        //Fill Question amd Answer
        String quesNext_question = txtQuestionNext_question.getText().toString();
        String[] quesNext_answer = new String[4];
        quesNext_answer[0] = txtQuestionNext_answer1.getText().toString();
        quesNext_answer[1] = txtQuestionNext_answer2.getText().toString();
        quesNext_answer[2] = txtQuestionNext_answer3.getText().toString();
        quesNext_answer[3] = txtQuestionNext_answer4.getText().toString();

        // Save Question to DB
        int questionID = Quiz.saveQuestion(quesNext_question, quizId); //get question from db

        // Set up choices data, it should be ready for saving to db.
        List<Choice> choices = new ArrayList<>(4);
        for (int s = 1; s <= 4; s++) {
            Choice choice = new Choice();
            choice.setChoiceName(quesNext_answer[s - 1]);
            if (correctAnswer == s) {
                choice.setIsCorrect(ApiConfig._TRUE);
            } else {
                choice.setIsCorrect(ApiConfig._FALSE);
            }
            choice.setChoiceId(s);

            choices.add(choice);
        }

        // Save List of choices to DB
        boolean isSuccess = Quiz.saveChoices(choices, questionID);
        if (isSuccess) {
            if (currentIndex == noOfQuestion) {
                // Go to Final Question
                Intent intent = new Intent(QuestionNext.this, Quizzes.class);
                startActivity(intent);
            } else {
                // Create Next Question
                Intent intent = new Intent(QuestionNext.this, QuestionNext.class);
                intent.putExtra("NumOfQuestion", noOfQuestion);
                intent.putExtra("QuizID", quizId);
                intent.putExtra("index", currentIndex);
                startActivity(intent);
            }
        } else {
            // Show Warning
        }
    }

    private class SaveQuestionTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(String... params) {
            onQuestionNextClickListener();
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            Toast.makeText(getApplicationContext(), "Save Question Already", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            Toast.makeText(getApplicationContext(), "Saving Question", Toast.LENGTH_LONG).show();
        }
    }
}
