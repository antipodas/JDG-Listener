package org.jboss.me;

import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;

public class DataGridNode extends Thread{

	public int status = -1;
	private CacheContainer cacheManager;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.cacheManager= DataGridRoot.getCacheManager();
		status = 1;
	}

	public void process() {
		// TODO Auto-generated method stub

		Cache<String, String> cache = cacheManager.getCache("listener");
		try {
			for (int i = 0; i < 10; i++){
				String key = this.getName() + "-" + i;
				cache.put(key, "X");
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			status = 0;
		}
	}

	public void cleanup() {
		// TODO Auto-generated method stub
		if (cacheManager != null)
			cacheManager.stop();
	}

}
