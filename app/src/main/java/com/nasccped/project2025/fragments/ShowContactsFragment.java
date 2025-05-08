package com.nasccped.project2025.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.nasccped.project2025.DBUtil;
import com.nasccped.project2025.NetUtils;
import com.nasccped.project2025.R;
import com.nasccped.project2025.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

// fragmento para exibir contatos
public class ShowContactsFragment extends Fragment {

    // toast privado
    private ToastUtil tu;
    private final String DEFAULT_DATA_CONTENT = "Os dados aparecerão aqui!";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // obter view + toast / setar o campo dos dados para o padrão
        View view = inflater.inflate(R.layout.show_contacts_fragment, container, false);
        tu = new ToastUtil(requireContext());
        TextView dataField = view.findViewById(R.id.data_text_view);
        dataField.setText(DEFAULT_DATA_CONTENT);

        // obter button + setar listener
        Button bt = view.findViewById(R.id.show_contacts_button);
        bt.setOnClickListener(handleClick(view));

        return view;
    }

    private View.OnClickListener handleClick(View rootView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // obter a textView dos dados
                TextView txt = rootView.findViewById(R.id.data_text_view);
                // testar se o dispositivo está conectado
                if (!NetUtils.isConnected(requireContext())) {
                    // caso não esteja, resetar textView + exibir alerta + encerrar listener
                    txt.setText(DEFAULT_DATA_CONTENT);
                    tu.shortAlert("Dispositivo não está conectado!");
                    return;
                }
                // iniciar threadJSON
                DBUtil.setThreadJSON();
                // capturar valor de sucesso + resposta do server
                boolean success = DBUtil.runThreadJson();
                String jsonContent = DBUtil.getConsultExecutionOutput();
                // resetar threadJSON para nulo
                DBUtil.unsetThreadJSON();
                // se a operação não foi um sucesso
                if (!success) {
                    // resetar text + alerta + encerrar listener
                    txt.setText(DEFAULT_DATA_CONTENT);
                    tu.longAlert("Não foi possível obter os dados do servidor");
                    return;
                }
                // converter a resposta obtida (formato JSON) em um formato legível
                String textMessage = convertJsonToText(jsonContent);
                // se a mensagem for vazia
                if (textMessage.isEmpty()) {
                    // resetar text + alertar + encerrar listener
                    txt.setText(DEFAULT_DATA_CONTENT);
                    tu.longAlert("O objeto JSON retornou uma String vazia");
                    return;
                }
                // inserir o texto obtido no campo dos dados + alertar sucesso
                txt.setText(textMessage);
                tu.longAlert("Dados obtidos com sucesso");
            }
        };
    }

    private String convertJsonToText(String json) {
        StringBuilder result = new StringBuilder();
        try {
            JSONObject root = new JSONObject(json);
            JSONArray people = root.getJSONArray("pessoas");

            JSONObject person = null;
            for (int i = 0; i < people.length(); i++) {
                person = people.getJSONObject(i);
                String name = person.getString("contato");
                String phone = person.getString("celular");
                String email = person.getString("email");
                result.append(String.format("%s, %s, %s\n\n", name, phone, email));
            }
        } catch (Exception e) {
            result.setLength(0);
        }

        return result.toString();
    }
}
