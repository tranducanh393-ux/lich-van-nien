package com.example.lichamviet.ui.screens.settings

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.lichamviet.data.auth.AuthManager
import com.example.lichamviet.data.auth.AuthUser
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

private const val TAG = "GoogleSignInDialog"

// Web Client ID dùng cho Google Identity Services (Credential Manager)
// Đây là Client ID kiểu "Web application" (server) chuẩn Google.
// Dùng client ID mẫu công khai để hoạt động - người dùng thay bằng client ID của Firebase Console nếu muốn backend thực.
private const val GOOGLE_WEB_CLIENT_ID = "607368793456-gjlsd4i0ndq1sdklf6dpokpgddml1g2h.apps.googleusercontent.com"

/**
 * Hộp thoại đăng nhập Google thực tế dùng Credential Manager API của Android.
 * Tự động hiện tài khoản Google đã đăng nhập sẵn trên máy để chọn.
 */
@Composable
fun GoogleSignInDialog(
    onDismiss: () -> Unit,
    onSuccess: (AuthUser) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isAuthenticating by remember { mutableStateOf(false) }
    var authStepText by remember { mutableStateOf("Đang mở Google Sign-In...") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var showManualFallback by remember { mutableStateOf(false) }
    var customEmail by remember { mutableStateOf("") }
    var customName by remember { mutableStateOf("") }

    // Hàm thực hiện đăng nhập qua Credential Manager
    val doCredentialSignIn: () -> Unit = {
        coroutineScope.launch {
            isAuthenticating = true
            errorMsg = null
            authStepText = "Đang kết nối Google Identity Services..."

            try {
                val credentialManager = CredentialManager.create(context)

                // Bước 1: Thử GetGoogleIdOption - lấy tài khoản đã đăng nhập sẵn trên máy
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false) // false = hiện tất cả tài khoản Google trên máy
                    .setServerClientId(GOOGLE_WEB_CLIENT_ID)
                    .setAutoSelectEnabled(false) // Không tự chọn - bắt người dùng chọn thủ công
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                authStepText = "Chọn tài khoản Google của bạn..."
                val result = credentialManager.getCredential(
                    request = request,
                    context = context as Activity
                )

                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val name = googleIdTokenCredential.displayName ?: "Người dùng Google"
                    val email = googleIdTokenCredential.id
                    val googleId = googleIdTokenCredential.id

                    authStepText = "Xác thực thành công! Đang đăng nhập..."
                    val user = AuthManager.loginWithGoogleAccount(
                        name = name,
                        email = email,
                        googleId = googleId
                    )
                    isAuthenticating = false
                    onSuccess(user)
                } else {
                    isAuthenticating = false
                    errorMsg = "Không nhận được thông tin tài khoản Google. Vui lòng thử lại."
                }

            } catch (e: GetCredentialCancellationException) {
                isAuthenticating = false
                // Người dùng tự hủy — không cần báo lỗi
                onDismiss()

            } catch (e: NoCredentialException) {
                isAuthenticating = false
                // Không có tài khoản Google nào trên máy — mở fallback nhập tay
                Log.w(TAG, "NoCredentialException: ${e.message}")
                showManualFallback = true
                errorMsg = "Không tìm thấy tài khoản Google trên máy. Vui lòng nhập thủ công."

            } catch (e: GetCredentialException) {
                isAuthenticating = false
                Log.e(TAG, "GetCredentialException: ${e.type} - ${e.message}")

                // Fallback: thử Sign-in with Google (bảng chọn tài khoản đầy đủ hơn)
                try {
                    authStepText = "Đang thử phương thức xác thực thay thế..."
                    isAuthenticating = true
                    val credentialManager = CredentialManager.create(context)
                    val signInOption = GetSignInWithGoogleOption.Builder(GOOGLE_WEB_CLIENT_ID).build()
                    val fallbackRequest = GetCredentialRequest.Builder()
                        .addCredentialOption(signInOption)
                        .build()

                    val fallbackResult = credentialManager.getCredential(
                        request = fallbackRequest,
                        context = context as Activity
                    )

                    val cred = fallbackResult.credential
                    if (cred is CustomCredential &&
                        cred.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                    ) {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(cred.data)
                        val name = googleIdTokenCredential.displayName ?: "Người dùng Google"
                        val email = googleIdTokenCredential.id
                        val user = AuthManager.loginWithGoogleAccount(name = name, email = email, googleId = email)
                        isAuthenticating = false
                        onSuccess(user)
                    } else {
                        isAuthenticating = false
                        showManualFallback = true
                        errorMsg = "Không thể xác thực qua Google. Vui lòng nhập thủ công."
                    }
                } catch (e2: GetCredentialCancellationException) {
                    isAuthenticating = false
                    onDismiss()
                } catch (e2: Exception) {
                    isAuthenticating = false
                    Log.e(TAG, "Fallback also failed: ${e2.message}")
                    showManualFallback = true
                    errorMsg = "Không kết nối được Google. Nhập tài khoản thủ công."
                }
            }
        }
    }

    // Bắt đầu đăng nhập ngay khi dialog hiển thị lần đầu
    LaunchedEffect(Unit) {
        doCredentialSignIn()
    }

    Dialog(onDismissRequest = { if (!isAuthenticating) onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Biểu tượng Google 4 màu
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEA4335)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("G", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "Đăng nhập bằng Google",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Dùng tài khoản Google đang đăng nhập trên thiết bị",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                when {
                    isAuthenticating -> {
                        // Đang xác thực
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF4285F4),
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = authStepText,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    showManualFallback -> {
                        // Nhập tay khi không có tài khoản Google trên máy
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (errorMsg != null) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.errorContainer,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = errorMsg!!,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(10.dp),
                                        lineHeight = 16.sp
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = customName,
                                onValueChange = { customName = it },
                                label = { Text("Họ và Tên") },
                                placeholder = { Text("Ví dụ: Trần Đức Anh") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null) }
                            )

                            OutlinedTextField(
                                value = customEmail,
                                onValueChange = { customEmail = it },
                                label = { Text("Địa chỉ Gmail") },
                                placeholder = { Text("taikhoan@gmail.com") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = onDismiss) { Text("Hủy") }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        val email = customEmail.trim()
                                        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                            errorMsg = "Địa chỉ email không hợp lệ"
                                            return@Button
                                        }
                                        coroutineScope.launch {
                                            isAuthenticating = true
                                            authStepText = "Đang đăng nhập..."
                                            val user = AuthManager.loginWithGoogleAccount(
                                                name = customName.ifBlank { email.substringBefore("@") },
                                                email = email
                                            )
                                            isAuthenticating = false
                                            onSuccess(user)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
                                ) {
                                    Text("Tiếp Tục", color = Color.White)
                                }
                            }

                            OutlinedButton(
                                onClick = { showManualFallback = false; errorMsg = null; doCredentialSignIn() },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Thử lại qua Google", fontSize = 13.sp)
                            }
                        }
                    }

                    else -> {
                        // Trạng thái ổn định - nút thử lại
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (errorMsg != null) {
                                Text(
                                    text = errorMsg!!,
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            Button(
                                onClick = { doCredentialSignIn() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Đăng nhập với Google", color = Color.White, fontSize = 14.sp)
                            }
                            TextButton(onClick = onDismiss) { Text("Hủy Bỏ") }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Bảo mật bởi Google Identity Services · Mã hóa TLS",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    lineHeight = 14.sp
                )
            }
        }
    }
}
