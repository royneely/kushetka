package com.fieldkt.kushetka

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Cat(
    override val id: String,
    val lazinessScore: Int,
    val maliciousDisposition: Boolean = true
): Animal
