package com.nativex.monetization.mraid;

import android.graphics.Rect;
import android.widget.ImageView;

class AdPosition {
    private Rect customPosition;
    private Rect forcedPosition;

    public AdPosition(Rect position) {
        this.customPosition = position;
    }

    public void setCustomPosition(Rect position) {
        this.customPosition = position;
    }

    public void setForcedPosition(Rect position) {
        this.forcedPosition = position;
    }

    public Rect getPosition(int orientation) {
        if (this.forcedPosition != null) {
            return this.forcedPosition;
        }
        return this.customPosition;
    }

    public void validateCustomPosition(ImageView closeRegion, Rect parentRect) {
        if (this.customPosition != null) {
            Rect rect;
            if (this.customPosition.width() < closeRegion.getMeasuredWidth()) {
                this.customPosition.right = this.customPosition.left + closeRegion.getMeasuredWidth();
            }
            if (this.customPosition.height() < closeRegion.getMeasuredHeight()) {
                this.customPosition.bottom = this.customPosition.top + closeRegion.getMeasuredHeight();
            }
            if (this.customPosition.right > parentRect.right) {
                rect = this.customPosition;
                rect.left -= this.customPosition.right - parentRect.right;
                this.customPosition.right = parentRect.right;
            }
            if (this.customPosition.bottom > parentRect.bottom) {
                rect = this.customPosition;
                rect.top -= this.customPosition.bottom - parentRect.bottom;
                this.customPosition.bottom = parentRect.bottom;
            }
            if (this.customPosition.left < parentRect.left) {
                this.customPosition.left = parentRect.left;
            }
            if (this.customPosition.top < parentRect.top) {
                this.customPosition.top = parentRect.top;
            }
        }
    }
}
