package piggene.resources;

import net.sf.json.JSONObject;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class GetJobs extends ServerResource {
	
	@Post
	public Representation post(Representation entity) {
//		Form form = new Form(entity);
//		String name = (String) form.getFirstValue("name");
//		return new StringRepresentation("Hallo " + name);
		
		
		MyTestObject o = new MyTestObject();
		o.setRel("a");
		o.setOper("FILTER");
		o.setRel2("b");
		o.setOpt("<3");
		
		JSONObject object = JSONObject.fromObject(o);
		return new StringRepresentation(object.toString(),MediaType.APPLICATION_JSON);
	}
	
}