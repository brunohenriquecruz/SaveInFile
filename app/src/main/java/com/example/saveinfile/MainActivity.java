package com.example.saveinfile;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtNome, edtEmail;
    TextView resNome, resEmail, resSexo, resCNH;
    RadioGroup rgSexo, rgSave ;
    RadioButton rbSave;
    Button cadastar;
    CheckBox cbCnh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);

        cbCnh = findViewById(R.id.cbCNH);
        rgSexo = findViewById(R.id.rgSexo);

        resCNH  = findViewById(R.id.tvResCnh);
        resEmail = findViewById(R.id.tvResEmail);
        resNome = findViewById(R.id.tvResNome);
        resSexo = findViewById(R.id.tvResSexo);

        cadastar = (Button) findViewById(R.id.btnCadastrar);
        cadastar.setOnClickListener(this);
    }

    public void onStart() {
        super.onStart();
        lerArquivoInterno();
        //lerArquivoExterno();
    }

    @Override
    public void onClick(View v) {

        if(getSave().equals("Externo")){
            salvarArquivoExterno(
                    this.edtNome.getText().toString(),
                    this.edtEmail.getText().toString(),
                    getCNH(),
                    getSexo()
            );
        } else {
            salvarArquivoInterno(
                    this.edtNome.getText().toString(),
                    this.edtEmail.getText().toString(),
                    getCNH(),
                    getSexo()
            );
        }

        finish();
        startActivity(getIntent());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    public String getCNH() {

        String ceneaga = "";

        if (cbCnh.isChecked()) {
            ceneaga += cbCnh.getText().toString();
        } else {
            ceneaga += "Não possuo";
        }
        return ceneaga;
    }

    private String getSexo(){
        String sSexo = " ";
        if(rgSexo.getCheckedRadioButtonId() != -1){
            RadioButton sexoSelecionado = (RadioButton) findViewById(rgSexo.getCheckedRadioButtonId());
            sSexo += sexoSelecionado.getText().toString();
        }

        return sSexo;
    }

    private String getSave(){

        String sSave = "";
        rgSave = findViewById(R.id.rgFile);
        RadioButton rbSave = findViewById(rgSave.getCheckedRadioButtonId());

        if(rbSave.getText().toString().equals("Interno")) {
            sSave += "Interno";
        } else {
            sSave += "Externo";
        }

        return sSave;
    }


    public void salvarArquivoInterno(String nome, String email, String cnh, String sexo) {
        String user = "";
        user += "nome="+ nome;
        user += "\n";
        user += "email="+ email;
        user += "\n";
        user += "sexo="+ sexo;
        user += "\n";
        user += "cnh="+ cnh;
        user += "\n";


        try {
            FileOutputStream fos = openFileOutput("user.txt", MODE_PRIVATE);
            fos.write(user.getBytes());
            fos.close();
            Toast.makeText(this,"Usuário cadastrado com sucesso no arquivo interno!",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("Error File: ", e.getMessage());
        }

    }

    public void lerArquivoInterno() {
        String nome = "";
        String email = "";
        String sexo = "";
        String cnh = "";

        try {
            File dir = getFilesDir();
            File file = new File(dir+"/user.txt");

            if (file.exists()) {
                FileInputStream fis = openFileInput("user.txt");
                byte[] buffer = new byte[(int)file.length()];

                while(fis.read(buffer) != -1) {
                    String texto = new String(buffer);

                    if (texto.indexOf("nome") != -1) {
                        int indice = texto.indexOf("=");
                        int indiceFinal = texto.indexOf("\n");
                        nome = texto.substring(indice+1, indiceFinal);
                        texto = texto.substring(indiceFinal+1);
                    }
                    if (texto.indexOf("email") != -1) {
                        int indice = texto.indexOf("=");
                        int indiceFinal = texto.indexOf("\n");
                        email = texto.substring(indice+1);
                    }
                    if (texto.indexOf("cnh") != -1) {
                        int indice = texto.indexOf("=");
                        int indiceFinal = texto.indexOf("\n");
                        cnh = texto.substring(indice+1);
                    }
                    if (texto.indexOf("sexo") != -1) {
                        int indice = texto.indexOf("=");
                        int indiceFinal = texto.indexOf("\n");
                        sexo = texto.substring(indice+1, indiceFinal);
                    }
                }

                this.resNome.setText(nome);
                this.resEmail.setText(email);
                this.resCNH.setText(cnh);
                this.resSexo.setText(sexo);
            }
        } catch (Exception e) {
            Log.e("Error File: ", e.getMessage());
        }
    }

    public void salvarArquivoExterno(String nome, String email, String cnh, String sexo) {
        String user = "";
        user += "nome="+ nome;
        user += "\n";
        user += "email="+ email;
        user += "\n";
        user += "sexo="+ sexo;
        user += "\n";
        user += "cnh="+ cnh;
        user += "\n";

        try {
            String estado = Environment.getExternalStorageState();
            if (estado.equals(Environment.MEDIA_MOUNTED)) {
                FileOutputStream fos = openFileOutput("ExternalUser.txt", MODE_PRIVATE);
                fos.write(user.getBytes());
                fos.close();
                Toast.makeText(this,"Usuário cadastrado com sucesso no arquivo externo",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("Error File: ", e.getMessage());
        }
    }

    public void lerArquivoExterno() {
        String nome = "";
        String email = "";
        String sexo = "";
        String cnh = "";

        try {
            String estado = Environment.getExternalStorageState();

            if (estado.equals(Environment.MEDIA_MOUNTED)) {
                File dir = getExternalFilesDir(null);
                File file = new File(dir + "/ExternalUser.txt");

                if (file.exists()) {
                    FileInputStream fis = openFileInput("ExternalUser.txt");
                    byte[] buffer = new byte[(int) file.length()];

                    while (fis.read(buffer) != -1) {
                        String texto = new String(buffer);

                        if (texto.indexOf("nome") != -1) {
                            int indice = texto.indexOf("=");
                            int indiceFinal = texto.indexOf("\n");
                            nome = texto.substring(indice + 1, indiceFinal);
                            texto = texto.substring(indiceFinal + 1);
                        }
                        if (texto.indexOf("email") != -1) {
                            int indice = texto.indexOf("=");
                            int indiceFinal = texto.indexOf("\n");
                            email = texto.substring(indice + 1);
                        }
                        if (texto.indexOf("cnh") != -1) {
                            int indice = texto.indexOf("=");
                            int indiceFinal = texto.indexOf("\n");
                            cnh = texto.substring(indice + 1);
                        }
                        if (texto.indexOf("sexo") != -1) {
                            int indice = texto.indexOf("=");
                            int indiceFinal = texto.indexOf("\n");
                            sexo = texto.substring(indice + 1, indiceFinal);
                        }
                    }

                    this.resNome.setText(nome);
                    this.resEmail.setText(email);
                    this.resCNH.setText(cnh);
                    this.resSexo.setText(sexo);
                }
            }
        } catch (Exception e) {
            Log.e("Error File: ", e.getMessage());
        }
    }
}

