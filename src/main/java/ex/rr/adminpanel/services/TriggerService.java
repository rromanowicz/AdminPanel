package ex.rr.adminpanel.services;

import ex.rr.adminpanel.database.DbTrigger;
import ex.rr.adminpanel.database.DbTriggerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TriggerService {

    private final DbTriggerRepository dbTriggerRepository;

    public List<DbTrigger> getDbTriggers() {
        return dbTriggerRepository.findAll();
    }

    public void save(DbTrigger dbTrigger) {
        dbTriggerRepository.save(dbTrigger);
    }

}
