package com.jefryjacky.auth.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefryjacky.auth.ui.theme.AuthenticationTheme
import com.jefryjacky.auth.ui.theme.Typography

@Composable
fun AkseleranOtp(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String? = null,
    digitCount: Int = 6
) {
    val context = LocalContext.current
    Column(modifier = modifier.wrapContentWidth()) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (it.length <= digitCount) {
                    onValueChange(it)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            decorationBox = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(digitCount) { index ->
                        Digit(index, value)
                    }
                }
            }
        )
        val errorMessageString = errorMessage
        if(!errorMessageString.isNullOrBlank()){
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessageString,
                style = Typography.bodySmall,
                color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun Digit(
    index: Int,
    text: String,
) {
    val digit: String = if (index < text.length) text[index].toString() else ""
    val boxColor: Color = if (digit.isNotEmpty()) MaterialTheme.colorScheme.primary else Color.LightGray

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = digit,
            color = MaterialTheme.colorScheme.primary,
            style = Typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .background(boxColor)
                .height(2.dp)
                .width(40.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewOtpField() {
    var value by remember {
        mutableStateOf("123")
    }
    AkseleranOtp(
        modifier = Modifier.padding(16.dp),
        value = value,
        errorMessage = "invalid otp number",
        onValueChange = { value = it }
    )
}