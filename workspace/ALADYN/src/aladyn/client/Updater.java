package aladyn.client;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import aladyn.XMLRMISerializable;

public class Updater {
	public static void setValue (Field f, Object obj, Object paramValue){
		Class<?> type = f.getType();

		if ( type.equals(Integer.class) || type.equals(int.class))
		{
			try {
				 f.setInt(obj, (Integer)paramValue);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(type.equals(Short.class) || type.equals(short.class) ) {
			try {
				 f.setShort(obj, (Short)paramValue);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (type.equals(Boolean.class) || type.equals(boolean.class) ) 
		{
			try {
				 f.setBoolean(obj, (Boolean)paramValue);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (type.equals(Double.class) || type.equals(double.class)) 
		{
			try {
				 f.setDouble(obj, (Double)paramValue);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (type.equals(Float.class) || type.equals(float.class) ) 
		{
			try {
				 f.setFloat(obj, (Float)paramValue);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (obj instanceof Date || obj instanceof Calendar)
		{
			//A FAIRE!!!
			//f.se
		}
		else if (type.equals(Character.class) || type.equals(char.class)) 
		{
			try {
				f.setChar(obj, String.valueOf(paramValue).toCharArray()[0] );
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (type.equals(Byte.class) || type.equals(byte.class)) 
		{
			try {
				f.setByte(obj,(Byte) paramValue);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (type.equals(Long.class) || type.equals(long.class)) 
		{
			try {
				f.setLong(obj, (Long) paramValue);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (type.equals(String.class) )
		{
			try {
				f.set(obj,  paramValue);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (type.equals(String.class) )
		{
			try {
				f.set(obj,  paramValue);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else	
		{
			try {
				f.set(obj, paramValue);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
