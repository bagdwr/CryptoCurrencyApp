package com.example.cryptocurrency.di

import com.example.cryptocurrency.CoinsListFragment
import com.example.cryptocurrency.di.module.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class])
@Singleton
interface AppComponent {
    public fun provideInj(fragment:CoinsListFragment)
}