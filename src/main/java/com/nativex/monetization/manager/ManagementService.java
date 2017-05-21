package com.nativex.monetization.manager;

import com.nativex.common.DeviceManager;

public class ManagementService {
    private static ManagementService instance;
    private DialogManager dialogManager = null;
    private MonetizationSharedDataManager sharedDataManager = null;

    private ManagementService() {
    }

    public DialogManager getDialogManager() {
        return this.dialogManager;
    }

    public void setDialogManager(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
    }

    public MonetizationSharedDataManager getMonetizationSharedDataManager() {
        return this.sharedDataManager;
    }

    public void setMonetizationSharedDataManager(MonetizationSharedDataManager sharedDataManager) {
        this.sharedDataManager = sharedDataManager;
    }

    public static synchronized ManagementService getInstance() {
        ManagementService managementService;
        synchronized (ManagementService.class) {
            if (instance == null) {
                instance = new ManagementService();
            }
            managementService = instance;
        }
        return managementService;
    }

    public static void release() {
        DialogManager.release();
        DeviceManager.release();
        instance = null;
    }
}
