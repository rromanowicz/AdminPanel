package ex.rr.adminpanel.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ex.rr.adminpanel.database.Template;
import ex.rr.adminpanel.database.TemplateRepository;
import ex.rr.adminpanel.models.templates.page.PageTemplate;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final ObjectMapper objectMapper;

    private Map<Long, PageTemplate> pageTemplateMap;

    @PostConstruct
    private void init() {
        pageTemplateMap = templateRepository.findAll().stream().collect(Collectors.toMap(Template::getId, t -> {
            try {
                return objectMapper.readValue(t.getTemplate(), PageTemplate.class);
            } catch (JsonProcessingException e) {
                log.error("Failed to load template [{}], with error [{}]", t.getId(), e.getMessage());
                return null;
            }
        }));
    }


    public PageTemplate getTemplateById(Long id) {
        return this.pageTemplateMap.get(id);
    }


}
