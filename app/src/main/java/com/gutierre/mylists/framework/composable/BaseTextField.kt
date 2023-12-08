package com.gutierre.mylists.framework.composable

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.EditCalendar
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.gutierre.mylists.framework.ui.theme.LightGray
import com.gutierre.mylists.framework.utils.logE
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalComposeUiApi::class)
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
                        .background(if (i % 2 == 0) LightGray else Color.White)
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DateSelector(
    toDoItemDateFinal: String?,
    activity: AppCompatActivity,
    resultDate: (value: TextFieldValue) -> Unit
) {

    var selectedDate by remember { mutableStateOf<Date?>(Date()) }

    val keyboardController = LocalSoftwareKeyboardController.current

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val dateText = selectedDate?.let { dateFormat.format(it) } ?: "Selecione a data"

    var text by remember { mutableStateOf(TextFieldValue(toDoItemDateFinal ?: dateText)) }

    resultDate(text)

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(5f)
                .clickable {
                    getDataPicker(
                        activity,
                        selectedDate
                    ) { resultDate ->
                        selectedDate = resultDate
                        if (resultDate != null) {
                            text = TextFieldValue(dateFormat.format(resultDate))
                            resultDate(text)
                        }
                        keyboardController?.hide()
                    }
                },
            value = text,
            onValueChange = {
                val formatDate = formatDateNumber(it.text)
                text = TextFieldValue(text = formatDate, selection = TextRange(formatDate.length))
                resultDate(text)
            },
            label = { Text("Prazo Final") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )

        IconButton(
            modifier = Modifier
                .weight(1f),
            onClick = {
                getDataPicker(
                    activity,
                    selectedDate
                ) { resultDate ->
                    selectedDate = resultDate
                    if (resultDate != null) {
                        text = TextFieldValue(dateFormat.format(resultDate))
                        resultDate(text)
                    }
                    keyboardController?.hide()
                }
            }
        ) {
            Icon(imageVector = Icons.Outlined.EditCalendar, contentDescription = "CalendarToday")
        }
    }
}

fun getDataPicker(
    activity: AppCompatActivity,
    selectedDate: Date?,
    returnDate: (date: Date?) -> Unit
) {
    val date = selectedDate ?: Date()
    val calendar = Calendar.getInstance()
    calendar.time = date

    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setSelection(calendar.timeInMillis)
        .build()

    datePicker.addOnPositiveButtonClickListener { selectedDateMillis ->
        returnDate(Date(selectedDateMillis + 50000000L))
    }

    datePicker.show(activity.supportFragmentManager, datePicker.toString())
}

private fun formatDateNumber(input: String): String {

    val cleanInput = input.replace(Regex("[^0-9]"), "")
    val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timerFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    val date = Date()

    return buildString {
        Log.e("buildString", this.toString())

        val day = cleanInput.substring(0, if (cleanInput.length >= 2) 2 else cleanInput.length)

        if (cleanInput.length <= 2) {
            append(getDayInMoth(day, "12"))
        } else {
            val moth = cleanInput.substring(2,  if (cleanInput.length >= 4) 4 else cleanInput.length)
            val mothReal = if ((moth.toIntNotNull()) > 12) "12" else if (moth == "00") "01" else moth
            append(getDayInMoth(day, mothReal))
            append("/")
            append(mothReal)
        }
        if (cleanInput.length > 4) {
            append("/")
            val yearMoment = yearFormat.format(date)
            val year = cleanInput.substring(4, if (cleanInput.length >= 8) 8 else cleanInput.length)
            append(if (year.length == 4 && year.toIntNotNull() < yearMoment.toIntNotNull()) yearMoment else year)
        }
    }.let {
        val calender = Calendar.getInstance()
        calender.time = date
        val form = timerFormat.format(calender.time)
        Log.e("form", "$form  ")

        if (it.length >= 10) {
            val dateParse = dateFormat.parse(it)
            val form2 = if (dateParse != null) timerFormat.format(dateParse) else it //to yyyyMMdd
            Log.e("form2", "$form2  ")
            Log.e("calender", "${calender.time.time} dateParse ${dateParse?.time} ")
            if (dateParse != null && date.time < dateParse.time) {
                dateFormat.format(dateParse)
            } else dateFormat.format(calender.time)
        } else it
    }
}

private fun getDayInMoth(day: String, moth: String): String {
    return if (moth.toIntOrNull() == 2 && day.toIntNotNull() > 29) {
        "29"
    } else if (listOf(1, 3, 5, 7, 8, 10, 12).contains(moth.toIntNotNull()) && day.toIntNotNull() > 31) {
        "31"
    } else if (listOf(4, 6, 9, 11).contains(moth.toIntNotNull()) && day.toIntNotNull() > 30) {
        "30"
    } else if (day == "00") "01"
    else day
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HourSelectorAlert(
    toDoItemHourInitAlert: String?,
    activity: AppCompatActivity,
    resultHour: (value: TextFieldValue) -> Unit
) {

    var selectedDate by remember { mutableStateOf<Date?>(Date()) }

    val keyboardController = LocalSoftwareKeyboardController.current

    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val dateText = selectedDate?.let { dateFormat.format(it) } ?: "Selecione a data"

    var text by remember { mutableStateOf(TextFieldValue(toDoItemHourInitAlert ?: dateText)) }

    resultHour(text)

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(5f)
                .clickable {
                    getHourPicker(
                        dateText,
                        activity,
                    ) { resultDate ->
                        selectedDate = dateFormat.parse(resultDate)
                        selectedDate?.let {
                            text = TextFieldValue(dateFormat.format(it))
                            resultHour(text)
                        }
                        keyboardController?.hide()
                    }
                },
            value = text,
            onValueChange = {
                val formatDate = formatHourNumber(it.text)
                text = TextFieldValue(text = formatDate, selection = TextRange(formatDate.length))
                resultHour(it)
            },
            label = { Text("Hora Alarme") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )

        IconButton(
            modifier = Modifier
                .weight(1f),
            onClick = {
                getHourPicker(
                    dateText,
                    activity,
                ) { resultDate ->
                    selectedDate = dateFormat.parse(resultDate)
                    selectedDate?.let {
                        text = TextFieldValue(dateFormat.format(it))
                        resultHour(text)
                    }
                    keyboardController?.hide()
                }
            }
        ) {
            Icon(imageVector = Icons.Outlined.AccessTime, contentDescription = "AccessTime")
        }
    }
}

fun getHourPicker(
    dateText: String,
    activity: AppCompatActivity,
    returnDate: (String) -> Unit
) {

    val datePicker = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
        .setHour(dateText.split(":").firstOrNull()?.toIntOrNull() ?: 12)
        .setMinute(dateText.split(":").lastOrNull()?.toIntOrNull() ?: 0)
        .setTitleText("Selecione a Hora")
        .build()

    datePicker.addOnPositiveButtonClickListener {
        val hour = datePicker.hour
        val minute = datePicker.minute
        Log.e("MyActivity", "Selected time: $hour:$minute")
        returnDate("$hour:$minute")
    }

    datePicker.show(activity.supportFragmentManager, datePicker.toString())
}
private fun formatHourNumber(input: String): String {

    val cleanInput = input.replace(Regex("[^0-9]"), "")

    return buildString {
        Log.e("buildString", this.toString())
        if (cleanInput.length >= 2) {
            val hour = cleanInput.substring(0, 2)
            append(if (hour.toIntNotNull() < 24) hour else "00")
            if (cleanInput.length >= 3) append(":")
            if (cleanInput.length >= 4) {
                val minutes = cleanInput.substring(2, 4)
                append(if (minutes.toIntNotNull() < 60) minutes else "00")
            } else append(cleanInput.substring(2, cleanInput.length))
        } else append(input)
    }
}

fun String?.toIntNotNull(valueIsNull: Int = 0): Int {
    return try {
        when {
            this == null -> valueIsNull
            isBlank() -> valueIsNull
            else -> toInt()
        }
    } catch (e: Exception) {
        logE("toIntNotNull $e")
        valueIsNull
    }
}