//package com.vivek.pokidex.presentation.ui.screen
//
//import android.widget.Space
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Menu
//import androidx.compose.material3.Card
//import androidx.compose.material3.DropdownMenu
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import coil.compose.AsyncImage
//import com.vivek.pokidex.data.remote.models.Ability
//import com.vivek.pokidex.data.remote.models.Attack
//import com.vivek.pokidex.data.remote.models.ImagesDTO
//import com.vivek.pokidex.data.remote.models.Resistance
//import com.vivek.pokidex.data.remote.models.Weakness
//import com.vivek.pokidex.domain.model.Pokemon
//import com.vivek.pokidex.presentation.ui.components.SortOrder
//import com.vivek.pokidex.presentation.viewModel.PokemonViewModel
//
//@Composable
//fun PokemonDetailScreen(
//    pokemonId: String,
//    viewModel: PokemonViewModel = hiltViewModel()
//) {
//    LaunchedEffect(pokemonId) {
//        viewModel.loadPokemonDetail(pokemonId)
//    }
//    val pokemonDetail by viewModel.pokemonDetail.collectAsState(initial = null)
//    pokemonDetail?.let {
//        PokemonDetailContent(pokemon = it)
//    }
//
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PokemonDetailContent(pokemon: Pokemon) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Pokemon Detail",
//                        modifier = Modifier.padding(start = 16.dp),
//                        textAlign = TextAlign.Center,
//                        style = MaterialTheme.typography.headlineLarge,
//                        fontWeight = FontWeight.Bold,
//                    )
//                },
//            )
//        }
//    ) { paddingValues ->
//        LazyColumn(
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.background)
//                .padding(horizontal = 16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            item {
//                AsyncImage(
//                    model = pokemon.images.large,
//                    contentDescription = "Pokemon Image",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(300.dp)
//                        .padding(8.dp)
//
//                )
//                Text(
//                    text = pokemon.name,
//                    style = MaterialTheme.typography.headlineLarge,
//                    fontWeight = FontWeight.Bold
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly
//                ) {
//                    Text(
//                        text = "Level: ${pokemon.level}",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Spacer(modifier = Modifier.width(16.dp))
//                    Text(
//                        text = "HP: ${pokemon.hp}",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//                TypesSection(types = pokemon.types, subTypes = pokemon.subtypes)
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//            item {
//                WeakAndResistanceSection(
//                    weaknesses = pokemon.weaknesses,
//                    resistances = pokemon.resistances
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//            item {
//                pokemon.attacks?.let { AttacksSection(attacks = it) }
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//
//            item {
//                pokemon.abilities?.let { AbilitiesSection(abilities = it) }
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//        }
//    }
//}
//
//@Composable
//fun TypesSection(types: List<String>, subTypes: List<String>?) {
//    Column {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Start
//        ) {
//            Text(text = "Types:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//            types.forEach { type ->
//                Badge(text = type, backgroundColor = Color(0xFF81C784))
//            }
//            Spacer(modifier = Modifier.width(8.dp))
//        }
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Start
//        ) {
//            Text(text = "Sub Types:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//            subTypes?.forEach { subType ->
//                Badge(text = subType, backgroundColor = Color(0xFF81C784))
//            }
//        }
//    }
//}
//
//@Composable
//fun AttacksSection(attacks: List<Attack>) {
//    Column {
//        Text(text = "Attacks:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//        attacks.forEach { attack ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 2.dp),
//            ) {
//                Column(
//                    modifier = Modifier.padding(12.dp)
//                ) {
//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        Text(text = "Name: ${attack.name}", fontWeight = FontWeight.Bold)
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(text = "Damage: ${attack.damage}")
//                    }
//                    Text(text = "Description: ${attack.text}")
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun WeakAndResistanceSection(weaknesses: List<Weakness>?, resistances: List<Resistance>?) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Absolute.SpaceAround
//    ) {
//        Column(
//            modifier = Modifier
//                .weight(1f)
//
//        ) {
//            Text(text = "Weaknesses:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//            weaknesses?.forEach { weakness ->
//                Row {
//                    Text(text = "${weakness.type}: ${weakness.value}")
//                }
//            }
//        }
//
//        Column(
//            modifier = Modifier
//                .weight(1f)
//                .padding(horizontal = 8.dp)
//        ) {
//            Text(text = "Resistances:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//            resistances?.forEach { resistance ->
//                Row {
//                    Text(text = "${resistance.type}: ${resistance.value}")
//                }
//
//            }
//        }
//    }
//}
//
//@Composable
//fun AbilitiesSection(abilities: List<Ability>) {
//    Column {
//        Text(text = "Abilities:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//        abilities.forEach { ability ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 2.dp),
//            ) {
//                Column(modifier = Modifier.padding(12.dp)) {
//                    Text(text = "Name: ${ability.name}", fontWeight = FontWeight.Bold)
//                    Text(text = "Type: ${ability.type}")
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Badge(text: String, backgroundColor: Color) {
//    Box(
//        modifier = Modifier
//            .padding(4.dp)
//            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
//            .padding(horizontal = 8.dp, vertical = 4.dp)
//    ) {
//        Text(text = text, color = Color.White, fontWeight = FontWeight.Bold)
//    }
//}
//
//
//@Composable
//@Preview(showBackground = true)
//fun PreviewDetails() {
//    val pokemon = Pokemon(
//        id = "001",
//        name = "Bulbasaur",
//        images = ImagesDTO(
//            small = "https://dummyimage.com/600x400/000/fff&text=Bulbasaur",
//            large = "https://dummyimage.com/600x400/000/fff&text=Bulbasaur"
//        ),
//        types = listOf("Grass", "Poison"),
//        subtypes = listOf("Seed"),
//        level = "5",
//        hp = "45",
//        attacks = listOf(
//            Attack(name = "Vine Whip", damage = "15", text = "Whips vines at the opponent."),
//            Attack(name = "Tackle", damage = "5", text = "Charges with a tackle.")
//        ),
//        weaknesses = listOf(
//            Weakness(type = "Fire", value = "×2")
//        ),
//        abilities = listOf(
//            Ability(
//                name = "Overgrow",
//                text = "Boosts the power of Grass-type moves in a pinch.",
//                type = "Trait"
//            )
//        ),
//        resistances = listOf(
//            Resistance(type = "Water", value = "-20")
//        )
//    )
//    PokemonDetailContent(pokemon)
//}
//
//
//
//
//
