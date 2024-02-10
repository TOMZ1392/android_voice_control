package com.example.voicecontrol;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech myTTS;
    private SpeechRecognizer mySPR;
    private TextView txtViewTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Listening..", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                mySPR.startListening(intent);


            }
        });
        initializeTTS();
        initializeSPR();
        txtViewTxt=findViewById(R.id.textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private  void initializeTTS(){
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(MainActivity.this,"There is no TTS engine available",Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    myTTS.setLanguage(Locale.US);
                    speakResponse("Hello : Im ready");
                }
            }
        });
    }
    private void speakResponse(String message){
        if(Build.VERSION.SDK_INT>=21) {
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else{
            myTTS.speak(message,TextToSpeech.QUEUE_FLUSH,null);
        }

    }

    private void initializeSPR(){
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            mySPR = SpeechRecognizer.createSpeechRecognizer(this);
            mySPR.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {

                    List<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                    processSPResult(results.get(0));

                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        }
    }

    private void processSPResult(String command) {
        command= command.toLowerCase();

        if(command.indexOf("what") != -1){
            if(command.indexOf("your name") != -1){
                speakResponse("Hello Master: My name is andro the great");
            }
            else if(command.indexOf("time now") != -1){
                Date now = new Date();
                String time= DateUtils.formatDateTime(this,now.getTime(),DateUtils.FORMAT_SHOW_TIME);
                speakResponse("The time now is hmm  " + time);
            }
            else if(command.indexOf("responsibility") != -1){

                speakResponse("My sole responsibility is to serve my master  " );
            }

        }
       else if(command.indexOf("so special") != -1){
            speakResponse("Thank you Master");
        }
        txtViewTxt.setText(command);
    }

    @Override
    protected void onPause(){
        super.onPause();
        myTTS.shutdown();
    }
}
