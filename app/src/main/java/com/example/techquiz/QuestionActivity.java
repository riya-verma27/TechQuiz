package com.example.techquiz;

import static com.example.techquiz.SetsActivity.category_id;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView question,question_count,timer;
    private Button option1,option2,option3,option4;
    private List<Question> questionList;
    private int ques_num;
    private CountDownTimer count;
    private int score;
    private FirebaseFirestore firestore;
    private int setNo;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        question=findViewById(R.id.question);
        question_count=findViewById(R.id.question_number);
        timer=findViewById(R.id.timer);
        option1=findViewById(R.id.option1);
        option2=findViewById(R.id.option2);
        option3=findViewById(R.id.option3);
        option4=findViewById(R.id.option4);
        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        loadingDialog=new Dialog(QuestionActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_backgound);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        setNo=getIntent().getIntExtra("SETNO",1);
        firestore=FirebaseFirestore.getInstance();
        getQuestionList();

        score=0;
    }
    private void getQuestionList()
    {
        questionList=new ArrayList<>();
        firestore.collection("QUIZ").document("CAT"+String.valueOf(category_id))
                .collection("SET"+String.valueOf(setNo)).get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot questions = task.getResult();
                    for(QueryDocumentSnapshot doc:questions)
                    {
                        questionList.add(new Question(doc.getString("QUESTION"),
                                doc.getString("A"),
                                doc.getString("B"),
                                doc.getString("C"),
                                doc.getString("D"),
                                Integer.valueOf(doc.getString("ANSWER"))
                                ));
                    }
                    setQuestion();
                } else {
                    Toast.makeText(QuestionActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.cancel();
            }
        });
    }
    private void setQuestion()
    {
        timer.setText(String.valueOf(10));
        question.setText(questionList.get(0).getQuestion());
        option1.setText(questionList.get(0).getOptionA());
        option2.setText(questionList.get(0).getOptionB());
        option3.setText(questionList.get(0).getOptionC());
        option4.setText(questionList.get(0).getOptionD());
        question_count.setText(String.valueOf(1)+"/"+String.valueOf(questionList.size()));
        startTimer();
        ques_num=0;

    }
    private void startTimer()
    {
       count=new CountDownTimer(12000,1000) {
            @Override
            public void onTick(long l) {
                if(l<10000)
                timer.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                changeQuestion();
            }
        };
        count.start();
    }

    @Override
    public void onClick(View v) {
        int selected_option=0;
        switch(v.getId())
        {
            case R.id.option1:
                selected_option=1;
                break;
            case R.id.option2:
                selected_option=2;
                break;
            case R.id.option3:
                selected_option=3;
                break;
            case R.id.option4:
                selected_option=4;
                break;
            default:
        }
        //we need to stop the timer when user clicks one of the option
        count.cancel();
        checkAnswer(selected_option,v);
    }
    private void checkAnswer(int selected_option,View view)
    {
        if(selected_option==questionList.get(ques_num).getCorrectAns())
        {
            //if answer is right,the colour will change to green
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;
        }
        else {
            //wrong answer
            ((Button) view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            switch (questionList.get(ques_num).getCorrectAns()) {
                case 1:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 2:
                    option2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 3:
                    option3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 4:
                    option4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }
        }
        Handler handler=new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                changeQuestion();
            }
        },1500);
    }
    private void changeQuestion()
    {
        if(ques_num<questionList.size()-1)
        {
            ques_num++;
            playAnim(question,0,0);
            playAnim(option1,0,1);
            playAnim(option2,0,2);
            playAnim(option3,0,3);
            playAnim(option4,0,4);
            question_count.setText(String.valueOf(ques_num+1)+"/"+String.valueOf(questionList.size()));
            timer.setText(String.valueOf(10));
            startTimer();
        }
        else         //if it is the last question
        {
            //now we need to display the score
            //go to score activity
            Intent intent=new Intent(QuestionActivity.this,ScoreActivity.class);
            intent.putExtra("SCORE",String.valueOf(score)+"/"+String.valueOf(questionList.size()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //QuestionActivity.this.finish();
        }
    }
    private void playAnim(View view,final int value,int viewNum)
    {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).
                setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(value==0)
                {
                    switch (viewNum)
                    {
                        case 0:
                            ((TextView)view).setText(questionList.get(ques_num).getQuestion());
                            break;
                        case 1:
                            ((Button)view).setText(questionList.get(ques_num).getOptionA());
                            break;
                        case 2:
                            ((Button)view).setText(questionList.get(ques_num).getOptionB());
                            break;
                        case 3:
                            ((Button)view).setText(questionList.get(ques_num).getOptionC());
                            break;
                        case 4:
                            ((Button)view).setText(questionList.get(ques_num).getOptionD());
                            break;

                    }
                    if(viewNum!=0)
                        ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EF6C00")));
                    playAnim(view,1,viewNum);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        count.cancel();
    }
}