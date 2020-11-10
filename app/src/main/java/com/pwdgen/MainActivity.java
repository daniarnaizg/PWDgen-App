package com.pwdgen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    private FrameLayout adContainerView;
    private AdView adView;

    Typeface tp;
    Typeface tp_it;
    TextView title;

    ToggleButton lettersButton;
    Boolean letterState;

    ToggleButton lettersCapsButton;
    Boolean letterCapState;

    ToggleButton numbersButton;
    Boolean numberState;

    ToggleButton symbolsButton;
    Boolean symbolState;

    TextView tvSlider_1;
    Integer long_pwd;

    TextView tvSlider_2;
    Integer num_pwd;

    LinearLayout pwd_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        adContainerView = findViewById(R.id.ad_view_container);
        // Step 1 - Create an AdView and set the ad unit ID on it.
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.main_banner_ad_unit_id));
        adContainerView.addView(adView);
        loadBanner();

        long_pwd = 6;
        num_pwd = 4;
        letterState = true;
        letterCapState = true;
        numberState = true;
        symbolState = false;

        tp_it = Typeface.createFromAsset(getAssets(), "fonts/RobotoMono-BoldItalic.ttf");
        title = findViewById(R.id.app_title);
        title.setTypeface(tp_it);

        pwd_view = findViewById(R.id.pwd_view);

        lettersButton = findViewById(R.id.letter);
        lettersButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    letterState = Boolean.TRUE;
                    createPasswords(long_pwd, num_pwd);
                } else {
                    letterState = Boolean.FALSE;
                    createPasswords(long_pwd, num_pwd);
                }
            }
        });

        lettersCapsButton = findViewById(R.id.letter_caps);
        lettersCapsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    letterCapState = Boolean.TRUE;
                    createPasswords(long_pwd, num_pwd);
                } else {
                    letterCapState = Boolean.FALSE;
                    createPasswords(long_pwd, num_pwd);
                }
            }
        });

        numbersButton = findViewById(R.id.numbers);
        numbersButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    numberState = Boolean.TRUE;
                    createPasswords(long_pwd, num_pwd);
                } else {
                    numberState = Boolean.FALSE;
                    createPasswords(long_pwd, num_pwd);
                }
            }
        });

        symbolsButton = findViewById(R.id.symbols);
        symbolsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    symbolState = Boolean.TRUE;
                    createPasswords(long_pwd, num_pwd);
                } else {
                    symbolState = Boolean.FALSE;
                    createPasswords(long_pwd, num_pwd);
                }
            }
        });

        SeekBar seekbar_1 = findViewById(R.id.seekBar_1);
        seekbar_1.setOnSeekBarChangeListener(seekBarChangeListener_1);

        int progress_1 = seekbar_1.getProgress();
        tvSlider_1 = findViewById(R.id.tvSeekbar_1);
        tvSlider_1.setText(Integer.toString(progress_1));

        SeekBar seekbar_2 = findViewById(R.id.seekBar_2);
        seekbar_2.setOnSeekBarChangeListener(seekBarChangeListener_2);

        int progress_2 = seekbar_1.getProgress();
        tvSlider_2 = findViewById(R.id.tvSeekbar_2);
        tvSlider_2.setText(Integer.toString(progress_2));

        tp = Typeface.createFromAsset(getAssets(), "fonts/RobotoMono-Bold.ttf");
        createPasswords(6, 5);

    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener_1 = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            long_pwd = progress;
            tvSlider_1.setText(Integer.toString(progress));
            createPasswords(long_pwd, num_pwd);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    SeekBar.OnSeekBarChangeListener seekBarChangeListener_2 = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            num_pwd = progress;
            tvSlider_2.setText(Integer.toString(progress));
            createPasswords(long_pwd, num_pwd);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    boolean isTestDevice() {
        return Boolean.parseBoolean(Settings.System.getString(getContentResolver(), "firebase.test.lab"));
    }

    private void loadBanner() {
        // Create an ad request. Check your logcat output for the hashed device ID
        // to get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
        // device."
//        AdRequest adRequest =
//                new AdRequest.Builder().addTestDevice("16B22153574ACFD206C0814C140D0FD0")
//                        .build();
        AdRequest adRequest =
                new AdRequest.Builder().build();

        AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);

        // Step 5 - Start loading the ad in the background.
//        if(!isTestDevice())
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    private void createPasswords(Integer long_pwd, Integer num_pwd) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 35);

        pwd_view.removeAllViews();

        final TextView[] myTextViews = new TextView[num_pwd];
//        final Button[] myButtons = new Button[num_pwd];

        for (int i = 0; i < num_pwd; i++) {

            final TextView tv = new TextView(this);
//            Button butt = new Button(this);

            String pwdString = genPwdString(long_pwd);
            tv.setText(pwdString);
            tv.setId(i);
            tv.setTypeface(tp);
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
            tv.setTextSize(18);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    copyToCB();

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("pwd", tv.getText().toString());
                    clipboard.setPrimaryClip(clip);

                    tv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));

                    Toast toast = Toast.makeText(getApplicationContext(), "Password copied!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

//            pwd_view.addView(butt);
            pwd_view.addView(tv, layoutParams);

//            myButtons[i] = butt;
            myTextViews[i] = tv;

        }
    }

    private String genPwdString(Integer long_pwd) {

        String abc = "";

        if (letterState)
            abc = abc.concat("abcdefghijklmnopqrstuvxyz");

        if (letterCapState)
            abc = abc.concat("ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        if (numberState)
            abc = abc.concat("0123456789");

        if (symbolState)
            abc = abc.concat("!\\#$%&()*+-/<=>?@[]^_{|}~");

        if (abc == "") {
            abc = " ";
        }

        StringBuilder sb = new StringBuilder(long_pwd);

        for (int i = 0; i < long_pwd; i++) {
            int index = (int) (abc.length() * Math.random());
            sb.append(abc.charAt(index));
        }
        return sb.toString();
    }

    public void reloadPasswords(View view) {
        createPasswords(long_pwd, num_pwd);
    }


}