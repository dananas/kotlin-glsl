package com.salakheev.shaderbuilderkt.builder.delegates

import com.salakheev.shaderbuilderkt.builder.Instruction
import com.salakheev.shaderbuilderkt.builder.InstructionType
import com.salakheev.shaderbuilderkt.builder.types.Variable
import kotlin.reflect.KProperty

class ConstructorDelegate<T : Variable>(private val v: T, initialValue: String? = null) {
	private var define: Instruction
	private var defined: Boolean = false

	init {
		val definitionString = "${v.typeName} {def} ${getInitializerExpr(initialValue)}"
		define = Instruction(InstructionType.DEFINE, definitionString)
		v.builder.instructions.add(define)
	}

	operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): ConstructorDelegate<T> {
		v.value = property.name
		return this
	}

	operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
		if (!defined) {
			define.result = define.result.replace("{def}", property.name)
			defined = true
		}
		return v
	}

	operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
		if (!defined) {
			define.result = define.result.replace("{def}", property.name)
			defined = true
		}
		v.builder.instructions.add(Instruction.assign(property.name, value.value))
	}

	private fun getInitializerExpr(initialValue: String?): String {
		return if (initialValue == null) "" else " = $initialValue"
	}
}
