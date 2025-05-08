package com.nasccped.project2025;

// classe não 'construível' (métodos estáticos apenas)
// para emitir as mensagens a serem exibidas pelo Toast.makeText(...)
public class DBAlert {

    // field privada para quando o parâmetro da função não for utilizado
    private static String str;

    // contato adicionado com sucesso
    public static String contactSuccessfullyAdded(String contactName) {
        return String.format("\"%s\" foi cadastrado(a) com sucesso", contactName);
    }

    // não foi possível estabelecer conexão
    public static String unableToOpenConnection(String str) {
        DBAlert.str = str;
        return "Não foi possível abrir a conexão com o banco de dados (erro1)";
    }

    // parâmetros da url estão incorretos / são insuficientes
    public static String parameterError(String str) {
        DBAlert.str = str;
        return "Houve algum erro de parâmetro com a URL";
    }

    // contato já existe (erro quando tentar inserir)
    public static String contactAlreadyExists(String contactName) {
        return String.format("Não foi possível inserir \"%s\". Contato já existe",
                contactName);
    }

    // contato não existe (erro quando tentar remover)
    public static String contactDoesntExists(String contactName) {
        return String.format("Não foi possível remover \"%s\". Contato não existe",
                contactName);
    }

    // erro para senha inválida
    public static String invalidPassword(String str) {
        DBAlert.str = str;
        return "Senha inválida";
    }

    // contato removido com sucesso
    public static String contactSuccessfullyRemoved(String contactName) {
        return String.format("\"%s\" foi removido(a) com sucesso", contactName);
    }

    // quando não se pôde definir a mensagem obtida do servidor
    public static String undefined() { return "Comportamento inesperado"; }
}