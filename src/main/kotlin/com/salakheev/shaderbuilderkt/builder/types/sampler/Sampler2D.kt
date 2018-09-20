package com.salakheev.shaderbuilderkt.builder.types.sampler

import com.salakheev.shaderbuilderkt.builder.ShaderBuilder
import com.salakheev.shaderbuilderkt.builder.types.Variable

class Sampler2D(override val builder: ShaderBuilder) : Variable {
	override val typeName: String = "sampler2D"
	override var value: String? = null
}

class ShadowTexture2D(override val builder: ShaderBuilder) : Variable {
	override val typeName: String = "sampler2D"
	override var value: String? = null
}
