package com.jainbhavuk.razorpay.merchant.mapper;

import com.jainbhavuk.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.jainbhavuk.razorpay.merchant.dto.response.MerchantSignupResponse;
import com.jainbhavuk.razorpay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {
    Merchant toEntityFromMerchantSignupRequest(MerchantSignupRequest request);
    MerchantSignupResponse toMerchantSignupResponse(Merchant merchant);
}
