package jp.co.se.android.recipe.chapter02;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class Ch0236Preference extends Preference implements
        OnRatingBarChangeListener {

    private float mCurrentRating;
    private float mOldRating;

    public Ch0236Preference(Context context, AttributeSet attrs) {
        super(context, attrs);
        // カスタマイズレイアウトを設定
        setWidgetLayoutResource(R.layout.ch0236_preference);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        // 初めてPreferenceが呼ばれた際に設定する初期値を返却
        return a.getFloat(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue,
            Object defaultValue) {
        // Preferencenの初期値を設定
        if (restorePersistedValue) {
            // restorePersistedValueがtrueの場合、SharedPreferenceから値を取得
            mCurrentRating = getPersistedFloat(mCurrentRating);
        } else {
            // restorePersistedValueがfalseの場合、Preferenceにデフォルト値を設定
            mCurrentRating = (Float) defaultValue;
            persistFloat(mCurrentRating);
        }
        mOldRating = mCurrentRating;
    }

    @Override
    protected void onBindView(View view) {
        // PreferenceとカスタマイズされたViewをBind
        final RatingBar rating = (RatingBar) view
                .findViewById(R.id.ratingPreference);
        if (rating != null) {
            rating.setRating(mCurrentRating);
            rating.setOnRatingBarChangeListener(this);
        }
        super.onBindView(view);
    }

    @Override
    public void onRatingChanged(RatingBar rating, float value, boolean arg2) {
        // ユーザーにより設定変更があった時に呼ばれる
        mCurrentRating = (callChangeListener(value)) ? value : mOldRating;
        persistFloat(mCurrentRating);
        mOldRating = mCurrentRating;
    }

}
