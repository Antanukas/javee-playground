package playground;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jpa.JPAFraction;
import org.wildfly.swarm.logging.LoggingFraction;


public class Application {

    public static void main(String[] args) throws Exception {

        Container container = new Container();


        DatasourcesFraction dsFraction = new DatasourcesFraction()
                .jdbcDriver("h2", (d) -> {
                    d.driverClassName("org.h2.Driver");
                    d.xaDatasourceClass("org.h2.jdbcx.JdbcDataSource");
                    d.driverModuleName("com.h2database.h2");
                })
                .dataSource("GreetingsDS", (ds) -> {
                    ds.driverName("h2");
                    ds.connectionUrl("jdbc:h2:mem:test3;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=TRUE");
                    ds.userName("sa2");
                    ds.password("sa");
                });
        container.fraction(dsFraction);
        container.fraction(new JPAFraction()
                                   .inhibitDefaultDatasource()
                                   .defaultDatasource("jboss/datasources/GreetingsDS")
        );
        container.fraction(LoggingFraction.createDefaultLoggingFraction());

        container.start();

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addPackage("playground");

        deployment.addAsWebInfResource(new ClassLoaderAsset(
                "META-INF/persistence.xml", Application.class.getClassLoader()), "classes/META-INF/persistence.xml");
        deployment.addAsWebInfResource(new ClassLoaderAsset(
                "META-INF/load.sql", Application.class.getClassLoader()), "classes/META-INF/load.sql");

        // Enable the swagger bits
        //TODO doesn't play well with CDI because of https://issues.jboss.org/browse/SWARM-271
/*        SwaggerArchive archive = deployment.as(SwaggerArchive.class);
        // Tell swagger where our resources are
        archive.setResourcePackages("playground");
        archive.setTitle("My Awesome Application");*/


        deployment.addAllDependencies();
        container.deploy(deployment);
    }
}
