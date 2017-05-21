package com.nativex.monetization.theme;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import com.nativex.monetization.manager.ImageService;
import com.nativex.monetization.manager.MonetizationSharedDataManager;

public class ThemeFromAssets extends Theme {
    private final SparseArray<String> encodedStrings = new SparseArray();
    private ImageService imageService;

    ThemeFromAssets() {
    }

    public Drawable getDrawable(ThemeElementTypes type) {
        Drawable background = super.getDrawable(type);
        if (background != null) {
            return background;
        }
        String encodedString = (String) this.encodedStrings.get(type.getKey());
        if (encodedString == null || MonetizationSharedDataManager.getContext() == null) {
            return background;
        }
        if (this.imageService == null) {
            this.imageService = new ImageService();
        }
        background = this.imageService.loadDrawableFromBase64String(MonetizationSharedDataManager.getContext(), encodedString);
        setDrawable(type, background);
        return background;
    }

    void addEncodedString(ThemeElementTypes type, String filename) {
        this.encodedStrings.put(type.getKey(), filename);
    }

    public void release() {
        super.release();
        this.encodedStrings.clear();
    }
}
