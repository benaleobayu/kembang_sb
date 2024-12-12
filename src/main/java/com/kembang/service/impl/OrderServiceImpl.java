package com.kembang.service.impl;

import com.kembang.converter.dictionary.PageCreateReturnApps;
import com.kembang.converter.parsing.GlobalConverter;
import com.kembang.entity.AppUser;
import com.kembang.entity.Order;
import com.kembang.entity.OrderHasProduct;
import com.kembang.model.*;
import com.kembang.model.projection.DataOrderResponse;
import com.kembang.model.projection.OrderIndexProjection;
import com.kembang.model.search.ListOfFilterPagination;
import com.kembang.model.search.SavedKeywordAndPageable;
import com.kembang.repository.AppUserRepository;
import com.kembang.repository.OrderHasProductRepository;
import com.kembang.repository.OrderRepository;
import com.kembang.repository.ProductRepository;
import com.kembang.repository.auth.AppAdminRepository;
import com.kembang.repository.handler.HandlerRepository;
import com.kembang.response.ResultPageResponseDTO;
import com.kembang.security.util.ContextPrincipal;
import com.kembang.service.DataProjectionService;
import com.kembang.service.OrderService;
import com.kembang.util.helper.Formatter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final AppAdminRepository adminRepository;
    private final AppUserRepository userRepository;
    private final DataProjectionService dataProjectionService;

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderHasProductRepository orderHasProductRepository;


    @Override
    public ResultPageResponseDTO<OrderIndexResponse> listDataOrder(CompilerFilterRequest f, LocalDate date, String location, Integer route) {
        ListOfFilterPagination filter = new ListOfFilterPagination(f.keyword(), date, location);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(f.pages(), f.limit(), f.sortBy(), f.direction(), f.keyword(), filter);

        LocalDate startDate = (date != null) ? date : LocalDate.of(1970, 1, 1);
        LocalDate endDate = (date != null) ? date : LocalDate.now().plusYears(1);
        Page<OrderIndexProjection> pageResult = orderRepository.listDataOrder(set.keyword(), set.pageable(), startDate, endDate, location, route);

        AtomicInteger count = new AtomicInteger(1);
        List<OrderIndexResponse> dtos = pageResult.stream().map((c) -> {
            OrderIndexResponse dto = new OrderIndexResponse();
            dto.setId(c.getSecureId());
            dto.setIndex(count.getAndIncrement());
            dto.setForwardName(c.getForwardName());
            dto.setForwardAddress(c.getForwardAddress());
            dto.setCustomerName(c.getCustomerName());
            dto.setCustomerAddress(c.getCustomerAddress());
            dto.setCustomerPhone(c.getCustomerPhone());
            dto.setCustomerLocation(c.getCustomerLocation());
            dto.setOrderDate(Formatter.formatLocalDate(c.getOrderDate()));
            dto.setDeliveryDate(Formatter.formatLocalDate(c.getDeliveryDate()));
            dto.setDriverName(c.getDriverName());
            dto.setRoute(c.getRoute());
            dto.setIsPaid(c.getIsPaid());
            dto.setIsActive(c.getIsActive());
            dto.setCreatedAt(Formatter.formatLocalDateTime(c.getCreatedAt()));

            handleListOrder(dto, c.getId());

            dto.setIsActive(c.getIsActive());
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
    }

    @Override
    public OrderDetailResponse findDataBySecureId(String id) {
        OrderIndexProjection data = orderRepository.findDataBySecureIdProjection(id);

        List<DataOrderResponse> listOrders = orderHasProductRepository.listDataOrderByOrderId(data.getId());
        List<ListOrderDTO> listOrder = listOrders.stream().map((c) -> new ListOrderDTO(
                c.getOrderId(),
                c.getProductId(),
                c.getProductName(),
                c.getQuantity(),
                c.getTotalPrice()
        )).collect(Collectors.toList());

        return new OrderDetailResponse(
                data.getSecureId(),
                data.getForwardName(),
                data.getForwardAddress(),
                data.getCustomerId(),
                data.getCustomerName(),
                data.getCustomerPhone(),
                data.getCustomerAddress(),
                data.getCustomerLocation(),
                listOrder,
                Formatter.formatLocalDate(data.getOrderDate()),
                Formatter.formatLocalDate(data.getDeliveryDate()),
                data.getDriverName(),
                data.getRoute(),
                data.getIsPaid(),
                data.getIsActive()
        );
    }

    @Override
    public void saveData(OrderCreateUpdateRequest dto) throws IOException {
        Order data = new Order();
        saveDataParsing(data, dto, "create", false);
    }

    @Override
    public void updateData(String id, OrderCreateUpdateRequest dto, Boolean isRoute) throws IOException {
        Order data = HandlerRepository.getEntityBySecureId(id, orderRepository, "Order not found");
        saveDataParsing(data, dto, "update", isRoute);
    }

    @Override
    public void deleteData(String id) {
        Order data = HandlerRepository.getEntityBySecureId(id, orderRepository, "Order not found");
        orderRepository.delete(data);
    }

    // -- parsing

    private void saveDataParsing(Order data, OrderCreateUpdateRequest dto, String type, Boolean isRoute) throws IOException {
        Long adminLoginId = ContextPrincipal.getId();

        AppUser user = null;
        if (dto.getCustomerId() != null) {
            user = HandlerRepository.getEntityBySecureId(dto.getCustomerId(), userRepository, "User not found");
        }
        if (!isRoute) {
            data.setCustomer(user); // set customer
            data.setForwardName(dto.getForwardName()); // set forward name
            data.setForwardAddress(dto.getForwardAddress()); // set forward address
            data.setDescription(dto.getDescription()); // set description

            data.setOrderDate(dto.getOrderDate()); // set order date
            data.setDeliveryDate(dto.getDeliveryDate()); // set delivery date
            data.setIsPaid(dto.getIsPaid()); // set isPaid
            data.setIsActive(dto.getIsActive()); // set isActive
        }

        data.setDriverName(dto.getDriverName()); // set driver name
        data.setRoute(dto.getRoute()); // set route

        if (type.equals("create")) {
            GlobalConverter.CmsAdminCreateAtBy(data, adminLoginId);
        } else if (type.equals("update")) {
            GlobalConverter.CmsAdminUpdateAtBy(data, adminLoginId);
        }

        Order savedData = orderRepository.save(data);

        if (!isRoute) {
            handleSaveListOrder(savedData, dto, type);
        }
    }

    private void handleSaveListOrder(Order data, OrderCreateUpdateRequest dto, String type) throws IOException {
        List<AddOrderRequest> orders = dto.getOrderProducts();
        // get list orderId in dto
        List<String> orderIdList = orders.stream().map(AddOrderRequest::orderId).toList();
        // get all list orderId in db
        List<String> orderIdMap = dataProjectionService.listOrderHasProductSecureIdByOrderId(List.of(data.getId())).get(data.getId());
        if (orderIdMap == null || orderIdMap.isEmpty()) {
            orderIdMap = null;
        }
        // filter with orderId in db is not equal to orderId in dto
        List<String> filteredOrderId = null;
        if (orderIdMap != null) {
            filteredOrderId = orderIdMap.stream().filter(orderId -> !orderIdList.contains(orderId)).collect(Collectors.toList());
        }
        // delete orderHasProduct
        if (filteredOrderId != null) {
            orderHasProductRepository.deleteBySecureIdIn(filteredOrderId);
        }

        List<String> productIdList = orders.stream().map(AddOrderRequest::productId).collect(Collectors.toList());
        Map<String, Integer> productPrices = dataProjectionService.listDataProductPriceByProductSecureId(productIdList);
        // filter not have orderId to added data
        List<String> finalOrderIdMap = orderIdMap;
        List<AddOrderRequest> filteredOrders = finalOrderIdMap != null ? orders.stream().filter(order -> !finalOrderIdMap.contains(order.orderId())).collect(Collectors.toList()) : orders;
        if (!filteredOrders.isEmpty()) {
            for (AddOrderRequest order : filteredOrders) {
                OrderHasProduct orderHasProduct = new OrderHasProduct();
                orderHasProduct.setOrder(data);
                orderHasProduct.setOrderNote(order.orderNote());
                orderHasProduct.setProduct(HandlerRepository.getEntityBySecureId(order.productId(), productRepository, "Product not found in id : " + order.productId()));
                orderHasProduct.setQuantity(order.quantity());
                orderHasProduct.setTotalPrice(order.quantity() * productPrices.get(order.productId()));
                orderHasProductRepository.save(orderHasProduct);
            }
        }
    }

    private void handleListOrder(OrderIndexResponse dto, Long id) {
        List<DataOrderResponse> listOrders = orderHasProductRepository.listDataOrderByOrderId(id);
        List<ListOrderDTO> listOrder = listOrders.stream().map((c) -> new ListOrderDTO(
                c.getOrderId(),
                c.getProductId(),
                c.getProductName(),
                c.getQuantity(),
                c.getTotalPrice()
        )).collect(Collectors.toList());
        dto.setListOrder(listOrder);
    }
}
