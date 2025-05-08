package com.nasccped.project2025;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// class Thread para carregar o conteúdo JSON do servidor
public class ThreadJSON extends Thread {

    // campos locais
    private final String targetUrl; // url alvo
    private final String requestMethod; // método usado
    private String serverResponse; // resposta do servidor (JSON)

    public ThreadJSON(String url, String requestMethod) {
        this.targetUrl = url;
        this.requestMethod = requestMethod;
        this.serverResponse = "";
    }

    @Override
    public void run() {
        // valores utilizados
        HttpURLConnection conn = null; // estabelecer a conexão
        InputStream is; // obter valores do server
        ByteArrayOutputStream baos; // armazenar valores do server
        String response; // resposta final

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
            response = "";
        } finally {
            if (conn != null) conn.disconnect();
        }
        // atualizar o campo local para a resposta obtida
        serverResponse = response;
    }

    // obter json fornecido pelo server
    public String getJSONString() { return serverResponse; }

    // testar se JSON foi obtido com sucesso
    public boolean successfullyConsult() { return serverResponse.compareTo("") != 0; }
}
