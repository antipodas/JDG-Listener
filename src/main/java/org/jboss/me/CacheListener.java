package org.jboss.me;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntriesEvicted;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.event.CacheEntriesEvictedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachemanagerlistener.annotation.CacheStopped;
import org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStoppedEvent;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;

@Listener(sync = false)
public class CacheListener {
	
	@CacheEntryCreated
	public void onEntryCreated(CacheEntryCreatedEvent<String, String> event) {
		if (!event.isPre()){
			System.out.println("Entry key:  " + event.getKey() + " created in the cache");
		}
	}

	@CacheEntriesEvicted
	public void onEviction(CacheEntriesEvictedEvent<String, String> event){
		System.out.println("These are the keys evicted: " + event.getEntries().keySet());
		}
	
	@CacheStopped
	public void onCacheStop(CacheStoppedEvent event){
		System.out.println("Cache Stopped!");
	}
	
	@ViewChanged
	public void onClusterChange(ViewChangedEvent event){
		System.out.println("New cluster topology: " + event.getNewMembers());
	}
	
}
