package com.salakheev.shaderbuilderkt.builder.types.vec

import com.salakheev.shaderbuilderkt.builder.ShaderBuilder
import com.salakheev.shaderbuilderkt.builder.str
import com.salakheev.shaderbuilderkt.builder.types.Vector
import com.salakheev.shaderbuilderkt.builder.types.scalar.GLFloat
import com.salakheev.shaderbuilderkt.builder.types.scalar.floatComponent
import com.salakheev.shaderbuilderkt.builder.delegates.ComponentDelegate

@Suppress("unused")
class Vec3(override val builder: ShaderBuilder) : Vector {

	override val typeName: String = "vec3"
	override var value: String? = null

	var x by floatComponent()
	var y by floatComponent()
	var z by floatComponent()

	var xx by vec2Component()
	var xy by vec2Component()
	var xz by vec2Component()
	var yx by vec2Component()
	var yy by vec2Component()
	var yz by vec2Component()
	var zx by vec2Component()
	var zy by vec2Component()
	var zz by vec2Component()

	constructor(builder: ShaderBuilder, value: String) : this(builder) {
		this.value = value
	}

	operator fun times(a: Float) = Vec3(builder, "(${this.value} * ${a.str()})")
	operator fun div(a: Float) = Vec3(builder, "(${this.value} / ${a.str()})")

	operator fun times(a: GLFloat) = Vec3(builder, "(${this.value} * ${a.value})")
	operator fun div(a: GLFloat) = Vec3(builder, "(${this.value} / ${a.value})")

	operator fun times(a: Vec3) = Vec3(builder, "(${this.value} * ${a.value})")
	operator fun div(a: Vec3) = Vec3(builder, "(${this.value} / ${a.value})")
	operator fun plus(a: Vec3) = Vec3(builder, "(${this.value} + ${a.value})")
	operator fun minus(a: Vec3) = Vec3(builder, "(${this.value} - ${a.value})")

	operator fun unaryMinus() = Vec3(builder, "-(${this.value})")
}

operator fun Float.times(a: Vec3) = Vec3(a.builder, "(${this.str()} * ${a.value})")
operator fun Float.div(a: Vec3) = Vec3(a.builder, "(${this.str()} / ${a.value})")
operator fun GLFloat.times(a: Vec3) = Vec3(a.builder, "(${this.value} * ${a.value})")

fun vec3Component() = ComponentDelegate(::Vec3)
