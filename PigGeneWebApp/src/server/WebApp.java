package server;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.resource.Directory;
import org.restlet.routing.Redirector;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.routing.TemplateRoute;

import piggene.resources.DeletionService;
import piggene.resources.DeserialisationService;
import piggene.resources.MyRestTest;
import piggene.resources.ScriptProvider;
import piggene.resources.SerialisationService;
import piggene.resources.WorkflowFinder;
import piggene.resources.WorkflowPresenter;

/**
 * WebApp class.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class WebApp extends Application {

	/**
	 * Creates a root restlet that will receive and foreward all incoming calls.
	 */
	@Override
	public synchronized Restlet createRoot() {

		// Create a router Restlet that routes each call
		final Router router = new Router(getContext());
		final String target = "riap://host/index_new.html";
		final Redirector redirector = new Redirector(getContext(), target, Redirector.MODE_SERVER_OUTBOUND);
		TemplateRoute route = router.attach("/", redirector);
		route.setMatchingMode(Template.MODE_EQUALS);

		// routes
		router.attach("/workflow/{id}", MyRestTest.class);

		router.attach("/save", SerialisationService.class);
		router.attach("/ld", DeserialisationService.class);
		router.attach("/del", DeletionService.class);
		router.attach("/get", WorkflowPresenter.class);
		router.attach("/exist", WorkflowFinder.class);
		router.attach("/dwld/{filename}", ScriptProvider.class);

		// clap protocol for usage in jar files
		final Directory dir = new Directory(getContext(), new LocalReference("clap://thread/web"));
		dir.setListingAllowed(false);

		route = router.attach("/", dir);
		route.setMatchingMode(Template.MODE_STARTS_WITH);
		return router;
	}

}