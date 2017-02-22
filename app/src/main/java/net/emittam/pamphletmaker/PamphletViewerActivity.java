package net.emittam.pamphletmaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by kato-h on 16/11/26.
 */

public class PamphletViewerActivity extends AppCompatActivity {

    // X軸最低スワイプ距離
    private static final int SWIPE_MIN_DISTANCE = 50;

    // X軸最低スワイプスピード
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    // Y軸の移動距離　これ以上なら横移動を判定しない
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private GestureDetector mGestureDetector;

    private int mCurrentPage = 0;
    private String mKey;
    private String mText;

    public static void startActivity(Activity base, String key, String text) {
        Intent intent = new Intent(base, PamphletViewerActivity.class);
        intent.putExtra("key", key);
        intent.putExtra("text", text);
        base.startActivity(intent);
    }

    private static final int NOTIFICATION_ID = 998;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("パンフレット");

        setContentView(R.layout.activity_pamphlet_viewer);
        mKey = getIntent().getStringExtra("key");
        mText = getIntent().getStringExtra("text");

        LeftPamphletViewerFragment fragment = LeftPamphletViewerFragment.createInstance(mCurrentPage);
        LeftPamphletViewerModel model = ImageRepository.getInstance(getApplicationContext(), mKey).getLeftModel(mCurrentPage);
        model.setText(mText);
        fragment.setViewModel(model);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.pamphlet_fragment, fragment);
        transaction.commit();
        mGestureDetector = new GestureDetector(this, mOnGestureListener);

        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.cancel(NOTIFICATION_ID);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private final GestureDetector.SimpleOnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        // フリックイベント
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            Log.d("debug", "onFling");
            try {
                float distance_x = Math.abs((event1.getX() - event2.getX()));
                float velocity_x = Math.abs(velocityX);

                // Y軸の移動距離が大きすぎる場合
                if (Math.abs(event1.getY() - event2.getY()) > SWIPE_MAX_OFF_PATH) {

                }
                // 開始位置から終了位置の移動距離が指定値より大きい
                // X軸の移動速度が指定値より大きい
                else if  (event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //Toast.makeText(PamphletViewerActivity.this, "左から右", Toast.LENGTH_SHORT).show();
                    if (mCurrentPage >= 0) {
                        mCurrentPage--;
                        LeftPamphletViewerFragment fragment = LeftPamphletViewerFragment.createInstance(mCurrentPage);
                        LeftPamphletViewerModel model = ImageRepository.getInstance(getApplicationContext(), mKey).getLeftModel(mCurrentPage);
                        model.setText(mText);
                        fragment.setViewModel(model);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.pamphlet_fragment, fragment);
                        transaction.commit();
                    }

                }
                // 終了位置から開始位置の移動距離が指定値より大きい
                // X軸の移動速度が指定値より大きい
                else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //Toast.makeText(PamphletViewerActivity.this, "右から左", Toast.LENGTH_SHORT).show();
                    if (mCurrentPage < ImageRepository.getInstance(getApplicationContext(), mKey).getImageCount() / 2) {
                        mCurrentPage++;
                        LeftPamphletViewerFragment fragment = LeftPamphletViewerFragment.createInstance(mCurrentPage);
                        LeftPamphletViewerModel model = ImageRepository.getInstance(getApplicationContext(), mKey).getLeftModel(mCurrentPage);
                        model.setText(mText);
                        fragment.setViewModel(model);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.pamphlet_fragment, fragment);
                        transaction.commit();
                    }
                }

            } catch (Exception e) {
                // TODO
            }

            return false;
        }
    };
}
