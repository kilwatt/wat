package com.kilowatt.Compiler.Builtins.Libraries.Net;

import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.github.prominence.openweathermap.api.enums.Language;

/*
Net -> Погода
 */
public class NetOwm {
    // ru
    public Language RU = Language.RUSSIAN;
    // en
    public Language EN = Language.ENGLISH;

    // соеденение
    public OpenWeatherMapClient connect(String apiKey) {
        return new OpenWeatherMapClient(apiKey);
    }
}
