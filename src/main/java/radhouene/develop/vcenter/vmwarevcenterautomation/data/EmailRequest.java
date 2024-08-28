package radhouene.develop.vcenter.vmwarevcenterautomation.data;

import lombok.Data;

@Data
public class EmailRequest {
    private String message;
    private String recipient;
}
