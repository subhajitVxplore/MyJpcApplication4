package com.example.myjpcapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.myjpcapplication.roomDb.RegisterDatabase
import com.vxplore.myjpcrecyclerviewapplication.Moviee
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

     //private val viewModel by viewModels<MyViewModel>()
    var blankScreenOpened = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrawingLayout(onDataloaded = { blankScreenOpened = false })
        }
    }
}
////////////////////////

@Composable
private fun AppNavGraphHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AppNavScreen.LoginScreen.name) {
        composable(
            route = AppNavScreen.LoginScreen.name) {
            LoginScreen(
                onNextClick = { navController.navigate(AppNavScreen.DashboardScreen.name) },
                onNextClick2 = { navController.navigate(AppNavScreen.RegistrationScreen.name) })
        }
        composable(route = AppNavScreen.RegistrationScreen.name) {
            RegistrationScreen(onNextClick = { navController.navigate(AppNavScreen.LoginScreen.name) })
        }
        composable(route = AppNavScreen.DashboardScreen.name) {
            DashboardScreen(onBackClick = { navController.navigateUp() })
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
//private fun LoginScreen( onNextClick: () -> Unit,onNextClick2: () -> Unit, viewModel: MyViewModel= androidx.lifecycle.viewmodel.compose.viewModel()) {//without DaggerHilt
private fun LoginScreen(onNextClick: () -> Unit,onNextClick2: () -> Unit,viewModel: MyViewModel = hiltViewModel()) {
    val context = LocalContext.current
    LaunchedEffect(key1 = viewModel._found.value) {
        if (viewModel._found.value != null) {
            Toast.makeText(context, "${viewModel._found.value}", Toast.LENGTH_SHORT).show()
            onNextClick()
            viewModel._found.value = null
        }
    }
    LaunchedEffect(key1 = viewModel._notFound.value) {
        if (viewModel._notFound.value != null) {
            Toast.makeText(context, "${viewModel._notFound.value}", Toast.LENGTH_SHORT).show()
            viewModel._notFound.value = null
        }
    }

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded })
    val coroutineScope = rememberCoroutineScope()
    BackHandler(sheetState.isVisible) { coroutineScope.launch { sheetState.hide() } }
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = { BottomSheet() },
        sheetShape = RoundedCornerShape(topStart = 30.dp,topEnd = 30.dp),
        scrimColor = Color(0xfffceff9),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(8.dp))
                    .background(color = Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "LoginScreen",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(16.dp)
                )
                var emailText by remember { mutableStateOf(TextFieldValue("")) }
                TextField(value = emailText, onValueChange = { emailText = it },
                    label = { Text("Email") }, placeholder = { Text(text = "yourEmail@mail.com") },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )

                var pswdText by remember { mutableStateOf(TextFieldValue("")) }
                TextField(value = pswdText, onValueChange = { pswdText = it },
                    label = { Text("Password") }, placeholder = { Text(text = "********") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    )
                )

                val context = LocalContext.current
                val scop2 = rememberCoroutineScope()
                val dao = RegisterDatabase.getDatabase(context).registerDao()
                Button(
                    onClick = {
                        // Toast.makeText(context, "LoginScreen", Toast.LENGTH_SHORT).show()
                        //  onNextClick
                        scop2.launch {
                            viewModel.readRegister(dao, emailText.text, pswdText.text)
                            //viewModel.checkEmail(context, emailText.text)
                        }

                    }, shape = RoundedCornerShape(30.dp), modifier = Modifier
                        .padding(20.dp)
                        .height(50.dp)
                ) { Text(text = "Login to Dashboard") }
            }
            TextButton(onClick = onNextClick2) {
                Text(
                    text = "Navigate to Registration",
                    textDecoration = TextDecoration.Underline
                )
            }
            TextButton(onClick = {

                coroutineScope.launch {
                    if (sheetState.isVisible) sheetState.hide()
                    else sheetState.show()
                }

            }) { Text(text = "Forgot Password ?", color = Color.Gray) }

        }

    }

}

@Composable
private fun RegistrationScreen(onNextClick: () -> Unit, viewModel: MyViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .shadow(
                    elevation = 30.dp,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(color = Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "RegistrationScreen",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(16.dp)
            )

            var nameText by remember { mutableStateOf(TextFieldValue("")) }
            TextField(value = nameText, onValueChange = { nameText = it },
                label = { Text("Name") }, placeholder = { Text(text = "yourName") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            var emailText by remember { mutableStateOf(TextFieldValue("")) }
            TextField(value = emailText, onValueChange = { emailText = it },
                label = { Text("Email") }, placeholder = { Text(text = "yourEmail@mail.com") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            var pswdText by remember { mutableStateOf(TextFieldValue("")) }
            TextField(value = pswdText, onValueChange = { pswdText = it },
                label = { Text("Password") }, placeholder = { Text(text = "********") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                )
            )

            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            Button(
                onClick = {
                    Toast.makeText(context, "RegistrationScreen", Toast.LENGTH_SHORT).show()
                    scope.launch {
                        viewModel.newRegister(context, nameText.text, emailText.text, pswdText.text)
                    }
                }, shape = RoundedCornerShape(30.dp), modifier = Modifier
                    .padding(20.dp)
                    .height(50.dp)
            ) { Text(text = "Register") }

            TextButton(onClick = onNextClick) {
                Text(
                    text = "Navigate to Login",
                    textDecoration = TextDecoration.Underline
                )
            }

        }
    }
}

@Composable
private fun DashboardScreen(onBackClick: () -> Unit) {
    val currentScreen = remember { mutableStateOf(DrawerAppScreen.Screen1) }
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val ctx = LocalContext.current
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        drawerContent = {
            DrawerContentComponent(
                currentScreen = currentScreen,
                closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } })
        },
        topBar = {
            TopAppBar(
                modifier = Modifier.clip(
                    RoundedCornerShape(
                        bottomStart = 8.dp,
                        bottomEnd = 8.dp
                    )
                )
            ) {
                IconButton(
                    onClick = {
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                        Toast.makeText(ctx, "IconButton", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(Icons.Filled.Menu, "")
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                BodyContentComponent(
                    currentScreen = currentScreen.value,
                    openDrawer = { coroutineScope.launch { scaffoldState.drawerState.open() } },
                    viewModel = hiltViewModel()
                )
            }
        },
        bottomBar = {
            BottomNavigationOnlySelectedLabelComponent(
                currentScreen = currentScreen,
                closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } })
        },
    )

}

@Composable
fun RotatingSquareComponent() {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            val infiniteTransition = rememberInfiniteTransition()
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween<Float>(
                        durationMillis = 3000,
                        easing = FastOutLinearInEasing
                    )
                )
            )
            Canvas(modifier = Modifier.size(200.dp)) {
                rotate(rotation) {
                    drawRect(
                        color = Color(
                            255,
                            138,
                            128
                        )
                    )
                }
            }
        })
}

//////////////////////DrawingLayout/////////////////////////////////////////////////////////////////
@Composable
fun DrawingLayout(
    onDataloaded: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    LaunchedEffect(key1 = Unit) {
        onDataloaded()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(color = Color(0xFF323FFD))
            )
        }
        Box(
            modifier = Modifier
                .padding(25.dp)
                .clip(CircleShape)
                .background(color = Color(0xFFFFFFFF))
                .size(90.dp)
                .align(Alignment.TopCenter)
        ) {
            Image(
                painter = painterResource(R.drawable.baseline_home_24),
                contentDescription = "profilePicture",
                modifier = Modifier
                    .size(100.dp)
                    .padding(10.dp)
                    .align(Alignment.Center)
            )
        }
    }
    AppNavGraphHost(navController)
}

//////////////////////BotomSheeetLayout/////////////////////////////////////////////////////////////////
@Composable
fun BottomSheet(viewModel: MyViewModel = hiltViewModel()) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
            //.shadow(elevation = 10.dp, shape = RoundedCornerShape(8.dp))
            .background(color = Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Forgot Password ?",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(10.dp)
        )

        var emailText by remember { mutableStateOf(TextFieldValue("")) }
        TextField(value = emailText, onValueChange = { emailText = it },
            label = { Text("Email") }, placeholder = { Text(text = "yourEmail@mail.com") },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        )

        var pswdText by remember { mutableStateOf(TextFieldValue("")) }
        TextField(value = pswdText, onValueChange = { pswdText = it },
            label = { Text("Password") }, placeholder = { Text(text = "********") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            )
        )
        val context = LocalContext.current
        val scope3 = rememberCoroutineScope()
        val dao = RegisterDatabase.getDatabase(context).registerDao()
        Button(
            onClick = {
                Toast.makeText(context, "BottomSheet", Toast.LENGTH_SHORT).show()

                scope3.launch {
                    viewModel.updatePassword(dao, emailText.text, pswdText.text)
                }

            }, shape = RoundedCornerShape(30.dp), modifier = Modifier
                .padding(20.dp)
                .height(50.dp)
        ) { Text(text = "Reset Password") }
    }
}

////////////recyclerView////////////////////////////////////////////////
@Composable
fun LazyColumnItemsScrollableComponent(personList: List<Person>) {
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(items = personList, itemContent = { person ->
            val index = personList.indexOf(person)
            Row(modifier = Modifier.fillParentMaxWidth()) {
                Card(
                    shape = RoundedCornerShape(4.dp),
                    backgroundColor = colors[index % colors.size],
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        person.name,
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        })
    }
}

///////////DrawerNavigation////////////////////////////////////////////////////

@Composable
fun DrawerContentComponent(currentScreen: MutableState<DrawerAppScreen>, closeDrawer: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        for (index in DrawerAppScreen.values().indices) {
            val screen = getScreenBasedOnIndex(index)
            Column(Modifier.clickable(onClick = {
                currentScreen.value = screen
                closeDrawer()
            }), content = {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = if (currentScreen.value == screen) {
                        MaterialTheme.colors.secondary
                    } else {
                        MaterialTheme.colors.surface
                    }
                ) {
                    Text(text = screen.name, modifier = Modifier.padding(16.dp))
                }
            })
        }
    }
}


fun getScreenBasedOnIndex(index: Int) = when (index) {
    0 -> DrawerAppScreen.Screen1
    1 -> DrawerAppScreen.Screen2
    2 -> DrawerAppScreen.Screen3
    3 -> DrawerAppScreen.Screen4
    else -> DrawerAppScreen.Screen1
}


@Composable
fun BodyContentComponent(
    currentScreen: DrawerAppScreen,
    openDrawer: () -> Unit,
    viewModel: MyViewModel
) {
    when (currentScreen) {
        DrawerAppScreen.Screen1 -> Screen1Component(openDrawer,viewModel)
        DrawerAppScreen.Screen2 -> Screen2Component(openDrawer, viewModel)
        DrawerAppScreen.Screen3 -> Screen3Component(openDrawer)
        DrawerAppScreen.Screen4 -> Screen4Component(openDrawer,viewModel)
    }
}

enum class DrawerAppScreen { Screen1, Screen2, Screen3 ,Screen4}

@Composable
fun Screen1Component(openDrawer: () -> Unit,viewModel: MyViewModel) {

    Column(modifier = Modifier.fillMaxSize()) {

        Surface(color = Color(0xFFffd7d7.toInt()), modifier = Modifier.weight(1f)) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    //StateFlowList2(viewModel = viewModel)
                    MovieList(movieList = viewModel.movieListResponse)
                    viewModel.getMovieList()
                }
            )
        }
    }
}

@Composable
fun Screen2Component(openDrawer: () -> Unit, viewModel: MyViewModel) {

    Column(modifier = Modifier.fillMaxSize()) {

        Surface(color = Color(0xFFffe9d6.toInt()), modifier = Modifier.weight(1f)) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                StateFlowList(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun Screen3Component(openDrawer: () -> Unit) {

    Column(modifier = Modifier.fillMaxSize()) {

        Surface(color = Color(0xFFfffbd0.toInt()), modifier = Modifier.weight(1f)) {

            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    //Text(text = "Screen 3")
                    LazyColumnItemsScrollableComponent(getPersonList())
                }
            )
        }
    }
}

@Composable
fun Screen4Component(openDrawer: () -> Unit,viewModel: MyViewModel) {

    Column(modifier = Modifier.fillMaxSize()) {

        Surface(color = Color(0xFFfffbd0.toInt()), modifier = Modifier.weight(1f)) {

            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                   // Text(text = "Screen 4")
                    StateFlowList2(viewModel=viewModel)
                }
            )
        }
    }
}

///////////botomNavigation////////////////////////////////////////////////////
val listItems = listOf("Scrn1", "Scrn2", "Scrn3", "Scrn4")

@Composable
fun BottomNavigationOnlySelectedLabelComponent(
    currentScreen: MutableState<DrawerAppScreen>,
    closeDrawer: () -> Unit
) {
    var selectedIndex by remember { mutableStateOf(0) }
    val contex = LocalContext.current

    BottomNavigation() {
        listItems.forEachIndexed { index, label ->
            val screen = getScreenBasedOnIndex(index)
            BottomNavigationItem(
                icon = {Icon(imageVector = Icons.Filled.Favorite,contentDescription = "Favorite")},
                label = { Text(text = label) },
                selected = selectedIndex == index,
                onClick = {
                    currentScreen.value = screen
                    selectedIndex = index
                    closeDrawer()
                },
                alwaysShowLabel = false
            )
        }
    }
}

@Composable
fun BottomNavigationAlwaysShowLabelComponent() {
    var selectedIndex by remember { mutableStateOf(0) }
    BottomNavigation(modifier = Modifier.padding(16.dp),
        //  sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
    ) {
        listItems.forEachIndexed { index, label ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorite"
                    )
                },
                label = { Text(text = label) },
                selected = selectedIndex == index,
                onClick = { selectedIndex = index }
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RoundedBottomDrawer() {

    val scope = rememberCoroutineScope()
    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)

    BottomDrawer(
        gesturesEnabled = true, // making scrollable to fit screen
        drawerState = drawerState,
        drawerBackgroundColor = Color.Transparent, // transparent background
        drawerContent = {

            Button(onClick = { scope.launch { drawerState.close() } }) {
                Text("Close")
            }

            Spacer(modifier = Modifier.height(16.dp)) // some padding

            BottomSheet()

        },
        content = {
            // outside content
            Button(onClick = { scope.launch { drawerState.open() } }) {
                Text("Open BottomDrawer")
            }
        }
    )
}


///////////////////////////////liveDataFromViewModel//////////////////
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StateFlowList(viewModel: MyViewModel) {

    val data = viewModel.dataList.collectAsState()
    LazyColumn {
        items(
            items = data.value, itemContent = { person ->
                Card(
                    shape = RoundedCornerShape(4.dp),
                    backgroundColor = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    ListItem(text = {
                        Text(
                            text = person.name,
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }, secondaryText = {
                        Text(
                            text = "Age: ${person.age}",
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Light,
                                color = Color.DarkGray
                            )
                        )

                    })
                }
            })
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StateFlowList2(viewModel: MyViewModel) {

    val moviedata = viewModel.dataList2.collectAsState()
    if (moviedata.value!= null) {
        LazyColumn {
            items(
                items = moviedata.value!!.results, itemContent = { movies ->
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        backgroundColor = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        ListItem(text = {
                            Text(
                                text = movies.original_title,
                                style = TextStyle(
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }, secondaryText = {
                            Text(
                                text = "Id: ${movies.id}",
                                style = TextStyle(
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Light,
                                    color = Color.DarkGray
                                )
                            )

                        })
                    }
                })
        }
    }

}



///////////////////////////recyclerList ApiData////////////////////////////////////////
@Composable
fun MovieList(movieList: List<Moviee>) {
    LazyColumn {
        itemsIndexed(items = movieList) { index, item ->
            MovieItem(movie = item)
        }
    }
}
@Composable
fun MovieItem(movie: Moviee) {
    Card(
        modifier = Modifier
            .padding(8.dp, 4.dp)
            .fillMaxWidth()
            .height(110.dp), shape = RoundedCornerShape(8.dp), elevation = 4.dp
    ) {
        Surface() {

            Row(
                Modifier
                    .padding(4.dp)
                    .fillMaxSize()
            ) {

                Image(
                    painter = rememberImagePainter(
                        data = movie.imageUrl,

                        builder = {
                            scale(Scale.FILL)
                            placeholder(R.drawable.baseline_home_24)
                            transformations(CircleCropTransformation())

                        }
                    ),
                    contentDescription = movie.desc,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.2f)
                )


                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxHeight()
                        .weight(0.8f)
                ) {
                    Text(
                        text = movie.name,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = movie.category,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .background(
                                Color.LightGray
                            )
                            .padding(4.dp)
                    )
                    Text(
                        text = movie.desc,
                        style = MaterialTheme.typography.body1,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                }
            }
        }
    }

}









