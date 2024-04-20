package com.example.vhsrental.di

import com.example.vhsrental.data.repositories.MovieRepository
import com.example.vhsrental.data.repositories.OrderRepository
import com.example.vhsrental.data.repositories.UserRepository
import com.example.vhsrental.ui.submodels.MoviesSubModel
import com.example.vhsrental.ui.submodels.OrderSubModel
import com.example.vhsrental.ui.submodels.UsersSubModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SubModelModule {

    @Provides
    fun provideMovieSubmodel(repository: MovieRepository) : MoviesSubModel {
        return MoviesSubModel(repository)
    }

    @Provides
    fun provideUsersSubmodel(repository: UserRepository) : UsersSubModel {
        return UsersSubModel(repository)
    }

    @Provides
    fun provideOrderSubmodel(repository: OrderRepository) : OrderSubModel {
        return OrderSubModel(repository)
    }
}