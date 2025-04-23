package com.kilowatt.Compiler.Builtins.Libraries.Net;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.Entities.VmInstance;
import com.kilowatt.WattVM.VmAddress;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/*
Net -> Http
 */
public class NetHttp {
    private final HttpClient client = HttpClient.newHttpClient();
    public HttpResponse<String> send_request(HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                address.getLine(),
                address.getFileName(),
                "http request error: " + e.getMessage(),
                "check your request body."
            );
        }
    }

    public VmInstance get_request(String url) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(url))
                .build();
        HttpResponse<String> response = send_request(httpRequest);
        // возвращаем ответ
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        WattCompiler.vm.push(response.statusCode());
        WattCompiler.vm.push(response.headers());
        WattCompiler.vm.push(response.body());
        return new VmInstance(
                WattCompiler.vm,
                WattCompiler.vm.getTypeDefinitions().lookup(address, "HttpResponse"),
                address
        );
    }

    public VmInstance post_request(String url, String body) {
        // отправляем запрос
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();
        HttpResponse<String> response = send_request(httpRequest);
        // возвращаем ответ
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        WattCompiler.vm.push(response.statusCode());
        WattCompiler.vm.push(response.headers());
        WattCompiler.vm.push(response.body());
        return new VmInstance(
            WattCompiler.vm,
            WattCompiler.vm.getTypeDefinitions().lookup(address, "HttpResponse"),
            address
        );
    }

    public VmInstance put_request(String url, String body) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = send_request(httpRequest);
        // возвращаем ответ
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        WattCompiler.vm.push(response.statusCode());
        WattCompiler.vm.push(response.headers());
        WattCompiler.vm.push(response.body());
        return new VmInstance(
                WattCompiler.vm,
                WattCompiler.vm.getTypeDefinitions().lookup(address, "HttpResponse"),
                address
        );
    }

    public VmInstance patch_request(String url, String body) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(body))
                    .build();
        HttpResponse<String> response = send_request(httpRequest);
        // возвращаем ответ
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        WattCompiler.vm.push(response.statusCode());
        WattCompiler.vm.push(response.headers());
        WattCompiler.vm.push(response.body());
        return new VmInstance(
                WattCompiler.vm,
                WattCompiler.vm.getTypeDefinitions().lookup(address, "HttpResponse"),
                address
        );
    }
    
    public VmInstance delete_request(String url) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response = send_request(httpRequest);
        // возвращаем ответ
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        WattCompiler.vm.push(response.statusCode());
        WattCompiler.vm.push(response.headers());
        WattCompiler.vm.push(response.body());
        return new VmInstance(
                WattCompiler.vm,
                WattCompiler.vm.getTypeDefinitions().lookup(address, "HttpResponse"),
                address
        );
    }

    public VmInstance head_request(String url) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .HEAD()
                .build();
        HttpResponse<String> response = send_request(httpRequest);
        // возвращаем ответ
        VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
        WattCompiler.vm.push(response.statusCode());
        WattCompiler.vm.push(response.headers());
        WattCompiler.vm.push(response.body());
        return new VmInstance(
                WattCompiler.vm,
                WattCompiler.vm.getTypeDefinitions().lookup(address, "HttpResponse"),
                address
        );
    }
}