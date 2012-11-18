package tests;



import tools.ObjectSerializable;
import xmlrmi.XMLRMIField;


public class Point extends ObjectSerializable {
	@XMLRMIField(serializationName="a",serializationType="double")
	protected double a ;
	@XMLRMIField(serializationName="b",serializationType="double")
	protected double b ;
	@XMLRMIField(serializationName="marque",serializationType="array")
	protected Integer[] marque ;
	/*@XMLRMIField(serializationName="next",serializationType="object")
	protected Point next;*/

	public Point(double a, double b, Integer[] marque){
		this.a=a;
		this.b=b;
		this.marque=marque;
		//this.next=next;
	}

	public double getA(){
		return a;
	}
}
