package com.salakheev.shaderbuilderkt.builder.delegates

import com.salakheev.shaderbuilderkt.builder.ShaderBuilder
import com.salakheev.shaderbuilderkt.builder.types.Variable
import kotlin.reflect.KProperty

class UniformDelegate<T : Variable>(private val factory: (ShaderBuilder) -> T) {
	private lateinit var v: T

	operator fun provideDelegate(thisRef: ShaderBuilder,
								 property: KProperty<*>): UniformDelegate<T> {
		v = factory(thisRef)
		v.value = property.name
		return this
	}

	operator fun getValue(thisRef: ShaderBuilder, property: KProperty<*>): T {
		thisRef.uniforms.add("${v.typeName} ${property.name}")
		return v
	}
}
