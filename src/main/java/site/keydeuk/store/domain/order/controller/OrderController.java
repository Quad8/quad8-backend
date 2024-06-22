package site.keydeuk.store.domain.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.order.dto.request.OrderCreateRequest;
import site.keydeuk.store.domain.order.dto.response.OrderCreateResponse;
import site.keydeuk.store.domain.order.dto.response.OrderDetailResponse;
import site.keydeuk.store.domain.order.dto.response.OrderResponse;
import site.keydeuk.store.domain.order.service.OrderService;
import site.keydeuk.store.domain.security.PrincipalDetails;

import java.util.List;

@Tag(name = "Order", description = "주문 관련 API입니다.")
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public CommonResponse<OrderCreateResponse> createOrder(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody List<OrderCreateRequest> requests
    ){
        Long userId = principalDetails.getUserId();
        OrderCreateResponse response = orderService.createOrder(userId, requests);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "전체 주문 조회", description = "로그인된 사용자의 전체 주문 내역을 조회합니다.")
    @GetMapping
    public CommonResponse<List<OrderResponse>> getAllOrders(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getUserId();
        List<OrderResponse> responses = orderService.getAllOrders(userId);
        return CommonResponse.ok(responses);
    }

    @Operation(summary = "주문 상세 조회", description = "사용자의 주문 내역을 상세 조회합니다.")
    @GetMapping("/{orderId}")
    public CommonResponse<OrderDetailResponse> getOrder(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long orderId) {
        Long userId = principalDetails.getUserId();
        OrderDetailResponse order = orderService.getOrder(userId, orderId);
        return CommonResponse.ok(order);
    }

    @Operation(summary = "주문 삭제", description = "주문을 삭제합니다.")
    @DeleteMapping
    public CommonResponse<Void> deleteOrders(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody List<Long> orderIds) {
        Long userId = principalDetails.getUserId();
        orderService.deleteOrders(userId, orderIds);
        return CommonResponse.ok();
    }
}
