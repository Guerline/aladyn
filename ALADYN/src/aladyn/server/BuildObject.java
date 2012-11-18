package aladyn.server;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtField.Initializer;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Loader;
import javassist.Modifier;
import javassist.NotFoundException;

public class BuildObject {
	private CtClass buildClass;
	private Class<?> myClass = null;
	private boolean isCreated = false;
	private ClassPool pool;
	private HashMap<String,CtClass> hm;

	public BuildObject(String nameClass) {
		pool = ClassPool.getDefault();
		buildClass = pool.makeClass(nameClass);
	}
	
	public void addIntField(String fieldName,int i) {
		checkFrozen();
		CtField field;
		try {
			field = new  CtField(CtClass.intType ,fieldName, buildClass) ;
			buildClass.addField(field, CtField.Initializer.constant(i));
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addBoolField(String fieldName,boolean b) {
		checkFrozen();
		CtField field;
		try {
			field = new  CtField(CtClass.booleanType,fieldName, buildClass) ;
			buildClass.addField(field, CtField.Initializer.constant(b));
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void addStringField(String fieldName,String s) {
		checkFrozen();
		CtField field;
		try {
			field = CtField.make("String " + fieldName + "= \"" + s +"\";", buildClass) ;
			field.setModifiers(Modifier.PUBLIC);
			buildClass.addField(field);
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void addDoubleField(String fieldName,double d) {
		checkFrozen();
		CtField field;
		try {
			field = new  CtField(CtClass.doubleType,fieldName, buildClass) ;
			buildClass.addField(field,CtField.Initializer.constant(d));
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void addDateField(String fieldName, String date) {
		checkFrozen();
		CtField field;
		try {
			field = CtField.make("java.util.GregorianCalendar " + fieldName + " = new java.util.GregorianCalendar("+ date + ");",  buildClass);
			buildClass.addField(field);
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void addField(String fieldName,String type,Initializer init) {
		checkFrozen();
		CtField field;
		try {
			field = new  CtField(hm.get(type),fieldName, buildClass) ;
			buildClass.addField(field);
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void addField(String fieldName, CtClass type,Initializer init) {
		checkFrozen();
		CtField field;
		
		try {
			field = new  CtField(type,fieldName, buildClass) ;
			buildClass.addField(field);
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void addMethod(String srcMethod) {
		checkFrozen();
		CtMethod method;
		try {
			method = CtNewMethod.make(srcMethod, buildClass);
			buildClass.addMethod(method);
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
	}

	public void addConstructor(String srcConstructor) {
		checkFrozen();
		CtConstructor constructor;
		try {
			constructor = CtNewConstructor.make(srcConstructor, buildClass);
			buildClass.addConstructor(constructor);
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
	}

	public void addInterface(Class<?> inter) {
		checkFrozen();
		ClassPool pool = ClassPool.getDefault();
		CtClass ctInter = null;
	
		try {
			ctInter = pool.get(inter.getName());
			buildClass.addInterface(ctInter);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addSuperClass(String inter) {
		checkFrozen();
		ClassPool pool = ClassPool.getDefault();
		CtClass ctInter = null;
	
		try {
			ctInter = pool.get(inter);
			buildClass.setSuperclass(ctInter);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

public Class<?> getMyClass() {
		checkFrozen();
		if(isCreated) {
			return(myClass);
		}
		isCreated = true;
		try {
			myClass = buildClass.toClass();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Object o = myClass.newInstance();
		
				System.out.println(o.toString());
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(myClass.toString());
		/*for(int i = 0; i < myClass.getDeclaredFields().length; i++)
		{
			System.out.println(myClass.getDeclaredFields()[i]);
		}*/
		for(int i = 0; i < buildClass.getDeclaredMethods().length; i++)
		{
			System.out.println(buildClass.getDeclaredMethods()[i]);
		}
		for(int i = 0; i < myClass.getInterfaces().length; i++)
		{
			System.out.println(myClass.getInterfaces()[i]);
		}
		return(myClass);
	}

	public void checkFrozen() {
		if (buildClass.isFrozen()) {
			buildClass.defrost();
		}
	}

}
