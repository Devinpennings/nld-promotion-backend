package service;

import actions.mailing.MailTemplate;
import data.jpa.MailTemplateJPADAO;
import dto.MailTemplateDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
import java.util.Collection;
import java.util.Optional;

@Stateless
public class MailService {

    @Inject
    private MailTemplateJPADAO mailTemplateDAO;

    public Collection<MailTemplate> getTemplates() {
        return this.mailTemplateDAO.get();
    }

    public Optional<MailTemplate> get(String id) {
        return this.mailTemplateDAO.get(id);
    }

    public MailTemplate addTemplate(MailTemplateDTO dto) {

        MailTemplate template = new MailTemplate(dto.title, dto.description, dto.subject, dto.html, dto.requiredFields);
        return this.mailTemplateDAO.add(template);

    }

    public Optional<MailTemplate> updateTemplate(MailTemplateDTO dto) {

        Optional<MailTemplate> template = this.mailTemplateDAO.get(dto.id);
        if (template.isPresent()) {
            MailTemplate updated = template.get();
            updated.setSubject(dto.subject);
            updated.setDescription(dto.description);
            updated.setHtml(dto.html);
            updated.setTitle(dto.title);
            updated.setRequiredFields(dto.requiredFields);
            return this.mailTemplateDAO.update(updated);
        }
        return Optional.empty();
    }
}
