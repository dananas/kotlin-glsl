package com.salakheev.shaderbuilderkt.builder.types

import com.salakheev.shaderbuilderkt.builder.ShaderBuilder

interface Variable {
	val builder: ShaderBuilder
	val typeName: String
	var value: String?
}

interface GenType : Variable
interface Vector : GenType
interface Matrix : Variable
