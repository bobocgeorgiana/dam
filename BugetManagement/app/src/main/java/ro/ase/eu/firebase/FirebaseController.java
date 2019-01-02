package ro.ase.eu.firebase;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ro.ase.eu.util.Expense;

public class FirebaseController implements FirebaseConstant {

    private DatabaseReference database;
    private FirebaseDatabase controller;
    private static FirebaseController firebaseController;

    private FirebaseController() {
        controller = FirebaseDatabase.getInstance();
    }

    private void open() {
        //se deschide o conexiune la firebase
        database = controller.getReference(EXPENSE_TABLE_NAME);
    }

    public static FirebaseController getInstance() {
        if (firebaseController == null) {
            synchronized (FirebaseController.class) {
                if (firebaseController == null) {
                    firebaseController = new FirebaseController();
                }
            }
        }

        return firebaseController;
    }

    public String upsertExpense(Expense expense) {
        if (expense == null) {
            return null;
        }

        open();
        // daca nu exista id global inseamna ca doresc sa fac un insert, in caz contrar se face un update
        if (expense.getGlobalId() == null || expense.getGlobalId().trim().isEmpty()) {
            //adaug o noua inregistrare in Firebase, iar id-ul returnat il stochez in expense
            expense.setGlobalId(database.push().getKey());
        }

        //trimit valorile campurilor catre firebase.
        database.child(expense.getGlobalId()).setValue(expense);

        //adaugare listener pentru a intercepta orice modificare pe un obiect de tip expense stocat in Firebase.
        database.child(expense.getGlobalId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Expense temp = dataSnapshot.getValue(Expense.class);
                if (temp != null) {
                    Log.i("FireController", "Expense is updated " + temp.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseController", "Expense is not saved");
            }
        });

        return expense.getGlobalId();
    }

    public List<Expense> findAll(ValueEventListener eventListener) {
        if (eventListener == null) {
            return null;
        }
        final List<Expense> expenses = new ArrayList<>();
        open();
        //setam listener-ul la nivelul radacinii astfel incat sa putem selecta toate inregistrarile.
        //listenerii se executa asincron. prin urmare trebuiesc utilizati direct in activatate
        database.addValueEventListener(eventListener);
        return expenses;
    }

    //stergerea unei inregistrari din Firebase
    public boolean remove(Expense expense) {
        if (expense == null || expense.getGlobalId() == null || expense.getGlobalId().trim().isEmpty()) {
            return false;
        }

        //deschidem conexiune catre tabela expenses din firebase
        open();
        //accesam nodul/inregistrare cu id-ul dorit si apelam remove
        database.child(expense.getGlobalId()).removeValue();
        //verificarea stergerii se poate face printr-un listener
        //aceste evenimente se recomanda se le implementati in activitate, deoarece sunt operatii asincron.
        //Rezultatul il puteti integra in business-ul solutiei.
        database.child(expense.getGlobalId()).removeEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("FirebaseController", "Remove is working");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.i("FirebaseController", "Remove is not working");
            }
        });

        return true;
    }

}
