package client;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

//Classe permettant de setter la valeur d'un champ
public class Updater {
	
	/**
	 * place la valeur correspondant à un champ
	 * @param f le champ à setter
	 * @param obj l'objet
	 * @param paramValue la valeur du champ
	 */
	public static void setValue(Field f, Object obj, Object paramValue){
		Class<?> type = f.getType();

		if(type.equals(Integer.class) || type.equals(int.class)) {
			try {
				f.setInt(obj, (Integer)paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else if(type.equals(Short.class) || type.equals(short.class)) {
			try {
				f.setShort(obj, (Short)paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else if(type.equals(Boolean.class) || type.equals(boolean.class)) {
			try {
				f.setBoolean(obj, (Boolean)paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else if (type.equals(Double.class) || type.equals(double.class)) {
			try {
				f.setDouble(obj, (Double)paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else if (type.equals(Float.class) || type.equals(float.class)) {
			try {
				f.setFloat(obj, (Float)paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else if (obj instanceof Date || obj instanceof Calendar) {
			//A FAIRE!!!
			//f.se
		}else if (type.equals(Character.class) || type.equals(char.class)) {
			try {
				f.setChar(obj, String.valueOf(paramValue).toCharArray()[0]);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else if (type.equals(Byte.class) || type.equals(byte.class)) {
			try {
				f.setByte(obj,(Byte) paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else if (type.equals(Long.class) || type.equals(long.class)) {
			try {
				f.setLong(obj, (Long) paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else if (type.equals(String.class)) {
			try {
				f.set(obj,paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else {
			try {
				f.set(obj, paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}
	}
}