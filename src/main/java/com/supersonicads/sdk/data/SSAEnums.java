package com.supersonicads.sdk.data;

public class SSAEnums {

    public enum BackButtonState {
        None,
        Device,
        Controller
    }

    public enum ControllerState {
        None,
        FailedToDownload,
        FailedToLoad,
        Loaded,
        Ready,
        Failed
    }

    public enum DebugMode {
        MODE_0(0),
        MODE_1(1),
        MODE_2(2),
        MODE_3(3);
        
        private int value;

        private DebugMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum ProductType {
        BrandConnect,
        OfferWall,
        Interstitial,
        OfferWallCredits
    }
}
