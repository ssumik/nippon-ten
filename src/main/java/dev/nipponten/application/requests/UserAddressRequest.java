package dev.nipponten.application.requests;

public record UserAddressRequest(
        String streetAddress, String number, String cep, String complement) {}
