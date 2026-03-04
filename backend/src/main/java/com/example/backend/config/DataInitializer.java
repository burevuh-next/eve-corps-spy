package com.example.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.backend.entity.MissionTemplate;
import com.example.backend.entity.Skill;
import com.example.backend.repository.MissionTemplateRepository;
import com.example.backend.repository.SkillRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MissionTemplateRepository templateRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public void run(String... args) throws Exception {
        // Инициализация навыков
        if (skillRepository.count() == 0) {
            skillRepository.save(new Skill(1, "hacking", "Взлом систем и кража данных", 5));
            skillRepository.save(new Skill(2, "social", "Социальная инженерия и манипуляции", 5));
            skillRepository.save(new Skill(3, "stealth", "Скрытность и заметание следов", 5));
            skillRepository.save(new Skill(4, "analysis", "Анализ данных и увеличение награды", 5));
        }

        // Инициализация шаблонов миссий
        if (templateRepository.count() == 0) {
            MissionTemplate t1 = new MissionTemplate();
            t1.setType("data_theft");
            t1.setName("Кража чертежей");
            t1.setDescription("Проникнуть в архив корпорации и скопировать чертежи нового крейсера.");
            t1.setBaseReward(500);
            t1.setBaseRisk(8);
            t1.setMinHackingLevel(2);
            t1.setMinStealthLevel(1);
            t1.setStepCount(3);
            templateRepository.save(t1);

            MissionTemplate t2 = new MissionTemplate();
            t2.setType("sabotage");
            t2.setName("Саботаж производства");
            t2.setDescription("Вывести из строя сборочную линию. Требуется диверсия.");
            t2.setBaseReward(300);
            t2.setBaseRisk(12);
            t2.setMinStealthLevel(2);
            t2.setStepCount(2);
            templateRepository.save(t2);

            MissionTemplate t3 = new MissionTemplate();
            t3.setType("isk_theft");
            t3.setName("Кража ISK");
            t3.setDescription("Перевести небольшую сумму со счета корпорации на подставной.");
            t3.setBaseReward(700);
            t3.setBaseRisk(15);
            t3.setMinHackingLevel(3);
            t3.setMinAnalysisLevel(2);
            t3.setStepCount(4);
            templateRepository.save(t3);
        }
    }
}