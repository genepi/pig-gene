package server;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.resource.Directory;
import org.restlet.routing.Redirector;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.routing.TemplateRoute;

import piggene.resources.WorkflowDeletionService;
import piggene.resources.WorkflowLoaderService;
import piggene.resources.WorkflowOverviewLoaderService;
import piggene.resources.WorkflowStorageService;
import piggene.serialisation.pig.ScriptDownloadService;

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
		final String target = "riap://host/index.html";
		final Redirector redirector = new Redirector(getContext(), target, Redirector.MODE_SERVER_OUTBOUND);
		TemplateRoute route = router.attach("/", redirector);
		route.setMatchingMode(Template.MODE_EQUALS);

		// routes
		router.attach("/wf", WorkflowOverviewLoaderService.class);
		router.attach("/wf/{id}", WorkflowLoaderService.class);
		router.attach("/save/wf", WorkflowStorageService.class);
		router.attach("/del/{id}", WorkflowDeletionService.class);
		router.attach("/dwnld/{id}", ScriptDownloadService.class);

		// clap protocol for usage in jar files
		final Directory dir = new Directory(getContext(), new LocalReference("clap://thread/web"));
		dir.setListingAllowed(false);

		route = router.attach("/", dir);
		route.setMatchingMode(Template.MODE_STARTS_WITH);
		return router;
	}

}