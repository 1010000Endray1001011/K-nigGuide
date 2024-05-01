package com.example.konigguide;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;

public class TranslatorFragment extends Fragment {
private Spinner spinner1,spinner2;
private TextInputEditText sourceText;
private ImageButton voiceButton;
private Button translateButton;
private TextView translatedText;
private static final int REQUEST_PERMISSION_CODE = 1;
String[] fromLanguage = {"Ввод","Английский","Немецкий","Французкий","Испанский","Русский","Арабский","Японский"};
String[] toLanguage = {"Вывод","Английский","Немецкий","Французкий","Испанский","Русский","Арабский","Японский"};
int languageCode,fromlanguageCode, tolanguageCode=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_translator, container, false);
        spinner1 = rootView.findViewById(R.id.Spin1);
        spinner2 = rootView.findViewById(R.id.Spin2);
        sourceText = rootView.findViewById(R.id.sourceText);
        voiceButton = rootView.findViewById(R.id.voiceButton);
        translateButton = rootView.findViewById(R.id.translateButton);
        translatedText = rootView.findViewById(R.id.TraslatedText);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromlanguageCode = getLanguageCode(fromLanguage[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter fromAdapter = new ArrayAdapter(getContext(),R.layout.spinner_item,fromLanguage);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(fromAdapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tolanguageCode = getLanguageCode(toLanguage[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter toAdapter = new ArrayAdapter(getContext(),R.layout.spinner_item,toLanguage);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(toAdapter);


        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translatedText.setText("");
                if(sourceText.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Введите текст для перевода",Toast.LENGTH_SHORT).show();
                }else if (fromlanguageCode==0){
                    Toast.makeText(getContext(),"Выберите язык ввода",Toast.LENGTH_SHORT).show();
                }else if (tolanguageCode==0){
                    Toast.makeText(getContext(),"Выберите язык вывода",Toast.LENGTH_SHORT).show();
                }else {
                    translateText(fromlanguageCode,tolanguageCode,sourceText.getText().toString());
                }
            }
        });

        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Говорите текст");
                try {
                    startActivityForResult(i,REQUEST_PERMISSION_CODE);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Ошибка: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        return (rootView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_PERMISSION_CODE){
            if (resultCode==-1&&data!=null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                sourceText.setText(result.get(0));
            }
        }

    }

    public void translateText(int fromlanguageCode, int tolanguageCode, String source){
        translatedText.setText("Скачивается языковой пакет...");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromlanguageCode)
                .setTargetLanguage(tolanguageCode)
                .build();

        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                translatedText.setText("Переводим текст");
                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        translatedText.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Ошибка перевода: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Инициализация языкового пакета: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getLanguageCode(String language) {
        int languageCode = 0;
        switch (language){
            case "Английский":
                languageCode = FirebaseTranslateLanguage.EN;
                break;
            case "Немецкий":
                languageCode = FirebaseTranslateLanguage.DE;
                break;
            case "Французкий":
                languageCode = FirebaseTranslateLanguage.FR;
                break;
            case "Испанский":
                languageCode = FirebaseTranslateLanguage.ES;
                break;
            case "Русский":
                languageCode = FirebaseTranslateLanguage.RU;
                break;
            case "Арабский":
                languageCode = FirebaseTranslateLanguage.AR;
                break;
            case "Японский":
                languageCode = FirebaseTranslateLanguage.JA;
                break;
            default:languageCode = 0;
        }
        return languageCode;
    }

}