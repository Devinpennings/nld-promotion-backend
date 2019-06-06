package util.logging;

import util.properties.Property;

import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

import static org.fusesource.jansi.Ansi.ansi;

@Named
public class Logger {

    @Inject
    private InjectionPoint ip;

    @Inject
    @Property("logging.level")
    private LogLevel logLevel;

    public void debug(String message) {
        if (this.logLevel.value() > LogLevel.DEBUG.value()) return;
        put(message, this.ip.getMember().getDeclaringClass(), "green");
    }

    public void info(String message) {
        if (this.logLevel.value() > LogLevel.INFO.value()) return;
        put(message, this.ip.getMember().getDeclaringClass(), "blue");
    }

    public void warn(String message) {
        if (this.logLevel.value() > LogLevel.WARN.value()) return;
        put(message, this.ip.getMember().getDeclaringClass(), "yellow");
    }

    public void error(String message) {
        if (this.logLevel.value() > LogLevel.ERROR.value()) return;
        put(message, this.ip.getMember().getDeclaringClass(), "red");
    }

    public static void debug(String message, Class context) {
        put(message, context, "green");
    }

    private static void put(String message, Class context, String contextColor) {
        System.out.println( ansi().render("@|" + contextColor + " [" + context.getSimpleName() + " | " + new Date().toString() + "]|@ " + message + ".") );
    }

}
