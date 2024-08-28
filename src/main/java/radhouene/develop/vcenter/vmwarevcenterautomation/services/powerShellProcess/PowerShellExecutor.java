//package radhouene.develop.vcenter.vmwarevcenterautomation.services.powerShellProcess;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.scheduling.annotation.Scheduled;
//
//@Component
//public class PowerShellExecutor {
//
//    @Autowired
//    private final PowerShellService powerShellService;
//
//    public PowerShellExecutor(PowerShellService powerShellService) {
//        this.powerShellService = powerShellService;
//        powerShellService.startPowerShellService(); // Start the PowerShell service
//    }
//
//    public void executeCommands() {
//        String tags = powerShellService.runCommand("Get-Tag | ConvertTo-Json");
//        System.out.println("Tags: " + tags);
//    }
//
//    @Scheduled(fixedRate = 50000)
//    public void scheduledExecution() {
//        executeCommands();
//    }
//}
