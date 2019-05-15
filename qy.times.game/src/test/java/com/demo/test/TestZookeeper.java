package com.demo.test;
/*package com.buoumall.game.test;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

*//**
 * http://blog.csdn.net/desilting/article/details/41280869
 * http://chenjingbo.iteye.com/blog/2039641
 * http://www.cnblogs.com/likehua/p/4060316.html
 * http://agapple.iteye.com/blog/1184040
 * @author Administrator
 *
 *//*
public class TestZookeeper {
	private static String connectString = "192.168.1.254:2181";//host:port
	private static int sessionTimeout = 2000;//ms

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	*//**
	 * @param args
	 *//*
	public static void main(String[] args) throws Exception{
		ZooKeeper zk = new ZooKeeper(connectString, sessionTimeout, new Watcher(){

			@Override
			public void process(WatchedEvent event) {
				KeeperState keeperState = event.getState();
				EventType eventType = event.getType();
				if(KeeperState.SyncConnected==keeperState){
					if(EventType.None==eventType){
						connectedSemaphore.countDown();
						System.out.println("zk 建立连接！");
					}
				}
			}
		});
		connectedSemaphore.await();
		System.out.println(zk.toString());
		//Thread.sleep(10000);
		//创建父节点
		//zk.create("/testRoot", "testRoot".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		//zk.create("/testRoot/children", "children data".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println(new String(zk.getData("/testRoot", false, null)));
		System.out.println(zk.getChildren("/testRoot", false));
		
		zk.setData("/testRoot", "Modify data root".getBytes(), -1);
		System.out.println(new String(zk.getData("/testRoot", false, null)));
		
		System.out.println(zk.exists("/testRoot", false));
		
		//zk.delete("/testRoot/children", -1);
		
		//System.out.println(zk.exists("/testRoot/children", false));
		
		zk.delete("/testRoot", -1);
		System.out.println(zk.exists("/testRoot", false));
		
		zk.close();
		
	}
}
*/