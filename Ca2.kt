
package com.example.jetpack.Projects

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


// ---------------- NAVIGATION ----------------
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "agreement") {

        composable("agreement") {
            AgreementScreen(navController)
        }

        composable("form") {
            RegistrationFormScreen(navController)
        }

        composable("details") {
            DetailsScreenRoute(navController)
        }

        composable("error") {
            ErrorScreen(navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    MaterialTheme {
        AppNavigation()
    }
}



// ---------------- SCREEN 1 ----------------
@Composable
fun AgreementScreen(navController: NavController) {

    var isChecked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier.padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    "Workshop Registration",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                Text("Please agree to continue")

                Spacer(Modifier.height(18.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = it
                            if (it) navController.navigate("form")
                        }
                    )
                    Text("I Agree to Continue")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgreementPreview() {
    MaterialTheme {
        AgreementScreen(rememberNavController())
    }
}



// ---------------- SCREEN 2 ----------------
@Composable
fun RegistrationFormScreen(navController: NavController) {

    var workerId by remember { mutableStateOf("") }
    var memberCount by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var confirmChecked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {

            Column(
                modifier = Modifier.padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    "Workshop Registration Form",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = workerId,
                    onValueChange = { workerId = it },
                    label = { Text("Worker ID") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = memberCount,
                    onValueChange = { memberCount = it },
                    label = { Text("Number of Workers") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = Color.Red)
                }

                Spacer(Modifier.height(15.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Checkbox(
                        checked = confirmChecked,
                        onCheckedChange = {

                            confirmChecked = it

                            if (it) {
                                if (workerId.isEmpty() || memberCount.isEmpty()) {
                                    errorMessage = "Please fill all fields"
                                    confirmChecked = false
                                    navController.navigate("error")
                                    return@Checkbox
                                }

                                val members = memberCount.toInt()

                                if (members <= 0) {
                                    errorMessage = "There is no member ❌"
                                    confirmChecked = false
                                    navController.navigate("error")
                                } else {
                                    errorMessage = ""

                                    navController.currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("workerId", workerId)

                                    navController.currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("members", members)

                                    navController.navigate("details")
                                }
                            }
                        }
                    )

                    Text("Confirm & Continue")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormPreview() {
    MaterialTheme {
        RegistrationFormScreen(rememberNavController())
    }
}



// ---------------- SCREEN 3 ----------------
@Composable
fun DetailsScreenRoute(navController: NavController) {

    val workerId =
        navController.previousBackStackEntry?.savedStateHandle?.get<String>("workerId") ?: "N/A"

    val members =
        navController.previousBackStackEntry?.savedStateHandle?.get<Int>("members") ?: 0

    DetailsScreen(workerId, members, navController)
}

@Composable
fun DetailsScreen(workerId: String, members: Int, navController: NavController? = null) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {

            Column(
                modifier = Modifier.padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    "Workshop Details",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(20.dp))

                Text("Worker ID: $workerId")
                Text("Total Members: $members")

                Spacer(Modifier.height(20.dp))

                navController?.let {
                    Button(onClick = { it.popBackStack() }) {
                        Text("Back")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsPreview() {
    MaterialTheme {
        DetailsScreen("WRK999", 5)
    }
}



// ---------------- ERROR SCREEN ----------------
@Composable
fun ErrorScreen(navController: NavController) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {

            Column(
                modifier = Modifier.padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("❌ Error",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )

                Spacer(Modifier.height(10.dp))

                Text("Invalid data! Please go back and try again.")

                Spacer(Modifier.height(20.dp))

                Button(onClick = { navController.popBackStack() }) {
                    Text("Go Back")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorPreview() {
    MaterialTheme {
        ErrorScreen(rememberNavController())
    }
}
