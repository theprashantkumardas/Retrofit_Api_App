package com.example.retrofitapi

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Space
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.retrofitapi.data.ProductsRepositoryImplementation
import com.example.retrofitapi.data.model.Product
import com.example.retrofitapi.data.model.Products
import com.example.retrofitapi.data.presentation.ProductsViewModel
import com.example.retrofitapi.ui.theme.RetrofitApiTheme
import kotlinx.coroutines.flow.collectLatest


class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ProductsViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {

            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProductsViewModel(
                    ProductsRepositoryImplementation(RetrofitInstance.api)
                ) as T
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetrofitApiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val productList = viewModel.products.collectAsState().value
                    val context = LocalContext.current

                    LaunchedEffect(key1 = viewModel.showErrorToastChannel) {
                        viewModel.showErrorToastChannel.collectLatest { show ->
                            if(show){
                                Toast.makeText(
                                    context,
                                    "Error",
                                    Toast.LENGTH_SHORT
                                )
                            }

                        }
                    }

                    if(productList.isEmpty()){
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    else {
                        Spacer(modifier =  Modifier.height(40.dp))
                        LazyColumn (
                            modifier = Modifier.fillMaxSize().padding(top = 36.dp , bottom = 50.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(productList.size){index ->
                                Product(productList[index])
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                        }
                    }

                }
            }
        }
    }
}

//Single Product Card Ui
@Composable
fun Product(product: Product){
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(product.thumbnail)
            .size(Size.ORIGINAL).build()
    ).state

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .height(320.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {

        if(imageState is AsyncImagePainter.State.Error){
            Box(
                modifier =Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }
        if(imageState is AsyncImagePainter.State.Success){
            Image(
                modifier = Modifier.fillMaxWidth().height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondary) // Color inside the clipped area
                    .border(
                        width = 10.dp,
                        color = MaterialTheme.colorScheme.primaryContainer, // Choose your border color
                        shape = RoundedCornerShape(16.dp) // Matches the clip shape
                    ) .padding(4.dp), // Optional: space between border and content,
                painter = imageState.painter,
                contentDescription = product.title,
//                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "${product.title} -- Price: $${product.price} ",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = product.description,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }


    }
}




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RetrofitApiTheme {
        Greeting("Android")
    }
}