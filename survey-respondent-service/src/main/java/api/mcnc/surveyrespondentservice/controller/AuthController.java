package api.mcnc.surveyrespondentservice.controller;

import org.springframework.web.bind.annotation.RestController;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-20 오후 11:19
 */
@RestController
public class AuthController {
/**
 * TODO: respondent service에서 해야할 일
 * 1 OAuth2 로그인 지원
 * 2 OAuth2 로그인 지원 시 email, username, phone_number 추출해서 save
 * 3 추출한 정보 + respondent_id 로 토큰 생성 해서 반환
 * 4 gateway에서 보낸 토큰 검증 - respondent_id와 함께 반환 (header의 request-by에 넣어야함)

 ===== 추가로
  survey_id는 survey-service에서 링크 공유 시에 query param 인코딩(혹은 로우 데이터) 해서 보내 거나, path variable로 보낼 듯
 respondent-gateway 타서 response-service 요청 하게 될 것 같고 그때 필터로 토큰 검사 해서
 토큰 존재 하면 바로 설문 응답 redirect주소 보내주고
 없으면 개인 인증 redirect 주소 보내줘야 함

 */


}
