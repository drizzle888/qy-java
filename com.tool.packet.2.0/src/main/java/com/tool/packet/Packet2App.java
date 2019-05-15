package com.tool.packet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Packet2App {
	private final static String resource_path = "src/main/resources";
	private final static String generate_path = "src/main/generates";

	public static void main(String[] args) {
		File resources = null;
		try {
			resources = new File(resource_path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] filelist = resources.list();
		List<Clazz> clazzList = new ArrayList<Clazz>();
		for (int i = 0; i < filelist.length; i++) {
			File file = new File(filelist[i]);
			System.out.println("name=" + file.getName());
			Clazz clazz = readFile(file.getName());
			clazzList.add(clazz);
		}
		for (int i = 0; i < clazzList.size(); i++) {
			Clazz clazz = clazzList.get(i);
			writeJavaFile(clazz);
			writeCSharpFile(clazz);
		}
	}

	private static Clazz readFile(String fileName) {
		Clazz clazz = new Clazz();
		File file = new File(resource_path + "/" + fileName);
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(file);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element root = doc.getRootElement();
		clazz.setPkg(root.attribute("package").getValue());
		clazz.setName(root.attribute("name").getValue());
		clazz.setParent(root.attribute("parent").getValue());
		clazz.setNamespace(root.attribute("namespace").getValue());
		Element element;
		for (Iterator<Element> i = root.elementIterator("using"); i.hasNext();) {
			element = (Element) i.next();
			String value = element.getStringValue();
			clazz.getUsings().add(value);
		}
		for (Iterator<Element> i = root.elementIterator("import"); i.hasNext();) {
			element = (Element) i.next();
			String value = element.getStringValue();
			clazz.getImports().add(value);
		}
		for (Iterator<Element> i = root.elementIterator("field"); i.hasNext();) {
			element = (Element) i.next();
			String name = element.attributeValue("name");
			String type = element.attributeValue("type");
			String generic = element.attributeValue("generic");
			String elmt = element.attributeValue("element");
			String comment = element.attributeValue("comment");
			if (comment == null) {
				comment = name;
			}
			Field field = new Field();
			field.setName(name);
			field.setType(type);
			field.setGeneric(generic);
			field.setElement(elmt);
			field.setComment(comment);
			clazz.getFields().add(field);
		}
		return clazz;
	}

	private static void writeJavaFile(Clazz clazz) {
		try {
			File file = new File(generate_path + "/java/" + clazz.getName() + ".jav");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file);
			writer.write(String.format("package %s;\n\n", clazz.getPkg()));
			for (int i = 0; i < clazz.getImports().size(); i++) {
				writer.write(String.format("import %s;\n", clazz.getImports().get(i)));
			}
			writer.write("\n");
			writer.write(String.format("public class %s extends %s {\n", clazz.getName(), clazz.getParent()));
			for (int i = 0; i < clazz.getFields().size(); i++) {
				Field field = clazz.getFields().get(i);
				writer.write(String.format("\n\t/** %s **/", field.getComment()));
				if (field.getType().equals("List")) {
					String generic = toJavaType(field.getGeneric());
					writer.write(String.format("\n\tpublic %s<%s> %s;", field.getType(), toJavaListType(generic), field.getName()));
				} else if (field.getType().equals("Array")) {
					writer.write(String.format("\n\tpublic %s[] %s;", field.getElement(), field.getName()));
				} else {
					String type = toJavaType(field.getType());
					writer.write(String.format("\n\tpublic %s %s;", type, field.getName()));
				}
			}
			// 生成deserialization方法
			writer.write("\n");
			StringBuilder deserializationTxt = new StringBuilder();
			for (int i = 0; i < clazz.getFields().size(); i++) {
				Field field = clazz.getFields().get(i);
				if (field.getType().equals("List")) {
					String generic = toJavaType(field.getGeneric());
					if (isPackageBase(generic)) {
						javaGengericDeserialization("this", deserializationTxt, field, generic);
					} else {
						String sizeName = String.format("%sSize", field.getName());
						deserializationTxt.append(String.format("\n\t\tshort %s = msg.getShort();", sizeName));
						deserializationTxt.append(String.format("\n\t\tthis.%s = new ArrayList<%s>(%s);", field.getName(), toJavaListType(generic), sizeName));
						deserializationTxt.append(String.format("\n\t\tfor (int i = 0; i < %s; i++) {\n\t\t\tthis.%s.add(msg.get%s());\n\t\t}", sizeName, field.getName(), upperCase(generic)));
					}
				} else if (field.getType().equals("Array")) {
					String element = toJavaType(field.getElement());
					if (isPackageBase(element)) {
						javaElementDeserialization("this", deserializationTxt, field, element);
					} else {
						String sizeName = String.format("%sSize", field.getName());
						deserializationTxt.append(String.format("\n\t\tint %s = msg.getInt();", sizeName));
						deserializationTxt.append(String.format("\n\t\tthis.%s = new %s[%s];", field.getName(), element, sizeName));
						deserializationTxt.append(String.format("\n\t\tfor (int i = 0; i < %s; i++) {\n\t\t\tthis.%s[i] = msg.get%s();\n\t\t}", sizeName, field.getName(), upperCase(element)));
					}
				} else {
					String type = toJavaType(field.getType());
					if (isPackageBase(field.getType())) {
						javaPackageDeserialization("this", deserializationTxt, field, type);
					} else {
						deserializationTxt.append(String.format("\n\t\tthis.%s = msg.get%s();", field.getName(), upperCase(type)));
					}
				}
			}
			writer.write(String.format("\n\tpublic void deserialization(Message msg) {%s\n\t}", deserializationTxt.toString()));
			// 生成serialization方法
			writer.write("\n");
			StringBuilder serializationTxt = new StringBuilder();
			for (int i = 0; i < clazz.getFields().size(); i++) {
				Field field = clazz.getFields().get(i);
				if (field.getType().equals("List")) {
					String generic = toJavaType(field.getGeneric());
					if (isPackageBase(generic)) {
						javaGengericSerialization("this", serializationTxt, field, generic);
					} else {
						serializationTxt.append(String.format("\n\t\tmsg.putShort((short)%s.size());", field.getName()));
						serializationTxt.append(String.format("\n\t\tfor (int i = 0; i < %s.size(); i++) {\n\t\t\tmsg.put%s(%s.get(i));\n\t\t}", field.getName(), upperCase(generic), field.getName()));
					}
				} else if (field.getType().equals("Array")) {
					String element = toJavaType(field.getElement());
					if (isPackageBase(element)) {
						javaElementSerialization("this", serializationTxt, field, element);
					} else {
						serializationTxt.append(String.format("\n\t\tmsg.putInt((short)%s.length);", field.getName()));
						serializationTxt.append(String.format("\n\t\tfor (int i = 0; i < %s.length; i++) {\n\t\t\tmsg.put%s(%s[i]);\n\t\t}", field.getName(), upperCase(element), field.getName()));
					}
				} else {
					String type = toJavaType(field.getType());
					if (isPackageBase(field.getType())) {
						javaPackageSerialization("this", serializationTxt, field, type);
					} else {
						serializationTxt.append(String.format("\n\t\tmsg.put%s(%s);", upperCase(type), field.getName()));
					}
				}
			}
			writer.write(String.format("\n\tpublic void serialization(Message msg) {%s\n\t}", serializationTxt.toString()));
			// 生成toString方法
			writer.write("\n");
			writer.write("\n\t@Override");
			writer.write("\n\tpublic String toString() {");
			writer.write("\n\t\tStringBuilder sb = new StringBuilder();");
			for (int i = 0; i < clazz.getFields().size(); i++) {
				Field field = clazz.getFields().get(i);
				writer.write(String.format("\n\t\tsb.append(\" %s=\" + %s);", field.getName(), field.getName()));
			}
			writer.write("\n\t\treturn sb.toString();");
			writer.write("\n\t}");	// toString方法的结束括号
			writer.write("\n}");	// 类的结束括号
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void javaElementSerialization(String supername, StringBuilder txt, Field field, String generic) {
		Clazz genericClazz = readFile(generic + ".xml");
		String sizeName = String.format("%sSize", field.getName());
		txt.append(String.format("\n\t\tint %s = msg.getInt();", sizeName));
		txt.append(String.format("\n\t\tfor (int i = 0; i < %s; i++) {", sizeName));
		String instanceName = lowerCase(genericClazz.getName());
		txt.append(String.format("\n\t\t\t%s %s = new %s();"
				, genericClazz.getName(), instanceName, genericClazz.getName()));
		for (int j = 0; j < genericClazz.getFields().size(); j++) {
			Field fd = genericClazz.getFields().get(j);
			String type = toJavaType(fd.getType());
			if (isPackageBase(fd.getType())) {
				javaPackageSerialization(instanceName, txt, fd, type);
			} else {
				txt.append(String.format("\n\t\t\t%s.%s = msg.get%s();", instanceName, fd.getName(), upperCase(type)));
			}
		}
		txt.append(String.format("\n\t\t\t%s.add(%s);", field.getName(), instanceName));
		txt.append("\n\t\t}");
	}
	
	private static void csharpElementSerialization(String supername, StringBuilder txt, Field field, String generic) {
		Clazz genericClazz = readFile(generic + ".xml");
		String sizeName = String.format("%sSize", field.getName());
		txt.append(String.format("\n\t\t\tint %s = msg.Body.GetInt();", sizeName));
		txt.append(String.format("\n\t\t\tfor (int i = 0; i < %s; i++) {", sizeName));
		String instanceName = lowerCase(genericClazz.getName());
		txt.append(String.format("\n\t\t\t\t%s %s = new %s();"
				, genericClazz.getName(), instanceName, genericClazz.getName()));
		for (int j = 0; j < genericClazz.getFields().size(); j++) {
			Field fd = genericClazz.getFields().get(j);
			String type = toJavaType(fd.getType());
			if (isPackageBase(fd.getType())) {
				csharpPackageSerialization(instanceName, txt, fd, type);
			} else {
				txt.append(String.format("\n\t\t\t\t%s.%s = msg.Body.Get%s();", instanceName, fd.getName(), upperCase(type)));
			}
		}
		txt.append(String.format("\n\t\t\t\t%s.Add(%s);", field.getName(), instanceName));
		txt.append("\n\t\t\t}");
	}
	
	private static void javaElementDeserialization(String supername, StringBuilder txt, Field field, String generic) {
		Clazz genericClazz = readFile(generic + ".xml");
		String sizeName = String.format("%sSize", field.getName());
		txt.append(String.format("\n\t\tint %s = msg.getInt();", sizeName));
		txt.append(String.format("\n\t\tfor (int i = 0; i < %s; i++) {", sizeName));
		String instanceName = lowerCase(genericClazz.getName());
		txt.append(String.format("\n\t\t\t%s %s = new %s();"
				, genericClazz.getName(), instanceName, genericClazz.getName()));
		for (int j = 0; j < genericClazz.getFields().size(); j++) {
			Field fd = genericClazz.getFields().get(j);
			String type = toJavaType(fd.getType());
			if (isPackageBase(fd.getType())) {
				javaPackageDeserialization(instanceName, txt, fd, type);
			} else {
				txt.append(String.format("\n\t\t\t%s.%s = msg.get%s();", instanceName, fd.getName(), upperCase(type)));
			}
		}
		txt.append(String.format("\n\t\t\t%s.add(%s);", field.getName(), instanceName));
		txt.append("\n\t\t}");
	}
	
	private static void csharpElementDeserialization(String supername, StringBuilder txt, Field field, String generic) {
		Clazz genericClazz = readFile(generic + ".xml");
		String sizeName = String.format("%sSize", field.getName());
		txt.append(String.format("\n\t\tint %s = msg.Body.GetInt();", sizeName));
		txt.append(String.format("\n\t\tfor (int i = 0; i < %s; i++) {", sizeName));
		String instanceName = lowerCase(genericClazz.getName());
		txt.append(String.format("\n\t\t\t%s %s = new %s();"
				, genericClazz.getName(), instanceName, genericClazz.getName()));
		for (int j = 0; j < genericClazz.getFields().size(); j++) {
			Field fd = genericClazz.getFields().get(j);
			String type = toJavaType(fd.getType());
			if (isPackageBase(fd.getType())) {
				javaPackageDeserialization(instanceName, txt, fd, type);
			} else {
				txt.append(String.format("\n\t\t\t%s.%s = msg.Body.Get%s();", instanceName, fd.getName(), upperCase(type)));
			}
		}
		txt.append(String.format("\n\t\t\t%s.Add(%s);", field.getName(), instanceName));
		txt.append("\n\t\t}");
	}

	private static void javaGengericDeserialization(String supername, StringBuilder txt, Field field, String generic) {
		Clazz genericClazz = readFile(generic + ".xml");
		String sizeName = String.format("%sSize", field.getName());
		txt.append(String.format("\n\t\tshort %s = msg.getShort();", sizeName));
		txt.append(String.format("\n\t\tthis.%s = new ArrayList<%s>();", field.getName(), toJavaListType(generic)));
		txt.append(String.format("\n\t\tfor (int i = 0; i < %s; i++) {", sizeName));
		String instanceName = lowerCase(genericClazz.getName());
		txt.append(String.format("\n\t\t\t%s %s = new %s();"
				, genericClazz.getName(), instanceName, genericClazz.getName()));
		for (int j = 0; j < genericClazz.getFields().size(); j++) {
			Field fd = genericClazz.getFields().get(j);
			String type = toJavaType(fd.getType());
			if (isPackageBase(fd.getType())) {
				javaPackageDeserialization(instanceName, txt, fd, type);
			} else {
				txt.append(String.format("\n\t\t\t%s.%s = msg.get%s();", instanceName, fd.getName(), upperCase(type)));
			}
		}
		txt.append(String.format("\n\t\t\t%s.add(%s);", field.getName(), instanceName));
		txt.append("\n\t\t}");
	}
	
	private static void csharpGengericDeserialization(String supername, StringBuilder txt, Field field, String generic) {
		Clazz genericClazz = readFile(generic + ".xml");
		String sizeName = String.format("%sSize", field.getName());
		txt.append(String.format("\n\t\t\tshort %s = msg.Body.GetShort();", sizeName));
		txt.append(String.format("\n\t\t\tthis.%s = new List<%s>(%s);", field.getName(), generic, sizeName));
		txt.append(String.format("\n\t\t\tfor (int i = 0; i < %s; i++) {", sizeName));
		String instanceName = lowerCase(genericClazz.getName());
		txt.append(String.format("\n\t\t\t\t%s %s = new %s();"
				, genericClazz.getName(), instanceName, genericClazz.getName()));
		for (int j = 0; j < genericClazz.getFields().size(); j++) {
			Field fd = genericClazz.getFields().get(j);
			String type = toJavaType(fd.getType());
			if (isPackageBase(fd.getType())) {
				csharpPackageDeserialization(instanceName, txt, fd, type);
			} else {
				txt.append(String.format("\n\t\t\t\t%s.%s = msg.Body.Get%s();", instanceName, fd.getName(), upperCase(type)));
			}
		}
		txt.append(String.format("\n\t\t\t\t%s.Add(%s);", field.getName(), instanceName));
		txt.append("\n\t\t}");
	}
	
	private static void javaGengericSerialization(String supername, StringBuilder txt, Field field, String generic) {
		Clazz genericClazz = readFile(generic + ".xml");
		txt.append(String.format("\n\t\tmsg.putShort((short)%s.size());", field.getName()));
//		txt.append(String.format("\n\t\tthis.%s = new ArrayList<%s>();", field.getName(), toJavaListType(generic)));
		txt.append(String.format("\n\t\tfor (int i = 0; i < %s.size(); i++) {", field.getName()));
		for (int j = 0; j < genericClazz.getFields().size(); j++) {
			Field fd = genericClazz.getFields().get(j);
			String type = toJavaType(fd.getType());
			if (isPackageBase(fd.getType())) {
				javaPackageSerialization(field.getName() + ".get(i)", txt, fd, type);
			} else {
				txt.append(String.format("\n\t\t\tmsg.put%s(%s.get(i).%s);", upperCase(type), field.getName(), fd.getName()));
			}
		}
		txt.append("\n\t\t}");
	}
	
	private static void csharpGengericSerialization(String supername, StringBuilder txt, Field field, String generic) {
		Clazz genericClazz = readFile(generic + ".xml");
		txt.append(String.format("\n\t\t\tmsg.Body.PutShort((short)%s.Count);", field.getName()));
//		txt.append(String.format("\n\t\t\tthis.%s = new List<%s>();", field.getName(), toJavaListType(generic)));
		txt.append(String.format("\n\t\t\tfor (int i = 0; i < %s.Count; i++) {", field.getName()));
		for (int j = 0; j < genericClazz.getFields().size(); j++) {
			Field fd = genericClazz.getFields().get(j);
			String type = toJavaType(fd.getType());
			if (isPackageBase(fd.getType())) {
				csharpPackageSerialization(field.getName() + "[i]", txt, fd, type);
			} else {
				txt.append(String.format("\n\t\t\t\tmsg.Body.Put%s(%s[i].%s);", upperCase(type), field.getName(), fd.getName()));
			}
		}
		txt.append("\n\t\t\t}");
	}
	
	private static void javaPackageDeserialization(String supername, StringBuilder txt, Field field, String type) {
		Clazz genericClazz = readFile(type + ".xml");
		txt.append(String.format("\n\t\t%s.%s = new %s();"
				, supername, field.getName(), genericClazz.getName()));
		for (int j = 0; j < genericClazz.getFields().size(); j++) {
			Field fd = genericClazz.getFields().get(j);
			String fdtype = toJavaType(fd.getType());
			if (isPackageBase(fd.getType())) {
				javaPackageDeserialization(supername + "." + field.getName(), txt, fd, fdtype);
			} else {
				txt.append(String.format("\n\t\t%s.%s.%s = msg.get%s();", supername, field.getName(), fd.getName(), upperCase(fdtype)));
			}
		}
	}
	
	private static void csharpPackageDeserialization(String supername, StringBuilder txt, Field field, String type) {
		Clazz genericClazz = readFile(type + ".xml");
		txt.append(String.format("\n\t\t%s.%s = new %s();"
				, supername, field.getName(), genericClazz.getName()));
		for (int j = 0; j < genericClazz.getFields().size(); j++) {
			Field fd = genericClazz.getFields().get(j);
			String fdtype = toJavaType(fd.getType());
			if (isPackageBase(fd.getType())) {
				csharpPackageDeserialization(supername + "." + field.getName(), txt, fd, fdtype);
			} else {
				txt.append(String.format("\n\t\t%s.%s.%s = msg.Body.Get%s();", supername, field.getName(), fd.getName(), upperCase(fdtype)));
			}
		}
	}
	
	private static void javaPackageSerialization(String supername, StringBuilder deserializationTxt, Field field, String type) {
		Clazz genericClazz = readFile(type + ".xml");
		for (int j = 0; j < genericClazz.getFields().size(); j++) {
			Field fd = genericClazz.getFields().get(j);
			String fdtype = toJavaType(fd.getType());
			if (isPackageBase(fd.getType())) {
				javaPackageSerialization(supername + "." + field.getName(), deserializationTxt, fd, fdtype);
			} else {
				deserializationTxt.append(String.format("\n\t\tmsg.put%s(%s.%s.%s);", upperCase(fdtype), supername, field.getName(), fd.getName()));
			}
		}
	}
	
	private static void csharpPackageSerialization(String supername, StringBuilder deserializationTxt, Field field, String type) {
		Clazz genericClazz = readFile(type + ".xml");
		for (int j = 0; j < genericClazz.getFields().size(); j++) {
			Field fd = genericClazz.getFields().get(j);
			String fdtype = toJavaType(fd.getType());
			if (isPackageBase(fd.getType())) {
				csharpPackageSerialization(supername + "." + field.getName(), deserializationTxt, fd, fdtype);
			} else {
				deserializationTxt.append(String.format("\n\t\t\tmsg.Body.Put%s(%s.%s.%s);", upperCase(fdtype), supername, field.getName(), fd.getName()));
			}
		}
	}

	private static String toJavaType(String type) {
		String result;
		if ("boolean".equals(type)) {
			result = "boolean";
		} else if ("byte".equals(type)) {
			result = "byte";
		} else if ("short".equals(type)) {
			result = "short";
		} else if ("int".equals(type)) {
			result = "int";
		} else if ("long".equals(type)) {
			result = "long";
		} else if ("float".equals(type)) {
			result = "float";
		} else if ("string".equals(type)) {
			result = "String";
		} else {
			result = type;
		}
		return result;
	}
	
	private static String toJavaListType(String type) {
		String result;
		if ("boolean".equals(type)) {
			result = "Boolean";
		} else if ("byte".equals(type)) {
			result = "Byte";
		} else if ("short".equals(type)) {
			result = "Short";
		} else if ("int".equals(type)) {
			result = "Integer";
		} else if ("long".equals(type)) {
			result = "Long";
		} else if ("float".equals(type)) {
			result = "Float";
		} else if ("string".equals(type)) {
			result = "String";
		} else {
			result = type;
		}
		return result;
	}
	
	private static boolean isPackageBase(String type) {
		boolean result;
		if ("boolean".equals(type)) {
			result = false;
		} else if ("byte".equals(type)) {
			result = false;
		} else if ("short".equals(type)) {
			result = false;
		} else if ("int".equals(type)) {
			result = false;
		} else if ("long".equals(type)) {
			result = false;
		} else if ("float".equals(type)) {
			result = false;
		} else if ("string".equals(type)) {
			result = false;
		} else {
			result = true;
		}
		return result;
	}
	
	private static void writeCSharpFile(Clazz clazz) {
		try {
			File file = new File(generate_path + "/csharp/" + clazz.getName() + ".cs");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file);
			for (int i = 0; i < clazz.getUsings().size(); i++) {
				if (i > 0) {
					writer.write("\n");
				}
				writer.write(String.format("using %s;", clazz.getUsings().get(i)));
			}
			writer.write("\n");
			writer.write(String.format("\nnamespace %s\n{", clazz.getNamespace()));
			writer.write("\n");
			writer.write(String.format("\tpublic class %s : %s \n\t{", clazz.getName(), clazz.getParent()));
			for (int i = 0; i < clazz.getFields().size(); i++) {
				Field field = clazz.getFields().get(i);
				writer.write(String.format("\n\t\t/** %s **/", field.getComment()));
				if (field.getType().equals("List")) {
					writer.write(String.format("\n\t\tpublic %s<%s> %s;", field.getType(), field.getGeneric(), field.getName()));
				} else if (field.getType().equals("Array")) {
					writer.write(String.format("\n\t\tpublic %s[] %s;", field.getElement(), field.getName()));
				} else if (field.getType().equals("boolean")) {
					writer.write(String.format("\n\t\tpublic %s %s;", "bool", field.getName()));
				} else {
					writer.write(String.format("\n\t\tpublic %s %s;", field.getType(), field.getName()));
				}
			}
			
			// 生成deserialization方法
			writer.write("\n");
			StringBuilder deserializationTxt = new StringBuilder();
			for (int i = 0; i < clazz.getFields().size(); i++) {
				Field field = clazz.getFields().get(i);
				if (field.getType().equals("List")) {
					String generic = toJavaType(field.getGeneric());
					if (isPackageBase(generic)) {
						csharpGengericDeserialization("this", deserializationTxt, field, generic);
					} else {
						String sizeName = String.format("%sSize", field.getName());
						deserializationTxt.append(String.format("\n\t\t\tshort %s = msg.Body.GetShort();", sizeName));
						deserializationTxt.append(String.format("\n\t\t\tfor (int i = 0; i < %s; i++) {\n\t\t\t\tthis.%s.Add(msg.Body.Get%s());\n\t\t\t\t}", sizeName, field.getName(), upperCase(generic)));
					}
				} else if (field.getType().equals("Array")) {
					String element = toJavaType(field.getElement());
					if (isPackageBase(element)) {
						csharpElementDeserialization("this", deserializationTxt, field, element);
					} else {
						String sizeName = String.format("%sSize", field.getName());
						deserializationTxt.append(String.format("\n\t\tint %s = msg.Body.GetInt();", sizeName));
						deserializationTxt.append(String.format("\n\t\tthis.%s = new %s[%s];", field.getName(), element, sizeName));
						deserializationTxt.append(String.format("\n\t\tfor (int i = 0; i < %s; i++) {\n\t\t\tthis.%s[i] = msg.Body.Get%s();\n\t\t\t}", sizeName, field.getName(), upperCase(element)));
					}
				} else {
					String type = toJavaType(field.getType());
					if (isPackageBase(field.getType())) {
						csharpPackageDeserialization("this", deserializationTxt, field, type);
					} else {
						deserializationTxt.append(String.format("\n\t\t\tthis.%s = msg.Body.Get%s();", field.getName(), upperCase(type)));
					}
				}
			}
			writer.write(String.format("\n\t\tpublic void deserialization(Message msg) {%s\n\t\t}", deserializationTxt.toString()));
			// 生成serialization方法
			writer.write("\n");
			StringBuilder serializationTxt = new StringBuilder();
			for (int i = 0; i < clazz.getFields().size(); i++) {
				Field field = clazz.getFields().get(i);
				if (field.getType().equals("List")) {
					String generic = toJavaType(field.getGeneric());
					if (isPackageBase(generic)) {
						csharpGengericSerialization("this", serializationTxt, field, generic);
					} else {
						serializationTxt.append(String.format("\n\t\t\t%s = new ArrayList<%s>();", field.getName(), field.getType()));
						serializationTxt.append(String.format("\n\t\t\tmsg.Body.PutShort((short)%s.Count);", field.getName()));
						serializationTxt.append(String.format("\n\t\t\tfor (int i = 0; i < %s.Count; i++) {\n\t\t\t\tmsg.Body.Put%s(%s[i]);\n\t\t\t\t}", field.getName(), upperCase(generic), field.getName()));
					}
				} else if (field.getType().equals("Array")) {
					String element = toJavaType(field.getElement());
					if (isPackageBase(element)) {
						csharpElementSerialization("this", serializationTxt, field, element);
					} else {
						serializationTxt.append(String.format("\n\t\t\tmsg.Body.PutInt((short)%s.Length);", field.getName()));
						serializationTxt.append(String.format("\n\t\t\tfor (int i = 0; i < %s.Length; i++) {\n\t\t\t\tmsg.Body.Put%s(%s[i]);\n\t\t\t\t}", field.getName(), upperCase(element), field.getName()));
					}
				} else {
					String type = toJavaType(field.getType());
					if (isPackageBase(field.getType())) {
						csharpPackageSerialization("this", serializationTxt, field, type);
					} else {
						serializationTxt.append(String.format("\n\t\t\tmsg.Body.Put%s(%s);", upperCase(type), field.getName()));
					}
				}
			}
			writer.write(String.format("\n\t\tpublic void serialization(Message msg) {%s\n\t\t}", serializationTxt.toString()));			
			// 生成toString方法
			writer.write("\n");
			writer.write("\n\t\tpublic override String ToString() \n\t\t{");
			writer.write("\n\t\t\tStringBuilder sb = new StringBuilder();");
			writer.write(String.format("\n\t\t\tsb.Append(\"%s[\");", clazz.getName()));
			for (int i = 0; i < clazz.getFields().size(); i++) {
				Field field = clazz.getFields().get(i);
				writer.write(String.format("\n\t\t\tsb.Append(\" %s=\" + %s);", field.getName(), field.getName()));
			}
			writer.write("\n\t\t\tsb.Append(\"]\");");
			writer.write("\n\t\t\treturn sb.ToString();");
			writer.write("\n\t\t}");	// toString方法的结束括号
			writer.write("\n\t}");	// 类的结束括号
			writer.write("\n}");	// namespace的结束括号
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String upperCase(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	private static String lowerCase(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

}
