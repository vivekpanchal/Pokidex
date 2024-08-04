package com.vivek.pokidex.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SearchBarPokeDex(query: String, onQueryChange: (String) -> Unit, onSearch: () -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search by name") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        trailingIcon = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    )
}