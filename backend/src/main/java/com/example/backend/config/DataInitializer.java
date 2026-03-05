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
            t1.setMinReputation(0);
            templateRepository.save(t1);

            MissionTemplate t2 = new MissionTemplate();
            t2.setType("sabotage");
            t2.setName("Саботаж производства");
            t2.setDescription("Вывести из строя сборочную линию. Требуется диверсия.");
            t2.setBaseReward(300);
            t2.setBaseRisk(12);
            t2.setMinStealthLevel(2);
            t2.setStepCount(2);
            t2.setMinReputation(20);
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
            t3.setMinReputation(50);
            templateRepository.save(t3);

            // 4. Ликвидация (assassination)
            MissionTemplate t4 = new MissionTemplate();
            t4.setType("assassination");
            t4.setName("Ликвидация цели");
            t4.setDescription("Устранить высокопоставленного чиновника корпорации. Требуется скрытность и подготовка.");
            t4.setBaseReward(800);
            t4.setBaseRisk(18);
            t4.setMinHackingLevel(0);
            t4.setMinSocialLevel(2);
            t4.setMinStealthLevel(3);
            t4.setMinAnalysisLevel(1);
            t4.setStepCount(3);
            t4.setMinReputation(30);
            templateRepository.save(t4);

            // 5. Перехват данных (interception)
            MissionTemplate t5 = new MissionTemplate();
            t5.setType("interception");
            t5.setName("Перехват данных");
            t5.setDescription("Перехватить секретные переговоры и расшифровать их. Нужен доступ к коммуникационным узлам.");
            t5.setBaseReward(600);
            t5.setBaseRisk(12);
            t5.setMinHackingLevel(3);
            t5.setMinSocialLevel(1);
            t5.setMinStealthLevel(2);
            t5.setMinAnalysisLevel(2);
            t5.setStepCount(4);
            t5.setMinReputation(20);
            templateRepository.save(t5);

            // 6. Подкуп чиновника (bribery)
            MissionTemplate t6 = new MissionTemplate();
            t6.setType("bribery");
            t6.setName("Подкуп чиновника");
            t6.setDescription("Склонить на свою сторону ответственного за закупки. Требует навыков убеждения.");
            t6.setBaseReward(400);
            t6.setBaseRisk(10);
            t6.setMinHackingLevel(0);
            t6.setMinSocialLevel(3);
            t6.setMinStealthLevel(1);
            t6.setMinAnalysisLevel(1);
            t6.setStepCount(2);
            t6.setMinReputation(10);
            templateRepository.save(t6);

            // 7. Технологический шпионаж (tech_espionage)
            MissionTemplate t7 = new MissionTemplate();
            t7.setType("tech_espionage");
            t7.setName("Технологический шпионаж");
            t7.setDescription("Добыть прототип нового двигателя. Сложная операция, требующая всех навыков.");
            t7.setBaseReward(1000);
            t7.setBaseRisk(22);
            t7.setMinHackingLevel(3);
            t7.setMinSocialLevel(2);
            t7.setMinStealthLevel(3);
            t7.setMinAnalysisLevel(2);
            t7.setStepCount(5);
            t7.setMinReputation(50);
            templateRepository.save(t7);

            // 8. Саботаж поставок (supply_sabotage) – ещё один вариант саботажа
            MissionTemplate t8 = new MissionTemplate();
            t8.setType("supply_sabotage");
            t8.setName("Саботаж поставок");
            t8.setDescription("Организовать сбой в цепочке поставок корпорации.");
            t8.setBaseReward(350);
            t8.setBaseRisk(8);
            t8.setMinHackingLevel(1);
            t8.setMinSocialLevel(1);
            t8.setMinStealthLevel(2);
            t8.setMinAnalysisLevel(0);
            t8.setStepCount(3);
            t8.setMinReputation(0);
            templateRepository.save(t8);
        }
    }
}