package bienew.blog.backend.auth.oauth;

import bienew.blog.backend.auth.domain.LoginType;
import bienew.blog.backend.auth.dto.OauthUserInfo;

public interface LoginResolver {

    // 이 LoginResolver가 지원하는 로그인 타입을 반환
    LoginType supports();

    // 로그인 타입에 따라 credential을 처리하고 사용자 정보를 반환
    OauthUserInfo resolve(String credential);
}
