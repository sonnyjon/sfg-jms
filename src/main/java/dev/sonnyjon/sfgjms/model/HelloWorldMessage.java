package dev.sonnyjon.sfgjms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Sonny on 10/17/2022.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HelloWorldMessage implements Serializable
{
    static final long serialVersionUID = -6703826490277916847L;

    private UUID id;
    private String message;
}