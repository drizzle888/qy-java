package com.tool.packet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class PacketApp {
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
			try {
				System.out.println("name=" + file.getName());
				Clazz clazz = readFile(file.getName());
				clazzList.add(clazz);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < clazzList.size(); i++) {
			Clazz clazz = clazzList.get(i);
			writeJavaFile(clazz);
			writeCSharpFile(clazz);
		}
	}

	private static Clazz readFile(String fileName) throws Exception {
		Clazz clazz = new Clazz();
		File file = new File(resource_path + "/" + fileName);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(file);
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
					writer.write(String.format("\n\tpublic %s<%s> %s;", field.getType(), generic, field.getName()));
				} else if (field.getType().equals("Array")) {
					writer.write(String.format("\n\tpublic %s[] %s;", field.getElement(), field.getName()));
				} else {
					String type = toJavaType(field.getType());
					writer.write(String.format("\n\tpublic %s %s;", type, field.getName()));
				}
			}
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
		} else if ("string".equals(type)) {
			result = "String";
		} else {
			result = type;
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

}
