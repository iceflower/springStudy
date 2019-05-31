package org.multimodule.common.entity.payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "JWT 리프레시 토큰 요청", description = "JWT 리프레시 토큰 payload")
public class TokenRefreshRequest {

    @NotBlank(message = "리프레시 토큰이 존재하지 않습니다.")
    @NotNull(message = "리프레시 토큰이 존재하지 않습니다.")
    @ApiModelProperty(value = "성공한 인증 전에 받은 유효한 리프레시 토큰", required = true, allowableValues = "문자열(NonEmpty)")
    private String refreshToken;

    public TokenRefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public TokenRefreshRequest() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
