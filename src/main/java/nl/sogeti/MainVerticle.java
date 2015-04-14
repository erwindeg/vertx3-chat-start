package nl.sogeti;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoService;
import io.vertx.ext.mongo.MongoServiceVerticle;
import io.vertx.ext.sockjs.BridgeOptions;
import io.vertx.ext.sockjs.SockJSServer;
import io.vertx.ext.sockjs.SockJSServerOptions;
import io.vertx.ext.sockjs.impl.RouteMatcher;

import java.util.UUID;

public class MainVerticle extends AbstractVerticle {

	private static final int PORT = 8080;
	private static final String PATH = "app";
	private static final String WELCOME_PAGE = "index.html";
	private static final String MONGO_ADDRESS = UUID.randomUUID().toString();

	private static final JsonObject HELLO_WORLD = new JsonObject().put("name", "vertx").put("text", "hello world!").put("date", System.currentTimeMillis());
		
		
	MongoService mongoService;

	@Override
	// tag::start[]
	public void start() throws Exception {
		RouteMatcher matcher = getRouteMatcher();
		matcher.matchMethod(
				HttpMethod.GET,
				"/api/history",
				request -> request.response().end(new JsonArray().add(HELLO_WORLD).toString()));

		setUpServer(matcher).listen();
	}
	//end::start[]
	private RouteMatcher getRouteMatcher() {
		RouteMatcher matcher = RouteMatcher.routeMatcher().matchMethod(HttpMethod.GET, "/",
				req -> req.response().sendFile(PATH + "/" + WELCOME_PAGE));
		matcher.matchMethod(HttpMethod.GET, "^\\/" + PATH + "\\/.*",
				req -> req.response().sendFile(req.path().substring(1)));
		return matcher;
	}

	private HttpServer setUpServer(RouteMatcher matcher) {
		HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(PORT)).requestHandler(
				req -> matcher.accept(req));
		SockJSServer.sockJSServer(vertx, server).bridge(new SockJSServerOptions().setPrefix("/eventbus"),
				new BridgeOptions().addInboundPermitted(new JsonObject()).addOutboundPermitted(new JsonObject()));
		return server;

	}

	private MongoService setUpMongo() {
		DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("address", MONGO_ADDRESS));
		vertx.deployVerticle(new MongoServiceVerticle(), options,
				result -> System.out.println("Mongo deployed: " + result.succeeded()));
		return MongoService.createEventBusProxy(vertx, MONGO_ADDRESS);
	}

}
