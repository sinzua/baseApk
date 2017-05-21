package com.nativex.monetization.mraid;

import android.support.annotation.NonNull;
import com.nativex.monetization.mraid.MRAIDUtils.PlacementType;
import java.util.HashMap;
import java.util.Map.Entry;

class MRAIDContainerHolder extends HashMap<String, MapDataHolder> {
    private static final long serialVersionUID = -1713078664768447210L;

    class MapDataHolder {
        MRAIDContainer cached;
        MRAIDContainer shown;

        MapDataHolder() {
        }
    }

    MRAIDContainerHolder() {
    }

    public void putContainer(@NonNull MRAIDContainer container, boolean cached) {
        MapDataHolder dataHolder = getMapDataHolder(container.getContainerName());
        if (cached) {
            if (dataHolder.cached != null) {
                dataHolder.cached.releaseWithCloseAnimation();
            }
            dataHolder.cached = container;
            return;
        }
        if (dataHolder.shown != null) {
            dataHolder.shown.releaseWithCloseAnimation();
        }
        dataHolder.shown = container;
    }

    MapDataHolder getMapDataHolder(PlacementType type, String name) {
        return getMapDataHolder(MRAIDContainer.getContainerName(type, name));
    }

    MapDataHolder getMapDataHolder(String name) {
        MapDataHolder dataHolder = (MapDataHolder) get(name);
        if (dataHolder != null) {
            return dataHolder;
        }
        dataHolder = new MapDataHolder();
        put(name, dataHolder);
        return dataHolder;
    }

    public boolean hasContainer(PlacementType type, String name, boolean cached) {
        MapDataHolder dataHolder = (MapDataHolder) get(MRAIDContainer.getContainerName(type, name));
        if (dataHolder == null) {
            return false;
        }
        if (cached) {
            if (dataHolder.cached != null) {
                return true;
            }
            return false;
        } else if (dataHolder.shown == null) {
            return false;
        } else {
            return true;
        }
    }

    public MRAIDContainer getCachedContainer(PlacementType type, String name) {
        MapDataHolder dataHolder = (MapDataHolder) get(MRAIDContainer.getContainerName(type, name));
        if (dataHolder != null) {
            return dataHolder.cached;
        }
        return null;
    }

    public MRAIDContainer getContainer(PlacementType type, String name) {
        return getContainer(MRAIDContainer.getContainerName(type, name));
    }

    public MRAIDContainer getContainer(String containerName) {
        MapDataHolder dataHolder = (MapDataHolder) get(containerName);
        if (dataHolder != null) {
            return dataHolder.shown;
        }
        return null;
    }

    public void releaseContainer(PlacementType type, String name, boolean cached) {
        MapDataHolder holder = (MapDataHolder) get(MRAIDContainer.getContainerName(type, name));
        if (holder == null) {
            return;
        }
        if (cached && holder.cached != null) {
            holder.cached.releaseWithCloseAnimation();
            holder.cached = null;
        } else if (holder.shown != null) {
            holder.shown.releaseWithCloseAnimation();
            holder.shown = null;
        }
    }

    public void releaseContainer(MRAIDContainer container, boolean closeAnimation) {
        if (container != null) {
            MapDataHolder holder = (MapDataHolder) get(container.getContainerName());
            if (holder != null) {
                if (holder.cached == container) {
                    holder.cached = null;
                    container.shouldHaltVideo = false;
                } else if (holder.shown == container) {
                    holder.shown = null;
                }
            }
            container.shouldReleaseWithCloseAnimation(closeAnimation);
        }
    }

    public void releaseContainersByName(PlacementType type, String name) {
        releaseContainers((MapDataHolder) get(MRAIDContainer.getContainerName(type, name)));
    }

    public void release() {
        for (Entry<String, MapDataHolder> entry : entrySet()) {
            releaseContainers((MapDataHolder) entry.getValue());
        }
        clear();
    }

    private void releaseContainers(MapDataHolder holder) {
        if (holder != null) {
            if (holder.shown != null) {
                holder.shown.releaseWithCloseAnimation();
                holder.shown = null;
            }
            if (holder.cached != null) {
                holder.cached.releaseWithCloseAnimation();
                holder.cached = null;
            }
        }
    }
}
