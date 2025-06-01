package com.sistema_de_agendamentos.utils;

import org.springframework.stereotype.Service;

@Service
public class StringUtil {

    public static String extrairEmail(String principalString) {
        int emailIndex = principalString.indexOf("email=");
        if (emailIndex == -1) {
            throw new RuntimeException("Email não encontrado na String do principal!");
        }
        int start = emailIndex + "email=".length();
        int end = principalString.indexOf(",", start);
        if (end == -1) {
            end = principalString.length();
        }
        return principalString.substring(start, end).trim();
    }
}
