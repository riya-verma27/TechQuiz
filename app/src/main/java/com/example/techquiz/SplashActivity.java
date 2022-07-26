package com.example.techquiz;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private TextView appName;

    public static List<String> category_list=new ArrayList<>();
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        appName=findViewById(R.id.appName);
        Animation anim= AnimationUtils.loadAnimation(this,R.anim.myanim);
        appName.setAnimation(anim);

        firestore=FirebaseFirestore.getInstance();

        new Thread()
        {
            public void run()
            {
                loadData();
            }
        }.start();
    }
    private void loadData()
    {
        category_list.clear();
        firestore.collection("QUIZ").document("Categories")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc=task.getResult();
                    if(doc.exists())
                    {
                        long count=(long)doc.get("COUNT");
                        for(int i=1;i<=count;i++)
                        {
                            String catName=doc.getString("CAT"+String.valueOf(i));
                            category_list.add(catName);
                        }
                        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                        SplashActivity.this.finish();
                    }
                    else
                    {
                        Toast.makeText(SplashActivity.this,"No Category Document Exists!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(SplashActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}