package com.salakheev.shaderbuilderkt.builder.delegates

import com.salakheev.shaderbuilderkt.builder.Instruction
import com.salakheev.shaderbuilderkt.builder.ShaderBuilder
import com.salakheev.shaderbuilderkt.builder.types.Variable
import kotlin.reflect.KProperty

class ComponentDelegate<T : Variable>(private val factory: (ShaderBuilder) -> T) {
	private lateinit var v: T

	operator fun provideDelegate(thisRef: Variable, property: KProperty<*>): ComponentDelegate<T> {
		v = factory(thisRef.builder)
		return this
	}

	operator fun getValue(thisRef: Variable, property: KProperty<*>): T {
		if (v.value == null) {
			v.value = "${thisRef.value}.${property.name}"
		}
		return v
	}

	operator fun setValue(thisRef: Variable, property: KProperty<*>, value: T) {
		if (v.value == null) {
			v.value = "${thisRef.value}.${property.name}"
		}
		thisRef.builder.instructions.add(Instruction.assign(v.value, value.value))
	}
}
