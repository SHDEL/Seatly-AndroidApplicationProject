package com.example.seatly.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Theaters
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seatly.R
import com.example.seatly.ui.theme.SeatlyTheme

@Composable
fun LoginScreen(onLoginClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Outlined.Theaters,
            contentDescription = "App Logo",
            modifier = Modifier.size(60.dp),
            tint = Color(0xFFE50914)
        )
        Text(
            text = "Seatly",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE50914),
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Welcome Back!",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
        Text(
            text = "Sign in to continue your movie journey",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Email or Username") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )
        TextButton(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.End)) {
            Text("Forgot Password?", color = Color(0xFFE50914))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Login", fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray)
            Text(" Or sign in with ", color = Color.Gray, modifier = Modifier.padding(horizontal = 8.dp))
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ) {
            SocialLoginButton(R.drawable.ic_google)
            SocialLoginButton(R.drawable.ic_instagram)
            SocialLoginButton(R.drawable.ic_facebook)
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Don't have an account?", color = Color.Gray)
            TextButton(onClick = { /*TODO*/ }) {
                Text("Sign up", color = Color(0xFFE50914), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SocialLoginButton(iconRes: Int) {
    OutlinedButton(
        onClick = { /*TODO*/ },
        modifier = Modifier.size(50.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp)
    ) {
        Image(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun LoginScreenPreview() {
    SeatlyTheme(darkTheme = true) {
        LoginScreen(onLoginClick = {})
    }
}
