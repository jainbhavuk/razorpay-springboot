package com.jainbhavuk.razorpay.payment.mapper;

import com.jainbhavuk.razorpay.payment.dto.response.OrderResponse;
import com.jainbhavuk.razorpay.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponse toOrderResponse(OrderRecord order);
}
