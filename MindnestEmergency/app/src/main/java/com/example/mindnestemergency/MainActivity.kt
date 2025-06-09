package com.example.mindnestemergency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mindnestemergency.ui.theme.MindnestEmergencyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main) {
            val contacts = listOf(
                EmergencyContact(ER1,"Befrienders Kenya""+254 722 178 177", R.drawable.ic_dove),
                EmergencyContact(ER2,"Niskize""0900 620 800", R.drawable.ic_plant),
                EmergencyContact(ER2,"Kenya Pyschological Association""+254 720 685 864", R.drawable.ic_couch)


            )
        }
    }
}

data class EmergencyContact(
    val id: Int,// For storage
    val name: String, // Number Identification
    val number: String, //Phone number
    val iconResId: Int  // For icon images
)

@Composable
fun Contacts() {
}

@Preview(showBackground = true)
@Composable
fun ContactsPreview() {
    MindnestEmergencyTheme {

    }
}