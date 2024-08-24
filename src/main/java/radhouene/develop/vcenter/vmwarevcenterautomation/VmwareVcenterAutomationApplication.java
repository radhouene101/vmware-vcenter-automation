package radhouene.develop.vcenter.vmwarevcenterautomation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VmwareVcenterAutomationApplication {

    public static void main(String[] args) {
        SpringApplication.run(VmwareVcenterAutomationApplication.class, args);
    }

}
