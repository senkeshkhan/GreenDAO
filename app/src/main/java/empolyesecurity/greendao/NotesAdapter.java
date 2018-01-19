package empolyesecurity.greendao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.greendao.query.Query;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import empolyesecurity.greendao.DBbeanclass.User;
import empolyesecurity.greendao.DBbeanclass.UserDao;
import empolyesecurity.greendao.modelpojo.DaoSession;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private NoteClickListener clickListener;
    private List<User> dataset;
    public static Activity context;
     Query<User> notesQuery;
     UserDao userDao;
    public interface NoteClickListener {
        void onNoteClick(int position);
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public TextView comment;


        Button editBtn;


        public NoteViewHolder(View itemView, final NoteClickListener clickListener) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.textViewNoteText);
            comment = (TextView) itemView.findViewById(R.id.textViewNoteComment);

            editBtn = (Button) itemView.findViewById(R.id.editbutton);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onNoteClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public NotesAdapter(NoteClickListener clickListener , Activity context1) {
        this.context =context1;
        this.clickListener = clickListener;
        this.dataset = new ArrayList<User>();

        System.out.println("1111111111111"+dataset);

    }

    public void setNotes(@NonNull List<User> notes) {
        dataset = notes;
        notifyDataSetChanged();
    }

    public User getNote(int position) {
        return dataset.get(position);
    }

    @Override
    public NotesAdapter.NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(final NotesAdapter.NoteViewHolder holder, final int position) {

        DaoSession daoSession = ((AppController) context.getApplication()).getDaoSession();

        userDao = daoSession.getUserDao();
        notesQuery = userDao.queryBuilder().orderAsc(UserDao.Properties.Name).build();

        User note = dataset.get(position);
        holder.text.setText(note.getName());
        holder.comment.setText(note.getCreatedAt());
        System.out.println("888888888888888"+note.getName());

      //  System.out.println("DBBBBBBBBBB"+note.getImagekeys()+"::");

      holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = context.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

                edt.setText(dataset.get(position).getName());
                dialogBuilder.setTitle("Edit Deo");

                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //do something with edt.getText().toString();

                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {



                        //pass
                    }
                });
              final AlertDialog b = dialogBuilder.create();
                b.show();
                b.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Boolean wantToCloseDialog = false;
                        //Do stuff, possibly set wantToCloseDialog to true then...


                        if(edt.length()>0){


                            User note = new User();




                            final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
                            String comment = "Added on " + df.format(new Date());

                            note.setId(dataset.get(position).getId());
                            note.setName(edt.getText().toString());
                            note.setUpdatedAt(comment);
                            note.setCreatedAt("@gmail.com");
                           // note.setUser_id(dataset.get(position).getUser_id());

                            //note.setImagekeys(dataset.get(position).getImagekeys());
                            userDao.update(note);
                            ((MainActivity)context).updateNotes();

                            b.dismiss();
                        }else{
                            Toast.makeText(context,"enter any values", Toast.LENGTH_SHORT).show();

                        }

                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                    }
                });

            }
        });
    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
