package site.keydeuk.store.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    COMMON_SYSTEM_ERROR(INTERNAL_SERVER_ERROR, "시스템 오류입니다."),
    COMMON_INVALID_PARAMETER(BAD_REQUEST, "요청한 값이 올바르지 않습니다."),
    COMMON_RESOURCE_NOT_FOUND(NOT_FOUND, "존재하지 않는 리소스입니다."),
    COMMON_ENTITY_NOT_FOUND(BAD_REQUEST, "존재하지 않는 엔티티입니다."),
    COMMON_JSON_PROCESSING_ERROR(BAD_REQUEST, "Json 변환 중 오류"),

    //Product
    PRODUCT_NOT_FOUND(NOT_FOUND, "상품 정보를 찾을 수 없습니다"),
    OPTION_NOT_FOUND(NOT_FOUND,"옵션 정보를 찾을 수 없습니다"),

    //Payment,
    PRODUCT_NOT_ON_SALE(NOT_FOUND, "현재 판매중이지 않은 상품입니다."),
    INVALID_PAYMENT_AMOUNT_ERROR(CONFLICT,"주문금액과 실 결제금액이 다릅니다."),
    PAYMENT_NOT_FOUND(BAD_REQUEST, "결제 정보를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(BAD_REQUEST, "주문 정보를 찾을 수 없습니다."),
    READY_ORDER_NOT_FOUND(BAD_REQUEST, "결제 대기중인 주문이 아닙니다."),
    USER_ORDER_NOT_MATCH(BAD_REQUEST,"해당 유저의 주문 정보가 아닙니다."),
    PAYMENT_ORDER_NOT_MATCH(BAD_REQUEST,"주문 정보와 결제 정보가 일치하지 않습니다."),

    // Auth
    ILLEGAL_REGISTRATION_ID(BAD_REQUEST, "올바르지 않은 소셜 로그인입니다"),
    EMPTY_EMAIL(BAD_REQUEST, "이메일은 필수 값 입니다."),
    EMPTY_PASSWORD(BAD_REQUEST, "비밀번호는 필수 값 입니다."),
    EMPTY_SUCCESS_HANDLER(INTERNAL_SERVER_ERROR, "SuccessHandler 필수 값 입니다."),
    EMPTY_FAILURE_HANDLER(INTERNAL_SERVER_ERROR, "FailureHandler 필수 값 입니다."),
    LOGIN_FAIL(BAD_REQUEST, "이메일, 비밀번호를 확인해주세요."),
    INVALID_TOKEN(UNAUTHORIZED, "유효하지 않은 토큰 입니다."),
    SAVE_REFRESH_TOKEN_FAILED(UNAUTHORIZED, "Token 저장 중 오류가 발생 했습니다."),
    EMPTY_REFRESH_TOKEN(BAD_REQUEST, "Refresh Token은 필수 값 입니다."),
    EMPTY_ACCESS_TOKEN(BAD_REQUEST, "Access Token은 필수 값 입니다."),
    LOGOUT_ACCESS_TOKEN(UNAUTHORIZED, "로그아웃 된 토큰입니다."),
    EXPIRED_TOKEN(UNAUTHORIZED, "만료된 토큰 입니다."),

    // User
    ALREADY_EXIST_EMAIL(BAD_REQUEST, "이미 사용중인 이메일입니다. 이미 가입하신 적이 있다면 로그인을 시도해주세요"),
    ALREADY_EXIST_NICKNAME(BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    ALREADY_EXIST_PHONENUM(BAD_REQUEST, "이미 사용중인 전화번호입니다."),
    USER_NOT_FOUND(NOT_FOUND, "유저 정보가 존재하지 않습니다"),
    INVALID_PASSWORD(BAD_REQUEST, "비밀번호를 확인해 주세요."),
    ALREADY_DELETE_USER(BAD_REQUEST, "이미 삭제된 사용자입니다."),
    PERMISSION_DENIED(FORBIDDEN, "이 작업을 수행할 권한이 없습니다."),

    //Order
    ORDER_HISTORY_NOT_FOUND(NOT_FOUND,"커스텀 키보드 주문 내역이 존재하지 않습니다."),

    //Likes
    LIKES_NOT_FOUND(BAD_REQUEST,"찜이 존재하지 않습니다"),
    ALREADY_EXIST_LIKE(BAD_REQUEST, "이미 찜한 상품입니다."),
    LIKED_PRODUCTS_NOT_FOUND(BAD_REQUEST, "찜한 상품이 존재하지 않습니다."),

    //Review
    ALREADY_EXIST_REVIEW(BAD_REQUEST, "이미 해당 제품에 대한 리뷰를 작성하였습니다."),
    REVIEW_NOT_FOUND(BAD_REQUEST, "리뷰 정보를 찾을 수 없습니다."),

    //Community
    POST_NOT_FOUND(NOT_FOUND, "게시글을 찾을 수 없습니다"),
    COMMENT_NOT_FOUND(NOT_FOUND, "댓글을 찾을 수 없습니다"),
    IMAGE_POST_NOT_MATCH(BAD_REQUEST,"해당 게시글의 이미지가 아닙니다."),
    IMAGE_MAX_COUNT(BAD_REQUEST,"게시글 이미지는 최대 4개까지 가능합니다."),

    //Shipping
    SHIPPING_NOT_FOUND(NOT_FOUND, "배송지 정보를 찾을 수 없습니다"),

    //Pageale
    INVALID_PAGEABLE_PAGE(BAD_REQUEST, "잘못된 페이지 접근입니다."),

    //Coupon
    USER_ALREADY_HAS_WELCOME_COUPON(CONFLICT, "이미 회원가입 쿠폰을 발급 받았습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
