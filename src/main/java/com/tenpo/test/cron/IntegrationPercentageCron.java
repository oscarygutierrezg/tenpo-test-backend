package com.tenpo.test.cron;

import com.tenpo.test.service.PercentageService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class IntegrationPercentageCron {

    private final PercentageService percentageService;

    @Scheduled(cron = "0 */30 * * * *")
    void syncCredits() {
        percentageService.saveCurrent();
    }
}
