package com.mehdisekoba.weather.data.stored

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mehdisekoba.weather.utils.CHOSE_ITEM_LANGUAGE
import com.mehdisekoba.weather.utils.SESSION_DATA
import com.mehdisekoba.weather.utils.USER_DATA
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class
DataStorePreference
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        private val appContext = context.applicationContext

        companion object {
            private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SESSION_DATA)
            private val CHOSE_LANGUAGE_KEY = intPreferencesKey(CHOSE_ITEM_LANGUAGE)
        }

        suspend fun saveSession(save: Boolean) {
            appContext.dataStore.edit {
                it[booleanPreferencesKey(USER_DATA)] = save
            }
        }

        suspend fun saveChoseItem(item: Int) {
            appContext.dataStore.edit {
                it[CHOSE_LANGUAGE_KEY] = item
            }
        }

        val getSession: Flow<Boolean?> =
            appContext.dataStore.data.map {
                it[booleanPreferencesKey(USER_DATA)] ?: false
            }

        val getChoseLanguageItem: Flow<Int> =
            appContext.dataStore.data.map {
                it[CHOSE_LANGUAGE_KEY] ?: 0
            }
    }
