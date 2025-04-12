package com.kilowatt.Commands;

import lombok.AllArgsConstructor;
import lombok.Getter;

// ошибка исполнения комманды
@AllArgsConstructor
@Getter
public class WattCommandError extends RuntimeException
{
    private final String message;
}
