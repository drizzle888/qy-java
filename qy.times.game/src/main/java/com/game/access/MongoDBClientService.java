package com.game.access;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

@Service
public class MongoDBClientService {

	private MongoTemplate mongoTemplate;
	
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public <T> T findById(String id,Class<T> entityType) {
		return mongoTemplate.findById(id, entityType);
	}
	
	public <T> List<T> find(Query query,Class<T> entityType) {
		return mongoTemplate.find(query, entityType);
	}
	public <T> List<T> find(Query query,Class<T> entityClass,String collectionName){
		return mongoTemplate.find(query, entityClass, collectionName);
	}

	public <T> T findOne(Query query,Class<T> entityType) {
		return mongoTemplate.findOne(query, entityType);
	}

	public <T> void update(Query query, Update update,Class<T> entityClass,String collectionName) {
		mongoTemplate.updateFirst(query, update, entityClass, collectionName);
	}
	
	public <T> void updateMulti(Query query, Update update, Class<T> entityType) {
		mongoTemplate.updateMulti(query, update, entityType);
	}
	
	public <T> T findAndModify(Query query, Update update,Class<T> entityType) {
		return mongoTemplate.findAndModify(query, update, entityType);
	}

	public <T> void save(T entity) {
		mongoTemplate.insert(entity);
	}
	
	public <T> void saveOrUpdate(T entity) {
		mongoTemplate.save(entity);
	}
	
	public <T> void batchSave(List<T> entities,Class<T> entityType) {
		mongoTemplate.insert(entities, entityType);
	}

	public <T> void remove(T entity) {
		mongoTemplate.remove(entity);
	}
	
	public <T> void remove(Query query,Class<T> entityClass,String collectionName) {
		mongoTemplate.remove(query, entityClass,collectionName);
	}
	
	public <T> List<T> findAndRemove(Query query,Class<T> entityType) {
		return mongoTemplate.findAllAndRemove(query, entityType);
	}
	
	public <T> Page<T> findPage(int pageNumber,int pageSize,Query query,Class<T> entityType){
		long count = this.count(query,entityType);
		Page<T> page = new Page<T>(pageNumber, pageSize);
		page.setTotal(count);
		query.skip((pageNumber - 1) * pageSize).limit(pageSize);
		List<T> rows = this.find(query,entityType);
		page.addAll(rows);
		return page;
	}
	
	public <T> long count(Query query,Class<T> entityType){
		return mongoTemplate.count(query, entityType);
	}
	
}
