package api.mcnc.survey.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class APIResponse<API> {
    private boolean result;
    private String resultCode;
    private String resultMessage;
    private API body;
}
