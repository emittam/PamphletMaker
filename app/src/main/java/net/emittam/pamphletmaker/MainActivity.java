package net.emittam.pamphletmaker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import net.emittam.pamphletmaker.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    public class MainPresenter extends BaseObservable {
        public void onButtonClick() {
//            SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//            SharedPreferences.Editor editor = defaultPref.edit();
//            editor.putString(ImageCollectBroadcastReceiver.SAVE_FILE_PATH_KEY, "test2");
//            editor.commit();
//            Toast.makeText(MainActivity.this, "好きなカメラアプリで写真を撮ってください", Toast.LENGTH_LONG).show();
//            finish();
            SelectorActivity.startActivity(MainActivity.this, false);
        }

        public void onTestButtonClick() {
            SelectorActivity.startActivity(MainActivity.this, true);
        }
    }

    private MainPresenter mMainPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("パンフレットメーカー");
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMainPresenter = new MainPresenter();
        binding.setPresenter(mMainPresenter);
    }
}
