package com.wefood.front.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wefood.front.order.adaptor.OrderAdaptor;
import com.wefood.front.order.dto.CartResponse;
import com.wefood.front.order.dto.request.DirectOrderCreateRequest;
import com.wefood.front.order.dto.request.OrderCreateRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Controller
public class WidgetController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final MessageSource messageSource;

    private final OrderAdaptor orderAdaptor;

    WidgetController(@Qualifier("messageSource") MessageSource messageSource, OrderAdaptor orderAdaptor) {
        this.messageSource = messageSource;
        this.orderAdaptor = orderAdaptor;
    }

    @RequestMapping(value = "/confirm")
    public ResponseEntity<JSONObject> confirmPayment(@RequestBody String jsonBody) throws Exception {


        JSONParser parser = new JSONParser();
        String orderId;
        String amount;
        String paymentKey;
        try {
            // 클라이언트에서 받은 JSON 요청 바디입니다.
            JSONObject requestData = (JSONObject) parser.parse(jsonBody);
            paymentKey = (String) requestData.get("paymentKey");
            orderId = (String) requestData.get("orderId");
            amount = (String) requestData.get("amount");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        ;
        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);
        obj.put("paymentKey", paymentKey);

        // TODO: 개발자센터에 로그인해서 내 결제위젯 연동 키 > 시크릿 키를 입력하세요. 시크릿 키는 외부에 공개되면 안돼요.
        // @docs https://docs.tosspayments.com/reference/using-api/api-keys
        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";

        // 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
        // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
        // @docs https://docs.tosspayments.com/reference/using-api/authorization#%EC%9D%B8%EC%A6%9D
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        // 결제 승인 API를 호출하세요.
        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
        // @docs https://docs.tosspayments.com/guides/v2/payment-widget/integration#3-결제-승인하기
        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);


        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        // TODO: 결제 성공 및 실패 비즈니스 로직을 구현하세요.
        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();

        return ResponseEntity.status(code).body(jsonObject);
    }

    /**
     * 인증성공처리
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String paymentRequest(@RequestParam String invoiceNumber,
                                 @RequestParam String receiverName,
                                 @RequestParam String receiverPhone,
                                 @RequestParam String receiverAddress,
                                 @RequestParam String receiverAddressDetail,
                                 @RequestParam(required = false) Integer amountValue,
                                 @RequestParam String deliveryMethod,
                                 @RequestParam(required = false) Integer quantity,
                                 @RequestParam(required = false) Integer price,
                                 @RequestParam(required = false) Long productId,
                                 @RequestParam(required = false) String directPay,
                                 @RequestParam(required = false) String transactionDate,
                                 @CookieValue(name = "cart", required = false) String cart,
                                 @CookieValue String id,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        // 값이 있으면 바로구매 한거임
        if (directPay.equals("1")) {

            // 택배
            if (deliveryMethod.equals("delivery")) {
                orderAdaptor.createOrder(new DirectOrderCreateRequest(amountValue, invoiceNumber, receiverPhone, receiverName, receiverAddress, receiverAddressDetail, LocalDate.now().toString(), null, productId, quantity, price), id);
            } else {
                // 직거래
                orderAdaptor.createOrder(new DirectOrderCreateRequest(amountValue, invoiceNumber, receiverPhone, receiverName, receiverAddress, receiverAddressDetail, null, transactionDate, productId, quantity, price), id);
            }
        } else { // 장바구니에서 구매한 거임

            List<CartResponse> farms;

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                farms = objectMapper.readValue(cart, new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            Integer totalValue = 0;

            for (int i = 0; i < farms.size(); i++) {
                for (int j = 0; j < farms.get(i).getProducts().size(); j++) {
                    totalValue += farms.get(i).getProducts().get(j).getPrice() * farms.get(i).getProducts().get(j).getQuantity();
                }
            }


            // 택배
            if (deliveryMethod.equals("delivery")) {
                orderAdaptor.createBasketOrder(new OrderCreateRequest(totalValue, invoiceNumber, receiverPhone, receiverName, receiverAddress, receiverAddressDetail, LocalDate.now().toString(), null), farms, id);
            } else {
                // 직거래
                orderAdaptor.createBasketOrder(new OrderCreateRequest(totalValue, invoiceNumber, receiverPhone, receiverName, receiverAddress, receiverAddressDetail, null, transactionDate), farms, id);
            }

            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    // "cart" 쿠키를 찾으면
                    if ("cart".equals(cookie.getName())) {
                        // 쿠키의 유효 기간을 0으로 설정하여 삭제
                        cookie.setMaxAge(0);
                        // 쿠키의 경로를 지정해야 정상적으로 삭제됨 (쿠키 설정 시의 경로와 일치해야 함)
                        cookie.setPath("/");
                        // 응답에 쿠키 추가
                        response.addCookie(cookie);
                    }
                }
            }

        }


        // delivery면 택배
        // pickup이면 직거래

        // todo 이거 가지고 order만들어
        // todo 장바구니에있는 쿠기 가지고 orderDetail만들어
        // todo 장바구니 쿠키 지워버려
        // todo 그럼 결제완료 -> 주문생성 -> 주문디테일까지 완료
        return "/success";
    }

    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public String tosspay(@RequestParam String invoiceNumber,
                          @RequestParam String receiverName,
                          @RequestParam String receiverPhone,
                          @RequestParam String receiverAddress,
                          @RequestParam(required = false) String amountValue,
                          @RequestParam String receiverAddressDetail,
                          @RequestParam String deliveryMethod,
                          @RequestParam(required = false) String quantity,
                          @RequestParam(required = false) String price,
                          @RequestParam(required = false) Long productId,
                          @RequestParam(required = false) String directPay,
                          @RequestParam(required = false) String transactionDate,
                          Model model) throws Exception {


        model.addAttribute("amountValue", amountValue);
        model.addAttribute("invoiceNumber", invoiceNumber);
        model.addAttribute("receiverName", receiverName);
        model.addAttribute("receiverPhone", receiverPhone);
        model.addAttribute("receiverAddress", receiverAddress);
        model.addAttribute("receiverAddressDetail", receiverAddressDetail);
        model.addAttribute("directPay", directPay);
        model.addAttribute("deliveryMethod", deliveryMethod);
        model.addAttribute("transactionDate", transactionDate);
        model.addAttribute("quantity", quantity);
        model.addAttribute("price", price);
        model.addAttribute("productId", productId);
        return "tosspay";
    }


    @RequestMapping(value = "/basket-pay", method = RequestMethod.GET)
    public String basketTosspay(@RequestParam String invoiceNumber,
                                @RequestParam String receiverName,
                                @RequestParam String receiverPhone,
                                @RequestParam String receiverAddress,
                                @RequestParam String receiverAddressDetail,
                                @RequestParam String deliveryMethod,
                                @RequestParam(required = false) String directPay,
                                @RequestParam(required = false) String transactionDate,
                                @CookieValue(name = "cart", required = false) String cart,
                                Model model) throws Exception {


        List<CartResponse> farms;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            farms = objectMapper.readValue(cart, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Integer totalValue = 0;

        for (int i = 0; i < farms.size(); i++) {
            for (int j = 0; j < farms.get(i).getProducts().size(); j++) {
                totalValue += farms.get(i).getProducts().get(j).getPrice() * farms.get(i).getProducts().get(j).getQuantity();
            }
        }
        model.addAttribute("amountValue", totalValue);
        model.addAttribute("invoiceNumber", invoiceNumber);
        model.addAttribute("receiverName", receiverName);
        model.addAttribute("receiverPhone", receiverPhone);
        model.addAttribute("receiverAddress", receiverAddress);
        model.addAttribute("receiverAddressDetail", receiverAddressDetail);
        model.addAttribute("directPay", directPay);
        model.addAttribute("deliveryMethod", deliveryMethod);
        model.addAttribute("transactionDate", transactionDate);
        return "basket-tosspay";
    }

    /**
     * 인증실패처리
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fail", method = RequestMethod.GET)
    public String failPayment(HttpServletRequest request, Model model) throws Exception {
        String failCode = request.getParameter("code");
        String failMessage = request.getParameter("message");

        model.addAttribute("code", failCode);
        model.addAttribute("message", failMessage);

        return "/fail";
    }

    @GetMapping("/payment/clear")
    public String paymentClear() {
        return "redirect:/order-list";
    }

}
