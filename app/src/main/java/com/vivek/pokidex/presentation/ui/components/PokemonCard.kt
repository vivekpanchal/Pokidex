package com.vivek.pokidex.presentation.ui.components

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.vivek.pokidex.data.local.entity.PokemonEntity
import com.vivek.pokidex.data.remote.models.Ability
import com.vivek.pokidex.data.remote.models.Attack
import com.vivek.pokidex.data.remote.models.ImagesDTO
import com.vivek.pokidex.data.remote.models.Resistance
import com.vivek.pokidex.data.remote.models.Weakness
import com.vivek.pokidex.domain.model.Pokemon
import org.jetbrains.annotations.Async

@Composable
fun PokemonCard(pokemon: Pokemon, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .clickable { onClick() },
    ) {

        Row(Modifier.padding(16.dp)) {
            AsyncImage(
                model = pokemon.images.large,
                contentDescription = "Pokeomon Image",
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(Modifier.weight(2f)) {
                Text(
                    text = pokemon.name,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = pokemon.types.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Level: ${pokemon.level}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "HP: ${pokemon.hp}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun TypeTag(type: String) {
    Text(
        text = type,
        color = Color.White,
        modifier = Modifier
            .background(Color.LightGray, RoundedCornerShape(50)) // Adjust the color based on type
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}


@Composable
@Preview(showBackground = true)
fun PokemonCardPreview() {
    // Sample data for the preview
    val pokemon = Pokemon(
        id = "001",
        name = "Bulbasaur",
        images = ImagesDTO(
            small = "https://dummyimage.com/600x400/000/fff&text=Bulbasaur",
            large = "https://dummyimage.com/600x400/000/fff&text=Bulbasaur"
        ),
        types = listOf("Grass", "Poison"),
        subtypes = listOf("Seed"),
        level = "5",
        hp = "45",
        attacks = listOf(
            Attack(name = "Vine Whip", damage = "15", text = "Whips vines at the opponent."),
            Attack(name = "Tackle", damage = "5", text = "Charges with a tackle.")
        ),
        weaknesses = listOf(
            Weakness(type = "Fire", value = "×2")
        ),
        abilities = listOf(
            Ability(
                name = "Overgrow",
                text = "Boosts the power of Grass-type moves in a pinch.",
                type = "Trait"
            )
        ),
        resistances = listOf(
            Resistance(type = "Water", value = "-20")
        )
    )
    PokemonCard(pokemon = pokemon) {

    }
}
