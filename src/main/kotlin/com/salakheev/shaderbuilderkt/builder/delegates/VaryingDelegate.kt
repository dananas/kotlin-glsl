package com.salakheev.shaderbuilderkt.builder.delegates

import com.salakheev.shaderbuilderkt.builder.Instruction
import com.salakheev.shaderbuilderkt.builder.ShaderBuilder
import com.salakheev.shaderbuilderkt.builder.types.Variable
import kotlin.reflect.KProperty

class VaryingDelegate<T : Variable>(private val factory: (ShaderBuilder) -> T) {

	private lateinit var v: T

	operator fun provideDelegate(thisRef: ShaderBuilder,
								 property: KProperty<*>): VaryingDelegate<T> {
		v = factory(thisRef)
		v.value = property.name
		return this
	}

	operator fun getValue(thisRef: ShaderBuilder, property: KProperty<*>): T {
		thisRef.varyings.add("${v.typeName} ${property.name}")
		return v
	}

	operator fun setValue(thisRef: ShaderBuilder, property: KProperty<*>, value: T) {
		thisRef.varyings.add("${v.typeName} ${property.name}")
		thisRef.instructions.add(Instruction.assign(property.name, value.value))
	}
}
