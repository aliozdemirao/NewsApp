package com.aliozdemir.newsapp.domain.usecases.app_entry

import com.aliozdemir.newsapp.domain.manager.LocalUserManger

class SaveAppEntry(
    private val localUserManger: LocalUserManger
) {

    suspend operator fun invoke() {
        localUserManger.saveAppEntry()
    }

}