package com.br.tomei;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.EditText;
import android.widget.Toast;

import com.br.tomei.api.BrejaAPI;
import com.br.tomei.model.Usuario;
import com.br.tomei.util.RetroFit;

public class FragmentCadUsu extends Fragment{

    private EditText etUsuario;
    private EditText etSenha;
    private EditText etSenha2;
    private FloatingActionButton btSave;

    private ProgressDialog progressDialog;


    private OnFragmentInteractionListener mListener;

    public FragmentCadUsu() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.content_cad_usuario, container, false);
        btSave = v.findViewById(R.id.btSalvarUsuario);

        etUsuario =  v.findViewById(R.id.etUsuario);
        etSenha = v.findViewById(R.id.etSenha);
        etSenha2 = v.findViewById(R.id.etSenha2);

        progressDialog = new ProgressDialog(getContext());

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etUsuario.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.errorUserRequired), Toast.LENGTH_LONG).show();
                }

                if (etSenha.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.errorPassRequired), Toast.LENGTH_LONG).show();
                }

                if (etSenha.getText().toString().equals(etSenha2.getText().toString())) {

                    showProgress(getString(R.string.hintUsuario), getString(R.string.savingUser));

                    RetroFit retroFit = new RetroFit();
                    BrejaAPI api = retroFit.getRetrofit().create(BrejaAPI.class);

                    Usuario usuario = new Usuario();

                    usuario.setUsuario(etUsuario.getText().toString());
                    usuario.setSenha(etSenha.getText().toString());

                    api.salvarUser(usuario)
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    dismissProgress();
                                    Toast.makeText(getContext(),getString(R.string.userCreated), Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    dismissProgress();
                                    Toast.makeText(getContext(),
                                            getString(R.string.errorUserCreated), Toast.LENGTH_SHORT).show();

                                }

                            });
                } else  {
                    Toast.makeText(getContext(), getString(R.string.errorRepeatPass), Toast.LENGTH_SHORT).show();
                    etSenha.setText("");
                    etSenha2.setText("");
                    etSenha.requestFocus();

                }

            }
        });

        return v;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    private void showProgress(String titulo, String mensagem) {
        if(progressDialog == null)
            progressDialog = new ProgressDialog(getContext());

        if(!progressDialog.isShowing()) {
            progressDialog.setMessage(mensagem);
            progressDialog.setTitle(titulo);
            progressDialog.show();
        }
    }

    private void dismissProgress() {
        if(progressDialog != null)
            progressDialog.dismiss();
    }


}
