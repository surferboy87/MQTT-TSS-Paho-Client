package net.huraki.persistence;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 * @author Raphael Huber
 *
 */
public class MapDBPersistence {

	private DB db;
	private final String storePath;
	private final int autosaveInterval;

	private MapDBTssTopicStore topicStore;

	protected final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public MapDBPersistence() {
		this.storePath = "././out/bla.db";
		this.autosaveInterval = 30;
	}
	
	public MapDBTssTopicStore tssTopicStore(){
		return this.topicStore;
	}

	public void initStore() {
		if (storePath == null || storePath.isEmpty()) {
			db = DBMaker.memoryDB().make();
		} else {
//			File tmpFile = null;
//			try {
//				tmpFile = new File(storePath);
//				boolean fileNewlyCreated = tmpFile.createNewFile();
//				System.out.println(fileNewlyCreated);
//			} catch (IOException ex) {
//
//			}
			db = DBMaker.fileDB(storePath).make();
		}
		scheduler.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				db.commit();
			}
		}, this.autosaveInterval, this.autosaveInterval, TimeUnit.SECONDS);

		this.topicStore = new MapDBTssTopicStore(db);
		this.topicStore.initStore();
	}

	public void close() {
		if (this.db.isClosed()) {
			// LOG.debug("already closed");
			return;
		}
		this.db.commit();
		// LOG.debug("persisted subscriptions {}", m_persistentSubscriptions);
		this.db.close();
		// LOG.debug("closed disk storage");
		this.scheduler.shutdown();
		// LOG.debug("Persistence commit scheduler is shutdown");
	}
}
