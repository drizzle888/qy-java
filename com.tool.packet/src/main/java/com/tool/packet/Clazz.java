package com.tool.packet;

import java.util.ArrayList;
import java.util.List;

public class Clazz {
	private String pkg;
	private String name;
	private String parent;
	private String namespace;
	private List<String> imports = new ArrayList<String>();
	private List<String> usings = new ArrayList<String>();
	private List<Field> fields = new ArrayList<Field>();
	
	public String getPkg() {
		return pkg;
	}
	public void setPkg(String pkg) {
		this.pkg = pkg;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public List<String> getImports() {
		return imports;
	}
	public void setImports(List<String> imports) {
		this.imports = imports;
	}
	public List<String> getUsings() {
		return usings;
	}
	public void setUsings(List<String> usings) {
		this.usings = usings;
	}
	public List<Field> getFields() {
		return fields;
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
}
