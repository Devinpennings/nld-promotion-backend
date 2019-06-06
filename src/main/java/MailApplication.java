import util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Startup
@Singleton
public class MailApplication {

    @Inject
    private Logger logger;

    @Inject
    private EventService service;

    @PostConstruct
    public void postConstruct() {

        this.logger.info("Starting mail service...");
        this.service.start();

    }

}
