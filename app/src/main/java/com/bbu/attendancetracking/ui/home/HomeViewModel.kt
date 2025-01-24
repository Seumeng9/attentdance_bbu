package com.bbu.attendancetracking.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbu.attendancetracking.data.model.ClassResponse
import com.bbu.attendancetracking.data.model.RecyclerViewItem
import com.bbu.attendancetracking.repository.ApiRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody

class HomeViewModel() : ViewModel() {

    private val _classItems = MutableStateFlow<List<RecyclerViewItem>>(emptyList())


    val classItems: StateFlow<List<RecyclerViewItem>> get() = _classItems

    private val _errorMessage = MutableStateFlow<String?>(null)

    private val _fetchDataTrigger = MutableLiveData<Boolean>()
    val fetchDataTrigger: LiveData<Boolean> = _fetchDataTrigger

    private var currentPage = 1;
    val errorMessage: StateFlow<String?> get() = _errorMessage

    // Function to trigger API call from other fragments
    fun triggerFetchData() {
        Log.d("Trigger:", "Trigger Fetch Data")
        _fetchDataTrigger.value = true
    }

    suspend fun fetchClasses(page: Int = 1) {
        if (currentPage == -1) {
            return
        }

        try {
            val response = ApiRepository().getListClass("40", "", page)

            if (response.isSuccessful) {
                response.body()?.let {
                    val items = parseClassResponse(it)
                    _classItems.value = items

                    currentPage = if (items.size < 19) -1 else currentPage + 1
                }
            } else {
                _errorMessage.value = "Failed to load data: ${response.message()}"
            }
        } catch (e: Exception) {
            _errorMessage.value = "Error: ${e.localizedMessage}"
        }
    }




    private fun parseClassResponse(classResponse: ClassResponse): List<RecyclerViewItem> {
        val items = mutableListOf<RecyclerViewItem>()
        for (category in classResponse.list) {
            // Add header for each category
            if(category.rec.isNotEmpty()){
                items.add(RecyclerViewItem.Header(category.cal))
                // Add class items for the category
                items.addAll(category.rec.map {
                    RecyclerViewItem.ClassItem(
                        classId = it.classId,
                        title = it.title,
                        labNo = it.labNo,
                        scheduled = it.scheduled
                    )
                })
            }
        }
        return items
    }
}
