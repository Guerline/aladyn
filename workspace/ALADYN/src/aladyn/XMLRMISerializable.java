package aladyn;


public interface XMLRMISerializable  {

	public String toXML();
	public void updateFromXML(org.w3c.dom.Element theXML);
}
