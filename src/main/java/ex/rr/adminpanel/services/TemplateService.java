package ex.rr.adminpanel.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ex.rr.adminpanel.database.Template;
import ex.rr.adminpanel.database.TemplateRepository;
import ex.rr.adminpanel.models.templates.page.Column;
import ex.rr.adminpanel.models.templates.page.PageTemplate;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final ObjectMapper objectMapper;

    private Map<String, PageTemplate> pageTemplateMap;

    @PostConstruct
    private void init() {

        List<Template> all = templateRepository.findAll();
        all.forEach(template -> template.setPageTemplate(parseTemplate(template)));
        pageTemplateMap = all.stream().collect(Collectors.toMap(it -> it.getPageTemplate().getName(), Template::getPageTemplate));


    }


    public PageTemplate getTemplateByName(String name) {
        return this.pageTemplateMap.get(name);
    }

    public Set<String> getTemplateList() {
        return pageTemplateMap.keySet();
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


}
