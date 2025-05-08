package com.nasccped.project2025;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.nasccped.project2025.fragments.AuthorsFragment;
import com.nasccped.project2025.fragments.DropContactFragment;
import com.nasccped.project2025.fragments.NewContactFragment;
import com.nasccped.project2025.fragments.ShowContactsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // estabelecendo a view padãro (activity_main)
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // setando o fragmento de 'novo contato' como padrão
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new NewContactFragment())
                .commit();

        // obtem o RadioGroup
        RadioGroup rGroup = findViewById(R.id.navbar);

        // realizar operação quando houver mudança de radio
        rGroup.setOnCheckedChangeListener(handleRadioChange());
    }

    private RadioGroup.OnCheckedChangeListener handleRadioChange() {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // preparando o fragmento a ser disponibilizado com operador ternário
                Fragment selectedFragment =
                        checkedId == R.id.nav_authors ? // para fragmento de autores
                                new AuthorsFragment() :
                        checkedId == R.id.nav_drop_contact ? // para fragmento de remover contato
                                new DropContactFragment() :
                        checkedId == R.id.nav_show_contacts ? // para fragmento de exibir contatos
                                new ShowContactsFragment() :
                        new NewContactFragment(); // caso contrário, fragmento de adicionar contato como padrão

                // substituir container de fragmento pelo fragmento selectionado
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            }
        };
    }
}