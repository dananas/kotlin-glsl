package com.salakheev.shaderbuilderkt.builder.types.sampler

import com.salakheev.shaderbuilderkt.builder.ShaderBuilder
import com.salakheev.shaderbuilderkt.builder.types.Variable
import com.salakheev.shaderbuilderkt.builder.types.scalar.GLInt

class Sampler2DArray(override val builder: ShaderBuilder) : Variable {
	override val typeName: String = "sampler2D"
	override var value: String? = null

	operator fun get(i: GLInt): Sampler2D {
		val result = Sampler2D(builder)
		result.value = "$value[${i.value}]"
		return result
	}
}
