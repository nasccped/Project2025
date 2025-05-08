package com.nasccped.project2025;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

// class Thread para realizar operações no servidor
public class ThreadRequest extends Thread  {

    // campos necessário
    private final String targetUrl; // url alvo
    private final String requestMethod; // método de request
    private String httpResponse; // resposta do servidor

    public ThreadRequest(String targetUrl, String requestMethod) {
        this.targetUrl = targetUrl;
        this.requestMethod = requestMethod;
        this.httpResponse = "";
    }

    @Override
    public void run() {
        // variáveis utilizados para obter os valores
        HttpURLConnection conn = null; // estabelecer conexão
        InputStream is; // obter valores do servidor
        ByteArrayOutputStream baos; // armazenar valores do servidor
        String response; // armazenar resposta

        try {
            URL url = new URL(targetUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(7000);
            conn.setRequestMethod(requestMethod);
            conn.setDoInput(true);
            conn.connect();

            is = conn.getInputStream();
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;

            while ((len = is.read(buffer)) > 0)
                baos.write(buffer, 0, len);

            response = baos.toString("UTF-8");
        } catch (Exception e) {
            response = "erro1";
        } finally {
            if (conn != null) conn.disconnect();
        }
        // atualizar variável local
        httpResponse = response;
    }

    // obter a resposta da conexão
    public String getHttpResponse() { return httpResponse; }

    // testar se a operação foi um sucesso
    public boolean successfullyModified() { return httpResponse.compareTo("ok") == 0; }
}
