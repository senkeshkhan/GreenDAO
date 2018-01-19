package empolyesecurity.greendao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.greendao.query.Query;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import empolyesecurity.greendao.DBbeanclass.User;
import empolyesecurity.greendao.DBbeanclass.UserDao;
import empolyesecurity.greendao.modelpojo.DaoSession;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private View addNoteButton;

    private NotesAdapter notesAdapter;
    //
    private Query<User> notesQuery;


    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNoteButton =(Button) findViewById(R.id.buttonAdd);
        editText = (EditText) findViewById(R.id.editTextNote);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });
        setUpViews();
        DaoSession daoSession = ((AppController) getApplication()).getDaoSession();
        userDao = daoSession.getUserDao();
        notesQuery = userDao.queryBuilder().orderAsc(UserDao.Properties.Name).build();
        updateNotes();
    }


    protected void setUpViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewNotes);
        //noinspection ConstantConditions
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notesAdapter = new NotesAdapter(noteClickListener,MainActivity.this);
        recyclerView.setAdapter(notesAdapter);


        //noinspection ConstantConditions
        addNoteButton.setEnabled(false);


        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addNote();
                    return true;
                }
                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enable = s.length() != 0;
                addNoteButton.setEnabled(enable);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }







    NotesAdapter.NoteClickListener noteClickListener = new NotesAdapter.NoteClickListener() {
        @Override
        public void onNoteClick(int position) {
            User note = notesAdapter.getNote(position);
            Long noteId = note.getId();

            userDao.deleteByKey(noteId);

            Log.d("DaoExample", "Deleted note, ID: " + noteId);

            updateNotes();
        }
    };



    private void addNote() {
        String noteText = editText.getText().toString();
        editText.setText("");

        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());

        User note = new User();
       // note.setId(1L);
        note.setName(noteText);
        note.setCreatedAt("now");
        note.setUpdatedAt("up");

        userDao.insert(note);

       Log.d("DaoExample", "Inserted new note, ID: " + note.getId());

        updateNotes();
    }



    public void updateNotes() {
        List<User> notes = notesQuery.list();
        notesAdapter.setNotes(notes);
    }
}
