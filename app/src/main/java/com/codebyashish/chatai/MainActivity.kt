package com.codebyashish.chatai

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codebyashish.chatai.adapters.ChatAdapter
import com.codebyashish.chatai.databinding.ActivityMainBinding
import com.codebyashish.chatai.networking.RetrofitClient
import com.codebyashish.chatai.utility.KeyConstants.CHAT_TYPE.AI
import com.codebyashish.chatai.utility.KeyConstants.CHAT_TYPE.CLOSED
import com.codebyashish.chatai.utility.KeyConstants.CHAT_TYPE.USER
import com.codebyashish.chatai.utility.KeyConstants.Companion.API_KEY
import com.codebyashish.chatai.utility.SharedPref
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val model = "gpt-3.5-turbo"
    private val jsonArray = JSONArray()
    private val TAG = "MainAct22"
    private lateinit var binding: ActivityMainBinding
    private var chatJSOnArray = JSONArray()
    private lateinit var activity: BaseActivity

    private lateinit var adapter: ChatAdapter
    private var apiKey : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        apiKey = SharedPref.getString(activity, API_KEY, "")

        if (apiKey.isEmpty()){
            askForApiKey()
        }

        toolbarInit(binding.toolbar)
        toolbarInit2(binding.toolbar2)
//        navigationDrawer()


        binding.btnSubmit.setOnClickListener {
            apiKey = SharedPref.getString(activity, API_KEY, "")
            if (apiKey.isNotEmpty()) {
                initRequest()
            } else {
                val snackbar = Snackbar.make(binding.root, "API key not found", Snackbar.LENGTH_SHORT)
                snackbar.setAction("Set") {
                    askForApiKey()
                }
                snackbar.show()
            }
        }

        showList()

    }

    private fun initRequest(){
        val jsonObject = JSONObject()
        jsonObject.put("role", "user")

        jsonArray.put(jsonObject)

        val msg = binding.etMsg.text.toString().trim()
        if (msg.isNotEmpty()) {
            jsonObject.put("content", msg)

            val chatJSOnObj = JSONObject()
            chatJSOnObj.put("type", USER)
            chatJSOnObj.put("msg", msg)
            chatJSOnArray.put(chatJSOnObj)

            val aiJson = JSONObject()
            aiJson.put("type", AI)
            chatJSOnArray.put(aiJson)

            adapter.notifyDataSetChanged()

            binding.etMsg.setText("")

            binding.recyclerView.scrollToPosition(chatJSOnArray.length() - 1)

            sendREq(aiJson)

        } else {
            Toast.makeText(this, "Please enter something...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showList() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        adapter = ChatAdapter(this, chatJSOnArray)
        binding.recyclerView.adapter = adapter

    }

    private fun sendREq(aiJson: JSONObject) {
        try {
            val reqObj = JSONObject()
            reqObj.put("model", model)
            reqObj.put("messages", jsonArray)
            val requestBody = reqObj.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val url = "https://api.openai.com/v1/chat/completions"
            val request = Request.Builder().url(url).post(requestBody).build()
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    OkHttpClient.Builder().readTimeout(500, TimeUnit.SECONDS)
                        .addInterceptor(RetrofitClient().interceptor(apiKey)).build().newCall(request)
                        .execute()
                }
                if (response.isSuccessful) {
                    val responseData = response.body.string()
                    showResponse(aiJson, responseData)
                } else if (response.code == 401){
                    val responseData = response.body.string()
                    showUnauthResponse(aiJson, responseData)
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "sendREq: error ${e.message}")

        }
    }

    private fun showUnauthResponse(aiJson: JSONObject, data: String){


        val jsonObject = JSONObject(data)
        val error = jsonObject.optString("error" )
        val errorObj = JSONObject(error)
        val msg = errorObj.optString("message")
        val code = errorObj.optString("code")

        if (code.contains("invalid_api_key")){

            chatJSOnArray.remove(chatJSOnArray.length()-1)

            val closedJson = JSONObject()
            closedJson.put("type", CLOSED)

            chatJSOnArray.put(closedJson)

            adapter.notifyDataSetChanged()

            val snackbar = Snackbar.make(binding.root, "API key is not correct.", Snackbar.LENGTH_SHORT)
            snackbar.setAction("Reset") {askForApiKey()}
            snackbar.show()


        }


    }

    private fun showResponse(aiJson: JSONObject, data: String) {
        val jsonObject = JSONObject(data)
        Log.d(TAG, "showResponse: $data")

        val arry = jsonObject.optString("choices")
        val jsonArray = JSONArray(arry)
        val jsObj = jsonArray.optJSONObject(0)


        val message = jsObj.optString("message")
        val msgObj = JSONObject(message)
        val msg = msgObj.optString("content")

        aiJson.put("msg", msg)


        adapter.notifyDataSetChanged()
        binding.recyclerView.scrollToPosition(chatJSOnArray.length())


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {



        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tool_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_reset -> {
                askForApiKey()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun navigationDrawer() {
        binding.navigationView.setNavigationItemSelectedListener(activity as NavigationView.OnNavigationItemSelectedListener?)
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
    }

    private fun askForApiKey() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_text, null)
        val editText = dialogView.findViewById<EditText>(R.id.editText)

        val dialog = MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_ChatGPT_AlertDialog)
            .setView(dialogView)
            .setTitle("API Key")
            .setPositiveButton("Save") { _, _ ->
                val enteredText = editText.text.toString()
                if (enteredText.isNotEmpty()) {
                    SharedPref.setString(activity, "API_KEY", enteredText)
                } else{
                    Snackbar.make(binding.root, "API Key cannot be blank", Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

}