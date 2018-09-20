package com.salakheev.shaderbuilderkt.sources

interface ShaderSourceProvider {
	fun getSource(): String
}

interface ShaderProgramSources {
	val vertex: String
	val fragment: String
}
