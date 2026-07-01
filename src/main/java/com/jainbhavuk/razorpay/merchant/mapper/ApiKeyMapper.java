package com.jainbhavuk.razorpay.merchant.mapper;

import com.jainbhavuk.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.jainbhavuk.razorpay.merchant.dto.response.ApiKeyResponse;
import com.jainbhavuk.razorpay.merchant.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {

    ApiKeyCreateResponse toApiKeyCreateResponse(ApiKey apiKey);

    @Mapping(target = "enabled", source = "enabled")
    ApiKeyResponse toApiKeyResponseList(ApiKey apiKey);
}
