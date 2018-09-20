package com.salakheev.shaderbuilderkt.builder.types

data class BoolResult(val value: String) {
	infix fun or(a: BoolResult): BoolResult = BoolResult("(${this.value} || ${a.value})")
	infix fun and(a: BoolResult): BoolResult = BoolResult("(${this.value} && ${a.value})")
}
