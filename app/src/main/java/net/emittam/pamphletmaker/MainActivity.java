package net.emittam.pamphletmaker;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.emittam.pamphletmaker.databinding.ActivityMainBinding;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public class MainPresenter extends BaseObservable {
        public void onButtonClick() {
            SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = defaultPref.edit();
            editor.putString(ImageCollectBroadcastReceiver.SAVE_FILE_PATH_KEY, "test");
            editor.commit();
        }

        public void onTestButtonClick() {
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("test", Context.MODE_PRIVATE);
            Set<String> savedSet = prefs.getStringSet(ImageCollectBroadcastReceiver.SAVE_IMAGE_URL_KEY, new HashSet<String>());
        }
    }

    private MainPresenter mMainPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMainPresenter = new MainPresenter();
        binding.setPresenter(mMainPresenter);
    }
}
