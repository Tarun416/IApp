package com.example.iapp.customViews;

import android.content.Context;

import android.util.AttributeSet;


import com.github.siyamed.shapeimageview.ShaderImageView;
import com.github.siyamed.shapeimageview.shader.CircleShader;
import com.github.siyamed.shapeimageview.shader.ShaderHelper;


/**
 * Created by rahul on 31/03/17.
 */

public class CircleImageView extends ShaderImageView {

    private CircleShader shader;

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        shader = new CircleShader();
        return shader;
    }

    public float getBorderRadius() {
        if(shader != null) {
            return shader.getBorderRadius();
        }
        return 0;
    }

    public void setBorderRadius(final float borderRadius) {
        if(shader != null) {
            shader.setBorderRadius(borderRadius);
            invalidate();
        }
    }
}