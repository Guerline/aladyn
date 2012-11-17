package aladyn.client;

public  class BuildCall {
	
	
	protected String buildParams( Object[] objects){
		String s="";
		for (Object object: objects) {
			s += "<param>\n" + Serializer.serialize(object) + "</param>\n";
		}
		return "<params>\n" + s + "</params>\n";
	}
	
	protected String buildMethod( String method){
		return "<methodName>"+ method + "</methodName>\n";
	}
	
	public String buildMethodCall(String method, Object [] objects){
		String s="";
		s += buildMethod(method);
		s += buildParams(objects);
		return "<methodCall>\n" + s + "</methodCall>";
	}
}
