package jwt.details.dto;

import lombok.Builder;

@Builder
public record ResponseDTO(String name, String token){}
