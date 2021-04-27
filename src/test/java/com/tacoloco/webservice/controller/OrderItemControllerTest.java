package com.tacoloco.webservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tacoloco.webservice.model.ApiError;
import com.tacoloco.webservice.model.OrderItem;
import com.tacoloco.webservice.repository.OrderItemRepository;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Tests the {@link OrderItemController}.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class OrderItemControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private OrderItemRepository mockRepository;

    /**
     * Tests to ensure success when valid order item is provided in the request.
     */
    @Test
    public void calculateTotal_OK() throws JSONException, MalformedURLException {
        OrderItem orderItem = new OrderItem("Veggie Taco", 2);
        when(mockRepository.findPriceByItemName("Veggie Taco")).thenReturn(Optional.of(3.50));
        when(mockRepository.doesItemNameExist("Veggie Taco")).thenReturn(true);
        String expected = "{totalPrice:7.0}";

        ResponseEntity<String> response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/calculateTotal/").toString(), Collections.singletonList(orderItem), String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).findPriceByItemName("Veggie Taco");
        verify(mockRepository, times(1)).doesItemNameExist("Veggie Taco");
    }

    /**
     * Tests to ensure success when valid order item that qualifies for discount is provided in the request.
     */
    @Test
    public void calculateTotal_DiscountApplied_OK() throws JSONException, MalformedURLException {
        OrderItem orderItem = new OrderItem("Chicken Taco", 4);
        when(mockRepository.findPriceByItemName("Chicken Taco")).thenReturn(Optional.of(2.00));
        when(mockRepository.doesItemNameExist("Chicken Taco")).thenReturn(true);
        String expected = "{totalPrice:6.4}";

        ResponseEntity<String> response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/calculateTotal/").toString(), Collections.singletonList(orderItem), String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).findPriceByItemName("Chicken Taco");
        verify(mockRepository, times(1)).doesItemNameExist("Chicken Taco");
    }

    /**
     * Tests to ensure success when multiple valid order items that qualifies for discount are provided in the request.
     */
    @Test
    public void calculateTotal_Multiple_OK() throws JSONException, MalformedURLException {
        OrderItem orderItem1 = new OrderItem("Veggie Taco", 2);
        OrderItem orderItem2 = new OrderItem("Chicken Taco", 2);

        when(mockRepository.findPriceByItemName("Veggie Taco")).thenReturn(Optional.of(3.50));
        when(mockRepository.doesItemNameExist("Veggie Taco")).thenReturn(true);
        when(mockRepository.findPriceByItemName("Chicken Taco")).thenReturn(Optional.of(3.50));
        when(mockRepository.doesItemNameExist("Chicken Taco")).thenReturn(true);

        String expected = "{totalPrice:11.2}";

        ResponseEntity<String> response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/calculateTotal/").toString(), List.of(orderItem1,orderItem2), String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).findPriceByItemName("Veggie Taco");
        verify(mockRepository, times(1)).doesItemNameExist("Veggie Taco");
        verify(mockRepository, times(1)).findPriceByItemName("Chicken Taco");
        verify(mockRepository, times(1)).doesItemNameExist("Chicken Taco");
    }

    /**
     * Tests to ensure success when multiple valid duplicate order items that qualifies for discount are provided in the request.
     */
    @Test
    public void calculateTotal_MultipleDuplicate_OK() throws JSONException, MalformedURLException {
        OrderItem orderItem1 = new OrderItem("Veggie Taco", 2);
        OrderItem orderItem2 = new OrderItem("Veggie Taco", 2);

        when(mockRepository.findPriceByItemName("Veggie Taco")).thenReturn(Optional.of(3.50));
        when(mockRepository.doesItemNameExist("Veggie Taco")).thenReturn(true);

        String expected = "{totalPrice:11.2}";

        ResponseEntity<String> response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/calculateTotal/").toString(), List.of(orderItem1,orderItem2), String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).findPriceByItemName("Veggie Taco");
        verify(mockRepository, times(2)).doesItemNameExist("Veggie Taco");
    }

    /**
     * Test to ensure failure when an empty item name is provided in the request.
     */
    @Test
    public void calculateTotal_EmptyItemName() throws JSONException, MalformedURLException, JsonProcessingException {
        OrderItem orderItem = new OrderItem("", 4);
        ApiError expectedError = new ApiError(HttpStatus.BAD_REQUEST, "The request is not valid", List.of("calculateTotal.orders[0].itemName : must not be blank", "calculateTotal.orders[0].itemName : The item name is not a valid menu item"));
        String expected = new ObjectMapper().writeValueAsString(expectedError);
        ResponseEntity<String> response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/calculateTotal/").toString(), Collections.singletonList(orderItem), String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);
        System.out.println(response.getBody());
    }

    /**
     * Test to ensure failure when a blank item name is provided in the request.
     */
    @Test
    public void calculateTotal_BlankItemName() throws JSONException, MalformedURLException, JsonProcessingException {
        OrderItem orderItem = new OrderItem(" ", 4);
        ApiError expectedError = new ApiError(HttpStatus.BAD_REQUEST, "The request is not valid", List.of("calculateTotal.orders[0].itemName  : must not be blank", "calculateTotal.orders[0].itemName  : The item name is not a valid menu item"));
        String expected = new ObjectMapper().writeValueAsString(expectedError);
        ResponseEntity<String> response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/calculateTotal/").toString(), Collections.singletonList(orderItem), String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);
        System.out.println(response.getBody());
    }

    /**
     *Test to ensure failure when invalid menu item name is provided in the request.
     */
    @Test
    public void calculateTotal_InvalidItemName() throws JSONException, MalformedURLException, JsonProcessingException {
        OrderItem orderItem = new OrderItem("veg", 4);
        when(mockRepository.doesItemNameExist("veg")).thenReturn(false);

        ApiError expectedError = new ApiError(HttpStatus.BAD_REQUEST, "The request is not valid", "calculateTotal.orders[0].itemName veg: The item name is not a valid menu item");
        String expected = new ObjectMapper().writeValueAsString(expectedError);
        ResponseEntity<String> response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/calculateTotal/").toString(), Collections.singletonList(orderItem), String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    /**
     * Test to ensure failure when invalid quantity is supplied in the request.
     */
    @Test
    public void calculateTotal_InvalidQuantity() throws JSONException, MalformedURLException, JsonProcessingException {
        OrderItem orderItem = new OrderItem("Veggie Taco", -2);
        when(mockRepository.doesItemNameExist("Veggie Taco")).thenReturn(true);

        ApiError expectedError = new ApiError(HttpStatus.BAD_REQUEST, "The request is not valid", "calculateTotal.orders[0].quantity -2: must be greater than or equal to 1");
        String expected = new ObjectMapper().writeValueAsString(expectedError);
        ResponseEntity<String> response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/calculateTotal/").toString(), Collections.singletonList(orderItem), String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }
}
