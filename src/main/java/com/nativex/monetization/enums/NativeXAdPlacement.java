package com.nativex.monetization.enums;

public enum NativeXAdPlacement {
    Exit_Ad_From_Application("Exit Ad from Application"),
    Game_Launch("Game Launch"),
    Main_Menu_Screen("Main Menu Screen"),
    Level_Completed("Level Completed"),
    Level_Failed("Level Failed"),
    Pause_Menu_Screen("Pause Menu Screen"),
    Player_Levels_Up("Player Levels Up"),
    Player_Generated_Event("Player Generated Event"),
    P2P_Competition_Won("P2P competition won"),
    P2P_Competition_Lost("P2P competition lost"),
    Store_Open("Store Open");
    
    private final String name;

    private NativeXAdPlacement(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
