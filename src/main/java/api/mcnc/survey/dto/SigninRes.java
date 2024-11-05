package api.mcnc.survey.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SigninRes {
    private String userId;
    private String password;
}
