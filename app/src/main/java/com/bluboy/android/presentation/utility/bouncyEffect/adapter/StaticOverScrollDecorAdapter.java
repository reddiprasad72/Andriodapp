package com.bluboy.android.presentation.utility.bouncyEffect.adapter;

import android.view.View;

import com.bluboy.android.presentation.utility.bouncyEffect.HorizontalOverScrollBounceEffectDecorator;
import com.bluboy.android.presentation.utility.bouncyEffect.VerticalOverScrollBounceEffectDecorator;


/**
 * A static adapter for views that are ALWAYS over-scroll-able (e.g. image view).
 *
 * @author amit
 *
 * @see HorizontalOverScrollBounceEffectDecorator
 * @see VerticalOverScrollBounceEffectDecorator
 */
public class StaticOverScrollDecorAdapter implements IOverScrollDecoratorAdapter {

    protected final View mView;

    public StaticOverScrollDecorAdapter(View view) {
        mView = view;
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public boolean isInAbsoluteStart() {
        return true;
    }

    @Override
    public boolean isInAbsoluteEnd() {
        return true;
    }
}