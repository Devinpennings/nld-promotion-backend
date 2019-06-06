package actions.mailing;

import com.google.common.primitives.Primitives;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MailTemplate {

    private String subject;
    private String html;
    private Map<String, String> parameters;

    public MailTemplate(String subject, String html) {
        this.subject = subject;
        this.html = html;
    }

    public MailTemplate(String subject, String html, Collection<String> keys) {
        this.subject = subject;
        this.html = html;
        this.parameters = new HashMap<>();
        keys.forEach(k -> this.parameters.put(k, null));
    }

    public String getHtml() {
        return this.html;
    }

    public String getHtmlFormatted() {

        String formatted = this.html;

        for(Map.Entry<String, String> entry : this.parameters.entrySet()) {
            if (formatted.contains("{{" + entry.getKey() + "}}")) {
                formatted = formatted.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
        }

        return formatted;

    }

    public String getSubject() {
        return this.subject;
    }

    public void seed(Map<String, Object> values) throws Exception {

        if (!values.values().stream().allMatch(k -> k instanceof  String || Primitives.isWrapperType(k.getClass()) || Primitives.allPrimitiveTypes().contains(k.getClass()))) {
            throw new Exception("Failed to parse incoming data, incoming data types are not of expected type.");
        }

        if (!this.parameters.keySet().stream().allMatch(k -> values.keySet().stream().anyMatch(ok -> ok.equals(k)))) {
            throw new Exception("Failed to parse incoming data, not all keys present.");
        }

        for (String key : this.parameters.keySet()) {

            this.parameters.replace(key, values.get(key).toString());

        }

    }

}
