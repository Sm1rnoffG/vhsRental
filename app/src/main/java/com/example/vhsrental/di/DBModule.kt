package com.example.vhsrental.di

import android.content.Context
import com.example.vhsrental.data.VHSRentalDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DBModule {

    @Provides
    fun provideDB(@ActivityContext context: Context) : VHSRentalDB {
        return VHSRentalDB(context)
    }
}