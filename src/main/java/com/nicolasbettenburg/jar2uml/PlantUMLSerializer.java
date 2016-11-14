package com.nicolasbettenburg.jar2uml;

import java.util.Collection;

import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

public class PlantUMLSerializer {

	public static String diagram(String content) {
		StringBuilder sb = new StringBuilder();
		sb.append("@startuml\n");
		sb.append("\tleft to right direction\n\n");
		sb.append(content);
		sb.append("@enduml\n");
		return sb.toString();
	}
	
	public static String serializeClasses(Collection<JavaClass> classes) {
		StringBuilder sb = new StringBuilder();
		
		for (JavaClass c: classes) {
			sb.append(serializeJavaClass(c));
		}
		
		return sb.toString();
	}
	
	public static String serializeJavaClass(JavaClass c) {
		StringBuilder sb = new StringBuilder();

		sb.append("\t");
		
		// Class, Interface, Enum or Annotation?
		if (c.isClass()) {
			// Abstract Class?
			if (c.isAbstract()) {
				sb.append("abstract ");
			}
			sb.append("class ");
		} else if (c.isInterface()) {
			sb.append("interface ");
		} else if (c.isEnum()) {
			sb.append("enum ");
		} else if (c.isAnnotation()) {
			sb.append("annotation ");
		} else {
			return "";
		}
		
		// Class Full-Qualified Name
		sb.append(c.getClassName());
		
		// Body
		sb.append(" {\n");
		
		// All Fields
		for (Field f : c.getFields()) {
			sb.append(serializeField(f, c));
		}
		
		// All Methods
		for (Method m : c.getMethods()) {
        	sb.append(serializeMethod(m, c));
        }
		
		// End Body
		sb.append("\t}\n\n");
		
		return sb.toString();
	}

	private static String serializeField(Field f, JavaClass c) {
		StringBuilder sb = new StringBuilder();
		sb.append("\t\t");
		sb.append(Utils.getPlantUMLModifierForAccess(f));
		sb.append(" ");
		if (f.isStatic()) {
			sb.append("{static} ");
		}
		sb.append(f.getName());
		sb.append(" : " + Utils.simpleName(f.getType().toString()));
		sb.append("\n");
		return sb.toString();
	}

	public static String serializeMethod(Method m, JavaClass c) {
		StringBuilder sb = new StringBuilder();

		sb.append("\t\t");
		sb.append(Utils.getPlantUMLModifierForAccess(m));
		sb.append(" ");
		if (m.isStatic()) {
			sb.append(Utils.getPlantUMLStaticForMethod(m));
			sb.append(" ");
		}
		sb.append(Utils.getPlantUMLMethodName(m, c.getClassName()));
		sb.append(Utils.getPlantUMLMethodArguments(m));
		sb.append(" : " +Utils.simpleName(m.getReturnType().toString()));
		sb.append("\n");
		

		return sb.toString();
	}
	
	public static String serializeInheritance(Collection<JavaClass> classes) {
		StringBuilder sb = new StringBuilder();
		for (JavaClass c: classes) {
			sb.append(serializeJavaClassInheritance(c));
		}
		return sb.toString();
	}

	private static String serializeJavaClassInheritance(JavaClass c) {
		StringBuilder sb = new StringBuilder();
		try {
			
			String s = c.getSuperclassName();
			if (!s.equalsIgnoreCase("java.lang.Object")) { 
				sb.append("\t");
				sb.append(c.getClassName() + " --|> " + s);
				sb.append("\n");
			}
			
			for (String i : c.getInterfaceNames()) {
				sb.append("\t");
				sb.append(c.getClassName() + " ..|> " + i);
				sb.append("\n");
			}
		} catch (Exception e) {
			// NOP
		}
		return sb.toString();
	}
	
}
