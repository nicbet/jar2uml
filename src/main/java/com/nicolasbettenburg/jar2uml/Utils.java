package com.nicolasbettenburg.jar2uml;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.Type;
import org.apache.commons.lang3.StringUtils;

public class Utils {

	public static String getPlantUMLMethodArguments(Method m) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		List<Type> argsList = Arrays.asList(m.getArgumentTypes());
		List<String> argsListStr = argsList.stream().map(elt -> elt.toString()).collect(Collectors.toList());
		List<String> simpleArgsList = argsListStr.stream().map(elt -> simpleName(elt)).collect(Collectors.toList());
		sb.append(StringUtils.join(simpleArgsList, ", "));
		sb.append(")");
		return sb.toString();
	}

	public static String getPlantUMLMethodName(Method m, String cname) {
		String mName = m.getName();
		if (mName.equalsIgnoreCase("<init>")) {
			return simpleName(cname);
		} else if (mName.equalsIgnoreCase("<clinit>")) {
			return "{static} " + simpleName(cname);
		}
		return mName;
	}

	public static String getPlantUMLStaticForMethod(Method m) {
		if (m.isStatic()) return "{static}";
		return "";
	}

	public static String getPlantUMLModifierForAccess(AccessFlags x) {
		if (x.isPrivate()) return "-";
		if (x.isProtected()) return "#";
		if (x.isPublic()) return "+";
		return "~";
	}
	
	public static String simpleName(String s) {
		if (s.contains(".")) {
			return s.substring(s.lastIndexOf('.')+1);
		} else {
			return s;
		}
	}
	
}
