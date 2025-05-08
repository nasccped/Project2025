package com.nasccped.project2025;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

// classe não 'construível' para disponibilizar uma série ligações indiretas com o servidor
public class DBUtil {
    // urls utilizadas + senha + método de request
    private static final String baseInsertionUrl = "https://mfpledon.com.br/contatos2025/cadastrarContatoTexto.php";
    private static final String baseDeletionUrl = "https://mfpledon.com.br/contatos2025/eliminarContato.php";
    private static final String consultUrl = "https://mfpledon.com.br/contatos2025/contatosJSON.php";
    private static final String password = "35wsx@3";
    private static final String requestMethod = "GET";
    // objetos quem herdam de 'Thread'
    private static ThreadRequest tr = null; // para operações no DB
    private static ThreadJSON tj = null; // para obter dados JSON

    // mapeamento de mensagens para operações no DB
    private static final HashMap<String, Function<String, String>> insertionStatus; // inserção
    private static final HashMap<String, Function<String, String>> deletionStatus; // deleção

    // povoando o mapa de mensagens (inserção)
    static {
        insertionStatus = new HashMap<>();
        insertionStatus.put("ok", DBAlert::contactSuccessfullyAdded);
        insertionStatus.put("erro1", DBAlert::unableToOpenConnection);
        insertionStatus.put("erro2", DBAlert::parameterError);
        insertionStatus.put("erro3", DBAlert::contactAlreadyExists);
        insertionStatus.put("erro4", DBAlert::invalidPassword);
    }

    // povoando o mapa de mensagens (deleção)
    static {
        deletionStatus = new HashMap<>();
        deletionStatus.put("ok", DBAlert::contactSuccessfullyRemoved);
        deletionStatus.put("erro1", DBAlert::unableToOpenConnection);
        deletionStatus.put("erro2", DBAlert::parameterError);
        deletionStatus.put("erro3", DBAlert::contactDoesntExists);
        deletionStatus.put("erro4", DBAlert::invalidPassword);
    }

    // função para gerar a url de inserção baseada nas fields do contato
    public static String generateUrlForInsertion(ContactFields cf) {
        String name = "nome=" + cf.getName();
        String phone = "fone=" + cf.getPhone();
        String email = "email=" + cf.getEmail();
        String pass = "senha=" + password;
        return baseInsertionUrl + "?" + name + "&" + phone
                                + "&" + email + "&" + pass;
    }

    // função para gerar a url de deleção baseada no nome do contato
    public static String generateUrlForDeletion(String contactName) {
        String name = "nome=" + contactName;
        String pass = "senha=" + password;
        return baseDeletionUrl + "?" + name + "&" + pass;
    }

    // cria um novo ThreadRequest baseado numa url
    public static void setThreadRequest(String url) {
        DBUtil.tr = new ThreadRequest(url, DBUtil.requestMethod);
    }

    // cria um novo ThreadJSON
    public static void setThreadJSON() {
        DBUtil.tj = new ThreadJSON(consultUrl, requestMethod);
    }

    // reseta o ThreadRequest para nulo
    public static void unsetThreadRequest() {
        DBUtil.tr = null;
    }

    // reseta o ThreadJSON para nulo
    public static void unsetThreadJSON() {
        DBUtil.tj = null;
    }

    // executa a ThreadRequest e retorna um boleano:
    //      retornou 'ok'       -> verdadeiro
    //      qualquer outro caso -> falso
    public static boolean runThread() {
        if (DBUtil.tr == null) return false;

        DBUtil.tr.start();

        // usando a função join para aguardar o término da thread
        try { DBUtil.tr.join(); } catch (Exception e) { return false; }

        // obtem valor de sucesso (internamente)
        return DBUtil.tr.successfullyModified();
    }

    public static boolean runThreadJson() {
        if (DBUtil.tj == null) return false;

        DBUtil.tj.start();
        try { DBUtil.tj.join(); } catch (Exception e) { return false; }
        return DBUtil.tj.successfullyConsult();
    }

    // obtém a mensagem final após tentativa de inserção
    public static String getInsertExecutionOutput(String contactName) {
        // obtem a response extraindo do campo interno da class (caso nulo, string vazia)
        String response = (DBUtil.tr == null) ? "" : DBUtil.tr.getHttpResponse();
        // obtem a função (do map de hashmap) para retornar a mensagem
        Function<String, String> binding = DBUtil.insertionStatus.get(response);
        // se a função existir (não nula)
        return (binding != null) ?
                binding.apply(contactName) : // retorna a função utilizando o contactName
                DBAlert.undefined(); // caso contrário, retorna a mensagem de 'undefinido'
    }

    // funciona da mesma maneira que a anterior, mas para mensagens de remoção
    public static String getDeletionExecutionOutput(String contactName) {
        String response = (DBUtil.tr == null) ? "" : DBUtil.tr.getHttpResponse();
        Function<String, String> binding = DBUtil.deletionStatus.get(response);
        return (binding != null) ? binding.apply(contactName) : DBAlert.undefined();
    }

    // obtem o valor JSON (em formato string) da class ThreadJSON
    public static String getConsultExecutionOutput() {
        ThreadJSON bind = DBUtil.tj; // preparar binding
        // caso exista (não nulo)
        return (bind != null) ?
                bind.getJSONString() : // retornar o valor interno
                ""; // caso contrário, string vazia
    }
}