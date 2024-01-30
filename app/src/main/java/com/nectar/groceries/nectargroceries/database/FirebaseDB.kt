package com.nectar.groceries.nectargroceries.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseDB {

    fun getCollectionReferenceForUser(): CollectionReference{
        val currentUser = FirebaseAuth.getInstance().currentUser
        Log.e( "getCollectionReferenceForUser: "," uid => ${currentUser!!.uid}" )
        return FirebaseFirestore.getInstance().collection("users")
            .document(currentUser!!.uid).collection("General")
    }
}