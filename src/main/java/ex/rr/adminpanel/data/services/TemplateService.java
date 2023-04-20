package ex.rr.adminpanel.data.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ex.rr.adminpanel.data.database.Template;
import ex.rr.adminpanel.data.database.TemplateRepository;
import ex.rr.adminpanel.data.models.templates.page.Column;
import ex.rr.adminpanel.data.models.templates.page.PageTemplate;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The {@code TemplateService} service class for Template related actions.
 * <p>All templates are loaded and parsed during initialization for faster access.</p>
 *
 * @author rromanowicz
 * @see Template
 * @see PageTemplate
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final ObjectMapper objectMapper;

    private Map<String, PageTemplate> pageTemplateMap;

    @PostConstruct
    private void init() {
        List<Template> all = templateRepository.findAllByActive(true);
        all.forEach(template -> template.setPageTemplate(parseTemplate(template)));
        pageTemplateMap = all.stream().collect(Collectors.toMap(it -> it.getPageTemplate().getName(), Template::getPageTemplate));
    }

    public PageTemplate getTemplateByName(String name) {
        return this.pageTemplateMap.get(name);
    }

    public Set<String> getTemplateList() {
        return pageTemplateMap.keySet();
    }

    public void disable(Integer id) {
        Template template = templateRepository.findById(id).orElseThrow();
        template.setActive(false);
        saveTemplate(template);
    }


    public void saveTemplate(Template template) {
        Template saved = templateRepository.save(template);
        PageTemplate pageTemplate = parseTemplate(saved);
        assert pageTemplate != null;
        if (template.isActive()) {
            pageTemplateMap.put(pageTemplate.getName(), pageTemplate);
        } else {
            pageTemplateMap.remove(pageTemplate.getName());
        }
    }

    private PageTemplate parseTemplate(Template t) {
        try {
            PageTemplate template = objectMapper.readValue(t.getTemplate(), PageTemplate.class);
            if (template.getGlobalFilters() != null && template.getGlobalFilters().size() > 0) {
                template.getGlobalFilters().forEach(filter -> filter.setColumnType(
                        template.getSections().stream().flatMap(pageSection -> pageSection.getColumns().stream()).toList()
                                .stream().filter(column -> filter.getColumn().equals(column.getName())).findFirst().map(Column::getType).get()
                ));
            }
            return template;
        } catch (JsonProcessingException e) {
            log.error("Failed to load template [{}], with error [{}]", t.getId(), e.getMessage());
            return null;
        }
    }

    public List<Template> findAll() {
        return templateRepository.findAll();
    }
}
