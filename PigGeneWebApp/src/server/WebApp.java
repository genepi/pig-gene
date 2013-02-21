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
import piggene.resources.DeserialisationProcessor;
import piggene.resources.ScriptProvider;
import piggene.resources.SerialisationProcessor;
import piggene.resources.WorkflowFinder;
import piggene.resources.WorkflowPresenter;

public class WebApp extends Application {

	/**
	 * Creates a root Restlet that will receive all incoming calls.
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
		router.attach("/ser", SerialisationProcessor.class);
		router.attach("/ld", DeserialisationProcessor.class);
		router.attach("/del", DeletionService.class);
		router.attach("/wf", WorkflowPresenter.class);
		router.attach("/ex", WorkflowFinder.class);
		router.attach("/dwld/{filename}", ScriptProvider.class);

		// clap protocol for usage in jar files
		final Directory dir = new Directory(getContext(), new LocalReference("clap://thread/web"));
		dir.setListingAllowed(false);

		route = router.attach("/", dir);
		route.setMatchingMode(Template.MODE_STARTS_WITH);

		return router;
	}

}