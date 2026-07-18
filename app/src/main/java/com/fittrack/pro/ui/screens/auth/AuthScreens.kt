package com.fittrack.pro.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import com.fittrack.pro.ui.components.FitTrackButton
import com.fittrack.pro.ui.theme.*
import com.fittrack.pro.viewmodel.AuthViewModel

@Composable
fun LoginScreen(authVM: AuthViewModel, onLoginSuccess: () -> Unit, onRegisterClick: () -> Unit, onForgotClick: () -> Unit) {
    val state by authVM.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPw by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is AuthViewModel.AuthState.Success) { authVM.resetState(); onLoginSuccess() }
    }

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(DeepBlue, GradientEnd)))) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(80.dp))
            Text("🏃", fontSize = 56.sp)
            Spacer(Modifier.height(16.dp))
            Text("Welcome Back!", style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Bold)
            Text("Sign in to continue your fitness journey", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(0.7f))
            Spacer(Modifier.height(40.dp))

            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Login", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        trailingIcon = { IconButton({ showPw = !showPw }) {
                            Icon(if (showPw) Icons.Default.VisibilityOff else Icons.Default.Visibility, null) } },
                        visualTransformation = if (showPw) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    TextButton(onClick = onForgotClick, modifier = Modifier.align(Alignment.End)) {
                        Text("Forgot Password?", color = DeepBlue) }
                    AnimatedVisibility(state is AuthViewModel.AuthState.Error) {
                        Text((state as? AuthViewModel.AuthState.Error)?.message ?: "",
                            color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
                    FitTrackButton(text = "Sign In", onClick = { authVM.login(email.trim(), password) },
                        modifier = Modifier.fillMaxWidth(), enabled = email.isNotBlank() && password.isNotBlank(),
                        isLoading = state is AuthViewModel.AuthState.Loading)
                }
            }
            Spacer(Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account? ", color = Color.White.copy(0.8f))
                Text("Sign Up", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.clickable(onClick = onRegisterClick))
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
fun RegisterScreen(authVM: AuthViewModel, onRegisterSuccess: () -> Unit, onLoginClick: () -> Unit) {
    val state by authVM.authState.collectAsState()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var showPw by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is AuthViewModel.AuthState.Success) { authVM.resetState(); onRegisterSuccess() }
    }

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(GradientGreen1, DeepBlue)))) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(60.dp))
            Text("💪", fontSize = 56.sp)
            Text("Create Account", style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Bold)
            Text("Start your fitness journey today", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(0.8f))
            Spacer(Modifier.height(32.dp))

            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("Register", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        trailingIcon = { IconButton({ showPw = !showPw }) {
                            Icon(if (showPw) Icons.Default.VisibilityOff else Icons.Default.Visibility, null) } },
                        visualTransformation = if (showPw) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(value = confirm, onValueChange = { confirm = it }, label = { Text("Confirm Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        visualTransformation = PasswordVisualTransformation(), isError = confirm.isNotEmpty() && confirm != password,
                        singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    AnimatedVisibility(state is AuthViewModel.AuthState.Error) {
                        Text((state as? AuthViewModel.AuthState.Error)?.message ?: "",
                            color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
                    FitTrackButton(text = "Create Account",
                        onClick = { if (password == confirm) authVM.register(email.trim(), password, name.trim()) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = name.isNotBlank() && email.isNotBlank() && password.isNotBlank() && password == confirm,
                        isLoading = state is AuthViewModel.AuthState.Loading, colors = listOf(GradientGreen1, GradientGreen2))
                }
            }
            Spacer(Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Already have an account? ", color = Color.White.copy(0.8f))
                Text("Sign In", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.clickable(onClick = onLoginClick))
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
fun ForgotPasswordScreen(authVM: AuthViewModel, onBack: () -> Unit) {
    val state by authVM.authState.collectAsState()
    var email by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(GradientPurple1, DeepBlue)))) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(60.dp))
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.Start)) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White) }
            Spacer(Modifier.height(40.dp))
            Text("🔐", fontSize = 64.sp)
            Spacer(Modifier.height(16.dp))
            Text("Reset Password", style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Bold)
            Text("Enter your email to receive a reset link", color = Color.White.copy(0.8f), style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(40.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    AnimatedVisibility(state is AuthViewModel.AuthState.Error) {
                        val msg = (state as? AuthViewModel.AuthState.Error)?.message ?: ""
                        Text(msg, color = if (msg.contains("sent")) EmeraldGreen else MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall) }
                    FitTrackButton(text = "Send Reset Link", onClick = { authVM.forgotPassword(email.trim()) },
                        modifier = Modifier.fillMaxWidth(), enabled = email.isNotBlank(),
                        isLoading = state is AuthViewModel.AuthState.Loading, colors = listOf(GradientPurple1, GradientPurple2))
                }
            }
        }
    }
}
