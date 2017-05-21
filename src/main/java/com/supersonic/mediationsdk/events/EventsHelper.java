package com.supersonic.mediationsdk.events;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.supersonic.mediationsdk.sdk.GeneralProperties;
import com.supersonic.mediationsdk.utils.GeneralPropertiesWorker;
import com.supersonic.mediationsdk.utils.SupersonicConstants;
import com.supersonic.mediationsdk.utils.SupersonicUtils;
import java.util.ArrayList;

public class EventsHelper {
    private static EventsHelper mInstance;
    private final int AVAILABILITY_CHANGED = 7;
    private final int DEFAULT_BACKUP_THRESHOLD = 1;
    private final String DEFAULT_EVENTS_URL = "https://outcome.supersonicads.com/mediation/";
    private final int DEFAULT_MAX_NUMBER_OF_EVENTS = 100;
    private final int GET_INSTANCE_CODE = 14;
    private final int INIT_INTERSTITISL_CODE = 16;
    private final int INIT_OFFERWALL_CODE = 15;
    private final int INIT_REWARDED_VIDEO_CODE = 1;
    private final int INIT_REWARDED_VIDEO_RESULT = 4;
    private final int IS_REWARDED_VIDEO_AVAILABLE_CODE = 3;
    private final int PAUSE_CODE = 13;
    private final int PUBLISHER_CHECK_AVAILABILITY = 18;
    private final int RELEASE_CODE = 11;
    private final int RESUME_CODE = 12;
    private final int REWARDED_VIDEO_AD_CLOSED = 6;
    private final int REWARDED_VIDEO_AD_OPENED = 5;
    private final int SHOW_REWARDED_VIDEO_CODE = 2;
    private final int SHOW_REWARDED_VIDEO_RESULT = 17;
    private final int VIDEO_AD_REWARDED = 10;
    private final int VIDEO_END = 9;
    private final int VIDEO_START = 8;
    private int mBackupThreshold = 1;
    private String mCurrentPlacement;
    private SupersonicDbHelper mDbHelper;
    private ArrayList<Event> mEvents;
    private boolean mIsEventsEnabled = true;
    private int mMaxNumberOfEvents = 100;
    private String mServerUrl;
    private int mSessionDepth;
    private int mTotalEvents;
    private SharedPreferences sharedPref;

    private EventsHelper(Context context) {
        initState();
        this.mDbHelper = new SupersonicDbHelper(context);
    }

    private EventsHelper() {
        initState();
    }

    private void initState() {
        this.mSessionDepth = 1;
        this.mEvents = new ArrayList();
        this.mTotalEvents = 0;
        this.mCurrentPlacement = "";
    }

    public static synchronized EventsHelper start(Context context) {
        EventsHelper eventsHelper;
        synchronized (EventsHelper.class) {
            if (mInstance == null) {
                mInstance = new EventsHelper(context);
            }
            eventsHelper = mInstance;
            mInstance.getClass();
            eventsHelper.mServerUrl = SupersonicUtils.getDefaultEventsURL(context, "https://outcome.supersonicads.com/mediation/");
            if (mInstance.mDbHelper == null) {
                mInstance.mDbHelper = new SupersonicDbHelper(context);
                SuperLooper.getLooper().post(new SendStoredEventsRunnable(mInstance.mDbHelper, mInstance.mServerUrl, SupersonicUtils.getGeneralProperties(context), new ArrayList()));
                SuperLooper.getLooper().post(new GeneralPropertiesWorker(context));
            }
            eventsHelper = mInstance;
        }
        return eventsHelper;
    }

    public static synchronized EventsHelper getInstance() {
        EventsHelper eventsHelper;
        synchronized (EventsHelper.class) {
            if (mInstance == null) {
                mInstance = new EventsHelper();
            }
            eventsHelper = mInstance;
        }
        return eventsHelper;
    }

    public void setBackupThreshold(int backupThreshold) {
        if (backupThreshold > 0) {
            this.mBackupThreshold = backupThreshold;
        }
    }

    public void setMaxNumberOfEvents(int maxNumberOfEvents) {
        if (maxNumberOfEvents > 0) {
            this.mMaxNumberOfEvents = maxNumberOfEvents;
        }
    }

    public void setEventsUrl(String eventsUrl, Context context) {
        if (!TextUtils.isEmpty(eventsUrl)) {
            this.mServerUrl = eventsUrl;
            SupersonicUtils.saveDefaultEventsURL(context, eventsUrl);
        }
    }

    public void setIsEventsEnabled(boolean isEnabled) {
        this.mIsEventsEnabled = isEnabled;
    }

    public synchronized void notifyInitRewardedVideoEvent(String provider) {
        addEvent(new Event(1, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth));
    }

    public synchronized void notifyShowRewardedVideoEvent(String provider, String placement) {
        setCurrentPlacement(placement);
        addEvent(new Event(2, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth));
    }

    public synchronized void notifyIsRewardedVideoAvailableEvent(String provider, boolean availability) {
        Event event = new Event(3, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth);
        event.setStatus(availability);
        addEvent(event);
    }

    public synchronized void notifyInitRewardedVideoResultEvent(String provider, boolean succeed) {
        Event event = new Event(4, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth);
        event.setStatus(succeed);
        addEvent(event);
    }

    public synchronized void notifyShowRewardedVideoResultEvent(String provider, boolean succeed) {
        Event event = new Event(17, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth);
        event.setStatus(succeed);
        addEvent(event);
    }

    public synchronized void notifyVideoAdOpenedEvent(String provider) {
        Event event = new Event(5, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth);
        event.setPlacementName(this.mCurrentPlacement);
        addEvent(event);
    }

    public synchronized void notifyVideoAdClosedEvent(String provider) {
        Event event = new Event(6, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth);
        event.setPlacementName(this.mCurrentPlacement);
        if (SupersonicConstants.MEDIATION_PROVIDER_NAME.equals(provider)) {
            this.mSessionDepth++;
        }
        addEvent(event);
    }

    public synchronized void notifyVideoAvailabilityChangedEvent(String provider, boolean availability) {
        Event event = new Event(7, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth);
        event.setStatus(availability);
        addEvent(event);
    }

    public synchronized void notifyVideoStartEvent(String provider) {
        Event event = new Event(8, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth);
        event.setPlacementName(this.mCurrentPlacement);
        addEvent(event);
    }

    public synchronized void notifyVideoEndEvent(String provider) {
        Event event = new Event(9, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth);
        event.setPlacementName(this.mCurrentPlacement);
        addEvent(event);
    }

    public synchronized void notifyMediationVideoAdRewardedEvent(String provider, String placementName, String rewardName, int rewardAmount) {
        setCurrentPlacement(placementName);
        Event event = new Event(10, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth);
        event.setPlacementName(this.mCurrentPlacement);
        event.setRewardName(rewardName);
        event.setRewardAmount(rewardAmount);
        addEvent(event);
    }

    public synchronized void notifyProviderVideoAdRewardedEvent(String provider, String placementName, String rewardName, int rewardAmount, String appKey) {
        setCurrentPlacement(placementName);
        Event event = new Event(10, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth);
        event.setPlacementName(this.mCurrentPlacement);
        event.setRewardName(rewardName);
        event.setRewardAmount(rewardAmount);
        event.setTransId(appKey);
        addEvent(event);
    }

    public synchronized void notifyReleaseEvent(String provider) {
        addEvent(new Event(11, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth));
    }

    public synchronized void notifyResumeEvent(String provider) {
        addEvent(new Event(12, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth));
    }

    public synchronized void notifyPauseEvent(String provider) {
        addEvent(new Event(13, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth));
    }

    public synchronized void notifyGetInstanceEvent(String provider) {
        addEvent(new Event(14, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth));
    }

    public synchronized void notifyInitOfferwallEvent(String provider) {
        addEvent(new Event(15, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth));
    }

    public synchronized void notifyInitInterstitialEvent(String provider) {
        addEvent(new Event(16, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth));
    }

    public synchronized void notifyPublisherCheckAvailabilityEvent(String provider, boolean isAdAvailable) {
        Event event = new Event(18, provider, SupersonicUtils.getTimeStamp(), this.mSessionDepth);
        event.setStatus(isAdAvailable);
        addEvent(event);
    }

    private void addEvent(Event event) {
        if (event != null && this.mIsEventsEnabled) {
            this.mEvents.add(event);
            this.mTotalEvents++;
            if (this.mDbHelper == null) {
                return;
            }
            if (shouldSendEvents(event)) {
                SuperLooper.getLooper().post(new SendStoredEventsRunnable(this.mDbHelper, this.mServerUrl, GeneralProperties.getProperties().toJSON(), this.mEvents));
                this.mEvents = new ArrayList();
                this.mTotalEvents = 0;
            } else if (shouldBackupEventsToDb(this.mEvents)) {
                SuperLooper.getLooper().post(new InsertEventRunnable(this.mDbHelper, this.mEvents));
                this.mEvents = new ArrayList();
            }
        }
    }

    private boolean shouldSendEvents(Event currentEvent) {
        return (currentEvent.getEventId() == 6 && SupersonicConstants.MEDIATION_PROVIDER_NAME.equals(currentEvent.getProvider())) || this.mTotalEvents >= this.mMaxNumberOfEvents;
    }

    private boolean shouldBackupEventsToDb(ArrayList<Event> events) {
        if (events != null) {
            return events.size() >= this.mBackupThreshold;
        } else {
            return false;
        }
    }

    private void setCurrentPlacement(String placement) {
        if (TextUtils.isEmpty(placement)) {
            this.mCurrentPlacement = "";
        } else {
            this.mCurrentPlacement = placement;
        }
    }
}
