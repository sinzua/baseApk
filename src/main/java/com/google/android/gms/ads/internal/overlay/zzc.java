package com.google.android.gms.ads.internal.overlay;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.view.Surface;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View.MeasureSpec;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzin;
import com.google.android.gms.internal.zzir;
import com.nativex.network.volley.DefaultRetryPolicy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@TargetApi(14)
@zzhb
public class zzc extends zzi implements OnAudioFocusChangeListener, OnBufferingUpdateListener, OnCompletionListener, OnErrorListener, OnInfoListener, OnPreparedListener, OnVideoSizeChangedListener, SurfaceTextureListener {
    private static final Map<Integer, String> zzDN = new HashMap();
    private final zzt zzDO;
    private int zzDP = 0;
    private int zzDQ = 0;
    private MediaPlayer zzDR;
    private Uri zzDS;
    private int zzDT;
    private int zzDU;
    private int zzDV;
    private int zzDW;
    private int zzDX;
    private float zzDY = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
    private boolean zzDZ;
    private boolean zzEa;
    private int zzEb;
    private zzh zzEc;

    static {
        zzDN.put(Integer.valueOf(-1004), "MEDIA_ERROR_IO");
        zzDN.put(Integer.valueOf(-1007), "MEDIA_ERROR_MALFORMED");
        zzDN.put(Integer.valueOf(-1010), "MEDIA_ERROR_UNSUPPORTED");
        zzDN.put(Integer.valueOf(-110), "MEDIA_ERROR_TIMED_OUT");
        zzDN.put(Integer.valueOf(100), "MEDIA_ERROR_SERVER_DIED");
        zzDN.put(Integer.valueOf(1), "MEDIA_ERROR_UNKNOWN");
        zzDN.put(Integer.valueOf(1), "MEDIA_INFO_UNKNOWN");
        zzDN.put(Integer.valueOf(700), "MEDIA_INFO_VIDEO_TRACK_LAGGING");
        zzDN.put(Integer.valueOf(3), "MEDIA_INFO_VIDEO_RENDERING_START");
        zzDN.put(Integer.valueOf(701), "MEDIA_INFO_BUFFERING_START");
        zzDN.put(Integer.valueOf(702), "MEDIA_INFO_BUFFERING_END");
        zzDN.put(Integer.valueOf(800), "MEDIA_INFO_BAD_INTERLEAVING");
        zzDN.put(Integer.valueOf(801), "MEDIA_INFO_NOT_SEEKABLE");
        zzDN.put(Integer.valueOf(802), "MEDIA_INFO_METADATA_UPDATE");
        zzDN.put(Integer.valueOf(901), "MEDIA_INFO_UNSUPPORTED_SUBTITLE");
        zzDN.put(Integer.valueOf(902), "MEDIA_INFO_SUBTITLE_TIMED_OUT");
    }

    public zzc(Context context, zzt com_google_android_gms_ads_internal_overlay_zzt) {
        super(context);
        setSurfaceTextureListener(this);
        this.zzDO = com_google_android_gms_ads_internal_overlay_zzt;
        this.zzDO.zza((zzi) this);
    }

    private void zzb(float f) {
        if (this.zzDR != null) {
            try {
                this.zzDR.setVolume(f, f);
                return;
            } catch (IllegalStateException e) {
                return;
            }
        }
        zzb.zzaK("AdMediaPlayerView setMediaPlayerVolume() called before onPrepared().");
    }

    private void zzfa() {
        Throwable e;
        zzin.v("AdMediaPlayerView init MediaPlayer");
        SurfaceTexture surfaceTexture = getSurfaceTexture();
        if (this.zzDS != null && surfaceTexture != null) {
            zzv(false);
            try {
                this.zzDR = new MediaPlayer();
                this.zzDR.setOnBufferingUpdateListener(this);
                this.zzDR.setOnCompletionListener(this);
                this.zzDR.setOnErrorListener(this);
                this.zzDR.setOnInfoListener(this);
                this.zzDR.setOnPreparedListener(this);
                this.zzDR.setOnVideoSizeChangedListener(this);
                this.zzDV = 0;
                this.zzDR.setDataSource(getContext(), this.zzDS);
                this.zzDR.setSurface(new Surface(surfaceTexture));
                this.zzDR.setAudioStreamType(3);
                this.zzDR.setScreenOnWhilePlaying(true);
                this.zzDR.prepareAsync();
                zzw(1);
            } catch (IOException e2) {
                e = e2;
                zzb.zzd("Failed to initialize MediaPlayer at " + this.zzDS, e);
                onError(this.zzDR, 1, 0);
            } catch (IllegalArgumentException e3) {
                e = e3;
                zzb.zzd("Failed to initialize MediaPlayer at " + this.zzDS, e);
                onError(this.zzDR, 1, 0);
            }
        }
    }

    private void zzfb() {
        if (zzfe() && this.zzDR.getCurrentPosition() > 0 && this.zzDQ != 3) {
            zzin.v("AdMediaPlayerView nudging MediaPlayer");
            zzb(0.0f);
            this.zzDR.start();
            int currentPosition = this.zzDR.getCurrentPosition();
            long currentTimeMillis = zzr.zzbG().currentTimeMillis();
            while (zzfe() && this.zzDR.getCurrentPosition() == currentPosition) {
                if (zzr.zzbG().currentTimeMillis() - currentTimeMillis > 250) {
                    break;
                }
            }
            this.zzDR.pause();
            zzfj();
        }
    }

    private void zzfc() {
        AudioManager zzfk = zzfk();
        if (zzfk != null && !this.zzEa) {
            if (zzfk.requestAudioFocus(this, 3, 2) == 1) {
                zzfh();
            } else {
                zzb.zzaK("AdMediaPlayerView audio focus request failed");
            }
        }
    }

    private void zzfd() {
        zzin.v("AdMediaPlayerView abandon audio focus");
        AudioManager zzfk = zzfk();
        if (zzfk != null && this.zzEa) {
            if (zzfk.abandonAudioFocus(this) == 1) {
                this.zzEa = false;
            } else {
                zzb.zzaK("AdMediaPlayerView abandon audio focus failed");
            }
        }
    }

    private boolean zzfe() {
        return (this.zzDR == null || this.zzDP == -1 || this.zzDP == 0 || this.zzDP == 1) ? false : true;
    }

    private void zzfh() {
        zzin.v("AdMediaPlayerView audio focus gained");
        this.zzEa = true;
        zzfj();
    }

    private void zzfi() {
        zzin.v("AdMediaPlayerView audio focus lost");
        this.zzEa = false;
        zzfj();
    }

    private void zzfj() {
        if (this.zzDZ || !this.zzEa) {
            zzb(0.0f);
        } else {
            zzb(this.zzDY);
        }
    }

    private AudioManager zzfk() {
        return (AudioManager) getContext().getSystemService("audio");
    }

    private void zzv(boolean z) {
        zzin.v("AdMediaPlayerView release");
        if (this.zzDR != null) {
            this.zzDR.reset();
            this.zzDR.release();
            this.zzDR = null;
            zzw(0);
            if (z) {
                this.zzDQ = 0;
                zzx(0);
            }
            zzfd();
        }
    }

    private void zzw(int i) {
        if (i == 3) {
            this.zzDO.zzfO();
        } else if (this.zzDP == 3 && i != 3) {
            this.zzDO.zzfP();
        }
        this.zzDP = i;
    }

    private void zzx(int i) {
        this.zzDQ = i;
    }

    public int getCurrentPosition() {
        return zzfe() ? this.zzDR.getCurrentPosition() : 0;
    }

    public int getDuration() {
        return zzfe() ? this.zzDR.getDuration() : -1;
    }

    public int getVideoHeight() {
        return this.zzDR != null ? this.zzDR.getVideoHeight() : 0;
    }

    public int getVideoWidth() {
        return this.zzDR != null ? this.zzDR.getVideoWidth() : 0;
    }

    public void onAudioFocusChange(int focusChange) {
        if (focusChange > 0) {
            zzfh();
        } else if (focusChange < 0) {
            zzfi();
        }
    }

    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        this.zzDV = percent;
    }

    public void onCompletion(MediaPlayer mp) {
        zzin.v("AdMediaPlayerView completion");
        zzw(5);
        zzx(5);
        zzir.zzMc.post(new Runnable(this) {
            final /* synthetic */ zzc zzEd;

            {
                this.zzEd = r1;
            }

            public void run() {
                if (this.zzEd.zzEc != null) {
                    this.zzEd.zzEc.zzfB();
                }
            }
        });
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        final String str = (String) zzDN.get(Integer.valueOf(what));
        final String str2 = (String) zzDN.get(Integer.valueOf(extra));
        zzb.zzaK("AdMediaPlayerView MediaPlayer error: " + str + ":" + str2);
        zzw(-1);
        zzx(-1);
        zzir.zzMc.post(new Runnable(this) {
            final /* synthetic */ zzc zzEd;

            public void run() {
                if (this.zzEd.zzEc != null) {
                    this.zzEd.zzEc.zzg(str, str2);
                }
            }
        });
        return true;
    }

    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        String str = (String) zzDN.get(Integer.valueOf(extra));
        zzin.v("AdMediaPlayerView MediaPlayer info: " + ((String) zzDN.get(Integer.valueOf(what))) + ":" + str);
        return true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultSize = getDefaultSize(this.zzDT, widthMeasureSpec);
        int defaultSize2 = getDefaultSize(this.zzDU, heightMeasureSpec);
        if (this.zzDT > 0 && this.zzDU > 0) {
            int mode = MeasureSpec.getMode(widthMeasureSpec);
            int size = MeasureSpec.getSize(widthMeasureSpec);
            int mode2 = MeasureSpec.getMode(heightMeasureSpec);
            defaultSize2 = MeasureSpec.getSize(heightMeasureSpec);
            if (mode == 1073741824 && mode2 == 1073741824) {
                if (this.zzDT * defaultSize2 < this.zzDU * size) {
                    defaultSize = (this.zzDT * defaultSize2) / this.zzDU;
                } else if (this.zzDT * defaultSize2 > this.zzDU * size) {
                    defaultSize2 = (this.zzDU * size) / this.zzDT;
                    defaultSize = size;
                } else {
                    defaultSize = size;
                }
            } else if (mode == 1073741824) {
                defaultSize = (this.zzDU * size) / this.zzDT;
                if (mode2 != Integer.MIN_VALUE || defaultSize <= defaultSize2) {
                    defaultSize2 = defaultSize;
                    defaultSize = size;
                } else {
                    defaultSize = size;
                }
            } else if (mode2 == 1073741824) {
                defaultSize = (this.zzDT * defaultSize2) / this.zzDU;
                if (mode == Integer.MIN_VALUE && defaultSize > size) {
                    defaultSize = size;
                }
            } else {
                int i = this.zzDT;
                defaultSize = this.zzDU;
                if (mode2 != Integer.MIN_VALUE || defaultSize <= defaultSize2) {
                    defaultSize2 = defaultSize;
                    defaultSize = i;
                } else {
                    defaultSize = (this.zzDT * defaultSize2) / this.zzDU;
                }
                if (mode == Integer.MIN_VALUE && r1 > size) {
                    defaultSize2 = (this.zzDU * size) / this.zzDT;
                    defaultSize = size;
                }
            }
        }
        setMeasuredDimension(defaultSize, defaultSize2);
        if (VERSION.SDK_INT == 16) {
            if ((this.zzDW > 0 && this.zzDW != defaultSize) || (this.zzDX > 0 && this.zzDX != defaultSize2)) {
                zzfb();
            }
            this.zzDW = defaultSize;
            this.zzDX = defaultSize2;
        }
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        zzin.v("AdMediaPlayerView prepared");
        zzw(2);
        this.zzDO.zzfz();
        zzir.zzMc.post(new Runnable(this) {
            final /* synthetic */ zzc zzEd;

            {
                this.zzEd = r1;
            }

            public void run() {
                if (this.zzEd.zzEc != null) {
                    this.zzEd.zzEc.zzfz();
                }
            }
        });
        this.zzDT = mediaPlayer.getVideoWidth();
        this.zzDU = mediaPlayer.getVideoHeight();
        if (this.zzEb != 0) {
            seekTo(this.zzEb);
        }
        zzfb();
        zzb.zzaJ("AdMediaPlayerView stream dimensions: " + this.zzDT + " x " + this.zzDU);
        if (this.zzDQ == 3) {
            play();
        }
        zzfc();
        zzfj();
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        zzin.v("AdMediaPlayerView surface created");
        zzfa();
        zzir.zzMc.post(new Runnable(this) {
            final /* synthetic */ zzc zzEd;

            {
                this.zzEd = r1;
            }

            public void run() {
                if (this.zzEd.zzEc != null) {
                    this.zzEd.zzEc.zzfy();
                }
            }
        });
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        zzin.v("AdMediaPlayerView surface destroyed");
        if (this.zzDR != null && this.zzEb == 0) {
            this.zzEb = this.zzDR.getCurrentPosition();
        }
        zzir.zzMc.post(new Runnable(this) {
            final /* synthetic */ zzc zzEd;

            {
                this.zzEd = r1;
            }

            public void run() {
                if (this.zzEd.zzEc != null) {
                    this.zzEd.zzEc.onPaused();
                    this.zzEd.zzEc.zzfC();
                }
            }
        });
        zzv(true);
        return true;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int w, int h) {
        Object obj = 1;
        zzin.v("AdMediaPlayerView surface changed");
        Object obj2 = this.zzDQ == 3 ? 1 : null;
        if (!(this.zzDT == w && this.zzDU == h)) {
            obj = null;
        }
        if (this.zzDR != null && obj2 != null && r1 != null) {
            if (this.zzEb != 0) {
                seekTo(this.zzEb);
            }
            play();
        }
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        this.zzDO.zzb(this);
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        zzin.v("AdMediaPlayerView size changed: " + width + " x " + height);
        this.zzDT = mp.getVideoWidth();
        this.zzDU = mp.getVideoHeight();
        if (this.zzDT != 0 && this.zzDU != 0) {
            requestLayout();
        }
    }

    public void pause() {
        zzin.v("AdMediaPlayerView pause");
        if (zzfe() && this.zzDR.isPlaying()) {
            this.zzDR.pause();
            zzw(4);
            zzir.zzMc.post(new Runnable(this) {
                final /* synthetic */ zzc zzEd;

                {
                    this.zzEd = r1;
                }

                public void run() {
                    if (this.zzEd.zzEc != null) {
                        this.zzEd.zzEc.onPaused();
                    }
                }
            });
        }
        zzx(4);
    }

    public void play() {
        zzin.v("AdMediaPlayerView play");
        if (zzfe()) {
            this.zzDR.start();
            zzw(3);
            zzir.zzMc.post(new Runnable(this) {
                final /* synthetic */ zzc zzEd;

                {
                    this.zzEd = r1;
                }

                public void run() {
                    if (this.zzEd.zzEc != null) {
                        this.zzEd.zzEc.zzfA();
                    }
                }
            });
        }
        zzx(3);
    }

    public void seekTo(int millis) {
        zzin.v("AdMediaPlayerView seek " + millis);
        if (zzfe()) {
            this.zzDR.seekTo(millis);
            this.zzEb = 0;
            return;
        }
        this.zzEb = millis;
    }

    public void setMimeType(String mimeType) {
    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        this.zzDS = uri;
        this.zzEb = 0;
        zzfa();
        requestLayout();
        invalidate();
    }

    public void stop() {
        zzin.v("AdMediaPlayerView stop");
        if (this.zzDR != null) {
            this.zzDR.stop();
            this.zzDR.release();
            this.zzDR = null;
            zzw(0);
            zzx(0);
            zzfd();
        }
        this.zzDO.onStop();
    }

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    public void zza(float f) {
        this.zzDY = f;
        zzfj();
    }

    public void zza(zzh com_google_android_gms_ads_internal_overlay_zzh) {
        this.zzEc = com_google_android_gms_ads_internal_overlay_zzh;
    }

    public String zzeZ() {
        return "MediaPlayer";
    }

    public void zzff() {
        this.zzDZ = true;
        zzfj();
    }

    public void zzfg() {
        this.zzDZ = false;
        zzfj();
    }
}
