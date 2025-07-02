package com.example.mindnest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mindnest.distract.DistractionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MindnestEmergencyApp()
        }
    }
}

@Composable
fun MindnestEmergencyApp() {
    val navController = rememberNavController()
    MindnestEmergencyTheme {
        NavHost(navController, startDestination = "login") {
            composable("login") { LoginScreen(navController) }
            composable("register") { RegistrationScreen(navController) }
            composable("home") { HomeScreen(navController) }
            composable("emergency") { EmergencyContactsScreen(navController) }
            composable("distract") { DistractMeScreen(navController) }
            composable("selfcare") { SelfCareScreen(navController) }
            composable("distract-content") { DistractMeContentScreen()
            }
        }
    }
}

// ===== SCREEN DEFINITIONS =====

// 1. Login Screen
@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign In")
        }
        TextButton(onClick = { navController.navigate("register") }) {
            Text("Don't have an account? Register")
        }
    }
}

// 2. Registration Screen
@Composable
fun RegistrationScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = birthday,
            onValueChange = { birthday = it },
            label = { Text("Date of Birth") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }
        TextButton(onClick = { navController.navigate("login") }) {
            Text("Already have an account? Login")
        }
    }
}

// 3. Home Screen
@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Mindnest", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(48.dp))

        // Navigation Buttons
        NavigationButton(
            icon = Icons.Default.Warning,
            text = "Emergency Contacts",
            onClick = { navController.navigate("emergency") }
        )

        NavigationButton(
            icon = Icons.Default.Star,
            text = "Distract Me",
            onClick = { navController.navigate("distract") }
        )

        NavigationButton(
            icon = Icons.Default.Face,
            text = "Self Care",
            onClick = { navController.navigate("selfcare") }
        )
    }
}

// 4. Emergency Contacts Screen
@Composable
fun EmergencyContactsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val contacts = listOf(
        "Befrienders Kenya" to "+254722178177",
        "Niskize Kenya" to "0900620800",
        "Childline Kenya" to "116",
        "Kenya Psychological Association" to "+254720685862",
        "Nairobi Women's Hospital" to "1195",
        "Q-Initiative Kenya" to "+254724276662",
        "NACADA" to "1192"
    )


    Column(Modifier.fillMaxSize().padding(16.dp)) {
        // Simple header with back button
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                "Emergency Contacts",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(Modifier.padding(16.dp)) {
            items(contacts) { (name, number) ->
                ContactItem(name, number) {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = "tel:$number".toUri()
                    }
                    context.startActivity(intent)
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.LightGray
                )
            }
        }
    }
}

// 5. Distract Me Screen
@Composable
fun DistractMeScreen(navController: NavHostController) {
    val games = listOf(
        "Breathing Exercise",
        "5-4-3-2-1 Grounding",
        "Quick Doodle"
    )


    Column(Modifier.fillMaxSize().padding(16.dp)) {
        // Simple header with back button
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                "Distract Me",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(Modifier.padding(16.dp)) {
            items(games) { game ->
                Text(
                    text = game,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .clickable {
                            if (game == "Quick Doodle") {
                                navController.navigate("distract-content")
                            }
                        }
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.LightGray
                )
            }
        }
    }
}

// A Distract Me Content Screen that shows a quote/fact/joke pulled from Room DB
@Composable
fun DistractMeContentScreen() {
    // Grabs the ViewModel instance
    val viewModel: DistractionViewModel = viewModel()

    // Observes the current distraction from the ViewModel
    val distraction by viewModel.distraction.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text("Your Distraction", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(40.dp))

        // Displays the distraction or "Loading..." if null
        Text(
            text = distraction?.content ?: "Loading...",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Button to fetch a new random distraction
        Button(onClick = { viewModel.refreshDistraction() }) {
            Text("Show Me Another")
        }
    }
}


// 6. Self Care Screen
@Composable
fun SelfCareScreen(navController: NavHostController) {

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        // Simple header with back button
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                "Self-care",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Column(Modifier.padding(16.dp)) {
            Text("Coming soon!", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

// ===== REUSABLE COMPONENTS =====

@Composable
fun NavigationButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(icon, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@Composable
fun ContactItem(name: String, number: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Call, contentDescription = null)
        Spacer(Modifier.width(16.dp))
        Column {
            Text(name, style = MaterialTheme.typography.bodyLarge)
            Text(number, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// ===== THEME SETUP =====

@Composable
fun MindnestEmergencyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF7A86B6),  // Soft Indigo
            secondary = Color(0xFFC3AED6), // Lavender
            tertiary = Color(0xFFFFB8A2)  // Coral Peach
        ),
        content = content
    )
}