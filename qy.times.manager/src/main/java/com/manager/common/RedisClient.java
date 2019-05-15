package com.manager.common;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import com.google.common.collect.Lists;

public class RedisClient<T> {

	private RedisTemplate<String, T> redisTemplate;
	
	
	public RedisTemplate<String, T> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, T> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	//加入缓存
	public void add(String key, T value) {
		if(StringUtils.isBlank(key)){
			return;
		}
		ValueOperations<String, T> valueops = redisTemplate.opsForValue();
		valueops.set(key, value);
	}

	//加入缓存，并设置缓存时间
	public void add(String key, T value, long timeout, TimeUnit unit) {
		if(StringUtils.isBlank(key)){
			return;
		}
		ValueOperations<String, T> valueops = redisTemplate.opsForValue();
		valueops.set(key, value, timeout, unit);
	}

	// 如果这个key不在redis中就设置
	public boolean setIfAbsent(String key, T value) {
		ValueOperations<String, T> valueops = redisTemplate.opsForValue();
		boolean result = valueops.setIfAbsent(key, value);
		return result;
	}
	
	//获取缓存的对象
	public T getValue(String key) {
		if(StringUtils.isBlank(key)){
			return null;
		}
		ValueOperations<String, T> valueops = redisTemplate.opsForValue();
		T value = valueops.get(key);
		return value;
	}

	//获取1分钟之内redis自增序列
	public Long getSequence(String key){
		redisTemplate.setEnableTransactionSupport(false);
		if(StringUtils.isBlank(key)){
			return null;
		}
		ValueOperations<String, T> valueOperations= redisTemplate.opsForValue();
		if(!exists(key)){
			valueOperations.set(key, null, 1l, TimeUnit.MINUTES);
		}
		Long sequence = valueOperations.increment(key,1l);
		return sequence;
	}
	
	//获取所有匹配的key 例如  KEYS w3c* 获取以 w3c 为开头的 key 的值
	public List<T> getByPattern(String pattern) {
		Set<String> keys = redisTemplate.keys(pattern);
		ValueOperations<String, T> valueops = redisTemplate.opsForValue();
		List<T> list = valueops.multiGet(keys);
		return list;
	}
	
	//批量加入redis
	public void multiSet(Map<String, T> map) {
		ValueOperations<String, T> opsForValue = redisTemplate.opsForValue();
		opsForValue.multiSet(map);
	}

	//通过key批量获取
	public List<T> getValueMulti(List<String> keys) {
		ValueOperations<String, T> valueops = redisTemplate.opsForValue();
		List<T> value = valueops.multiGet(keys);
		return value;
	}
	
	
	 /**
	  * 缓存List数据
	  * @param key  缓存的键值
	  * @param dataList 待缓存的List数据
	  * @return   缓存的对象
	  */
	 public void setCacheList(String key,List<T> dataList){
	  ListOperations<String, T> listOperation = redisTemplate.opsForList();
	  if(CollectionUtils.isEmpty(dataList)){
		  return;
	  }
	  listOperation.leftPushAll(key, dataList);
	 }
	 public void setCacheList(String key,List<T> dataList, long timeout, TimeUnit unit ){
		 setCacheList(key,dataList);
		 expire(key, timeout, unit);
	 }
	/**
	 * 获得缓存的list对象
	 * 
	 * @param key
	 *            缓存的键值
	 * @return 缓存键值对应的数据
	 */
	public List<T> getCacheList(String key) {
		ListOperations<String, T> listOperation = redisTemplate.opsForList();
		Long size = listOperation.size(key);
		return listOperation.range(key, 0, size);
	}
	  
	 /**
	  * 缓存Set
	  * @param key  缓存键值
	  * @param dataSet 缓存的数据
	  * @return   缓存数据的对象
	  */
	public void setCacheSet(String key,T[] dataSet) {
		SetOperations<String, T> setOperation = redisTemplate.opsForSet();
		setOperation.add(key, dataSet);
	}
	  
	 /**
	  * 获得缓存的set
	  * @param key
	  * @param operation
	  * @return
	  */
	 public Set<T> getCacheSet(String key){
	  SetOperations<String,T> operation = redisTemplate.opsForSet(); 
	  return operation.members(key);
	 }
	
	 /**
	  * 
	 * @Title: RedisClientService
	 * @Date: 2016年1月12日 
	 * @author: 余
	 * @Description: 有序缓存
	 * @return
	 *
	  */
	 public void setCacheSortSet(String key,List<T> dataSet) {
		 if(CollectionUtils.isEmpty(dataSet)){
			 return;
		 }
		 ZSetOperations<String, T> opsForZSet = redisTemplate.opsForZSet();
		 for (int i = 0; i < dataSet.size(); i++) {
			 opsForZSet.add(key, dataSet.get(i), Double.valueOf(i));
		}
	}
	 
	 /**
	  * 
	 * @Title: RedisClientService
	 * @Date: 2016年1月12日 
	 * @author: 余
	 * @Description: 获取有序缓存
	 * @param key
	 * @return
	 *
	  */
	 public List<T> getCacheSortSet(String key) {
		ZSetOperations<String, T> opsForZSet = redisTemplate.opsForZSet();
		return Lists.newArrayList(opsForZSet.range(key, 0, opsForZSet.size(key)));
	}
	 
	 public void setCacheMap(String key, String field, T value) {
		 HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
		 hashOperations.put(key, field, value);
	 }
	 
	 public T getCacheMap(String key, String field) {
		 HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
		 return hashOperations.get(key, field);
	 }
	 
	 /**
	  * 缓存Map
	  * @param key
	  * @param dataMap
	  * @return
	  */
	 public void setCacheMap(String key,Map<String, T> dataMap){
		 if(dataMap.isEmpty()){
			 return;
		 }
	    HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
	    hashOperations.putAll(key, dataMap);
	 }
	  
	 /**
	  * 获得缓存的Map
	  * @param key
	  * @param hashOperation
	  * @return
	  */
	 public Map<String, T> getCacheMap(String key){
	  HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
	  return hashOperations.entries(key);
	}
	 
	//单个删除
	public void delete(String key) {
		if(StringUtils.isBlank(key)){
			return;
		}
		redisTemplate.delete(key);
	}

	/**
	 * 批量删除
	 */
	public void delete(Collection<String> keys) {
		if(CollectionUtils.isEmpty(keys)){
			return;
		}
		redisTemplate.delete(keys);
	}

	//判断key是否存在
	public boolean exists(String key) {
		if(StringUtils.isBlank(key)){
			return false;
		}
		Boolean b = redisTemplate.hasKey(key);
		return b==null?false:b.booleanValue();
	}
	
	//设置指定key的过期时间
	public boolean expire(String key,long timeout,TimeUnit unit) {
		return redisTemplate.expire(key, timeout, unit);
	}
	
	//设置指定key的过期时间
	public boolean expireAt(String key,Date date) {
		return redisTemplate.expireAt(key, date);
	}

	//获取key过期剩余时间，单位是秒
	public Long getExpire(String key) {
		if(StringUtils.isBlank(key)){
			return null;
		}
		if(exists(key)){
			return redisTemplate.getExpire(key);
		}else{
			return null;
		}
	}
	//获取key过期剩余时间
	public Long getExpire(String key,TimeUnit unit) {
		if(StringUtils.isBlank(key)){
			return null;
		}
		if(exists(key)){
			return redisTemplate.getExpire(key,unit);
		}else {
			return null;
		}
	}
	
}
