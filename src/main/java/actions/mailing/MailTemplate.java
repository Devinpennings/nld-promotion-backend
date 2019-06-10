package actions.mailing;

import com.google.common.primitives.Primitives;
import model.Model;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Entity
public class MailTemplate extends Model {

    private String title;
    private String description;
    private String subject;
    private String html;

    @ElementCollection(fetch= FetchType.EAGER)
    @CollectionTable(name = "mail_template_params")
    @MapKeyColumn(name = "field")
    @Column(name = "value")
    private Map<String, String> parameters;

    public MailTemplate(String subject, String html) {
        this.subject = subject;
        this.html = html;
    }

    public MailTemplate(String title, String description, String subject, String html, Collection<String> keys) {
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.html = html;
        this.parameters = new HashMap<>();
        keys.forEach(k -> this.parameters.put(k, null));
    }

    public MailTemplate() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getHtml() {
        return this.html;
    }

    public String getHtmlFormatted() {

        String formatted = this.html;

        for(Map.Entry<String, String> entry : this.parameters.entrySet()) {
            if (formatted.contains("{{" + entry.getKey() + "}}")) {
                if (entry.getValue() != null) {
                    formatted = formatted.replace("{{" + entry.getKey() + "}}", entry.getValue());
                }
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
