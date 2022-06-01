package com.example.refugeehelper.requests

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.refugeehelper.R
import com.example.refugeehelper.databinding.ActivityRequestBinding
import com.example.refugeehelper.requests.models.HousingRequest
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.time.OffsetDateTime

class RequestActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRequestBinding

    private lateinit var database: DatabaseReference
    private var housingRequests: MutableList<HousingRequest> = mutableListOf()

    private var currentNotificationId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Firebase.database.reference

        binding = ActivityRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarRequest.toolbar)

        binding.appBarRequest.fab.setOnClickListener { view ->
            addHousingRequest(view)
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_request)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_housing_requests,
                R.id.nav_work_requests
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        fetchAndDisplayHousingRequests()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.request, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_request)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun fetchAndDisplayHousingRequests() {
        val housingRequests = database.child("requests").child("housingRequests")

        housingRequests.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again whenever data at this location is updated.
                val requestsMap = snapshot.getValue<Map<String, HousingRequest>>() ?: return
                this@RequestActivity.housingRequests.clear()

                for (housingRequestsMap in requestsMap.values) {
                    this@RequestActivity.housingRequests.add(
                        HousingRequest(
                            housingRequestsMap.description,
                            housingRequestsMap.requestPeriod,
                            housingRequestsMap.contactPhoneNumber,
                            housingRequestsMap.creationDate,
                            housingRequestsMap.adultsNumber,
                            housingRequestsMap.childrenNumber
                        )
                    )
                }
                this@RequestActivity.housingRequests.sortBy { it.creationDate }
                displayHousingRequests()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase", "Failed to read value.", error.toException())
            }
        })

        Log.d("Fetch and display", "...")
    }

    private fun displayHousingRequests() {
        val mainLinearLayout = findViewById<LinearLayout>(R.id.housing_requests_list)

        mainLinearLayout.removeAllViews()
        for (housingRequest in housingRequests) {
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

            // generate description text view
            val description = TextView(this)
            description.text = housingRequest.description
            description.textSize = 16f
            description.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC)
            description.setTextColor(Color.parseColor("#E0F2F1"))

            // generate request period text view
            val requestPeriod = TextView(this)
            requestPeriod.text = housingRequest.requestPeriod
            requestPeriod.textSize = 16f
            requestPeriod.setTextColor(Color.WHITE)
            requestPeriod.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL)

            // generate contact number text view
            val contactNumber = TextView(this)
            val contactNumberText = "Contact number: "
            contactNumber.text = contactNumberText.plus(housingRequest.contactPhoneNumber.toString())
            contactNumber.textSize = 16f
            contactNumber.setTextColor(Color.WHITE)
            contactNumber.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL)

            // generate requesting people description text view
            val requestPeopleDescription = TextView(this)
            val initialString = "We are "
            requestPeopleDescription.text = initialString
                .plus(housingRequest.adultsNumber.toString())
                .plus(" adults taking care of ")
                .plus(housingRequest.childrenNumber.toString())
                .plus(".")
            requestPeopleDescription.textSize = 16f
            requestPeopleDescription.setTextColor(Color.WHITE)
            requestPeopleDescription.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL)

            // make links between card view and layout
            cardLinearLayout.addView(description)
            cardLinearLayout.addView(requestPeriod)
            cardLinearLayout.addView(requestPeopleDescription)
            cardLinearLayout.addView(contactNumber)

            cardView.addView(cardLinearLayout)

            // add card view to main layout
            mainLinearLayout.addView(cardView)
        }
    }

    private fun addHousingRequest(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Housing Request")

        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL

        val descriptionInput = EditText(this)
        descriptionInput.inputType = InputType.TYPE_CLASS_TEXT
        descriptionInput.hint = "Description"
        linearLayout.addView(descriptionInput)

        val contactPhoneNumber = EditText(this)
        contactPhoneNumber.inputType = InputType.TYPE_CLASS_PHONE
        contactPhoneNumber.hint = "Contact phone number"
        linearLayout.addView(contactPhoneNumber)

        val requestPeriod = EditText(this)
        requestPeriod.inputType = InputType.TYPE_CLASS_TEXT
        requestPeriod.hint = "Request period (days/months)"
        linearLayout.addView(requestPeriod)

        val adultsNumber = EditText(this)
        adultsNumber.inputType = InputType.TYPE_CLASS_NUMBER
        adultsNumber.hint = "Adults number"
        linearLayout.addView(adultsNumber)

        val childrenNumber = EditText(this)
        childrenNumber.inputType = InputType.TYPE_CLASS_NUMBER
        childrenNumber.hint = "Children number"
        linearLayout.addView(childrenNumber)

        builder.setView(linearLayout)

        builder.setPositiveButton(
            "Save"
        ) { dialog, which ->
            saveHousingRequest(
                HousingRequest(
                    descriptionInput.text.toString(),
                    requestPeriod.text.toString(),
                    contactPhoneNumber.text.toString().toInt(),
                    OffsetDateTime.now().toString(),
                    adultsNumber.text.toString().toInt(),
                    childrenNumber.text.toString().toInt()
                )
            )
            Snackbar.make(
                view,
                "Housing request was added sucessfully",
                Snackbar.LENGTH_LONG
            )
                .setAction("Action", null).show()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }

        builder.show()
    }


    private fun saveHousingRequest(request: HousingRequest) {
        notifyNewHousingRequest(request)

        val housingRequestsDatabaseReference = database.child("requests").child("housingRequests")

        val housingRequestKey = housingRequestsDatabaseReference.push().key

        housingRequestsDatabaseReference.child(housingRequestKey!!).setValue(request)
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

    private fun createNotificationChannel(channelId: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun notifyNewHousingRequest(request: HousingRequest) {
        val textTitle = "Housing request"
        val textContent = "A new housing request was added."

        val channelId = "5"
        createNotificationChannel(channelId)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(androidx.core.R.drawable.notification_template_icon_bg)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(currentNotificationId, builder.build())
        }
        currentNotificationId++
    }
}