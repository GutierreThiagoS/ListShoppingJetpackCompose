package com.example.mylists.framework.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mylists.NavigationScreen
import com.example.mylists.framework.ui.theme.MyListsTheme
import com.example.mylists.framework.ui.theme.TealBlack10
import org.koin.androidx.compose.koinViewModel


@Composable
fun FragmentMessage() {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column(modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .fillMaxHeight()
        ) {

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainPreview() {
    MyListsTheme {
        ToolbarAppBar()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarAppBar(viewModel: MainViewModel = koinViewModel()) {

    val navigationState by viewModel.navigationState.collectAsState()
    val total by viewModel.total.collectAsState(0f)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = navigationState.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = TealBlack10,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )
                    )
                },
                /*navigationIcon = {
                    IconButton(onClick = {  doSomething()  }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },*/
                actions = {
                    if (navigationState.title == NavigationScreen.SHOPPING.label) {
                        Row {
                            Icon(
                                modifier = Modifier.padding(end = 5.dp)
                                    .align(Alignment.CenterVertically),
                                imageVector = Icons.Filled.Calculate,
                                contentDescription = "Localized description"
                            )
                            Text(
                                modifier = Modifier.padding(end = 5.dp)
                                    .align(Alignment.CenterVertically),
                                text = String.format("R$ %.2f", total ?: 0f),
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
            BottomMenu(contentPadding = innerPadding)
        }
    )
}