package com.jainbhavuk.razorpay.payment.processor.dto;

public sealed interface PaymentProcessorResponse permits
        PaymentProcessorResponse.Failure,
        PaymentProcessorResponse.Pending,
        PaymentProcessorResponse.Success {

        public record Pending(String processorReference) implements PaymentProcessorResponse {}
        public record Success(String processorReference, String bankReference) implements PaymentProcessorResponse {}
        public record Failure(String errorCode, String errorDescription) implements PaymentProcessorResponse {}
}
