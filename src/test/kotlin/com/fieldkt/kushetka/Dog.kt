package com.fieldkt.kushetka

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Dog(
    override val id: String,
    val lazinessScore: Int,
    val excitement: Int
) : Animal
