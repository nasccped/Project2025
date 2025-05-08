package com.nasccped.project2025;

// classe  simples para abrigar os campos de contatos (útil para inserção)
public class ContactFields {
    private final String name;
    private final String phone;
    private final String email;

    public ContactFields(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName() { return name; }

    public String getPhone() { return phone; }

    public String getEmail() { return email; }
}
