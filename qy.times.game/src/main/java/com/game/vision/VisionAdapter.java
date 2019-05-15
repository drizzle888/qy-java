package com.game.vision;

import java.util.List;
import java.util.Map;

import com.common.entity.Book;
import com.common.entity.Box;
import com.common.entity.Location;
import com.common.entity.Role;

public class VisionAdapter {
	private final static VisionList<Box> boxListTool = new VisionList<Box>();
	private final static VisionList<Book> bookListTool = new VisionList<Book>();
	private final static VisionList<Role> roleListTool = new VisionList<Role>();
	private final static VisionMap<Short, Box> boxMapTool = new VisionMap<Short, Box>();
	private final static VisionMap<Integer, Book> bookMapTool = new VisionMap<Integer, Book>();
	private final static VisionMap<Long, Role> roleMapTool = new VisionMap<Long, Role>();
	
	public static List<Role> getVisionList(Location location, Map<Long, Role> map, int vision) {
		return roleMapTool.getVisionList(location, map, vision);
	}
	
	public static List<Role> getVisionRoleList(Role currRole, Map<Long, Role> map, int vision) {
		List<Role> visionRoleList = roleMapTool.getVisionList(currRole.location, map, vision);
		visionRoleList.remove(currRole);
		return visionRoleList;
	}
	
	public static List<Role> getVisionRoleList(Book book, Map<Long, Role> map, int vision) {
		return roleMapTool.getVisionList(book.location, map, vision);
	}
	
	public static List<Box> getVisionBoxList(Role currRole, Map<Short, Box> map, int vision) {
		return boxMapTool.getVisionList(currRole.location, map, vision);
	}
	
	public static List<Book> getVisionBookList(Role currRole, Map<Integer, Book> map, int vision) {
		return bookMapTool.getVisionList(currRole.location, map, vision);
	}
	
	public static List<Role> getInRoleList(List<Role> oldList, List<Role> newList) {
		return roleListTool.getInList(oldList, newList);
	}
	
	public static List<Role> getOutRoleList(List<Role> oldList, List<Role> newList) {
		return roleListTool.getOutList(oldList, newList);
	}
	
	public static List<Box> getInBoxList(List<Box> oldList, List<Box> newList) {
		return boxListTool.getInList(oldList, newList);
	}
	
	public static List<Box> getOutBoxList(List<Box> oldList, List<Box> newList) {
		return boxListTool.getOutList(oldList, newList);
	}
	
	public static List<Book> getInBookList(List<Book> oldList, List<Book> newList) {
		return bookListTool.getInList(oldList, newList);
	}
	
	public static List<Book> getOutBookList(List<Book> oldList, List<Book> newList) {
		return bookListTool.getOutList(oldList, newList);
	}
	
	public static Role getMinDistanceRole(List<Role> roleList, Location location) {
		return roleListTool.getMinDistanceEntity(roleList, location);
	}
	
	public static Box getMinDistanceBox(List<Box> boxList, Location location) {
		return boxListTool.getMinDistanceEntity(boxList, location);
	}
	
	public static Book getMinDistanceBook(List<Book> bookList, Location location) {
		return bookListTool.getMinDistanceEntity(bookList, location);
	}
	
	public static Book getMinDistanceBook(Map<Integer, Book> map, Location location) {
		return bookMapTool.getMinDistanceEntity(location, map);
	}
}
