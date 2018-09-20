package com.salakheev.shaderbuilderkt.builder.types.scalar

import com.salakheev.shaderbuilderkt.builder.ShaderBuilder
import com.salakheev.shaderbuilderkt.builder.types.GenType

class GLInt(override val builder: ShaderBuilder) : GenType {
	override val typeName: String = "int"
	override var value: String? = null

	constructor(builder: ShaderBuilder, value: String) : this(builder) {
		this.value = value
	}

	operator fun plus(a: GLInt) = GLInt(builder, "(${this.value} + ${a.value})")
	operator fun plus(a: Int) = GLInt(builder, "(${this.value} + $a)")
}
