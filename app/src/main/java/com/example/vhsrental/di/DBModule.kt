package com.example.vhsrental.di

import android.content.Context
import com.example.vhsrental.ui.MainActivity
import com.example.vhsrental.data.VHSRentalDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DBModule {

    @Provides
    fun provideDB(@ApplicationContext context: Context) : VHSRentalDB {
        return VHSRentalDB(context)
    }}