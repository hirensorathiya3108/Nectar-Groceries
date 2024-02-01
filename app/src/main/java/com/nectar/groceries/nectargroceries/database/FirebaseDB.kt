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
            .document(currentUser.uid).collection("General")
    }

    fun getCollectionReferenceForBestSelling(): CollectionReference{
        return FirebaseFirestore.getInstance().collection("products")
            .document("bestselling").collection("best_product")
    }
    fun getCollectionReferenceForExclusiveOffer(): CollectionReference{
        return FirebaseFirestore.getInstance().collection("products")
            .document("exclusive_offer").collection("exclusive_product")
    }

    fun getCollectionReferenceForOffer(): CollectionReference{
        return FirebaseFirestore.getInstance().collection("products")
            .document("offer").collection("offer_product")
    }

    fun getCollectionReferenceForBasket(deviceId:String): CollectionReference{
        return FirebaseFirestore.getInstance().collection("basket")
            .document(deviceId).collection("basket_product")
    }
}