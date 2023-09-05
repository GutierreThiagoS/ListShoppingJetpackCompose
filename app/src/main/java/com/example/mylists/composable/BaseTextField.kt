package com.example.mylists.composable

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.example.mylists.framework.ui.theme.GrayLight

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AutocompleteOutlinedTextField(
    value: String,
    suggestions: List<String>,
    onValueChange: (String) -> Unit,
    view: View = LocalView.current,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    label: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {

    var isDropdownVisible by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    val filteredSuggestions by remember(value) {
        derivedStateOf {
            suggestions.filter { it.contains(value, ignoreCase = true) }
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = { newText ->
            onValueChange(newText)
            isDropdownVisible = true
        },
        label = label,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        isError = isError,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isDropdownVisible = focusState.isFocused
                isFocused = focusState.isFocused
        },
        trailingIcon = if (isDropdownVisible) {
            {
                IconButton(onClick = { isDropdownVisible = false }) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = "")
                }
            }
        } else trailingIcon,
    )
/*
    CompositionLocalProvider(LocalTextSelectionColors provides TextSelectionColors(handleColor = if (isError) Color.Red else Color.Black, backgroundColor = Color.Blue)) {
        BasicTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                isDropdownVisible = true
            },
            readOnly = readOnly,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            modifier = if (label != null) {
                modifier
                    .fillMaxWidth()
                    .semantics(mergeDescendants = true) {}
                    .padding(top = 8.dp)
            } else {
                modifier
                    .fillMaxWidth()
            }
                .defaultMinSize(
                    minWidth = TextFieldDefaults.MinWidth,
                    minHeight = TextFieldDefaults.MinHeight
                )
                .onFocusChanged { focusState ->
                    isDropdownVisible = focusState.isFocused
                    isFocused = focusState.isFocused
                },
            maxLines = maxLines,
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.OutlinedTextFieldDecorationBox(
                    value = value,
                    visualTransformation = visualTransformation,
                    innerTextField = innerTextField,
                    placeholder = placeholder,
                    label = label,
                    leadingIcon = leadingIcon,
                    trailingIcon = if (isDropdownVisible) {
                        {
                            IconButton(onClick = { isDropdownVisible = false }) {
                                Icon(imageVector = Icons.Outlined.Close, contentDescription = "")
                            }
                        }
                    } else trailingIcon,
                    supportingText = supportingText,
                    singleLine = singleLine,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors,
                    container = {
                        TextFieldDefaults.OutlinedBorderContainerBox(
                            enabled = enabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            colors = colors,
                            shape = shape
                        )
                    }
                )
            }
        )
    }*/

    if (isDropdownVisible) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
                .background(Color.White)
                .border(
                    1.dp, Color.Gray, MaterialTheme.shapes.small.copy(
                        topStart = CornerSize(0.dp),
                        topEnd = CornerSize(0.dp)
                    )
                )
        ) {
            itemsIndexed(filteredSuggestions) { i, suggestion ->
                Text(
                    text = suggestion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onValueChange(suggestion)
                            isDropdownVisible = false
                        }
                        .background(if (i % 2 == 0) GrayLight else Color.White)
                        .padding(start = 16.dp, top = 6.dp, bottom = 6.dp)
                )
            }
        }
    }

    DisposableEffect(view) {
        onDispose {
            keyboardController?.hide()
        }
    }
}