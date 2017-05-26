package com.bichan.shop;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by cuong on 5/23/2017.
 */

public class RxHelper {
    public static Observable<String> getTextWatcherObservable(@NonNull final EditText editText) {

        final PublishSubject<String> subject = PublishSubject.create();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                subject.onNext(s.toString());
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    subject.onNext(editText.getText().toString());
                }
            }
        });

        return subject;
    }

    public static Observable<Float> getTextWatcherObservable(@NonNull final RatingBar ratingBar) {

        final PublishSubject<Float> subject = PublishSubject.create();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                subject.onNext(rating);
            }
        });
        return subject;
    }

}
