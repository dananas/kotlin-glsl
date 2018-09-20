package com.salakheev.shaderbuilderkt.builder.delegates

import com.salakheev.shaderbuilderkt.builder.Instruction
import com.salakheev.shaderbuilderkt.builder.ShaderBuilder
import com.salakheev.shaderbuilderkt.builder.types.vec.Vec4
import kotlin.reflect.KProperty

class BuiltinVarDelegate {
	private lateinit var v: Vec4

	operator fun provideDelegate(thisRef: ShaderBuilder,
								 property: KProperty<*>): BuiltinVarDelegate {
		v = Vec4(thisRef, property.name)
		return this
	}

	operator fun getValue(thisRef: ShaderBuilder, property: KProperty<*>): Vec4 {
		return v
	}

	operator fun setValue(thisRef: ShaderBuilder, property: KProperty<*>, value: Vec4) {
		thisRef.instructions.add(Instruction.assign(property.name, value.value))
	}
}
