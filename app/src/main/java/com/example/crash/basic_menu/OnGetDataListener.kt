package com.example.crash.basic_menu

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

interface OnGetDataListener {
    fun onStart();
    fun onSuccess(data: DataSnapshot);
    fun onFailed(data: DatabaseError);
}