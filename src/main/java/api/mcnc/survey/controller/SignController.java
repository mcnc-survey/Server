package api.mcnc.survey.controller;

import api.mcnc.survey.dto.APIResponse;
import api.mcnc.survey.dto.SigninRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.crypto.dsig.SignedInfo;

@RequiredArgsConstructor
@RequestMapping("/api")
public class SignController {


    @PostMapping("/signin")
    public ResponseEntity<APIResponse<API>> postTest(@RequestBody SigninRes param){
        String userId = param.getUserId();



    }
}
