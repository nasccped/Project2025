package com.nasccped.project2025.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.nasccped.project2025.ContactFields;
import com.nasccped.project2025.DBUtil;
import com.nasccped.project2025.NetUtils;
import com.nasccped.project2025.R;
import com.nasccped.project2025.ToastUtil;

// fragmento para novo contato
public class NewContactFragment extends Fragment {

    // toast interno
    private ToastUtil tu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // carregar view + toast
        View view = inflater.inflate(R.layout.new_contact_fragment, container, false);
        tu = new ToastUtil(requireContext());

        // obter button + setar listener
        Button bt = view.findViewById(R.id.add_contact_button);
        bt.setOnClickListener(buttonListener(view));

        return view;
    }

    private View.OnClickListener buttonListener(View rootView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // testar se o dispositivo está conectado
                if (!NetUtils.isConnected(requireContext())) {
                    // caso não esteja, exibir alerta + encerrar listener
                    tu.shortAlert("Dispositivo não está conectado!");
                    return;
                }
                // obter os campos do layout
                EditText nameField = rootView.findViewById(R.id.new_contact_name);
                EditText phoneField = rootView.findViewById(R.id.new_contact_phone);
                EditText emailField = rootView.findViewById(R.id.new_contact_email);
                // obter os valores dentro dos campos
                String contactName = nameField.getText().toString();
                String phoneNumber = phoneField.getText().toString();
                String email = emailField.getText().toString();
                // colocá-los em um array
                String[] asArr = {contactName, phoneNumber, email};
                // iterar nos elementos
                for (String field : asArr) {
                    // caso um deles esteja vazio
                    if (field.isEmpty()) {
                        // emitir alerta + encerrar listener
                        tu.longAlert("Preencha todos os campos antes de inserir o cadastro");
                        return;
                    }
                }
                // gerando url para inserção de um novo cadastro
                String url = DBUtil.generateUrlForInsertion(
                        new ContactFields(contactName,
                                          phoneNumber,
                                          email)
                );
                // setando uma nova thread (baseada em uma url)
                DBUtil.setThreadRequest(url);
                // obtendo valor de sucesso + mensagem do server
                boolean success = DBUtil.runThread();
                String message = DBUtil.getInsertExecutionOutput(contactName);
                // resetando a thread para nulo após o uso
                DBUtil.unsetThreadRequest();
                // caso tenha sido bem sucedida
                if (success) {
                    // resetar as TextFields
                    nameField.setText("");
                    phoneField.setText("");
                    emailField.setText("");
                }
                // alertar utilizando o ToastUtil
                tu.longAlert(message);
            }
        };
    }
}
