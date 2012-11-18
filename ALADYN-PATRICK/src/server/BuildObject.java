package server;

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
import javassist.Modifier;
import javassist.NotFoundException;

//Classe de recontruciton d'un objet depuis un XML
public class BuildObject {
	private CtClass buildClass;
	private Class<?> myClass = null;
	private boolean isCreated = false;
	private ClassPool pool;
	private HashMap<String, CtClass> hm;

	/**
	 * methode pour la reconstruction de l'objet
	 * @param nameClass le nom de la classe a recontruire
	 */
	public BuildObject(String nameClass) {
		pool = ClassPool.getDefault();
		buildClass = pool.makeClass(nameClass);
	}
	
	/**
	 * Verification si un objet est gelé dans le pool
	 */
	public void checkFrozen() {
		if(buildClass.isFrozen()) {
			buildClass.defrost();
		}
	}
	
	/**
	 * methode pour ajouter un champ à l'objet à reconstruire
	 * @param fieldName le nom du champ pour l'objet à recontruire
	 * @param i la valeur du champ
	 */
	public void addIntField(String fieldName, int i) {
		CtField field;
		
		checkFrozen();
		try {
			field = new CtField(CtClass.intType, fieldName, buildClass);
			buildClass.addField(field, CtField.Initializer.constant(i));
		}catch(CannotCompileException e) {
			System.err.println(e + "error building the field");
		}
	}
	
	/**
	 * methode pour ajouter un champ à l'objet à reconstruire
	 * @param fieldName le nom du champ pour l'objet à recontruire
	 * @param i la valeur du champ
	 */
	public void addBoolField(String fieldName, boolean b) {
		CtField field;
		
		checkFrozen();
		try {
			field = new CtField(CtClass.booleanType,fieldName, buildClass);
			buildClass.addField(field, CtField.Initializer.constant(b));
		}catch(CannotCompileException e) {
			System.err.println(e + "error building the field");
		}
		
	}
	
	/**
	 * methode pour ajouter un champ à l'objet à reconstruire
	 * @param fieldName le nom du champ pour l'objet à recontruire
	 * @param s la valeur du champ
	 */
	public void addStringField(String fieldName, String s) {
		CtField field;
		
		checkFrozen();
		try {
			field = CtField.make("String " + fieldName + "= \"" + s +"\";", buildClass);
			field.setModifiers(Modifier.PUBLIC);
			buildClass.addField(field);
		}catch(CannotCompileException e) {
			System.err.println(e + "error building the field");
		}
		
	}
	
	/**
	 * methode pour ajouter un champ à l'objet à reconstruire
	 * @param fieldName le nom du champ pour l'objet à recontruire
	 * @param d la valeur du champ
	 */
	public void addDoubleField(String fieldName, double d) {
		CtField field;
		
		checkFrozen();
		try {
			field = new CtField(CtClass.doubleType, fieldName, buildClass);
			buildClass.addField(field, CtField.Initializer.constant(d));
		}catch(CannotCompileException e) {
			System.err.println(e + "error building the field");
		}
		
	}
	
	/**
	 * methode pour ajouter un champ à l'objet à reconstruire
	 * @param fieldName le nom du champ pour l'objet à recontruire
	 * @param type la valeur du champ
	 * @param year l'année de la date
	 * @param mounth le mois de la date
	 * @param date le jour de la date
	 * @param hour l'heure de la date
	 * @param minutes les minutes de la date
	 * @param seconds les secondes de la date
	 */
	public void addDateField(String fieldName, String date) {
		CtField field;
		
		checkFrozen();
		try {
			field = CtField.make("java.util.GregorianCalendar " + fieldName + " = new java.util.GregorianCalendar("+ date + ");",  buildClass);
			buildClass.addField(field);
		}catch(CannotCompileException e) {
			System.err.println(e + "error building the field");
		}
	}
	
	/**
	 * methode pour ajouter un champ à l'objet à reconstruire
	 * @param fieldName le nom du champ pour l'objet à recontruire
	 * @param type le type du champ
	 * @param init l'initialisaion du champ
	 */
	public void addField(String fieldName, String type, Initializer init) {
		CtField field;
		
		checkFrozen();
		try {
			field = new CtField(hm.get(type), fieldName, buildClass) ;
			buildClass.addField(field);
		}catch(CannotCompileException e) {
			System.err.println(e + "error building the field");
		}
	}
	
	/**
	 * methode pour ajouter un champ à l'objet à reconstruire
	 * @param fieldName le nom du champ pour l'objet à recontruire
	 * @param type le type du champ
	 * @param init l'initialisaion du champ 
	 */
	public void addField(String fieldName, CtClass type, Initializer init) {
		CtField field;
		
		checkFrozen();
		try {
			field = new CtField(type,fieldName, buildClass) ;
			buildClass.addField(field);
		}catch(CannotCompileException e) {
			System.err.println(e + "error building the field");
		}
	}

	/**
	 * methode pour ajouter une methode à l'objet à reconstruire
	 * @param srcMethod la source de la methode
	 */
	public void addMethod(String srcMethod) {
		CtMethod method;
		
		checkFrozen();
		try {
			method = CtNewMethod.make(srcMethod, buildClass);
			buildClass.addMethod(method);
		}catch(CannotCompileException e) {
			System.err.println(e + "error building the method");
		}
	}

	/**
	 * methode pour ajouter un constructeur à l'objet
	 * @param srcConstructor la source du constructeur
	 */
	public void addConstructor(String srcConstructor) {
		CtConstructor constructor;
		
		checkFrozen();
		try {
			constructor = CtNewConstructor.make(srcConstructor, buildClass);
			buildClass.addConstructor(constructor);
		}catch(CannotCompileException e) {
			System.err.println(e + "error building the constructor");
		}
	}

	/**
	 * methode pour ajouter une interface à l'objet
	 * @param inter l'interface a ajouter
	 */
	public void addInterface(Class<?> inter) {
		ClassPool pool = ClassPool.getDefault();
		CtClass ctInter = null;
	
		checkFrozen();
		try {
			ctInter = pool.get(inter.getName());
			buildClass.addInterface(ctInter);
		}catch (NotFoundException e) {
			System.err.println(e + "error finding the interface");
		}
	}
	
	/**
	 * methode pour ajouter une superclasse à l'objet
	 * @param sup la superclasse de l'objet
	 */
	public void addSuperClass(String sup) {
		ClassPool pool = ClassPool.getDefault();
		CtClass ctSup = null;
		
		checkFrozen();
		try {
			ctSup = pool.get(sup);
			buildClass.setSuperclass(ctSup);
		} catch (NotFoundException e) {
			System.err.println(e + "error finding the superclasse");
		}catch(CannotCompileException e) {
			System.err.println(e + "error building the superclass");
		}
	}

	/**
	 * methode pour ajouter un champ à l'objet à reconstruire
	 * @return
	 */
	public Class<?> getMyClass() {
		checkFrozen();
		
		if(isCreated) {
			return myClass;
		}
		
		isCreated = true;
		try {
			myClass = buildClass.toClass();
		}catch(CannotCompileException e) {
			System.err.println(e + "error building the classe");
		}
		
		try {
			Object o = myClass.newInstance();
			System.out.println(o.toString());
		}catch(InstantiationException e) {
			System.err.println(e + "error initializing the classe");
		}catch(IllegalAccessException e) {
			System.err.println(e + "error accessing the classe");
		}
		System.out.println(myClass.toString());
		/*for(int i = 0; i < myClass.getDeclaredFields().length; i++)
		{
			System.out.println(myClass.getDeclaredFields()[i]);
		}*/
		for(int i = 0; i < buildClass.getDeclaredMethods().length; i++) {
			System.out.println(buildClass.getDeclaredMethods()[i]);
		}
		for(int i = 0; i < myClass.getInterfaces().length; i++) {
			System.out.println(myClass.getInterfaces()[i]);
		}
		return myClass;
	}
}


