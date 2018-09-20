package com.salakheev.shaderbuilderkt.builder.types.vec

import com.salakheev.shaderbuilderkt.builder.ShaderBuilder
import com.salakheev.shaderbuilderkt.builder.str
import com.salakheev.shaderbuilderkt.builder.types.Vector
import com.salakheev.shaderbuilderkt.builder.types.scalar.GLFloat
import com.salakheev.shaderbuilderkt.builder.types.scalar.floatComponent
import com.salakheev.shaderbuilderkt.builder.delegates.ComponentDelegate

class Vec2(override val builder: ShaderBuilder) : Vector {
	override val typeName: String = "vec2"
	override var value: String? = null

	var x by floatComponent()
	var y by floatComponent()

	constructor(builder: ShaderBuilder, value: String) : this(builder) {
		this.value = value
	}

	operator fun times(a: Float) = Vec2(builder, "(${this.value} * ${a.str()})")
	operator fun div(a: Float) = Vec2(builder, "(${this.value} / ${a.str()})")

	operator fun times(a: GLFloat) = Vec2(builder, "(${this.value} * ${a.value})")
	operator fun div(a: GLFloat) = Vec2(builder, "(${this.value} / ${a.value})")

	operator fun times(a: Vec2) = Vec2(builder, "(${this.value} * ${a.value})")
	operator fun div(a: Vec2) = Vec2(builder, "(${this.value} / ${a.value})")
	operator fun plus(a: Vec2) = Vec2(builder, "(${this.value} + ${a.value})")
	operator fun plus(a: Float) = Vec2(builder, "(${this.value} + ${a.str()})")
	operator fun minus(a: Vec2) = Vec2(builder, "(${this.value} - ${a.value})")

	operator fun unaryMinus() = Vec2(builder, "-(${this.value})")
}

operator fun Float.times(a: Vec2) = Vec2(a.builder, "(${this.str()} * ${a.value})")
operator fun Float.div(a: Vec2) = Vec2(a.builder, "(${this.str()} / ${a.value})")

fun vec2Component() = ComponentDelegate(::Vec2)
