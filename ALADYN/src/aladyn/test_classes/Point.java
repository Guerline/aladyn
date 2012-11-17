package aladyn.test_classes;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import aladyn.XMLRMIField;
import aladyn.XMLRMISerializable;
import aladyn.client.Serializer;
import aladyn.client.Updater;


public class Point implements XMLRMISerializable {
	@XMLRMIField(serializationName="a",serializationType="double")
	protected double a ;
	@XMLRMIField(serializationName="b",serializationType="double")
	protected double b ;
	@XMLRMIField(serializationName="marque",serializationType="string")
	protected String marque ;
	/*@XMLRMIField(serializationName="next",serializationType="object")
	protected Point next;*/
	
	public Point(double a, double b, String marque){
		this.a=a;
		this.b=b;
		this.marque=marque;
		//this.next=next;
	}
	
	public double getA(){
		return a;
	}


	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		StringBuilder res = new StringBuilder();
		String res_fields="",res_methods="", name,intR,type, serializedValue;
		Annotation[] annotations;
		Class<?> innerClass = this.getClass();
		
		Field[] inter_fields= innerClass.getDeclaredFields();
		
		for(Field field : inter_fields)
		{
			field.setAccessible(true);
			annotations = field.getDeclaredAnnotations();
			intR="";
			for(Annotation annotation : annotations)
			{
				if(annotation instanceof XMLRMIField)
				{
					intR="";
					XMLRMIField myAnnotation = (XMLRMIField) annotation;
					name= myAnnotation.serializationName();
					type= myAnnotation.serializationType();
					System.out.println(name);
					try {
						// ADd the type in the params and cast it inside the factory
						
							System.out.println(field.getType().toString());
							try {
								serializedValue = Serializer.serialize(field.get(this));
								intR="\t<field name=\""+ name +"\">" + serializedValue + "</field>\n";
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						
					} 
					catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
				res_fields+= intR;
			}
			
		}
		res_fields = "<fields>\n" + res_fields + "</fields>\n";
		res.append(res_fields) ;
		
		//Block for methods
		
		res_methods = "<method language=\"java\">public String toString() { a=3.3 +b; return String.valueOf(a); }</method> ";
		res_methods = "<methods>\n" + res_methods + "</methods>\n";
		res.append(res_methods);
		/*
		 * Needs to add object key and tag
		 */
		return "<object oid=\"100\">\n" + res + "</object>\n";
	}

	@Override
	public void updateFromXML(Element theXML) {
		NodeList fields = theXML.getElementsByTagName("fields");
		
		Element fields_element = (Element) fields.item(0);
		NodeList fieldNodes = fields_element.getElementsByTagName("field");
		
		for (int i = 0; i < fieldNodes.getLength(); i++) {
			
			Element elementField = (Element) fieldNodes.item(i);
			NamedNodeMap attributes= elementField.getAttributes();
			Attr nodeAttr = (Attr)attributes.item(0);

			// Get the new value of the object
			Element  elementValue = (Element)elementField.getElementsByTagName("value").item(0);
			NodeList valueChilds = elementValue.getChildNodes();
			Element valueTypeElement = (Element)valueChilds.item(0);
			//Update the field
			//Remember to set all field accessible
			Object paramValue = Serializer.getValueFromElement(valueTypeElement);
			Field f1;
			try 
			{
				f1 = this.getClass().getDeclaredField(nodeAttr.getValue().toString());
				f1.setAccessible(true);
				Updater.setValue(f1, this, paramValue);
				
			}
			catch (NoSuchFieldException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (SecurityException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalArgumentException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
