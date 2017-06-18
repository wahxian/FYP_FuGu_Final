package com.minimalwaste.fypv2_fragments;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

/**
 * Created by Wah Xian on 17/06/2017.
 */

public class AdapterPersonalRecipe extends ArrayAdapter<String>{
    public AdapterPersonalRecipe(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }
}

