package com.vivek.pokidex.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SortMenu(selectedOrder: SortOrder, onOrderSelected: (SortOrder) -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(8.dp)) {
        Button(onClick = { expanded = true }) {
            Text("Sort")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(text = { "Sort by Level" }, onClick = { onOrderSelected(SortOrder.Level) })
            DropdownMenuItem(text = { "Sort by HP" }, onClick = { onOrderSelected(SortOrder.Hp) })
        }
    }
}


enum class SortOrder {
    None, Level, Hp
}