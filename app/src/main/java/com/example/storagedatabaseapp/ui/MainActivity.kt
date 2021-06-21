package com.example.storagedatabaseapp.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.storagedatabaseapp.*
import com.example.storagedatabaseapp.data.datasource.LocalDataSource
import com.example.storagedatabaseapp.data.db.AppDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"
    private lateinit var database: LocalDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = fragment.findNavController()
        bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.materialDetailFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> bottomNavigationView.visibility = View.VISIBLE
            }
        }

        database = LocalDataSource(AppDatabase.getDatabase(applicationContext).materialDao())
        testDatabase()
    }

    fun hideKeyBoard() {
        val view: View? = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun testDatabase() {
        lifecycleScope.launchWhenStarted {
            withContext(Dispatchers.IO) {
                materialTypeList.forEach { database.insertMaterialType(it) }
                partnerNameList.forEach { database.insertPartnerName(it) }

                Log.d(TAG, "testDatabase: --------------------------------------")
                materialList.forEach {
                    if (!database.insertMaterial(it)) {
                        Log.d(TAG, "testDatabase: Inserting Material failed, Material: ${it.type}")
                    }
                }
                Log.d(TAG, "testDatabase: --------------------------------------")

                takingRequestList.forEach {
                    if (!database.insertTakingRequest(it)) {
                        Log.d(
                            TAG,
                            "testDatabase: Inserting Taking Request failed, Request: ${it.requestedFrom}"
                        )
                    }
                }
                Log.d(TAG, "testDatabase: --------------------------------------")

                givingRequestList.forEach {
                    if (!database.insertGivingRequest(it)) {
                        Log.d(
                            TAG,
                            "testDatabase: Inserting Giving Request failed, Request: ${it.requestedFrom}"
                        )
                    }
                }
                Log.d(TAG, "testDatabase: --------------------------------------")

                val materialsInStock = database.getMaterials(1)
                Log.d(
                    TAG,
                    "testDatabase: Materials in stock are:\n  ${materialsInStock.joinToString { "$it \n" }}"
                )
                Log.d(TAG, "testDatabase: --------------------------------------")

                val time1 = System.currentTimeMillis()
                val materialsNotGiving = database.getMaterialsNotGiving(
                    time1 - TimeUnit.DAYS.toMillis(30), time1
                )
                Log.d(
                    TAG,
                    "testDatabase: Materials not giving are:\n  ${materialsNotGiving.joinToString { "$it \n" }}"
                )
                Log.d(TAG, "testDatabase: --------------------------------------")

                val time2 = System.currentTimeMillis()
                val materialsWithRefusalGiving = database.getMaterialsWithGivingStatus(
                    time2 - TimeUnit.DAYS.toMillis(30), time2, false
                )
                Log.d(
                    TAG,
                    "testDatabase: Materials with refuses on giving are:\n  ${materialsWithRefusalGiving.joinToString { "$it \n" }}"
                )
                Log.d(TAG, "testDatabase: --------------------------------------")
            }
        }
    }
}