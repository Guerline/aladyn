package aladyn.server;

import java.lang.reflect.Field;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
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

	public BuildObject(String nameClass) {
		pool = ClassPool.getDefault();
		buildClass = pool.makeClass(nameClass);
	}
	
	public void addField(String srcField) {
		checkFrozen();
		CtField field;
		
		try {
			field =  CtField.make(srcField, buildClass) ;
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
