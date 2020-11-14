package com.example.moviedetails.ViewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedetails.Model.Guest
import com.example.moviedetails.Repository.OtherRepository
import com.example.moviedetails.Repository.TvShowRepository
import kotlinx.coroutines.launch

class OtherViewModel @ViewModelInject constructor(private val otherRepository: OtherRepository): ViewModel() {

    private var guestSessionId: MutableLiveData<Guest> = MutableLiveData()

    fun readyGuestSessionId() {
        viewModelScope.launch {
            otherRepository.getGuestSessionId()
            guestSessionId.value = otherRepository.guestSessionId
        }
    }

    fun getGuestSessionId(): LiveData<Guest> = guestSessionId
}