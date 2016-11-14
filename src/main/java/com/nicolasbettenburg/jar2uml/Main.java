package com.nicolasbettenburg.jar2uml;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;


public class Main {

	public static void main(String[] args) {
		
		// First argument is the filename
		String filename = args[0];
		
		// Read File
		JarFile jf = null;
		try {
			jf = new JarFile(filename);
			
			// Parse and serialize to PlantUML
			parseJarFile(filename, jf);
		} catch (Exception e) {
			// Emit error
			System.err.println("Error processing input file: " + filename);
			System.err.println(e.getMessage());
			e.printStackTrace();
			
			// Exit
			System.exit(1);
		} finally {
			try {
				// Cleanup when we are done
				jf.close();
			} catch (IOException e) {
				// NOP
			}
		}
	}

	private static void parseJarFile(String jarName, JarFile jarFile) {
		try {
			Map<String, JavaClass> javaClasses = collectJavaClasses(jarName, jarFile);
			
			Date now = new Date();
			System.out.println("' Created: " + now.toString());
			System.out.println("' Input: " + jarName);
			System.out.println("' Parsed " + javaClasses.size() + " classes");
			
			String classes = PlantUMLSerializer.serializeClasses(javaClasses.values());
			String inheritanceCode = PlantUMLSerializer.serializeInheritance(javaClasses.values());
			String umlCode = PlantUMLSerializer.diagram(classes + inheritanceCode);
			System.out.println(umlCode);
			
		} catch (ClassFormatException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static Map<String, JavaClass> collectJavaClasses(String jarName, JarFile jarFile) throws ClassFormatException, IOException {
	        Map<String, JavaClass> javaClasses = new LinkedHashMap<String, JavaClass>();
	        Enumeration<JarEntry> entries = jarFile.entries();
	        while (entries.hasMoreElements())
	        {
	            JarEntry entry = entries.nextElement();
	            if (!entry.getName().endsWith(".class"))
	            {
	                continue;
	            }

	            ClassParser parser = new ClassParser(jarName, entry.getName());
	            JavaClass javaClass = parser.parse();
	            javaClasses.put(javaClass.getClassName(), javaClass);
	        }
	        return javaClasses;
	    }

}
