package com.nasccped.project2025.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.nasccped.project2025.DBUtil;
import com.nasccped.project2025.NetUtils;
import com.nasccped.project2025.R;
import com.nasccped.project2025.ToastUtil;

// fragmento para eliminar contato
public class DropContactFragment extends Fragment {

    // toast interno
    private ToastUtil tu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // carregar view + toast
        View view = inflater.inflate(R.layout.drop_contact_fragment, container, false);
        tu = new ToastUtil(requireContext());

        // obter button + setar action
        Button bt = view.findViewById(R.id.drop_contact_button);
        bt.setOnClickListener(handleClick(view));

        return view;
    }

    private View.OnClickListener handleClick(View rootView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // testar se o dispositivo está conectado
                if (!NetUtils.isConnected(requireContext())) {
                    // caso não esteja, exibir alerta + encerrar listener
                    tu.shortAlert("Dispositivo não está conectado!");
                    return;
                }
                // obter nameField + valor interno
                EditText nameField = rootView.findViewById(R.id.drop_contact_name);
                String name = nameField.getText().toString();
                // se o valor estiver vazio
                if (name.isEmpty()) {
                    // alertar + encerrar
                    tu.shortAlert("Preencha todos os campos");
                    return;
                }
                // gerar url para deleção
                String url = DBUtil.generateUrlForDeletion(name);
                // setar thread com url
                DBUtil.setThreadRequest(url);
                // obter valor de sucesso + mensagem
                boolean success = DBUtil.runThread();
                String message = DBUtil.getDeletionExecutionOutput(name);
                // resetar Thread para nulo
                DBUtil.unsetThreadRequest();
                // se a operação foi um sucesso, apagar conteúdo da field
                if (success) nameField.setText("");
                // alertar operação com o toast
                tu.longAlert(message);
            }
        };
    }
}