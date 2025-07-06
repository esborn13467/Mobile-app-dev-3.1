package com.example.mindnest

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import model.User
import model.UserRepository
import com.example.mindnest.ui.MindNestDatabase
import model.ReminderReceiver
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.mindnest.distract.DistractionViewModel


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MindNestDatabase.getDatabase(this)
        setContent {
            MindnestEmergencyApp()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
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
            composable("breathing") { BreathingExerciseScreen(navController) }
            composable("grounding") { GroundingExerciseScreen(navController) }
        }
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red)
            Spacer(Modifier.height(8.dp))
        }

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
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Please enter email and password"
                } else {
                    coroutineScope.launch {
                        val user = userRepository.loginUser(email, password)
                        if (user != null) {
                            navController.navigate("home")
                        } else {
                            errorMessage = "Invalid email or password"
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign In")
        }
        TextButton(onClick = { navController.navigate("register") }) {
            Text("Don't have an account? Register")
        }
    }
}

@Composable
fun RegistrationScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red)
            Spacer(Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
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
            onClick = {
                when {
                    username.isBlank() -> errorMessage = "Username required"
                    email.isBlank() -> errorMessage = "Email required"
                    !email.contains("@") -> errorMessage = "Invalid email format"
                    password.length < 6 -> errorMessage = "Password too short (min 6 characters)"
                    birthday.isBlank() -> errorMessage = "Birthday required"
                    else -> {
                        coroutineScope.launch {
                            val user = User(
                                username = username,
                                email = email,
                                password = password, // Should be hashed!
                                birthday = birthday
                            )
                            val success = userRepository.registerUser(user)
                            if (success) {
                                navController.navigate("home")
                            } else {
                                errorMessage = "Email already registered"
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }
        TextButton(onClick = { navController.navigate("login") }) {
            Text("Already have an account? Login")
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to Mindnest", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(48.dp))

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
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Emergency Contacts", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(start = 8.dp))
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
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun DistractMeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: DistractionViewModel = viewModel()
    val distraction by viewModel.distraction.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Distract Me", style = MaterialTheme.typography.headlineSmall)
        }

        Spacer(Modifier.height(16.dp))

        // Random distraction section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Random Distraction", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))
                Text(
                    text = distraction?.content ?: "Loading...",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                Button(onClick = { viewModel.refreshDistraction() }) {
                    Text("Get Another Distraction")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Existing activities grid
        Text("Guided Activities", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        val activities = listOf(
            DistractActivity("Breathing Bubble", "Follow the bubble to calm your mind"),
            DistractActivity("5-4-3-2-1 Grounding", "A technique to reduce anxiety"),
            DistractActivity("Positive Affirmations", "Boost your mood instantly")
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(activities) { activity ->
                DistractCard(
                    activity = activity,
                    onClick = {
                        when (activity.title) {
                            "Breathing Bubble" -> navController.navigate("breathing")
                            "5-4-3-2-1 Grounding" -> navController.navigate("grounding")
                            else -> {
                                Toast.makeText(
                                    context,
                                    "${activity.title} launched",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BreathingExerciseScreen(navController: NavHostController) {
    var isBreathingIn by remember { mutableStateOf(true) }
    var count by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000L) // 4 seconds per breath cycle
            isBreathingIn = !isBreathingIn
            if (!isBreathingIn) count++
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Breathing Exercise", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(
                    color = if (isBreathingIn) Color(0xFF7A86B6) else Color(0xFFC3AED6)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isBreathingIn) "Breathe In" else "Breathe Out",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
        }

        Spacer(Modifier.height(24.dp))
        Text("Cycle count: $count", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(32.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Back to Distractions")
        }
    }
}


@Composable
fun GroundingExerciseScreen(navController: NavHostController) {
    val steps = listOf(
        "5 things you can see around you",
        "4 things you can touch right now",
        "3 things you can hear at this moment",
        "2 things you can smell or like the smell of",
        "1 thing you can taste or like the taste of"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("5-4-3-2-1 Grounding", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))

        steps.forEachIndexed { index, step ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${5 - index}",
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(text = step, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Spacer(Modifier.height(32.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Back to Distractions")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SelfCareScreen(navController: NavHostController) {
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Self-care", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(Modifier.height(24.dp))

        Text("Set a self-care reminder message:", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("E.g. Drink water, breathe deeply...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = { scheduleReminder(context, message) }, modifier = Modifier.fillMaxWidth()) {
            Text("Set Reminder (after 10 seconds)")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
fun scheduleReminder(context: Context, message: String) {
    val intent = Intent(context, ReminderReceiver::class.java).apply {
        putExtra("reminderMessage", message)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val triggerTime = System.currentTimeMillis() + 10_000

    if (alarmManager.canScheduleExactAlarms()) {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        Toast.makeText(context, "Reminder set!", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Enable exact alarms in settings", Toast.LENGTH_LONG).show()
    }
}

// Data classes and reusable components
data class DistractActivity(val title: String, val description: String)

@Composable
fun DistractCard(activity: DistractActivity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = activity.title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = activity.description,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun NavigationButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
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

@Composable
fun HorizontalDivider() {
    Divider(
        modifier = Modifier.padding(vertical = 8.dp),
        thickness = 1.dp,
        color = Color.LightGray
    )
}

@Composable
fun MindnestEmergencyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF7A86B6),
            secondary = Color(0xFFC3AED6),
            tertiary = Color(0xFFFFB8A2)
        ),
        content = content
    )
}

