package com.example.leonardocavalera.myquizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import static com.example.leonardocavalera.myquizapp.R.id.layout_button;

public class Questions extends AppCompatActivity implements View.OnClickListener {

    private int quizCounter;
    private double rightAnswerCounter;
    private double wrongAnswerCounter;
    private double score;
    LinearLayout buttonView;
    LinearLayout editTextView;
    String writtenAnswer;
    boolean[] answersGiven;
    TextView titleTextView;
    TextView questionTextView;
    EditText editSpace;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button editButton;
    ArrayList<Quiz> quizArray = new ArrayList<>();
    Quiz currentQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        buttonView = (LinearLayout) findViewById(layout_button);
        buttonView.setVisibility(View.GONE);

        editTextView = (LinearLayout)findViewById(R.id.edit_text_view);
        editTextView.setVisibility(View.GONE);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);

        editSpace = (EditText)findViewById(R.id.edit_text);
        editButton = (Button)findViewById(R.id.button_x);

        titleTextView = (TextView) findViewById(R.id.title);
        questionTextView = (TextView) findViewById(R.id.question);

        quizCounter = 0;
        rightAnswerCounter = 0;
        answersGiven = new boolean[]{false, false, false, false};

        try {
            createQuestions();
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentQuiz = quizArray.get(quizCounter);
        score = 100;

        setView();

    }

    public void setView() {
        int quizN = quizCounter + 1;
        titleTextView.setText("Question #" + quizN);
        questionTextView.setText(currentQuiz.getQuestionText());

        switch (currentQuiz.getQuestionType()) {
            case "inputText":
                titleTextView.setVisibility(View.VISIBLE);
                buttonView.setVisibility(View.GONE);
                editTextView.setVisibility(View.VISIBLE);
                break;
            default:
                titleTextView.setVisibility(View.VISIBLE);
                buttonView.setVisibility(View.VISIBLE);
                editTextView.setVisibility(View.GONE);
                button1.setText(currentQuiz.getAnswersText()[0]);
                button2.setText(currentQuiz.getAnswersText()[1]);
                button3.setText(currentQuiz.getAnswersText()[2]);
                button4.setText(currentQuiz.getAnswersText()[3]);
                break;

        }
        answersGiven = new boolean[]{false, false, false, false};
        if (quizCounter==quizArray.size()){
            buttonView.setVisibility(View.GONE);
            editSpace.setVisibility(View.GONE);
            titleTextView.setVisibility(View.GONE);
//            titleTextView.setText("Your Score is");
            score = ( rightAnswerCounter / ( rightAnswerCounter + wrongAnswerCounter) )* 100;
            questionTextView.setText(String.format("Your final score is: %.2f", score));
            editTextView.setVisibility(View.VISIBLE);
            editSpace.setVisibility(View.GONE);
            editButton.setText("Restart");
        }
//        if(quizCounter>quizArray.size()){
//            buttonView.setVisibility(View.GONE);
//            editTextView.setVisibility(View.GONE);
//            titleTextView.setVisibility(View.GONE);
//            questionTextView.setVisibility(View.GONE);
//
//        }

    }

    public void createQuestions() throws IOException {

        String strContent;
        InputStream is = getResources().openRawResource(R.raw.quiz);
        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }

        strContent = builder.toString();

        try {
            JSONObject questions = new JSONObject(strContent);
            JSONArray questionsArray = questions.getJSONArray("quizContainer");
            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject currentQuestion = questionsArray.optJSONObject(i);
                JSONObject currentQuiz = currentQuestion.getJSONObject("quiz");
                String questionText = currentQuiz.getString("question");
                JSONArray answerText = currentQuiz.getJSONArray("answers");
                String[] answerArray = new String[answerText.length()];
                if (answerText == null) {
                    answerArray = null;
                } else {
                    for (int n = 0; n < answerArray.length; n++) {
                        answerArray[n] = answerText.optString(n);
                    }
                }
                JSONArray correctAnswers = currentQuiz.optJSONArray("correctAnswers");
                String type = currentQuiz.getString("type");
                boolean[] correctionArrayBoolean = new boolean[correctAnswers.length()];
                String[] correctionArrayString = new String[correctAnswers.length()];
                for (int j = 0; j < correctAnswers.length(); j++) {
                    switch (type) {             //Extraction of the answers set for each type
                        case "inputText":
                            correctionArrayString[j] = correctAnswers.getString(j);
                            break;
                        default:
                            if (correctAnswers.optInt(j) == 1) {
                                correctionArrayBoolean[j] = true;
                            }else{correctionArrayBoolean[j] = false;}
                            break;
                    }
                }
                Quiz quiz;
                switch (type){
                    case "inputText":
                        quiz = new Quiz(questionText, answerArray, correctionArrayString, type);
                        break;
                    default:
                        quiz = new Quiz(questionText, answerArray, correctionArrayBoolean, type);
                        break;
                }

                quizArray.add(i, quiz);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                answersGiven[0] = true;
                break;
            case R.id.button2:
                answersGiven[1] = true;
                break;
            case R.id.button3:
                answersGiven[2] = true;
                break;
            case R.id.button4:
                answersGiven[3] = true;
                break;
            case R.id.button_x:
                if (quizCounter>=quizArray.size()){
                    questionTextView.setVisibility(View.GONE);
                    Intent intent = new Intent(Questions.this, MainActivity.class);
                    startActivity(intent);
                    break;
                }
                writtenAnswer = editSpace.getText().toString();
                break;
        }
            if(currentQuiz.answerChecker(answersGiven, writtenAnswer)){
                Toast toastRightAnswer = Toast.makeText(getApplicationContext(), "Right!", Toast.LENGTH_SHORT);
                toastRightAnswer.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 400);
                toastRightAnswer.show();
                quizCounter++;
                rightAnswerCounter++;
                if (quizCounter<quizArray.size()) {
                    currentQuiz = quizArray.get(quizCounter);
                }
                setView();
            }else{
                Toast toastRightAnswer = Toast.makeText(getApplicationContext(), "Try Again!", Toast.LENGTH_SHORT);
                toastRightAnswer.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 400);
                toastRightAnswer.show();
                wrongAnswerCounter++;
                if (currentQuiz.getQuestionType() == "inputText"){
                    quizCounter++;
                    currentQuiz = quizArray.get(quizCounter);
                }
                setView();
            }

    }
}

