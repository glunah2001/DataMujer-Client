package com.uned.clientedatamujer.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TermsReader {

    public static String loadText(String filePath){
        try (InputStream is = TermsReader.class.getResourceAsStream(filePath)) {

            if (is == null) {
                return "No se pudo encontrar el archivo: " + filePath;
            }

            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            return "Error cargando archivo";
        }
    }
}
