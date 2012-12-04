package piggene;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.resource.Directory;
import org.restlet.routing.Redirector;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.routing.TemplateRoute;

import piggene.resources.GetJobs;

public class WebApp extends Application {

	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public synchronized Restlet createRoot() {
		// Create a router Restlet that routes each call to a
		Router router = new Router(getContext());
		String target = "riap://host/index.html";
		Redirector redirector = new Redirector(getContext(), target,
				Redirector.MODE_SERVER_OUTBOUND);
		TemplateRoute route = router.attach("/", redirector);
		route.setMatchingMode(Template.MODE_EQUALS);

		//routes
		router.attach("/jobs", GetJobs.class);
		
		
		// clap protocol for usage in jar files
		Directory dir = new Directory(getContext(), new LocalReference(
				"clap://thread/web"));
		dir.setListingAllowed(false);

		route = router.attach("/", dir);
		route.setMatchingMode(Template.MODE_STARTS_WITH);

		return router;
	}

}