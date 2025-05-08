package com.nasccped.project2025.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.nasccped.project2025.R;

// fragmento de autores
public class AuthorsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate na view
        View view = inflater.inflate(R.layout.authors_fragment, container, false);

        // obter button + setar action
        Button bt = view.findViewById(R.id.projects);
        bt.setOnClickListener(handleClick());

        return view;
    }

    private View.OnClickListener handleClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/nasccped";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        };
    }
}
