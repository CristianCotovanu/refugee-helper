package com.example.refugeehelper.foundations

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.refugeehelper.R
import com.example.refugeehelper.databinding.ActivityFoundationBinding
import com.example.refugeehelper.foundations.models.Foundation
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.time.OffsetDateTime


class FoundationActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityFoundationBinding
    private var foundations: MutableList<Foundation> = mutableListOf()

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Firebase.database.reference

        binding = ActivityFoundationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarFoundation.toolbar)

        binding.appBarFoundation.fab.setOnClickListener { view ->
            addFoundation(view)
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_foundation)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_foundations_home,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        fetchAndDisplayFoundations()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.foundation, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_foundation)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun fetchAndDisplayFoundations() {
        val foundationsDatabase = database.child("foundations")

        foundationsDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again whenever data at this location is updated.
                val foundationsMap = snapshot.getValue<Map<String, Map<String, String>>>() ?: return
                foundations.clear()

                for (foundationMap in foundationsMap.values) {
                    foundations.add(
                        Foundation(
                            foundationMap["name"]!!,
                            foundationMap["description"]!!,
                            foundationMap["phoneNumber"]!!,
                            foundationMap["websiteUrl"]!!,
                            foundationMap["creationDate"]!!
                        )
                    )
                }
                foundations.sortBy { it.creationDate }
                displayFoundations()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase", "Failed to read value.", error.toException())
            }
        })

        Log.d("Fetch and display", "...")
    }

    private fun displayFoundations() {
        val mainLinearLayout = findViewById<LinearLayout>(R.id.foundations_list)

        mainLinearLayout.removeAllViews()
        for (foundation in foundations) {
            val cardLinearLayout = LinearLayout(this)
            cardLinearLayout.orientation = LinearLayout.VERTICAL

            val params = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(16, 16, 16, 16)

            // add card view
            val cardView = CardView(this)
            cardView.radius = 15f
            cardView.setCardBackgroundColor(Color.parseColor("#FFD600"))
            cardView.setContentPadding(36, 36, 36, 36)
            cardView.layoutParams = params
            cardView.cardElevation = 30f

            // generate name text view
            val name = TextView(this)
            name.text = foundation.name
            name.textSize = 24f
            name.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC)
            name.setTextColor(Color.parseColor("#E0F2F1"))

            // generate description text view
            val description = TextView(this)
            description.text = foundation.description
            description.textSize = 16f
            description.setTextColor(Color.WHITE)
            description.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL)

            // generate phoneNumber text view
            val phoneNumber = TextView(this)
            phoneNumber.text = foundation.phoneNumber
            phoneNumber.textSize = 16f
            phoneNumber.setTextColor(Color.WHITE)
            phoneNumber.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL)

            // generate website text view
            val website = TextView(this)
            website.text = foundation.websiteUrl
            website.textSize = 16f
            website.setTextColor(Color.WHITE)
            website.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL)

            // make links between card view and layout
            cardLinearLayout.addView(name)
            cardLinearLayout.addView(description)
            cardLinearLayout.addView(phoneNumber)
            cardLinearLayout.addView(website)

            cardView.addView(cardLinearLayout)

            // add card view to main layout
            mainLinearLayout.addView(cardView)
        }
    }

    private fun addFoundation(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Foundation")

        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL

        val nameInput = EditText(this)
        nameInput.inputType = InputType.TYPE_CLASS_TEXT
        nameInput.hint = "Name"
        linearLayout.addView(nameInput)

        val descriptionInput = EditText(this)
        descriptionInput.inputType = InputType.TYPE_CLASS_TEXT
        descriptionInput.hint = "Description"
        linearLayout.addView(descriptionInput)

        val phoneNumberInput = EditText(this)
        phoneNumberInput.inputType = InputType.TYPE_CLASS_TEXT
        phoneNumberInput.hint = "Phone number"
        linearLayout.addView(phoneNumberInput)

        val websiteInput = EditText(this)
        websiteInput.inputType = InputType.TYPE_CLASS_TEXT
        websiteInput.hint = "Website"
        linearLayout.addView(websiteInput)

        builder.setView(linearLayout)

        builder.setPositiveButton(
            "Save"
        ) { dialog, which ->
            saveFoundation(
                Foundation(
                    nameInput.text.toString(),
                    descriptionInput.text.toString(),
                    phoneNumberInput.text.toString(),
                    websiteInput.text.toString(),
                    OffsetDateTime.now().toString()
                )
            )
            Snackbar.make(
                view,
                "Foundation " + nameInput.text.toString() + " was added successfully",
                Snackbar.LENGTH_LONG
            )
                .setAction("Action", null).show()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }

        builder.show()
    }


    private fun saveFoundation(foundation: Foundation) {
        val foundationKey = database.child("foundations").push().key

        database.child("foundations").child(foundationKey!!).setValue(foundation)
            .addOnCompleteListener {
                Log.d("FirebaseOkayish", "Data saved correctly locally!")
            }
            .addOnSuccessListener {
                Log.d("FirebaseAwesome", "Data PUSH ONLINE correctly!")
            }
            .addOnFailureListener {
                Log.d("FirebaseError", "Error: " + it.message)
            }
    }

}