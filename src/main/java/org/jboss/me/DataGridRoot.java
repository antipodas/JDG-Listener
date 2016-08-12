package org.jboss.me;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.CacheContainer;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class DataGridRoot {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CacheListener listener = new CacheListener();
		CacheContainer cacheManager = getCacheManager();
		((EmbeddedCacheManager) cacheManager).addListener(listener);
		Cache<String, String> cache = cacheManager.getCache("listener");
		cache.addListener(listener);
		
		DataGridNode threads[] = new DataGridNode[5];
		
		for (int i = 0; i < 5; i++){
			threads[i] = new DataGridNode();
			threads[i].start();
			//System.out.println(threads[i].getName());
		}
		
		while (true) { //wait till cluster is fully up
			int count = 0;
			for (DataGridNode thread : threads){
				if (thread.status == 1)
					++count;
			}
				if (count == 5)
					break;
				
				try { Thread.sleep(25); } catch (InterruptedException e) {};
		}
		
		for (int i = 0; i < 5; i++) // begin loading cache
			threads[i].process();
		
		System.out.println("Final cache entry count: " + cache.size());
		
		while(true) { //wait for work to finish
			int count = 0;
			for (DataGridNode thread : threads){
				if (thread.status == 0)
					++count;
			}
			if (count == 5)
				break;
			
			try { Thread.sleep(25); } catch (InterruptedException e) {};
		}
		
		System.out.println("Final cache entry count: " + cache.size());
		
		for (int i = 0; i < 5; i++) // shutdown cluster
			threads[i].cleanup();
		
		cacheManager.stop();
		
	}

	public static CacheContainer getCacheManager() {
		// TODO Auto-generated method stub
		GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
				.clusteredDefault()
				.globalJmxStatistics().allowDuplicateDomains(true)
				.transport().addProperty("configurationFile", "jgroups.xml")
				.build();
		
		Configuration config = new ConfigurationBuilder()
		.clustering().cacheMode(CacheMode.REPL_SYNC)
		.eviction().maxEntries(32).strategy(EvictionStrategy.LIRS)
		.build();
		
		return new DefaultCacheManager(globalConfig, config, true);
	}

}
