package com.salakheev.shaderbuilderkt.builder.types.mat

import com.salakheev.shaderbuilderkt.builder.Instruction
import com.salakheev.shaderbuilderkt.builder.ShaderBuilder
import com.salakheev.shaderbuilderkt.builder.types.Variable
import kotlin.reflect.KProperty

class MatrixColumnDelegate<T : Variable>(
	private val index: Int,
	private val factory: (builder: ShaderBuilder) -> T
) {
	private lateinit var v: T

	operator fun provideDelegate(thisRef: Variable,
								 property: KProperty<*>): MatrixColumnDelegate<T> {
		v = factory(thisRef.builder)
		return this
	}

	operator fun getValue(thisRef: Variable, property: KProperty<*>): T {
		if (v.value == null) {
			v.value = "${thisRef.value}[$index]"
		}
		return v
	}

	operator fun setValue(thisRef: Variable, property: KProperty<*>, value: T) {
		if (v.value == null) {
			v.value = "${thisRef.value}[$index]"
		}
		thisRef.builder.instructions.add(Instruction.assign(v.value, value.value))
	}
}
