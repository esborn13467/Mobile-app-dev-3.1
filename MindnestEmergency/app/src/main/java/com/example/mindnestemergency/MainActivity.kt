package com.example.mindnestemergency

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindnestemergency.ui.theme.MindnestEmergencyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindnestEmergencyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EmergencyContactsScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Data classes
data class Contact(val title: String, val telephone: String)
data class ContactSection(val title: String, val contacts: List<Contact>)

@Composable
fun EmergencyContactsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Organized contact data
    val sections = listOf(
        ContactSection(
            "Suicide Prevention & Crisis Support",
            listOf(
                Contact("Befrienders Kenya", "+254722178177"),
                Contact("Niskize", "0900620800")
            )
        ),
        ContactSection(
            "Psychologists & Counseling",
            listOf(
                Contact("Kenya Psychological Association", "+254720685862"),
                Contact("Nairobi Women's Hospital", "1195")
            )
        ),
        ContactSection(
            "Vulnerable Communities",
            listOf(
                Contact("Childline Kenya", "116"),
                Contact("Teens Counselling Kenya", "+254790258263"),
                Contact("Q-Initiative Kenya", "+254724276662")
            )
        ),
        ContactSection(
            "Addiction & Substance Abuse",
            listOf(
                Contact("NACADA", "1192")
            )
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Text(
                text = "Guardian Tree",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        sections.forEach { section ->
            item {
                Text(
                    text = section.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
                HorizontalDivider(thickness = 1.dp)
            }

            items(section.contacts) { contact ->
                Text(
                    text = "${contact.title}: ${contact.telephone}",
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .clickable {
                            val intent = Intent(
                                Intent.ACTION_DIAL,
                                Uri.parse("tel:${contact.telephone.filter { it.isDigit() }}")
                            )
                            context.startActivity(intent)
                        }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmergencyContactsPreview() {
    MindnestEmergencyTheme {
        EmergencyContactsScreen()
    }
}