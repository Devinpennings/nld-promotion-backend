package actions.mailing;

import com.google.common.primitives.Primitives;
import model.Model;

import javax.persistence.*;
import java.util.*;

@Entity
public class MailTemplate extends Model {

    private String title;
    private String description;
    private String subject;

    @Column(columnDefinition="text")
    private String html;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> fields = new HashSet<>();

    @Transient
    private Map<String, String> parameters = new HashMap<>();

    public MailTemplate(String subject, String html) {
        this.subject = subject;
        this.html = html;
    }

    public MailTemplate(String title, String description, String subject, String html, Set<String> keys) {
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.html = html;
        this.fields = keys;
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

    public void setRequiredFields(Collection<String> fields) {

        this.parameters.clear();
        fields.forEach(k -> this.parameters.put(k, null));

    }

    public void seed(Map<String, Object> values) throws Exception {

        if (!values.values().stream().allMatch(k -> k instanceof  String || Primitives.isWrapperType(k.getClass()) || Primitives.allPrimitiveTypes().contains(k.getClass()))) {
            throw new Exception("Failed to parse incoming data, incoming data types are not of expected type.");
        }

        if (!this.parameters.keySet().stream().allMatch(k -> values.keySet().stream().anyMatch(ok -> ok.equals(k)))) {
            throw new Exception("Failed to parse incoming data, not all keys present.");
        }

        for (String key : this.fields) {

            this.parameters.put(key, values.get(key).toString());

        }

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
