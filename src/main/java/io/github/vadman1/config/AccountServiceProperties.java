package io.github.vadman1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccountServiceProperties {

    @Value("${account.default-amount:0}")
    private double defaultAmount;

    @Value("${account.transfer-commission:0}")
    private double transferCommission;

    public double getDefaultAmount() {
        return defaultAmount;
    }

    public double getTransferCommission() {
        return transferCommission;
    }
}
