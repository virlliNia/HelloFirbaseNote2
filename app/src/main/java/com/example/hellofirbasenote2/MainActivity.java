package com.example.hellofirbasenote2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    RecyclerView recyclerViewNotes;

    Button buttonAdd, buttonDelete, buttonMove;
    EditText etTitle, etSubtitle;


    NoteAdapter mAdapter;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asociamos los elementos de la vista al código
        this.initUI();
        FirebaseApp.initializeApp(getApplicationContext());

        mDatabase = FirebaseDatabase.getInstance().getReference();




        // Datos del shared
        ArrayList<NotesModel> noteLists = new ArrayList<NotesModel>();

        mAdapter = new NoteAdapter(noteLists);
        recyclerViewNotes.setAdapter(mAdapter);

        // Añade el touch a mi mAdapter
        ItemTouchHelper helper = new ItemTouchHelper(new MyTouchHelperCallback(mAdapter));
        helper.attachToRecyclerView(recyclerViewNotes);
    }

    private void initUI() {

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonMove = findViewById(R.id.buttonMove);

        etTitle = findViewById(R.id.etTitle);
        etSubtitle = findViewById(R.id.etSubtitle);

        // Iniciamos el sharedPreferences - para poder acceder a nuestros datos en local

        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        // Para que no cambie de tamaño cada vez que se añadan objetos
        recyclerViewNotes.setHasFixedSize(true);
        // Configuración comportate como una lista - puede comportarse como una tabla, o como celdas
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));

    }
    private void onBoarding() {

        new AlertDialog.Builder(this)
                .setTitle("Bienvenido")
                .setMessage("Pulsa Add para añaidr una nueva nota y Delete para borrar")


                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("No mostrar más", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation

                    }
                })
                .show();

    }
    @Override
    public void onClick(View v) {
        Button button = (Button) v;

        switch (button.getId()) {
            case R.id.buttonAdd :

                Log.e("Botón Add", "button ADD");



                NotesModel note = new NotesModel(etTitle.getText().toString(), etSubtitle.getText().toString());
                mAdapter.addNote(note, 0);
                publicarNuevaNoticia(note);
                ///mAdapter.notifyDataSetChanged();


                break;
            case R.id.buttonMove :


                break;
            case R.id.buttonDelete :
                mAdapter.deleteNote();
                // mAdapter.notifyDataSetChanged();

                break;
        }
    }

    private void publicarNuevaNoticia(NotesModel note) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss ddMMyyyy");
        String currentDate = sdf.format(new Date());

        mDatabase.child("news").child(currentDate).setValue(note);
    }



}

