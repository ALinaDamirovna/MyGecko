package com.example.mygecko

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mygecko.ui.theme.MyGeckoTheme
import com.skydoves.landscapist.glide.GlideImage
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyGeckoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting(this)
                }
            }
        }
    }
}

@Composable
fun Greeting(context: Context) {
    val name = remember { mutableStateOf("") }
    val imgLink = remember { mutableStateOf("") }
    val state = remember {
        mutableStateOf(" ")
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxHeight(0.2f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            GlideImage(
                imageModel = imgLink.value,
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
            )        }
        Box(
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Цена ${name.value}= ${state.value} $")
        }
        Box(
            modifier = Modifier
                .fillMaxHeight(0.2f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TextField( value = name.value,onValueChange = {newText -> name.value = newText},
                    label = {Text(text="Введите криптовалюту")})
        }
        Box(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    getPriceData(name.value, context, state)
                    getImgData(name.value, context, imgLink)
                    Toast.makeText(context, "Запрос отправлен", Toast.LENGTH_LONG).show()

                }, modifier = Modifier
                    .fillMaxHeight(0.3f)
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(20.dp)

            )
            {

                Text(text = "Выполнить")
            }
        }

    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyGeckoTheme {

    }
}
fun getPriceData(name: String, context: Context, mState: MutableState<String>) {
    val url = "https://api.coingecko.com/api/v3/coins/$name"
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            val obj = JSONObject(response)
            val temp = obj.getJSONObject("market_data").getJSONObject("current_price")
            mState.value = temp.getString("usd")
            Log.d("MyLog", "Response: ${temp.getString("usd")}")
        },
        {
            Log.d("MyLog", "Volley error: $it")
        }
    )
    queue.add(stringRequest)
}

fun getImgData(name: String, context: Context, mState: MutableState<String>) {
    val url = "https://api.coingecko.com/api/v3/coins/$name"
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            val obj = JSONObject(response)
            val temp = obj.getJSONObject("image")
            mState.value = temp.getString("small")
            Log.d("MyLog", "Response: ${temp.getString("small")}")
        },
        {
            Log.d("MyLog", "Volley error: $it")
        }
    )
    queue.add(stringRequest)
}
