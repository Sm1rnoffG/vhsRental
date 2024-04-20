package com.example.vhsrental.di

import com.example.vhsrental.data.VHSRentalDB
import com.example.vhsrental.data.repositories.MovieRepository
import com.example.vhsrental.data.repositories.OrderRepository
import com.example.vhsrental.data.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideMovieRepository(db: VHSRentalDB) : MovieRepository {
        return MovieRepository(db)
    }

    @Provides
    fun provideUserRepository(db: VHSRentalDB) : UserRepository {
        return UserRepository(db)
    }

    @Provides
    fun provideOrderRepository(db: VHSRentalDB) : OrderRepository {
        return OrderRepository(db)
    }

}