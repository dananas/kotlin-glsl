package com.salakheev.shaderbuilderkt.builder.types.vec

import com.salakheev.shaderbuilderkt.builder.ShaderBuilder
import com.salakheev.shaderbuilderkt.builder.types.Variable
import com.salakheev.shaderbuilderkt.builder.types.scalar.GLInt

class Vec2Array(override val builder: ShaderBuilder) : Variable {
	override val typeName: String = "vec2"
	override var value: String? = null

	operator fun get(i: GLInt): Vec2 {
		val result = Vec2(builder)
		result.value = "$value[${i.value}]"
		return result
	}
}

class Vec3Array(override val builder: ShaderBuilder) : Variable {
	override val typeName: String = "vec3"
	override var value: String? = null

	operator fun get(i: GLInt): Vec3 {
		val result = Vec3(builder)
		result.value = "$value[${i.value}]"
		return result
	}
}

class Vec4Array(override val builder: ShaderBuilder) : Variable {
	override val typeName: String = "vec4"
	override var value: String? = null

	operator fun get(i: GLInt): Vec4 {
		val result = Vec4(builder)
		result.value = "$value[${i.value}]"
		return result
	}
}
